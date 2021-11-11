package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class Simple_Mecanum extends LinearOpMode {
    Controls controls;
    Drivetrain drivetrain;
    FxMotors fxMotors;

    @Override
    public void runOpMode() {
        controls = new Controls(
                gamepad1
        );

        drivetrain = new Drivetrain(
                hardwareMap,
                "frontLeft",
                "frontRight",
                "backLeft",
                "backRight"
        );

        fxMotors = new FxMotors(
                hardwareMap,
                "CarouselSpinner"
        );

        waitForStart();
        if (opModeIsActive()) {
            drivetrain.setZeroPowerBehavior();

            while (opModeIsActive()) {
                controls.update();

                drivetrain.setAllMotors(
                    controls.getLeftFrontPower(),
                    controls.getRightFrontPower(),
                    controls.getLeftBackPower(),
                    controls.getRightBackPower()
                );

                fxMotors.setSpinner(
                        controls.getSpinnerPower()
                );

                telemetry.addData("Spinner power", controls.getSpinnerPower());
                telemetry.update();
            }
        }
    }
}