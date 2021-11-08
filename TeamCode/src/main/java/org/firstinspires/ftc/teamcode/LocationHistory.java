package org.firstinspires.ftc.teamcode;

import java.util.Arrays;

// History of movements, used by Localizer.
// List of arrays, 0 index being rotation in degrees, 1 and 2 being X and Y
public class LocationHistory {
    static final double FIELD_HEIGHT_CM = 365.7;
    static final double FIELD_WIDTH_CM = 365.7;

    double[][] history;

    public double[][] getHistory() { return history; };

    public int getHistoryLength() { return history.length; };

    public double[] getPreviousCoordinate() { return history[getHistoryLength() - 1]; };

    public void pushCoords(double[] newCoords) {
        history = Arrays.copyOf(history, history.length + 1);
        history[history.length - 1] = newCoords;
    };

    // Creates image representing history 2D
    public void createImage(String outputPath) {}
}
