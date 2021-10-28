package main.java.org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import main.java.org.firstinspires.ftc.teamcode.Controls;
import main.java.org.firstinspires.ftc.teamcode.Drivetrain;

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
            "slowmode",
            drivetrain.getFrontLeftMotor(),
            drivetrain.getFrontRightMotor(),
            drivetrain.getBackLeftMotor(),
            drivetrain.getFrontLeftMotor(),
        );

        waitForStart();
        if (opModeIsActive()) {
            while (opModeIsActive()) {
                controls.update();
                
                this.drivetrain.directSetLeftFrontPower(fl);
                this.drivetrain.directSetRightFrontPower(fr);
                this.drivetrain.directSetLeftBackPower(bl);
                this.drivetrain.directSetRightBackPower(br);

                telemetry.update();
            }
        }
    }
}
