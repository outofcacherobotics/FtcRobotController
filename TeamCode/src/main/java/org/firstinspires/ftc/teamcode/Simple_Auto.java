package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name="Simple Auto", group="Autos")
public class Simple_Auto extends LinearOpMode {
    AutoPathController pathController;
    FxMotors fxMotors;

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
            pathController.gyroDrive(60);

            telemetry.update();
        }
    }
}
