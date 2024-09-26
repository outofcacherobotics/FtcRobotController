package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;
//import org.apache.commons.lang3.concurrent.ThresholdCircuitBreaker;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Class represents motors operating independently of drivetrain
 */
public class Spinner {
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

    public void setSpinnerPower(double power) {
        spinner.setPower(power);
    }

    public void spinFor(double seconds) {
        setSpinnerPower(0.6);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setSpinnerPower(0);
    }
}