package main.java.org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * Controls gets information from the gamepad and performs calculations in order
 * to allow smooth driving.
 *
 * This class WILL NOT be used in autonomous modes.
 */
public class Controls {
    private Gamepad gamepad;
    private double left_front_power, right_front_power, left_back_power, right_back_power;
    private String mode;

    public Controls(Gamepad gamepad, String mode) {
        this.gamepad = gamepad;

        this.left_front_power = 0;
        this.right_front_power = 0;
        this.left_back_power = 0;
        this.right_back_power = 0;

        this.mode = mode;
    }

    public double getLeftFrontPower() { return left_front_power; };

    public double getRightFrontPower() { return right_front_power; };

    public double getLeftBackPower() { return left_back_power; };

    public double getRightBackPower() { return right_back_power; };

    public void update() {
        switch (mode) {
            case "simple":
                updateSimple();
            case "slowmode":
                updateSlowmode();
        }
    }

    private void updateSimple() {
        float x = gamepad.left_stick_x;
        float y = gamepad.left_stick_y;
        float clockwise = gamepad.right_stick_x;

        left_front_power = y - x - clockwise;
        right_front_power = y - x + clockwise;
        left_back_power = y + x - clockwise;
        right_back_power = y + x + clockwise;
    }

    private void updateSlowmode() {
        float x = gamepad.left_stick_x;
        float y = gamepad.left_stick_y;
        float clockwise = gamepad.right_stick_x;

        if (gamepad.right_bumper) {
            left_front_power = (y - x - clockwise)/2;
            right_front_power = (y - x + clockwise)/2;
            left_back_power = (y + x - clockwise)/2;
            right_back_power = (y + x + clockwise)/2;
        } else {
            left_front_power = y - x - clockwise;
            right_front_power = y - x + clockwise;
            left_back_power = y + x - clockwise;
            right_back_power = y + x + clockwise;
        }
    }
}
