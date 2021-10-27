package main.java.org.firstinspires.ftc.teamcode;

public class Controls {
    private Gamepad gamepad;
    private boolean motor_enabled;
    private double left_front_power, right_front_power, left_back_power, right_back_power;
    private DcMotor left_front_motor, right_front_motor, left_back_motor, right_back_motor;

    public Controls(Gamepad gamepad) {
        this.gamepad = gamepad;

        this.left_front_power = 0;
        this.right_front_power = 0;
        this.left_back_power = 0;
        this.right_back_power = 0;

        this.motor_enabled = false;
    }

    public Controls(
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

        motor_enabled = true;
    }

    public void setPowerBehavior() {
        if (!this.motor_enabled) {
            return;
        }

        left_front_power.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        right_front_power.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        left_back_power.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        right_back_power.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void update() {
        x = gamepad.left_stick_x;
        y = gamepad.left_stick_y;
        clockwise = gamepad.right_stick_x;

        self.left_front_power = y - clockwise - x;
        self.right_front_power = y + clockwise - x;
        self.left_back_power = y - clockwise + x;
        self.right_back_power = y + clockwise + x;
    }

    public double getLeftFrontPower() { return left_front_power; }

    public double getRightFrontPower() { return right_front_power; }

    public double getLeftBackPower() { return left_back_power; }

    public double getRightBackPower() { return right_back_power; }
}
