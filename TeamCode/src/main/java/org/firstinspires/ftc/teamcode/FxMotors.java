package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Class represents motors operating independently of drivetrain
 */
public class FxMotors {
    DcMotor spinner;

    public FxMotors(
            HardwareMap hardwareMap,
            String spinner_name
    ) {
        this.spinner = hardwareMap.get(DcMotor.class, spinner_name);
        this.spinner.setDirection(DcMotor.Direction.REVERSE);
    }

    public void setSpinner(double power) {
        spinner.setPower(power);
    }
}