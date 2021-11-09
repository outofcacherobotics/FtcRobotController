package org.firstinspires.ftc.teamcode;

import org.opencv.core.Core;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

// See https://github.com/OpenFTC/EasyOpenCV/blob/master/doc/user_docs/camera_initialization_overview.md
public class OpenCV {
    OpenCvWebcam webcam;

    public OpenCV(HardwareMap hardwareMap, OpenCvPipeline pipeline) {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);

        webcam.setPipeline(pipeline);

        // Open webcam, everything defined in Webcam class, going to use that soon
    }
}
