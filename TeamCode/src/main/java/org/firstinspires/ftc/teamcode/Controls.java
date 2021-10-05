package main.java.org.firstinspires.ftc.teamcode;

public class Controls {
    private Gamepad gamepad;
    private double left_front_power, right_front_power, left_back_power, right_back_power;

    public Controls(Gamepad gamepad) {
        this.gamepad = gamepad;
        left_front_power = 0;
        right_front_power = 0;
        left_back_power = 0;
        right_back_power = 0;
    }

    public double getRight_joystick_power() {
        return right_joystick_power;
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

    public void update() {
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

    // Verification functions that run along with the TeleOp that enforces ruling of our control protocol
    // For example, enforcing that one joystick only controls one function of the robot, etc.
}
