package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

/**
* Docs
* https://first-tech-challenge.github.io/SkyStone/org/firstinspires/ftc/robotcore/external/tfod/package-summary.html
 * https://github.com/FIRST-Tech-Challenge/FtcRobotController/wiki/Java-Sample-TensorFlow-Object-Detection-Op-Mode
*/
public class Decisions {
    private String teamColor = "blue"; // Figure out how to dynamically set

    /**
     * Ideal coordinates of the robot to correctly perceive the game objects (X, Y, rot)
     * X is reflected depending on the competing team.
     *
     * Technically the robot is setup in this position, but we will need to angle the camera
     * properly to get a view of the object.
     */
    public int[] IDEAL_CAMERA_COORDS = {};
    public double POSITION_OFFSET = 0.1;

    /**
     * Three in length, starts with 0
     */
    private int[] LEFT_SEPERATION_BOUNDARIES = {};

    /**
     * Three in length, ends with the width of the image
     */
    private int[] RIGHT_SEPERATION_BOUNDARIES = {};

    /**
     * Model and labels that are going to be detected.
     * Customize at https://github.com/FIRST-Tech-Challenge/fmltc/blob/main/doc/usage.md#overview
     */
    private static final String TFOD_MODEL_ASSET = "FreightFrenzy_BCDM.tflite";
    private static final String[] TFOD_LABELS = {
            "Ball",
            "Cube",
            "Duck",
            "Marker"
    };

    private static final String VUFORIA_KEY =
            "AY1QVL7/////AAABmZvbIQTxgEgxrat3T09oet0ZnR7xp5SehuUNW6rLWPHQdzhBj4eDeg4rH/RS4rmJlDMdDZ6tWquBZXKUtswzper6SOOXYkKWUWON9Nk1+8v64b55E99uL/yvfk5K/5j9H1Ievzln1xCeaJrdT0U2cLHoGbMGLoAebvpI36twHCphMPZ6KL0S5vXuEzxLkmCmS5ZIrZFP2JnV2Z1InhYQtucuT+fY2CGEgypHvpJIBd9fpw7CjHmKj1BPjnWJVlY5anzGeEu/7CHc5+EFvxJBcwrlXbqhnBDXIzJ1Hy7jU8cmAGov3sxUDWvP3paD7H08UX9iN+KR2octx7fcsH9puwjyunvWiIY2Rw7imR0Pkh6Z";

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private VuforiaLocalizer vuforia;

    /**
     * {@link #tfod} is the variable we will use to store our instance of the TensorFlow Object
     * Detection engine.
     */
    private TFObjectDetector tfod;

    public Decisions(HardwareMap hardwareMap, String teamColor) {
        this.teamColor = teamColor;

        initVuforia(hardwareMap);
        initTfod(hardwareMap);
    }

    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia(HardwareMap hardwareMap) {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     * https://first-tech-challenge.github.io/SkyStone/org/firstinspires/ftc/robotcore/external/tfod/TFObjectDetector.html
     */
    private void initTfod(HardwareMap hardwareMap) {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 320;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, TFOD_LABELS);

        if (tfod != null) {
            tfod.activate();

            // https://github.com/FIRST-Tech-Challenge/FtcRobotController/wiki/Using-a-Custom-TensorFlow-Model-with-Java#adjusting-the-zoom-factor
            tfod.setZoom(2.5, 16.0/9.0);
        }
    }

    /**
     * https://first-tech-challenge.github.io/SkyStone/org/firstinspires/ftc/robotcore/external/tfod/Recognition.html
     */
    private List<Recognition> getTensorflowRecognitions() {
        return tfod.getUpdatedRecognitions();
    }

    /**
     * After in the right camera position on the field, call this to decide which spot the duck is in.
     */
    public int getDecision(int[] currentCoordiates) {
        List<Recognition> updatedRecognitions = getTensorflowRecognitions();
        /*
            identify 3 objects - 1 duck, 2 non-duck
            identify which object is the duck
            return an integer ({0, 1, 2}) based on duck position

            switch (getDecision()) {
                case 0:
                    // left
                case 1:
                    // middle
                case 2:
                    // right
            }
        */

        // Check for acceptable currentCoordinates
        if (Math.abs(currentCoordiates[0] - IDEAL_CAMERA_COORDS[0]) > POSITION_OFFSET ||
                Math.abs(currentCoordiates[1] - IDEAL_CAMERA_COORDS[1]) > POSITION_OFFSET) {
            return 0;
        }
        
        /*
         * Loop through new recognitions, for each one decide whether it is in 
         * the pre defined boundaries above.
         */
        for (Recognition recognition : updatedRecognitions) {
            if (recognition.getLabel() == "Duck") {
                for (int i=0; i<LEFT_SEPERATION_BOUNDARIES.length; i++) {
                    if (recognition.getLeft() > LEFT_SEPERATION_BOUNDARIES[i] &&
                            recognition.getRight() < RIGHT_SEPERATION_BOUNDARIES[i]) {
                        return i + 1;
                    }
                }
            }
        }

        return 0;
    }

    // Blue or red, use in every OpMode
    public void setTeamColor(String teamColor) { this.teamColor = teamColor; };
}