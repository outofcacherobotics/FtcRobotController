package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name="Simple auto", group="Autos")
public class Simple_Auto extends LinearOpMode {
    private AutoPathController pathController;

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

        waitForStart();
        if (opModeIsActive()) {
            // Probably not necessary
            pathController.setZeroPowerBehavior();

            while (opModeIsActive()) {
                pathController.update();

                // Drive forward 20 cm
                pathController.drive(20, 20, 5);

                telemetry.update();
            }
        }
    }
}
