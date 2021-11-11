package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import java.util.Locale;

/**
 * AutoPathController is an abstraction of the drivetrain. Use this for Autos
 * where a predefined path needs to be executed.
 */
public class AutoPathController {
    DcMotor left_front, right_front, left_back, right_back;

    Localizer localizer;

    // https://first-tech-challenge.github.io/SkyStone/com/qualcomm/hardware/bosch/BNO055IMUImpl.html
    BNO055IMU imu;
    Orientation angles;
    Acceleration gravity;

    // Orientation relative to signingthe top of the field.
    // https://github.com/acmerobotics/road-runner/blob/master/gui/src/main/resources/field.png
    double currentAngle;

    static final ElapsedTime runtime = new ElapsedTime();

    // Drive speed constants
    static final double AUTO_DRIVE_SPEED = 0.7;
    static final double AUTO_TURN_SPEED = 0.5;

    static final double     HEADING_THRESHOLD       = 1 ;      // As tight as we can make it with an integer gyro
    static final double     P_TURN_COEFF            = 0.1;     // Larger is more responsive, but also less stable
    static final double     P_DRIVE_COEFF           = 0.15;     // Larger is more responsive, but also less stable

    // Motor encoder configuration constants
    static final double PULSES_PER_REVOLUTION = 384.5;
    static final double WHEEL_DIAMETER_CM = 8.7;
    static final double PULSES_PER_CM = PULSES_PER_REVOLUTION / (WHEEL_DIAMETER_CM * 3.1415);

    public AutoPathController(
            HardwareMap hardwareMap,
            String left_front_name,
            String right_front_name,
            String left_back_name,
            String right_back_name,
            String setupPosition,
            double setupAngle
    ) {
        initIMU(hardwareMap);

        this.left_front = hardwareMap.get(DcMotor.class, left_front_name);
        this.right_front = hardwareMap.get(DcMotor.class, right_front_name);
        this.left_back = hardwareMap.get(DcMotor.class, left_back_name);
        this.right_back = hardwareMap.get(DcMotor.class, right_back_name);

        localizer = new Localizer(setupPosition);
        
        currentAngle = setupAngle;
    }

    public void update() {
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        gravity = imu.getGravity();
    }

