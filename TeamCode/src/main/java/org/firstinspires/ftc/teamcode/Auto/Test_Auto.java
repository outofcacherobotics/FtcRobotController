package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="Test Auto", group="Autos")
public class Test_Auto extends LinearOpMode {
    AutoPathController pathController;
    FxMotors fxMotors;

    static final ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        pathController = new AutoPathController(
                hardwareMap,
                "frontLeft",
                "frontRight",
                "backLeft",
                "backRight",
                "blueBottom",
                90,
                telemetry
        );

        pathController.initHardware();

        fxMotors = new FxMotors(
                hardwareMap,
                "CarouselSpinner",
                telemetry
        );

        waitForStart();
        if (opModeIsActive()) {
            // Probably not necessary
            pathController.setZeroPowerBehavior();

            pathController.update();

            // Drive forward 20 cm
            pathController.drive(60, 60);
            // left positive, right negative

            pathController.gyroDrive(120);
//            try {
//                pathController.rotate(90);
//                Thread.sleep(3000);
//                pathController.rotate(90);
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

            telemetry.update();
        }
    }
}
