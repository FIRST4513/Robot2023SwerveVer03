package frc.robot.intake;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.lib.telemetry.WidgetsAndLayouts;
import frc.robot.Robot;

public class IntakeTelemetry {
    protected ShuffleboardTab tab;

    public IntakeTelemetry() {
        tab = Shuffleboard.getTab("Intake");
        tab.addString("Cube Limit Switch",()-> Robot.intake.cubeDetectSwitchStatus())   .withPosition(0, 0).withSize(3, 2);
        tab.addString("Cone Detect Switch",()-> Robot.intake.coneDetectSwitchStatus())  .withPosition(0, 3).withSize(3, 2);
        WidgetsAndLayouts.TalonFXLayout("Motor", tab, Robot.intake.intakeUpperMotor)    .withPosition(4, 0).withSize(3, 7);
        WidgetsAndLayouts.TalonFXLayout("Motor", tab, Robot.intake.intakeUpperMotor)    .withPosition(8, 0).withSize(3, 7);
    }
}
