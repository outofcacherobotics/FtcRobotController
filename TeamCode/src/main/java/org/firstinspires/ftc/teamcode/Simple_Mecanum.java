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
        this.controls = new Controls(
                gamepad1,
                "slowmode"
        );

        this.drivetrain = new Drivetrain(
                hardwareMap,
                "frontLeft",
                "frontRight",
                "backLeft",
                "backRight"
        );

        this.fxMotors = new FxMotors(
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

                telemetry.update();
            }
        }
    }
}