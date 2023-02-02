package frc.robot.arm;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.lib.telemetry.WidgetsAndLayouts;
import frc.robot.Robot;

//the funny code

public class ArmTelemetry {
    protected ShuffleboardTab tab;

    public ArmTelemetry() {
        tab = Shuffleboard.getTab("Arm");
        tab.addString("Lower Limit Switch",()-> Robot.arm.lowerLimitSwitchStatus()).withSize(3, 2).withPosition(0, 0);
        tab.addString("Upper Limit Switch",()-> Robot.arm.UpperLimitSwitchStatus()).withSize(3, 2).withPosition(0, 4);
        tab.addDouble("Encoder Cnt", ()->Robot.arm.getEncoder())                   .withSize(3, 3).withPosition(0, 6);
        WidgetsAndLayouts.TalonFXLayout("Motor", tab, Robot.arm.mArmMotor)         .withSize(3, 5).withPosition(6, 0);
    }
}
