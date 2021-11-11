package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Class represents motors operating independently of drivetrain
 */
public class FxMotors {
    DcMotor spinner;
    Telemetry telemetry;

    public FxMotors(
            HardwareMap hardwareMap,
            String spinner_name,
            Telemetry telemetry
    ) {
        spinner = hardwareMap.get(DcMotor.class, spinner_name);
        spinner.setDirection(DcMotor.Direction.REVERSE);
        spinner.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.telemetry = telemetry;
    }

    public void setSpinner(double power) {
        spinner.setPower(power);
    }
}