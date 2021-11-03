package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class Simple_Mecanum extends LinearOpMode {
    private Drivetrain drivetrain;
    private Controls controls;

    @Override
    public void runOpMode() {
        this.drivetrain = new Drivetrain(
                hardwareMap,
                "frontLeft",
                "frontRight",
                "backLeft",
                "backRight"
        );

        this.controls = new Controls(
            gamepad1, 
            "slowmode"
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

                telemetry.update();
            }
        }
    }
}