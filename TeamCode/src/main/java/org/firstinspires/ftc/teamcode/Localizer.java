package org.firstinspires.ftc.teamcode;

/*
 * ConceptVuforiaFieldNavigationWebcam
 * ConceptVumarkIdentificationWebcam
 * Will implement Vuforia localization
 */
public class Localizer {
    public LocationHistory locationHistory;

    public Localizer(String setupPosition) {
        locationHistory = new LocationHistory(setupPosition);
    };
}