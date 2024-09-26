package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="Simple Auto", group="Autos")
public class Simple_Auto extends LinearOpMode {
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

            while (opModeIsActive()) {
                pathController.updateIMUHeading();

                // Drive forward 20 cm
                pathController.gyroDrive(20);
                pathController.gyroTurnWithUnits(20);

            telemetry.update();
        }
    }
}
