package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class Simple_Mecanum extends LinearOpMode {
    private DcMotor left_front_motor, right_front_motor, left_back_motor, right_back_motor;
    private Controls controls;
    private Drivetrain drivetrain;

    @Override
    public void runOpMode() {
        this.controls = new Controls(
            gamepad1, 
            left_front_motor,
            right_front_motor,
            left_back_motor,
            right_back_motor,
        );

        this.drivetrain = new Drivetrain(
            "frontLeft",
            "frontRight",
            "backLeft",
            "backRight",
        );

        waitForStart();
        if (opModeIsActive()) {
            controls.setPowerBehavior();
            while (opModeIsActive()) {
                controls.update()

                this.drivetrain.directSetLeftFrontPower(controls.getLeftFrontPower());
                this.drivetrain.directSetRightFrontPower(controls.getRightFrontPower());
                this.drivetrain.directSetLeftBackPower(controls.getLeftBackPower());
                this.drivetrain.directSetRightBackPower(controls.getRightBackPower());

                telemetry.update();
            }
        }
    }
}