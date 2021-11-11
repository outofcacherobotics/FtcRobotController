package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name="Decisions auto", group="Autos")
public class Decisions_Auto extends LinearOpMode {
    AutoPathController pathController;
    FxMotors fxMotors;
    Decisions decisions;
    int duckRegion;

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

        // ATM, Decisions (TFLite and Vuforia) vs OpenCV is either-or
        decisions = new Decisions(hardwareMap, "blue");

        waitForStart();
        if (opModeIsActive()) {
            // Probably not necessary
            pathController.setZeroPowerBehavior();

            if (opModeIsActive()) {
                pathController.update();

                double[] previousCoords = pathController.getHistory().getPreviousCoords();
                if (decisions.idealCoords(previousCoords)) {
                    duckRegion = decisions.getDecision(previousCoords);
                }

                // duckRegion 1 if left, 2 if middle, 3 if right,
                // store for later use

                // Drive forward 20 cm
                pathController.gyroDrive(20);

                telemetry.update();
            }
        }
    }
}
