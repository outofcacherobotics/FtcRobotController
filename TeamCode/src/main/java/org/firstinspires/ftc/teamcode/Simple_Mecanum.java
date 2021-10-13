package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class Simple_Mecanum extends LinearOpMode {
    private DcMotor left_front_motor, right_front_motor, left_back_motor, right_back_motor;
    private Controls controls;

    @Override
    public void runOpMode() {
        left_front_motor = hardwareMap.get(DcMotor.class, "frontLeft");
        right_front_motor = hardwareMap.get(DcMotor.class, "frontRight");
        left_back_motor = hardwareMap.get(DcMotor.class, "backLeft");
        right_back_motor = hardwareMap.get(DcMotor.class, "backRight");
        right_front_motor.setDirection(DcMotor.Direction.REVERSE);
        right_back_motor.setDirection(DcMotor.Direction.REVERSE);

        controls = new Controls(
            gamepad1, 
            left_front_motor,
            right_front_motor,
            left_back_motor,
            right_back_motor,
        );

        waitForStart();
        if (opModeIsActive()) {
            controls.setPowerBehavior();
            while (opModeIsActive()) {
                controls.update()
                
                left_front_motor.setPower(controls.getLeftFrontPower());
                right_front_motor.setPower(controls.getRightFrontPower());
                left_back_motor.setPower(controls.getLeftBackPower());
                right_back_motor.setPower(controls.getRightBackPower());

                telemetry.update();
            }
        }
    }
}