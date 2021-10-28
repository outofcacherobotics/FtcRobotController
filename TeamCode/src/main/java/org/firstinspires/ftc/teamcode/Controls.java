package main.java.org.firstinspires.ftc.teamcode;

public class Controls {
    private Gamepad gamepad;
    private boolean motor_enabled;
    private double left_front_power, right_front_power, left_back_power, right_back_power;
    private DcMotor left_front, right_front, left_back, right_back;
    private String mode;

    public Controls(Gamepad gamepad, String mode) {
        this.gamepad = gamepad;

        this.left_front_power = 0;
        this.right_front_power = 0;
        this.left_back_power = 0;
        this.right_back_power = 0;

        this.motor_enabled = false;
    }

    public Controls(
        Gamepad gamepad,
        String mode,
        DcMotor left_front_motor,
        DcMotor right_front_motor,
        DcMotor left_back_motor,
        DcMotor right_back_motor
    ) {
        this.gamepad = gamepad;
        this.mode = mode;
        
        this.left_front_power = 0;
        this.right_front_power = 0;
        this.left_back_power = 0;
        this.right_back_power = 0;

        this.left_front = left_front_motor;
        this.right_front = right_front_motor;
        this.left_back = left_back_motor;
        this.right_back = right_back_motor;

        this.motor_enabled = true;

        setPowerBehavior();
    }

    public void setPowerBehavior() {
        if (this.motor_enabled) {
            left_front.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            right_front.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            left_back.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            right_back.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
    }

    public void update() {
        switch (mode) {
            case "simple":
                updateSimple();
            case "slowmode":
                updateSlowmode();
        }
    }

    private void updateSimple() {
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

    private void updateSlowmode() {
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

    public double getLeftFrontPower() { return left_front_power; }

    public double getRightFrontPower() { return right_front_power; }

    public double getLeftBackPower() { return left_back_power; }

    public double getRightBackPower() { return right_back_power; }
}
