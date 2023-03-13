package frc.robot.intake;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class IntakeTelemetry {
    protected ShuffleboardTab tab;

    public IntakeTelemetry( IntakeSubSys intake) {
        tab = Shuffleboard.getTab("Intake");
        tab.addBoolean("Gamepiece Detected?", () -> intake.isGamepieceDetected()) .withPosition(0, 0).withSize(3, 2);
        tab.addNumber("Motor Speed:",         () -> intake.getMotorSpeed())       .withPosition(0, 2).withSize(3, 2);
        tab.addNumber("IR Sensor Value:",     () -> intake.getSensorVal())        .withPosition(0, 4).withSize(3, 2);
        tab.add("Current Commands:",          intake)                             .withPosition(0, 6).withSize(5, 2);
    }
}
