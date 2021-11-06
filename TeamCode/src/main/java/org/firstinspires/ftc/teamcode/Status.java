package org.firstinspires.ftc.teamcode;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

// Read something like this: https://openjdk.java.net/groups/net/httpclient/recipes.html
public class Status {
    static final String API_URL = "https://us-central1-robotics-api.cloudfunctions.net";
    // String API_KEY = System.getenv("outofcache_api_key");

    HTTPClient client;
    private boolean robotOn;

    public Status() {
        this.client = HTTPClient.newHTTPClient();
        this.robotOn = getRobotStatus();
    }

    public boolean getRobotOn() {
        return this.robotOn;
    }

    public void setRobotOn(boolean robotOn) {
        this.robotOn = robotOn;
    }

    public boolean getRobotStatus() {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(this.API_URL + "/setOn"))
                .header("accept", "application/json")
                .build();

        HttpResponse<Object> response;
        try {
            response = client.send(request, HTTPResponse.BodyHandlers);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        int statusCode = response.statusCode();
        if (statusCode >= 200 && statusCode <= 299) {
            throw new Error("request failed");
        }

        ObjectMapper mapper = new ObjectMapper();
        StatusResponse statusResponse = mapper.readValue(response.Body(), new TypeReference<StatusResponse>);
    }

    public void startRobot() {
        // make post
        HttpRequest request = HttpRequest.newBuilder(
                URI.create(this.API_URL + "/setOn"))
                .header("accept", "application/json")
                .build();

        HttpResponse<Object> response;
        try {
            response = client.send(request, null);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        int statusCode = response.statusCode();
        if (statusCode >= 200 && statusCode <= 299) {
            throw new Error("request failed");
        }

        this.setRobotOn(true);
    }

    public void stopRobot() {

        // Make post
        HttpRequest request = HttpRequest.newBuilder(
                URI.create(API_URL + "/setOff"))
                .header("accept", "application/json")
                .build();

        HttpResponse<Object> response;
        try {
            response = client.send(request, null);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        int statusCode = response.statusCode();
        if (statusCode >= 200 && statusCode <= 299) {
            throw new Error("request failed");
        }

        this.setRobotOn(false);
    }

    public static boolean parseRobotRunning(String responseBody) {
        boolean isRunning;
        JSONArray running = new JSONArray(responseBody);
        try {
            isRunning = running.getBoolean(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return isRunning;
    }
}

@Test
class StatusTest {
    public void TestStartRobot() {
        Status status = new Status();
        assertEquals(status.getRobotOn(), false);
        status.startRobot();
        assertEquals(status.getRobotOn(), true);
    }

    public void TestStopRobot() {
        Status status = new Status();
        assertEquals(status.getRobotOn(), false);
        status.startRobot();
        status.startRobot();
        assertEquals(status.getRobotOn(), false);
    }
}
