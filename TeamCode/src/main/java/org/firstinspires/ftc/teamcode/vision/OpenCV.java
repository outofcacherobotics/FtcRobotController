package org.firstinspires.ftc.teamcode.vision;

import org.opencv.core.Core;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvWebcam;

/**
 * Initializes OpenCV
 *
 * See https://github.com/OpenFTC/EasyOpenCV/blob/master/doc/user_docs/camera_initialization_overview.md for more.
 */
public class OpenCV {
    OpenCvWebcam webcam;

    public OpenCV(HardwareMap hardwareMap, OpenCvPipeline pipeline) {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);

        webcam.setPipeline(pipeline);

        // Open webcam, everything defined in Webcam class, going to use that soon
        webcam.setMillisecondsPermissionTimeout(2500); // Timeout for obtaining permission is configurable. Set before opening.
        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                webcam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {

            }
        });
    }

    public void stop() {
        webcam.stopStreaming();
        webcam.closeCameraDevice();
    }
}
