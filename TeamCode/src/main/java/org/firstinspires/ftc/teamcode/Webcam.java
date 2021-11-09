 package org.firstinspires.ftc.teamcode;

 import android.graphics.Bitmap;
 import android.graphics.ImageFormat;
 import android.os.Handler;

 import androidx.annotation.NonNull;

 import com.qualcomm.robotcore.hardware.HardwareMap;
 import com.qualcomm.robotcore.util.RobotLog;

 import org.firstinspires.ftc.robotcore.external.ClassFactory;
 import org.firstinspires.ftc.robotcore.external.Telemetry;
 import org.firstinspires.ftc.robotcore.external.android.util.Size;
 import org.firstinspires.ftc.robotcore.external.function.Consumer;
 import org.firstinspires.ftc.robotcore.external.function.Continuation;
 import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
 import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCaptureRequest;
 import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCaptureSequenceId;
 import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCaptureSession;
 import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCharacteristics;
 import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraException;
 import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraFrame;
 import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraManager;
 import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
 import org.firstinspires.ftc.robotcore.internal.collections.EvictingBlockingQueue;
 import org.firstinspires.ftc.robotcore.internal.network.CallbackLooper;
 import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
 import org.firstinspires.ftc.robotcore.internal.system.ContinuationSynchronizer;
 import org.firstinspires.ftc.robotcore.internal.system.Deadline;

 import java.io.File;
 import java.io.FileOutputStream;
 import java.io.IOException;
 import java.util.Locale;
 import java.util.concurrent.ArrayBlockingQueue;
 import java.util.concurrent.TimeUnit;

 /**
  * Docs
  * https://github.com/FIRST-Tech-Challenge/FtcRobotController/wiki/Using-an-External-Webcam-with-Control-Hub
  * Initializes webcam
  */
 public class Webcam {
     static final String TAG = "Webcam Sample";
     Telemetry telemetry;

     static final int secondsPermissionTimeout = Integer.MAX_VALUE;

     // Camera interaction state
     CameraManager cameraManager;
     WebcamName cameraName;
     Camera camera;
     CameraCaptureSession cameraCaptureSession;

     // Where frames are stored before being saved to disk
     EvictingBlockingQueue<Bitmap> frameQueue;

     // Amount of bitmaps saved to disk
     int captureCounter = 0;
     File captureDirectory = AppUtil.ROBOT_DATA_DIR;

     Handler callbackHandler;

     public Webcam(HardwareMap hardwareMap, String webcamName) {
         callbackHandler = CallbackLooper.getDefault().getHandler();

         cameraManager = ClassFactory.getInstance().getCameraManager();
         cameraName = hardwareMap.get(WebcamName.class, webcamName);

         initializeFrameQueue(2);
         AppUtil.getInstance().ensureDirectoryExists(captureDirectory);

         openCamera();
         if (camera == null) return;

         startCamera();
         if (cameraCaptureSession == null) return;
     }

     public Bitmap getCurrentFrame() {
         return frameQueue.poll();
     }

     public void onNewFrame(Bitmap frame) {
         saveBitmap(frame);
         frame.recycle();
     }

     private void initializeFrameQueue(int capacity) {
         frameQueue = new EvictingBlockingQueue<Bitmap>(new ArrayBlockingQueue<Bitmap>(capacity));
         frameQueue.setEvictAction(new Consumer<Bitmap>() {
             @Override public void accept(Bitmap frame) {
                 // Frame eviction, will happen if overflowing occurs
                 frame.recycle(); // not strictly necessary, but helpful
             }
         });
     }

     // Opens camera, prepares it to start
     public void openCamera() {
         if (camera != null) return;

         // Create deadline for getting camera (in this case, it is infinite)
         Deadline deadline = new Deadline(secondsPermissionTimeout, TimeUnit.SECONDS);
         // Use cameraManager to request permission and acquire camera
         camera = cameraManager.requestPermissionAndOpenCamera(deadline, cameraName, null);
         if (camera == null) {
             error("camera not found or permission to use not granted");
         }
     }

     // Creates a cameraCaptureSession
     public void startCamera() {
         // If cameraCaptureSession exists, exit. This immediately indicates the camera is already started
         if (cameraCaptureSession != null) return;

         // Ensures YUY2 format is supported
         final int imageFormat = ImageFormat.YUY2;
         CameraCharacteristics cameraCharacteristics = cameraName.getCameraCharacteristics();
         if (!contains(cameraCharacteristics.getAndroidFormats(), imageFormat)) {
             error("image format not supported");
             return;
         }

         // Gets size and max fps
         final Size size = cameraCharacteristics.getDefaultSize(imageFormat);
         final int fps = cameraCharacteristics.getMaxFramesPerSecond(imageFormat, size);

         // Allows for async execution, will resolve with value (like promise in JS)
         final ContinuationSynchronizer<CameraCaptureSession> synchronizer = new ContinuationSynchronizer<>();

         try {
             camera.createCaptureSession(Continuation.create(callbackHandler, new CameraCaptureSession.StateCallbackDefault() {
                 @Override
                 public void onConfigured(@NonNull CameraCaptureSession session) {
                     try {
                         /** The session is ready to go. Start requesting frames */
                         final CameraCaptureRequest captureRequest = camera.createCaptureRequest(imageFormat, size, fps);
                         session.startCapture(captureRequest,
                                 new CameraCaptureSession.CaptureCallback() {
                                     @Override public void onNewFrame(@NonNull CameraCaptureSession session, @NonNull CameraCaptureRequest request, @NonNull CameraFrame cameraFrame) {
                                         /** A new frame is available. The frame data has <em>not</em> been copied for us, and we can only access it
                                          * for the duration of the callback. So we copy here manually. */
                                         Bitmap bmp = captureRequest.createEmptyBitmap();
                                         cameraFrame.copyToBitmap(bmp);
                                         frameQueue.offer(bmp);
                                     }
                                 },
                                 Continuation.create(callbackHandler, new CameraCaptureSession.StatusCallback() {
                                     @Override public void onCaptureSequenceCompleted(@NonNull CameraCaptureSession session, CameraCaptureSequenceId cameraCaptureSequenceId, long lastFrameNumber) {
                                         RobotLog.ii(TAG, "capture sequence %s reports completed: lastFrame=%d", cameraCaptureSequenceId, lastFrameNumber);
                                     }
                                 })
                         );
                         synchronizer.finish(session);
                     } catch (CameraException|RuntimeException e) {
                         RobotLog.ee(TAG, e, "exception starting capture");
                         error("exception starting capture");
                         session.close();
                         synchronizer.finish(null);
                     }
                 }
             }));
         } catch (CameraException|RuntimeException e) {
             RobotLog.ee(TAG, e, "exception starting camera");
             error("exception starting capture");
             synchronizer.finish(null);
         }

         try {
             synchronizer.await();
         }   catch (InterruptedException e) {
             Thread.currentThread().interrupt();
         }

         cameraCaptureSession = synchronizer.getValue();
     }

     private void stopCamera() {
         if (cameraCaptureSession != null) {
             cameraCaptureSession.stopCapture();
             cameraCaptureSession.close();
             cameraCaptureSession = null;
         }
     }

     private void closeCamera() {
         stopCamera();
         if (camera != null) {
             camera.close();
             camera = null;
         }
     }

     private void error(String msg) {
         telemetry.log().add(msg);
         telemetry.update();
     }

     private void error(String format, Object...args) {
         telemetry.log().add(format, args);
         telemetry.update();
     }

     private boolean contains(int[] array, int value) {
         for (int i : array) {
             if (i == value) return true;
         }
         return false;
     }

     private void saveBitmap(Bitmap bitmap) {
         File file = new File(captureDirectory, String.format(Locale.getDefault(), "webcam-frame-%d.jpg", captureCounter++));
         try {
             try (FileOutputStream outputStream = new FileOutputStream(file)) {
                 bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                 telemetry.log().add("captured %s", file.getName());
             }
         } catch (IOException e) {
             RobotLog.ee(TAG, e, "exception in saveBitmap()");
             error("exception saving %s", file.getName());
         }
     }
 }
