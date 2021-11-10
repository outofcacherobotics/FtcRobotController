package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name="Decisions auto", group="Autos")
public class Decisions_Auto extends LinearOpMode {
    AutoPathController pathController;
    Decisions decisions;

    @Override
    public void runOpMode() {
        this.pathController = new AutoPathController(
                false,
                hardwareMap,
                "frontLeft",
                "frontRight",
                "backLeft",
                "backRight",
                "blueBottom",
                90
        );

        pathController.initializeHardware();

        decisions = new Decisions(hardwareMap, "blue");

        waitForStart();
        if (opModeIsActive()) {
            // Probably not necessary
            pathController.setZeroPowerBehavior();

            while (opModeIsActive()) {
                int duckRegion;
                double[] previousCoords = pathController.getHistory().getPreviousCoordinate();
                if (decisions.idealCoords(previousCoords)) {
                    duckRegion = decisions.getDecision(previousCoords);
                }

                // duckRegion 1 if left, 2 if middle, 3 if right

                // Drive forward 20 cm
                pathController.drive(20, 20, 5);

                telemetry.update();
            }
        }
    }
}