    public void initIMU(HardwareMap hardwareMap) {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
        imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);
    }

    public void setZeroPowerBehavior() {
        left_front.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        right_front.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        left_back.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        right_back.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void initHardware() {
        setStopAndResetEncoder();

        setRunUsingEncoder();
    }

    public void initHardwareWithGyro() {
        setStopAndResetEncoder();

//        gyro.calibrate();

        setRunUsingEncoder();

//        gyro.resetZAxisIntegrator();
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
     * @param rightCM   Distance (in cm) to move left motors from current position.
     * @param leftCM   Distance (in cm) to move right motors from current position.
     */
    public void drive(double rightCM, double leftCM, double timeoutS) {
        // For now, only straightline allowed
        int leftCounts = (int) (leftCM * PULSES_PER_CM);
        int rightCounts = (int) (rightCM * PULSES_PER_CM);

        int newFrontLeftTarget = left_front.getCurrentPosition() + leftCounts;
        int newFrontRightTarget = right_front.getCurrentPosition() + rightCounts;
        int newBackLeftTarget = left_back.getCurrentPosition() + leftCounts;
        int newBackRightTarget = right_back.getCurrentPosition() + rightCounts;

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
            double[] previousCoordinate = localizer.locationHistory.getPreviousCoordinate();
            double xMoved = previousCoordinate[1] + (leftCM * Math.cos(currentAngle));
            double yMoved = previousCoordinate[2] + (leftCM * Math.sin(currentAngle));
            double[] coordinateEntry = { previousCoordinate[0], xMoved, yMoved };
            localizer.locationHistory.pushCoords(coordinateEntry);
        } else if (Math.abs(leftCM) == Math.abs(rightCM) && leftCM > 0 && rightCM < 0) {
            // Not recommended for rotation, instead use @rotate with a relative angle.
            double[] previousCoordinate = localizer.locationHistory.getPreviousCoordinate();
            // Find out how to determine rotation angle from leftCM and rightCM
            double[] coordinateEntry = { previousCoordinate[0], previousCoordinate[1], previousCoordinate[2] };
            localizer.locationHistory.pushCoords(coordinateEntry);
        }
    }

    /**
     *  Method to drive with a gyro enabled.
     *
     * @param relativeAngle Angle relative to currentAngle that robot is to turn.
     */
    public void rotate(double relativeAngle) {
        // NOT IMPLEMENTED
        // Convert angle to inches
        double left = 0.0;
        double right = 0.0;
        drive(left, right, 4.0);
    }

    /**
     *  Method to drive with a gyro enabled.
     *
     * @param distance   Distance (in cm) to move forward from current position.
     */
    public void gyroDrive(double distance) {
        verifyMovement(distance);

        double max;
        double error;
        double steer;
        double leftSpeed;
        double rightSpeed;

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
        double[] previousCoordinate = localizer.locationHistory.getPreviousCoordinate();
        double xMoved = previousCoordinate[1] + (distance * Math.cos(currentAngle));
        double yMoved = previousCoordinate[2] + (distance * Math.sin(currentAngle));
        double[] coordinateEntry = { previousCoordinate[0], xMoved, yMoved };
        localizer.locationHistory.pushCoords(coordinateEntry);
    }

    /**
     *  Method to turn with a gyro enabled.
     *
     * @param relativeAngle      Absolute Angle (in Degrees) relative to last gyro reset.
     *                   0 = fwd. +ve is CCW from fwd. -ve is CW from forward.
     *                   If a relative angle is required, add/subtract from current heading.
     */
    public void gyroTurn(double relativeAngle) {
        while (!onHeading(currentAngle)) {}

        currentAngle += relativeAngle;
        double[] previousCoordinate = localizer.locationHistory.getPreviousCoordinate();
        double[] coordinateEntry = { relativeAngle, previousCoordinate[1], previousCoordinate[2] };
        localizer.locationHistory.pushCoords(coordinateEntry);
    }

    /**
     *  Method to obtain & hold a heading for a finite amount of time
     *  Move will stop once the requested time has elapsed
     *
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

    public boolean onHeading(double angle) {
        double error, steer, leftSpeed, rightSpeed;
        boolean onTarget = false;

        error = getError(angle);
        if (Math.abs(error) <= HEADING_THRESHOLD) {
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
     * @param   angle  Desired angle (relative to global reference established at last Gyro Reset).
     * @return  error angle: Degrees in the range +/- 180. Centered on the robot's frame of reference
     *          +ve error means the robot should turn LEFT (CCW) to reduce error.
     */
    public double getError(double angle) {
        double robotError = angle - (double) angles.firstAngle;
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

        for (double[] entry : localizer.locationHistory.getHistory()) {
            rot += entry[0];
            xPos += entry[1];
            yPos += entry[2];
        }

        return new double[] { rot, xPos, yPos };
    }

    /**
     * returns desired steering force.  +/- 1 range.  +ve = steer left
     * @param forwardMovement   forward movement being verified
     * @return
     */
    public boolean verifyMovement(double forwardMovement) {
        double xMov = 0.0;
        double yMov = 0.0;
        double[] position = getCurrentLocation();

        if ((position[2] % 90) == 0) {
            // Moves in one direction
        } else {
            xMov = Math.cos(position[2]) * forwardMovement;
            yMov = Math.sin(position[2]) * forwardMovement;


        }
        if ((position[1] + xMov) > LocationHistory.FIELD_WIDTH_CM || (position[2] + yMov) > LocationHistory.FIELD_HEIGHT_CM) {
            return false;
        }

        return true;
    }

    public String formatAngle(AngleUnit angleUnit, double angle) {
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }

    public String formatDegrees(double degrees){
        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
    }

    public LocationHistory getHistory() { return localizer.locationHistory; };
}
