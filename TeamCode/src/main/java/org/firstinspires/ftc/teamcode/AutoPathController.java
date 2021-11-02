package main.java.org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * AutoPathController is an abstraction of the drivetrain. Use this for Autos
 * where a pre defined path needs to be executed.
 */
public class AutoPathController {
    DcMotor left_front, right_front, left_back, right_back;

    // History of movements, used by Localizer.
    // List of arrays, 0 index being rotation in degrees, 1 and 2 being X and Y
    double[][] history;

    public double[][] getHistory() { return history; };

    private void addToHistory(double[] newCoords) {
        history[history.length] = newCoords;
    };

    ModernRoboticsI2cGyro gyro;

    // Orientation relative to the top of the field.
    // https://github.com/acmerobotics/road-runner/blob/master/gui/src/main/resources/field.png
    double currentAngle;
    private ElapsedTime runtime = new ElapsedTime();
    static final double FIELD_HEIGHT_CM = 365.7;
    static final double FIELD_WIDTH_CM = 365.7;

    static final double[] BLUE_BOTTOM_STARTING_COORDS = { 11.5, 33.5 };
    static final double[] BLUE_TOP_STARTING_COORDS = { 11.5, 81.0 };
    static final double[] RED_BOTTOM_STARTING_COORDS = { FIELD_WIDTH_CM - 11.5, 33.5 };
    static final double[] RED_TOP_STARTING_COORDS = { FIELD_WIDTH_CM - 11.5, 81.0 };

    static final double AUTO_DRIVE_SPEED = 0.7;
    static final double AUTO_TURN_SPEED = 0.5;

    static final double     HEADING_THRESHOLD       = 1 ;      // As tight as we can make it with an integer gyro
    static final double     P_TURN_COEFF            = 0.1;     // Larger is more responsive, but also less stable
    static final double     P_DRIVE_COEFF           = 0.15;     // Larger is more responsive, but also less stable

    static final double PULSES_PER_REVOLUTION = 384.5;
    static final double WHEEL_DIAMETER_CM = 8.7;
    static final double PULSES_PER_CM = PULSES_PER_REVOLUTION / (WHEEL_DIAMETER_CM * 3.1415);

    public AutoPathController(
            boolean gyroEnabled,
            HardwareMap hardwareMap,
            String left_front_name,
            String right_front_name,
            String left_back_name,
            String right_back_name,
            String setupPosition,
            double setupAngle
    ) {
        if (gyroEnabled) {
            this.gyro = (ModernRoboticsI2cGyro)hardwareMap.gyroSensor.get("gyro");
        }

        this.left_front = hardwareMap.get(DcMotor.class, left_front_name);
        this.right_front = hardwareMap.get(DcMotor.class, right_front_name);
        this.left_back = hardwareMap.get(DcMotor.class, left_back_name);
        this.right_back = hardwareMap.get(DcMotor.class, right_back_name);

        switch (setupPosition) {
            case "blueBottom":
                addToHistory(BLUE_BOTTOM_STARTING_COORDS);
            case "blueTop":
                addToHistory(BLUE_TOP_STARTING_COORDS);
            case "redBottom":
                addToHistory(RED_BOTTOM_STARTING_COORDS);
            case "redTop":
                addToHistory(RED_TOP_STARTING_COORDS);
        }
        
        currentAngle = setupAngle;
    }

