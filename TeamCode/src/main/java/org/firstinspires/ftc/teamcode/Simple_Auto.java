package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name="Simple Auto", group="Autos")
public class Simple_Auto extends LinearOpMode {
    AutoPathController pathController;
    FxMotors fxMotors;

    @Override
    public void runOpMode() {
        this.pathController = new AutoPathController(
                hardwareMap,
                "frontLeft",
                "frontRight",
                "backLeft",
                "backRight",
                "blueBottom",
                90
        );

        pathController.initHardware();

        fxMotors = new FxMotors(
                hardwareMap,
                "CarouselSpinner"
        );

        waitForStart();
        if (opModeIsActive()) {
            // Probably not necessary
            pathController.setZeroPowerBehavior();

            while (opModeIsActive()) {
                pathController.updateIMUHeading();

                // Drive forward 20 cm
                pathController.gyroDrive(20);
                pathController.gyroTurnWithUnits(20);

                telemetry.update();
            }
        }
    }
}
