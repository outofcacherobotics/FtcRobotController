package main.java.org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.Arrays;

/**
 * Drivetrain is an abstraction of the drivetrain. Use drivetrain to move around.
 * Has some information about robot movement.
 *
 * Note: time is always in seconds and is represented by a double for 8-bit precision.
 */
public class Drivetrain {
    DcMotor left_front, right_front, left_back, right_back;
    List<DcMotor> motors;
    double left_front_power, right_front_power, left_back_power, right_back_power = 0;
    double MAX_POWER = 1.0;

    public Drivetrain(
            String left_front_name,
            String right_front_name,
            String left_back_name,
            String right_back_name
    ) {
        telemetry.addData("Initializing drivetrain...");
        telemetry.update();

        left_front = hardwareMap.get(DcMotor.class, left_front_name);
        right_front = hardwareMap.get(DcMotor.class, right_front_name);
        left_back = hardwareMap.get(DcMotor.class, left_back_name);
        right_back = hardwareMap.get(DcMotor.class, right_back_name);

        DcMotor[] motorArray = {left_front, right_front, left_back, right_back};
        motors = Arrays.asList<DcMotor>(motorArray);

        telemetry.addData("Initialized drivetrain...");
        telemetry.update();
    }

    public Drivetrain(
            String left_front_name,
            String right_front_name,
            String left_back_name,
            String right_back_name,
            double max_power
    ) {
        this(left_front_name, right_front_name, left_back_name, right_back_name);
        MAX_POWER = max_power;
    }

    private boolean valid_power(double power) {
        return 0 < power && power < MAX_POWER;
    }

    public int forward(double power, double time) {
        if (!this.valid_power(power)) {
            return 1; // invalid power code
        }

        for (DcMotor m : motors) {
            m.setPower(power);
        }

        sleep(time * 1000);

        for (DcMotor m : motors) {
            m.setPower(0);
        }

        return 0;
    }

    public int backward(double power, double time) {
        if (!this.valid_power(power)) {
            return 1; // invalid power code
        }


        this.reverse();

        for (DcMotor m : motors) {
            m.setPower(power);
        }

        sleep(time * 1000);

        for (DcMotor m : motors) {
            m.setPower(0);
        }

        return 0;
    }

    public int strafe_right(double power, double time) {
        fl = power;
        fr = -power;
        bl = -power;
        br = power;

        frontLeft.setPower(fl);
        frontRight.setPower(fr);
        backLeft.setPower(bl);
        backRight.setPower(br);

        sleep(time * 1000);

        for (DcMotor m : motors) {
            m.setPower(0);
        }
    }

    public int strafe_left(double power, double time) {
        fl = -power;
        fr = power;
        bl = power;
        br = -power;

        frontLeft.setPower(fl);
        frontRight.setPower(fr);
        backLeft.setPower(bl);
        backRight.setPower(br);

        sleep(time * 1000);

        for (DcMotor m : motors) {
            m.setPower(0);
        }
    }

    public int move_with_coords(double x, double y, double rot, double power, double time) {
        fl = y - (clockwise - x);
        fr = y + clockwise + x;
        bl = y - (clockwise + x);
        br = y + (clockwise - x);

        frontLeft.setPower(power * fl);
        frontRight.setPower(power * fr);
        backLeft.setPower(power * bl);
        backRight.setPower(power * br);

        sleep(time * 1000);

        for (DcMotor m : motors) {
            m.setPower(0);
        }
    }

    void reverse() {
        for (DcMotor m : motors) {
            m.setDirection(DcMotor.Direction.REVERSE);
        }
    }

    // Add rotation while moving, but only after proper encoder distance calibration is calculated.
    // Want to be able to correctly move the robot by distance instead of power output and time.
    // When that is achieved, rotation can be implemented along with distance.
}

