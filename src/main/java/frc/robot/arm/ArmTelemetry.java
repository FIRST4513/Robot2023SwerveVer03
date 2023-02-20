package frc.robot.arm;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.robot.Robot;

public class ArmTelemetry {
    protected ShuffleboardTab tab;

    public ArmTelemetry( ArmSubSys arm) {
        tab = Shuffleboard.getTab("Arm");
        tab.addString("Lower Limit Switch",()-> arm.lowerLimitSwitchStatus()).withPosition(0, 0).withSize(3, 2);
        tab.addString("Upper Limit Switch",()-> arm.upperLimitSwitchStatus()).withPosition(0, 2).withSize(3, 2);
        tab.addDouble("Encoder Cnt", ()->arm.getEncoderCnt())                .withPosition(3, 0).withSize(3, 2);
        tab.addDouble("Arm Angle", ()->arm.getArmAngle())                    .withPosition(3, 2).withSize(3, 2);
        tab.addNumber("Motor Pwr", () -> arm.mArmMotor.get())                .withPosition(6, 0).withSize(3, 2);
        tab.addNumber("Input from Operator Joystick", () -> Robot.operatorGamepad.getArmInput()).withPosition(6, 2).withSize(3, 2);
        
        tab.add("Arm Commands",arm)                                          .withPosition(0, 4).withSize(5, 2);
    }
}
