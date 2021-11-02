package main.java.org.firstinspires.ftc.teamcode;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import static org.junit.Assert.*;
import org.json.*;

class Main { 
    public static void main(String[] args){
        boolean robotRunning = Status.getRobotStatus();
        System.out.println(robotRunning);
    }
    /*JSON string set to running: true if robot is running, likely not going to work first tries*/
}