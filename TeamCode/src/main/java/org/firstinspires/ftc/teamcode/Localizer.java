package org.firstinspires.ftc.teamcode;

/*
 * ConceptVuforiaFieldNavigationWebcam
 * ConceptVumarkIdentificationWebcam
 * Will implement Vuforia localization
 */
public class Localizer {
    double[] currentLocation;

    public Localizer() {};

    public void setCurrentLocation(double[] currentLocation) {
        this.currentLocation = currentLocation;
    }
}