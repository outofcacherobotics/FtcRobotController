package main.java.org.firstinspires.ftc.teamcode;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import static org.junit.Assert.*;

public class Status {
    private final String API_URL = "https://us-central1-robotics-api.cloudfunctions.net";
    // private String API_KEY = System.getenv("outofcache_api_key");
    private HttpClient client = null;
    private boolean robotOn;

    public Status() {
        this.client = HttpClient.newHttpClient();
        this.robotOn = this.getRobotStatus();
    }

    public boolean getRobotOn() {
        return this.robotOn;
    }

    public void setRobotOn(boolean robotOn) {
        this.robotOn = robotOn;
    }

    public boolean getRobotStatus() {
        HttpRequest request = HttpRequest.newBuilder(
                URI.create(this.API_URL + "/status"))
                .header("accept", "application/json")
                .build();

        var response = this.client.send(request, new JsonBodyHandler<>(APOD.class));
        var statusCode = response.statusCode();
        if (!(statusCode >= 200 && statusCode <= 299)) {
            throw new Error("request failed");
        }

        this.robotOn = response.body().get().running;
        return this.robotOn;
    }

    public void startRobot() {
        this.setRobotOn(true);

        HttpRequest request = HttpRequest.newBuilder(
                URI.create(this.API_URL + "/setOn"))
                .header("accept", "application/json")
                .build();

        var response = this.client.send(request, new JsonBodyHandler<>(APOD.class));
        var statusCode = response.statusCode();
        if (!(statusCode >= 200 && statusCode <= 299)) {
            throw new Error("request failed");
        }
    }

    public void stopRobot() {
        this.setRobotOn(true);

        HttpRequest request = HttpRequest.newBuilder(
                URI.create(this.API_URL + "/setOff"))
                .header("accept", "application/json")
                .build();

        var response = this.client.send(request, new JsonBodyHandler<>(APOD.class));
        var statusCode = response.statusCode();
        if (!(statusCode >= 200 && statusCode <= 299)) {
            throw new Error("request failed");
        }
    }
}

@Test
class StatusTest {
    public void TestStartRobot() {
        Status status = new Status();
        assertEquals(false, status.getRobotOn());
        status.startRobot();
        assertEquals(true, status.getRobotOn());
    }

    public void TestStopRobot() {
        Status status = new Status();
        assertEquals(false, status.getRobotOn());
        status.startRobot();
        status.startRobot();
        assertEquals(false, status.getRobotOn());
    }
}