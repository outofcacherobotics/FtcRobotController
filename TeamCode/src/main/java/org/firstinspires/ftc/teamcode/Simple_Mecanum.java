package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class Simple_Mecanum extends LinearOpMode {
    private Drivetrain drivetrain;
    private Controls controls;

    @Override
    public void runOpMode() {
        this.drivetrain = new Drivetrain(
            "frontLeft",
            "frontRight",
            "backLeft",
            "backRight",
        );

        this.controls = new Controls(
            gamepad1, 
            drivetrain.getFrontLeftMotor(),
            drivetrain.getFrontRightMotor(),
            drivetrain.getBackLeftMotor(),
            drivetrain.getFrontLeftMotor(),
        );

        waitForStart();
        if (opModeIsActive()) {
            controls.setPowerBehavior();
            while (opModeIsActive()) {
                controls.updateSimple()

                this.drivetrain.directSetLeftFrontPower(controls.getLeftFrontPower());
                this.drivetrain.directSetRightFrontPower(controls.getRightFrontPower());
                this.drivetrain.directSetLeftBackPower(controls.getLeftBackPower());
                this.drivetrain.directSetRightBackPower(controls.getRightBackPower());

                telemetry.update();
            }
        }
    }
}