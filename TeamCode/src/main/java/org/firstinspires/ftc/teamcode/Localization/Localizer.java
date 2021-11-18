package org.firstinspires.ftc.teamcode.Localization;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.LocationHistory;

/*
 * ConceptVuforiaFieldNavigationWebcam
 * ConceptVumarkIdentificationWebcam
 * Will implement Vuforia localization
 */
public class Localizer {
    Telemetry telemetry;
    public LocationHistory locationHistory;

    public Localizer(String setupPosition, Telemetry telemetry) {
        this.telemetry = telemetry;
        locationHistory = new LocationHistory(setupPosition, telemetry);
    };
}