package frc.robot.intake;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.robot.intake.commands.IntakeConeCmd;

public class IntakeTelemetry {
    protected ShuffleboardTab tab;

    public IntakeTelemetry( IntakeSubSys intake) {
        tab = Shuffleboard.getTab("Intake");
        tab.addString("Cube Eject Detect",()-> intake.cubeEjectDetectStatus())  .withPosition(0, 0).withSize(3, 2);
        tab.addString("Cube Retract Detect",()-> intake.cubeRetractDetectStatus())  .withPosition(0, 2).withSize(3, 2);
        tab.addString("Cone Detect",()-> intake.coneDetectStatus())  .withPosition(0, 3).withSize(0, 4);
        tab.addNumber("Upper Motor Speed", () -> intake.intakeUpperMotor.get()) .withPosition(4, 0).withSize(3, 2);
        tab.addNumber("Lower Motor Speed", () -> intake.intakeLowerMotor.get()) .withPosition(4, 3).withSize(3, 2);
        tab.addString("State Machine value", () -> IntakeConeCmd.getStateString()) .withPosition(0, 6).withSize(3,2);
        tab.addNumber("Cone IR Val", () -> intake.coneDetectSensor.getAverageVoltage()).withPosition(8, 0).withSize(3,2);
        tab.addNumber("Cube IR Val", () -> intake.cubeDetectSensor.getAverageVoltage()).withPosition(8, 3).withSize(3,2);
    }
}
