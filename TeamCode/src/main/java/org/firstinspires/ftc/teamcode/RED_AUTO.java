package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="Red Auto", group="Autos")
public class RED_AUTO extends LinearOpMode {
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
//            pathController.drive(60);
            // left positive, right negative

            pathController.rotate(-90);
            // forward 60
            pathController.gyroDrive(60);
            // right 90
            pathController.rotate(180);
            // spin spinner for 3 seconds
            fxMotors.spinFor(5);
            // left 180
            pathController.rotate(-90);
            // forward 60
            pathController.gyroDrive(60);
            // backward 60
            pathController.gyroDrive(-60);
            pathController.rotate(90);
            pathController.gyroDrive(120);
            pathController.rotate(-90);
            pathController.gyroDrive(60);
            pathController.rotate(-90);
            pathController.gyroDrive(120);

            telemetry.update();
        }
    }
}
