package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.Controls;
import org.firstinspires.ftc.teamcode.Drivetrain;
import org.firstinspires.ftc.teamcode.FxMotors;

@TeleOp
public class Simple_Mecanum extends LinearOpMode {
    Controls controls;
    Drivetrain drivetrain;
    FxMotors fxMotors;

    @Override
    public void runOpMode() {
        controls = new Controls(
                gamepad1,
                telemetry
        );

        drivetrain = new Drivetrain(
                hardwareMap,
                "frontLeft",
                "frontRight",
                "backLeft",
                "backRight",
                telemetry
        );

        fxMotors = new FxMotors(
                hardwareMap,
                "CarouselSpinner",
                telemetry
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

                fxMotors.setSpinnerPower(
                        controls.getSpinnerPower()
                );

                telemetry.addData("Spinner power", controls.getSpinnerPower());
                telemetry.update();
            }
        }
    }
}