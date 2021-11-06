package org.firstinspires.ftc.teamcode;

// run: https://stackoverflow.com/questions/11527941/java-packages-cannot-find-symbol
class Parsetest {
    public static void main(String[] args) {
        Status robotStatus = new Status();
        boolean robotRunning = robotStatus.getRobotStatus();
        System.out.println(robotRunning);
    }
}