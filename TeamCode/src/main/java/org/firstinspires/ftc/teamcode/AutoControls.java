package main.java.org.firstinspires.ftc.teamcode;

public class AutoControls {
    private Gamepad gamepad;
    private boolean motor_enabled;
    private double left_front_power, right_front_power, left_back_power, right_back_power;
    private DcMotor left_front_motor, right_front_motor, left_back_motor, right_back_motor;

    public AutoControls(Gamepad gamepad) {
        this.gamepad = gamepad;

        this.left_front_power = 0;
        this.right_front_power = 0;
        this.left_back_power = 0;
        this.right_back_power = 0;

        this.motor_enabled = false;
    }

    public AutoControls(
        Gamepad gamepad,
        DcMotor left_front_motor,
        DcMotor right_front_motor,
        DcMotor left_back_motor,
        DcMotor right_back_motor
    ) {
        this.gamepad = gamepad;

        this.left_front_power = 0;
        this.right_front_power = 0;
        this.left_back_power = 0;
        this.right_back_power = 0;

        this.left_front_motor = left_front_motor;
        this.right_front_power = right_front_power;
        this.left_back_power = left_back_power;
        this.right_back_power = right_back_power;

        this.motor_enabled = true;
    }

    public void setPowerBehavior() {
        if (!this.motor_enabled) {
            return;
        }

        this.left_front_power.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.right_front_power.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.left_back_power.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.right_back_power.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public double getLeftFrontPower() { return left_front_power; }

    public double getRightFrontPower() {
        return right_front_power;
    }

    public double getLeftBackPower() {
        return left_back_power;
    }

    public double getRightBackPower() {
        return right_back_power;
    }

    public void updateSimple() {
        x = gamepad.left_stick_x;
        y = gamepad.left_stick_y;
        clockwise = gamepad.right_stick_x;

        forward = x * sin + y * cos;
        right = x * cos - y * sin;

        left_front_power = forward + clockwise + right;
        right_front_power = forward - (clockwise - right);
        left_back_power = forward + (clockwise - right);
        right_back_power = forward - (clockwise + right);
    }

    public void updateSlowmode() {
        x = gamepad1.left_stick_x;
        y = gamepad1.left_stick_y;

        clockwise = gamepad1.right_stick_x;

        if (gamepad1.right_bumper) {
            fl = (y - x - clockwise)/2;
            fr = (y - x + clockwise)/2;
            bl = (y + x - clockwise)/2;
            br = (y + x + clockwise)/2;
        } else {
            fl = y - x - clockwise;
            fr = y - x + clockwise;
            bl = y + x - clockwise;
            br = y + x + clockwise;
        }
    }
}
