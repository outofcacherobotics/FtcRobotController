package org.firstinspires.ftc.teamcode;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
//import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

/**
 * Sets online status of robot for website
 */
public class Status {
    static final String API_URL = "https://us-central1-robotics-api.cloudfunctions.net";
    // String API_KEY = System.getenv("outofcache_api_key");

    OkHttpClient client;
    private boolean robotOn;

    public Status() {
        this.client = new OkHttpClient();
        this.robotOn = getRobotStatus();
    }

    public boolean getRobotOn() {
        return this.robotOn;
    }

    public void setRobotOn(boolean robotOn) {
        this.robotOn = robotOn;
    }

    public boolean getRobotStatus() {
        Request request = new Request.Builder()
                .url(API_URL)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        ResponseBody responseBody;
        try {
            responseBody = client.newCall(request).execute().body();
            StatusResponse statusResponse = objectMapper.readValue(responseBody.string(), StatusResponse.class);
            System.out.println(statusResponse.getRunning());
            return statusResponse.getRunning();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void startRobot() {
        this.setRobotOn(true);
    }

    public void stopRobot() {
        this.setRobotOn(false);
    }
}

class StatusResponse {
    boolean running;

    public boolean getRunning() { return running; }
}

class StatusTest {
    @Test
    public void TestStartRobot() {
        Status status = new Status();
        assertFalse(status.getRobotOn());
        status.startRobot();
        assertTrue(status.getRobotOn());
    }

    @Test
    public void TestStopRobot() {
        Status status = new Status();
        assertFalse(status.getRobotOn());
        status.startRobot();
        status.startRobot();
        assertTrue(status.getRobotOn());
    }
}
