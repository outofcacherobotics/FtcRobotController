package main.java.org.firstinspires.ftc.teamcode;

public class Webcam {
    private static final String TAG = "Webcam Sample";

    private static final int secondsPermissionTimeout = Integer.MAX_VALUE;

    // Camera interaction state
    private CameraManager cameraManager;
    private WebcamName cameraName;
    private Camera camera;
    private CameraCaptureSession cameraCaptureSession;

    // Where frames are stored before being saved to disk
    private EvictingBlockingQueue<Bitmap> frameQueue;

    // Amount of bitmaps saved to disk
    private int captureCounter = 0;
    private File captureDirectory = AppUtil.ROBOT_DATA_DIR;

    private Handler callbackHandler;

    public Webcam(String webcamName) {
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

    public void saveFrame(Bitmap frame) {
        saveBitmap(frame);
        frame.recycle();
    }

    public void getAndSaveFrame() {
        return saveFrame(frameQueue.poll());
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
            error("camera not found or permission to use not granted")
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
                @Override public void onConfigured(@NonNull CameraCaptureSession session) {
                    try {
                        final CameraCaptureRequest captureRequest = camera.createCaptureRequest(imageFormat, size, fps);
                        session.startCapture(captureRequest, 
                            new CameraCaptureSession.CaptureCallback() {
                                @Override public void onNewFrame(@NonNull CameraCaptureSession session, @NonNull, CameraCaptureRequest request, @NonNull CameraFrame cameraFrame) {
                                    Bitmap bmp = captureRequest.createEmptyBitmap();
                                    cameraFrame.copyToBitmap(bmp);
                                    frameQueue.offer(bmp);
                                }
                            },
                            Continuation.create(callbackHandler, new CameraCaptureSession.StatusCallback() {
                                @Override public void onCaptureSequenceCompleted(@NonNull CameraCaptureSession session, CameraCaptureSequenceId cameraCaptureSequenceId, long lastFrameNumber) {
                                    // Logging
                                }
                            })
                        );
                        synchronizer.finish(session);
                    } catch (CameraException|RuntimeException e) {
                        error("exception starting camera");
                        session.close();
                        synchronizer.finish(null);
                    }
                }
            }));

            try {
                synchronizer.await();
            }   catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            cameraCaptureSession = synchronizer.getValue();
        }
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
        telemtry.update();
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