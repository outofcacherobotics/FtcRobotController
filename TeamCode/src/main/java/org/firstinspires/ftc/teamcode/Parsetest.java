package org.firstinspires.ftc.teamcode;

import java.io.IOException;

class Parsetest {
    public static void main(String[] args){
        Status robotStatus = new Status();
        boolean robotRunning = robotStatus.getRobotStatus();
        System.out.println(robotRunning);
    }
}