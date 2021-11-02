package main.java.org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.Arrays;

/**
 * Drivetrain is an abstraction of the drivetrain. Use drivetrain to move around.
 * Has some information about robot movement.
 *
 * Note: time is always in seconds and is represented by a double for 8-bit precision.
 * View HardwarePushbot for encoder use
 */
public class Drivetrain {
    DcMotor left_front, right_front, left_back, right_back;

    // History of movements, used by Localizer
    double[][] history;

    public double[][] getHistory() { return history; };

    public double[] getLatestHistory() { return history[history.length - 1]; };

    private void addToHistory(double[] newCoords) {
        history = ArrayUtils.add(history, newCoords);
    };

    double MAX_POWER = 1.0;

    public Drivetrain(
            HardwareMap hardwareMap,
            String left_front_name,
            String right_front_name,
            String left_back_name,
            String right_back_name
    ) {
        this.left_front = hardwareMap.get(DcMotor.class, left_front_name);
        this.right_front = hardwareMap.get(DcMotor.class, right_front_name);
        this.left_back = hardwareMap.get(DcMotor.class, left_back_name);
        this.right_back = hardwareMap.get(DcMotor.class, right_back_name);

        this.right_front.setDirection(DcMotor.Direction.REVERSE);
        this.right_back.setDirection(DcMotor.Direction.REVERSE);

        setZeroPowerBehavior();
    }

    public Drivetrain(
            HardwareMap hardwareMap,
            String left_front_name,
            String right_front_name,
            String left_back_name,
            String right_back_name,
            double max_power
    ) {
        this(hardwareMap, left_front_name, right_front_name, left_back_name, right_back_name);
        MAX_POWER = max_power;
    }

    public void setZeroPowerBehavior() {
        left_front.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        right_front.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        left_back.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        right_back.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    private boolean valid_power(double power) {
        return 0 < power && power < MAX_POWER;
    }

    public void setAllMotors(
        double left_front_power,
        double right_front_power,
        double left_back_power,
        double right_back_power
    ) {
        left_front.setPower(left_front_power);
        right_front.setPower(right_front_power);
        left_back.setPower(left_back_power);
        right_back.setPower(right_back_power);
    }

    // Add rotation while moving, but only after proper encoder distance calibration is calculated.
    // Want to be able to correctly move the robot by distance instead of power output and time.
    // When that is achieved, rotation can be implemented along with distance.

    public DcMotor getFrontLeftMotor() { return this.left_front; };

    public DcMotor getFrontRightMotor() { return this.right_front; };

    public DcMotor getBackLeftMotor() { return this.left_back; };

    public DcMotor getBackLeftMotor() { return this.right_back; };

    public void directSetLeftFrontPower(double power) { this.left_front.setPower(power); };

    public void directSetRightFrontPower(double power) { this.left_front.setPower(power); };

    public void directSetLeftBackPower(double power) { this.left_front.setPower(power); };

    public void directSetRightBackPower(double power) { this.left_front.setPower(power); };
}

