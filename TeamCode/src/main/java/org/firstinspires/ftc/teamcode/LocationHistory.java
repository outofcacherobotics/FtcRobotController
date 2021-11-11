package org.firstinspires.ftc.teamcode;

import java.util.ArrayList;
import java.util.Arrays;

// History of movements, used by Localizer.
// List of arrays, 0 index being rotation in degrees, 1 and 2 being X and Y
public class LocationHistory {
    // Field constants
    public static final double FIELD_HEIGHT_CM = 365.7;
    public static final double FIELD_WIDTH_CM = 365.7;

    // Starting position constants
    public static final double[] BLUE_BOTTOM_STARTING_COORDS = { 11.5, 33.5 };
    public static final double[] BLUE_TOP_STARTING_COORDS = { 11.5, 81.0 };
    public static final double[] RED_BOTTOM_STARTING_COORDS = { FIELD_WIDTH_CM - 11.5, 33.5 };
    public static final double[] RED_TOP_STARTING_COORDS = { FIELD_WIDTH_CM - 11.5, 81.0 };

    public double[] initialCoords = new double[3];
    public ArrayList<double[]> history = new ArrayList<double[]>();

    public LocationHistory(String setupPosition) {
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
    }

    public ArrayList<double[]> getHistory() { return history; };

    public int getHistoryLength() { return history.size(); };

    public double[] getPreviousCoordinate() { return history.get(history.size() - 1); };

    public void pushCoords(double[] newCoords) {
        history.add(newCoords);
    };

    // Creates image representing history 2D
    public void createImage(String outputPath) {}
}
