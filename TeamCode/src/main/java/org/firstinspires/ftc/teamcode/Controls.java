package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * Controls gets information from the gamepad and performs calculations in order
 * to allow smooth driving.
 *
 * This class WILL NOT be used in autonomous modes.
 */
public class Controls {
    Gamepad gamepad;

    static final double dpad_power = 1.0;

    double left_front_power, right_front_power, left_back_power, right_back_power;
    double spinner_power;

    public Controls(Gamepad gamepad, String mode) {
        this.gamepad = gamepad;

        this.left_front_power = 0;
        this.right_front_power = 0;
        this.left_back_power = 0;
        this.right_back_power = 0;
    }

    public double getLeftFrontPower() { return left_front_power; };

    public double getRightFrontPower() { return right_front_power; };

    public double getLeftBackPower() { return left_back_power; };

    public double getRightBackPower() { return right_back_power; };

    public double getSpinnerPower() { return spinner_power; };

    public void update() {
        updateSpinner();
        updateSlowmode();
    }

    private void updateSpinner() {
        if (gamepad.a) {
            spinner_power = 0.5;
        } else if (gamepad.b) {
            spinner_power = -0.5;
        }
    }

    private void updateSlowmode() {
        float x = gamepad.left_stick_x;
        float y = gamepad.left_stick_y;
        float clockwise = gamepad.right_stick_x;

        if (gamepad.dpad_right) {
            x = 1;
            y = 0;
        } else if (gamepad.dpad_left) {
            x = -1;
            y = 0;
        } else if (gamepad.dpad_up) {
            x = 0;
            y = 1;
        } else if (gamepad.dpad_down) {
            x = 0;
            y = -1;
        }

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