    public void setZeroPowerBehavior() {
        left_front.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        right_front.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        left_back.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        right_back.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void initializeHardware() {
        setStopAndResetEncoder();

        setRunUsingEncoder();
    }

    public void initializeHardwareWithGyro() {
        setStopAndResetEncoder();

        gyro.calibrate();

        setRunUsingEncoder();

        gyro.resetZAxisIntegrator();
    }

    public void setStopAndResetEncoder() {
        // Stop to reset encoders and calibrate gyro
        left_front.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right_front.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        left_back.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right_back.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void setRunUsingEncoder() {
        left_front.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right_front.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        left_back.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right_back.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void setRunToPosition() {
        left_front.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        right_front.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        left_back.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        right_back.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void setZeroPower() {
        left_front.setPower(0);
        right_front.setPower(0);
        left_back.setPower(0);
        right_back.setPower(0);
    }

    private void setLeftAndRightPower(double leftSpeed, double rightSpeed) {
        left_front.setPower(leftSpeed);
        right_front.setPower(rightSpeed);
        left_back.setPower(leftSpeed);
        right_back.setPower(rightSpeed);
    }

    /**
     *  Method to drive with a gyro disabled.
     *
     * @param distance   Distance (in cm) to move from current position.  Negative distance means move backwards.
     */
    public void drive(double leftCM, double rightCM, double timeoutS) {
        // For now, only straightline/rotating allowed
        if (Math.abs(leftCM) != Math.abs(rightCM)) {
            return;
        }

        double leftCounts = (int) (leftCM * PULSES_PER_CM);
        double rightCounts = (int) (rightCM * PULSES_PER_CM);

        int newFrontLeftTarget = left_front.getCurrentPosition() + (int)leftCounts;
        int newFrontRightTarget = right_front.getCurrentPosition() + (int)rightCounts;
        int newBackLeftTarget = left_back.getCurrentPosition() + (int)leftCounts;
        int newBackRightTarget = right_back.getCurrentPosition() + (int)rightCounts;

        left_front.setTargetPosition(newFrontLeftTarget);
        right_front.setTargetPosition(newFrontRightTarget);
        left_back.setTargetPosition(newBackLeftTarget);
        right_back.setTargetPosition(newBackRightTarget);

        setRunToPosition();

        runtime.reset();
        setLeftAndRightPower(AUTO_DRIVE_SPEED, AUTO_DRIVE_SPEED);

        while (left_front.isBusy() &&
                right_front.isBusy() &&
                left_back.isBusy() &&
                right_back.isBusy() &&
                runtime.seconds() < timeoutS) {

        }

        setZeroPower();
        setRunUsingEncoder();

        // Append to history (if it is straightline)
        if (leftCM > 0 && leftCM == rightCM) {
            double[] previousCoordinate = history[history.length - 1];
            double xMoved = previousCoordinate[1] + (leftCM * Math.cos(currentAngle));
            double yMoved = previousCoordinate[2] + (leftCM * Math.sin(currentAngle));
            double[] coordinateEntry = { previousCoordinate[0], xMoved, yMoved };
            addToHistory(coordinateEntry);
        } else if (Math.abs(leftCM) == Math.abs(rightCM) && leftCM > 0 && rightCM < 0) {
            double[] previousCoordinate = history[history.length - 1];
            // Find out how to determine rotation angle from leftCM and rightCM
            double[] coordinateEntry = { previousCoordinate[0], previousCoordinate[1], previousCoordinate[2] };
            addToHistory(coordinateEntry);
        }
    }

    public void rotate(double relativeAngle) {
        // Test tomorrow
    }

    /**
     *  Method to drive with a gyro enabled.
     *
     * @param distance   Distance (in cm) to move from current position.  Negative distance means move backwards.
     */
    public void gyroDrive(double distance) {
        double max, error, steer, leftSpeed, rightSpeed;
        int moveCounts = (int)(distance * PULSES_PER_CM);
        int newFrontLeftTarget = left_front.getCurrentPosition() + moveCounts;
        int newFrontRightTarget = right_front.getCurrentPosition() + moveCounts;
        int newBackLeftTarget = left_back.getCurrentPosition() + moveCounts;
        int newBackRightTarget = right_back.getCurrentPosition() + moveCounts;

        // This will set the target position and allow the motors to travel to that distance.
        left_front.setTargetPosition(newFrontLeftTarget);
        right_front.setTargetPosition(newFrontRightTarget);
        left_back.setTargetPosition(newBackLeftTarget);
        right_back.setTargetPosition(newBackRightTarget);

        setRunToPosition();

        double speed = Range.clip(Math.abs(AUTO_DRIVE_SPEED), 0.0, 1.0);
        setLeftAndRightPower(speed, speed);

        while (left_front.isBusy() && right_front.isBusy() && left_back.isBusy() && right_back.isBusy()) {
            error = getError(currentAngle);
            steer = getSteer(error, P_DRIVE_COEFF);

            if (distance < 0)
                steer *= -1.0;

            leftSpeed = speed - steer;
            rightSpeed = speed + steer;

            max = Math.max(Math.abs(leftSpeed), Math.abs(rightSpeed));
            if (max > 1.0)
            {
                leftSpeed /= max;
                rightSpeed /= max;
            }

            setLeftAndRightPower(leftSpeed, rightSpeed);
        }

        setZeroPower();
        setRunUsingEncoder();

        // Append to history
        double[] previousCoordinate = history[history.length - 1];
        double xMoved = previousCoordinate[1] + (distance * Math.cos(currentAngle));
        double yMoved = previousCoordinate[2] + (distance * Math.sin(currentAngle));
        double[] coordinateEntry = { previousCoordinate[0], xMoved, yMoved };
        addToHistory(coordinateEntry);
    }

    /**
     *  Method to turn with a gyro enabled.
     *
     * @param angle      Absolute Angle (in Degrees) relative to last gyro reset.
     *                   0 = fwd. +ve is CCW from fwd. -ve is CW from forward.
     *                   If a relative angle is required, add/subtract from current heading.
     */
    public void gyroTurn(double relativeAngle) {
        while (!onHeading(currentAngle)) {

        }

        currentAngle += relativeAngle;
        double[] previousCoordinate = history[history.length - 1];
        double[] coordinateEntry = { relativeAngle, previousCoordinate[1], previousCoordinate[2] };
        addToHistory(coordinateEntry);
    }

    /**
     *  Method to obtain & hold a heading for a finite amount of time
     *  Move will stop once the requested time has elapsed
     *
     * @param angle      Absolute Angle (in Degrees) relative to last gyro reset.
     *                   0 = fwd. +ve is CCW from fwd. -ve is CW from forward.
     *                   If a relative angle is required, add/subtract from current heading.
     * @param holdTime   Length of time (in seconds) to hold the specified heading.
     */
    public void gyroHold(double holdTime) {
        ElapsedTime holdTimer = new ElapsedTime();

        holdTimer.reset();
        while (holdTimer.time() < holdTime) {
            onHeading(currentAngle);
        }

        setLeftAndRightPower(0, 0);
    }

    boolean onHeading(double angle) {
        double error, steer, leftSpeed, rightSpeed;
        boolean onTarget = false;

        error = getError(angle);
        if (Math.abs(error) <= HEADING_THRESHOLD) {
            steer = 0.0;
            leftSpeed = 0.0;
            rightSpeed = 0.0;
            onTarget = true;
        } else {
            steer = getSteer(error, P_TURN_COEFF);
            rightSpeed = AUTO_TURN_SPEED * steer;
            leftSpeed = -rightSpeed;
        }

        setLeftAndRightPower(leftSpeed, rightSpeed);

        return onTarget;
    }

    /**
     * getError determines the error between the target angle and the robot's current heading
     * @param   targetAngle  Desired angle (relative to global reference established at last Gyro Reset).
     * @return  error angle: Degrees in the range +/- 180. Centered on the robot's frame of reference
     *          +ve error means the robot should turn LEFT (CCW) to reduce error.
     */
    public double getError(double targetAngle) {
        double robotError = targetAngle - gyro.getIntegratedZValue();
        while (robotError > 180) robotError -= 360;
        while (robotError <= -180) robotError += 360;
        return robotError;
    }

    /**
     * returns desired steering force.  +/- 1 range.  +ve = steer left
     * @param error   Error angle in robot relative degrees
     * @return
     */
    public double getSteer(double error, double PCoeff) {
        return Range.clip(error * PCoeff, -1, 1);
    }

    /**
     * returns current location from history
     * @return
     */
    public double[] getCurrentLocation() {
        double rot = 0;
        double xPos = 0;
        double yPos = 0;

        for (int i=0; i<history.length; i++) {
            rot += history[i][0];
            xPos += history[i][1];
            yPos += history[i][2];
        }

        return new double[] { rot, xPos, yPos };
    }

    /**
     * returns desired steering force.  +/- 1 range.  +ve = steer left
     * @param xMov   X Movement being verified
     * @param yMov   Y Movement being verified
     * @return
     */
    public boolean verifyMovement(double xMov, double yMov) {
        double[] position = getCurrentLocation();
        if ((position[1] + xMov) > FIELD_WIDTH_CM) {
            return false;
        } else if ((position[2] + yMov) > FIELD_HEIGHT_CM) {
            return false;
        }

        return true;
    }
}
