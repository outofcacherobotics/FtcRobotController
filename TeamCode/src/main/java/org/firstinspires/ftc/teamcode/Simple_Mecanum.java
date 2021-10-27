package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import main.java.org.firstinspires.ftc.teamcode.Controls;
import main.java.org.firstinspires.ftc.teamcode.Drivetrain;

@TeleOp
public class Simple_Mecanum extends LinearOpMode {
    private DcMotor left_front_motor, right_front_motor, left_back_motor, right_back_motor;
    private Controls controls;
    private Drivetrain drivetrain;

    @Override
    public void runOpMode() {
        drivetrain = new Drivetrain(
                "frontLeft",
                "frontRight",
                "backLeft",
                "backRight"
        );

        controls = new Controls(
            gamepad1, 
            left_front_motor,
            right_front_motor,
            left_back_motor,
            right_back_motor
        );

        waitForStart();
        if (opModeIsActive()) {
            controls.setPowerBehavior();

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