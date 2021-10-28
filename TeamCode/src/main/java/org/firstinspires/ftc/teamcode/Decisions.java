package main.java.org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

public class Decisions {
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

    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {
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
    private void initTfod() {
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

    public void printTensorflowRecognitions() {
        for (recognition : updatedRecognitions) {
            telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
            telemetry.addData(
                String.format(
                    "label (%d)",
                    i
                ),
                recognition.getLabel()
            );
            telemetry.addData(
                String.format(
                    "  left,top (%d)",
                    i
                ),
                "%.03f , %.03f",
                recognition.getLeft(),
                recognition.getTop()
            );
            telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                    recognition.getRight(), recognition.getBottom());
        }
    }

    public int getDecision() {
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
    }