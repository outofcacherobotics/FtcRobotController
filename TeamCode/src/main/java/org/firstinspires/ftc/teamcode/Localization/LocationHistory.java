package org.firstinspires.ftc.teamcode.Localization;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

// History of movements, used by Localizer.
// List of arrays, 0 index being rotation in degrees, 1 and 2 being X and Y
public class LocationHistory {
    Telemetry telemetry;

    // Field constants
    public static final double FIELD_HEIGHT_CM = 365.7;
    public static final double FIELD_WIDTH_CM = 365.7;

    // Starting position constants
    public static final double[] BLUE_BOTTOM_STARTING_COORDS = { 180, 11.5, 33.5 };
    public static final double[] BLUE_TOP_STARTING_COORDS = { 180, 11.5, 81.0 };
    public static final double[] RED_BOTTOM_STARTING_COORDS = { 0, FIELD_WIDTH_CM - 11.5, 33.5 };
    public static final double[] RED_TOP_STARTING_COORDS = { 0, FIELD_WIDTH_CM - 11.5, 81.0 };

    public double[] initialCoords = new double[3];
    public List<double[]> history = Collections.synchronizedList(new ArrayList<double[]>());

    public LocationHistory(String setupPosition, Telemetry telemetry) {
        telemetry.addData("Initializing coords: ", setupPosition);
        switch (setupPosition) {
            case "blueBottom":
                initialCoords = BLUE_BOTTOM_STARTING_COORDS;
                pushCoords(BLUE_BOTTOM_STARTING_COORDS);
            case "blueTop":
                initialCoords = BLUE_TOP_STARTING_COORDS;
                pushCoords(BLUE_TOP_STARTING_COORDS);
            case "redBottom":
                initialCoords = RED_BOTTOM_STARTING_COORDS;
                pushCoords(RED_BOTTOM_STARTING_COORDS);
            case "redTop":
                initialCoords = RED_TOP_STARTING_COORDS;
                pushCoords(RED_TOP_STARTING_COORDS);
        }

        telemetry.addData("New coords: ", history);
        this.telemetry = telemetry;
    }

    public List<double[]> getHistory() { return history; };

    public int getHistoryLength() { return history.size(); };

    public double[] getPreviousCoords() {
        int index = history.size() - 1;
        if (index < 0) {
            return null;
        }

        double[] last = history.get(index);
        telemetry.addData("Last: ", last);
        return last;
    };

    public void pushCoords(double[] newCoords) {
        try {
            telemetry.addData("Adding coords: ", newCoords);
            history.add(newCoords);
        } catch (Exception e) {
            e.printStackTrace();
        }
    };

    // Creates image representing history 2D
    public void createImage(String outputPath) {}

    public String toString() {
        String resultString = "";

        for (int i = 0; i<history.size(); i++) {
            resultString += String.join(", ", StringUtils.join(ArrayUtils.toObject(history.get(i)), " - "));
        }

        return resultString;
    }
}
