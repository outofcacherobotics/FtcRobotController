package main.java.org.firstinspires.ftc.teamcode;

@TeleOp(name="Basic TeleOp", group="DriverControlled")
public class TeleOp_Basic extends OpMode {
    private drivetrain Drivetrain;
    private Controls controls;

    @Override
    public void init() {
        drivetrain = new Drivetrain(hardwareMap, "left_front", "right_front", "left_back", "right_back");
        controls = new Controls(this.gamepad1, "slowmode");
        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void loop() {

    }
}
