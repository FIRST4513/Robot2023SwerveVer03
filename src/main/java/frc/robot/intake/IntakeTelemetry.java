package frc.robot.intake;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.robot.Robot;

public class IntakeTelemetry {
    protected ShuffleboardTab tab;

    public IntakeTelemetry() {
        tab = Shuffleboard.getTab("Intake");
        tab.addString("Cube Limit Switch",()-> Robot.intake.cubeDetectSwitchStatus());
        tab.addString("Cone Detect Switch",()-> Robot.intake.coneDetectSwitchStatus());
    }


}
