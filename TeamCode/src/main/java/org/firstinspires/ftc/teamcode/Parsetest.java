package main.java.org.firstinspires.ftc.teamcode;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import static org.junit.Assert.*;
import org.json.*;

Class main{
    public static void main(String[] args){
        boolean robotRunning = Status.getRobotStatus(robotOn);
            if (robotRunning) {
                String statusJsonString = '{
                    "running": true
                  }';
            } else {
                String statusJsonString = '{
                    "running": false
                  }';
            }
    }
    /* creates boolean robotRunning from getRobotStatus, robotRunning being true creates "running: true" JSON string
       otherwise "running: false" string is created
       likely not going to work first attempts, fix during meet if screwed up
       */
}