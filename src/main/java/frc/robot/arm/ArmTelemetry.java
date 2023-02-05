package frc.robot.arm;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.robot.Robot;

//the funny code

public class ArmTelemetry {
    protected ShuffleboardTab tab;

    public ArmTelemetry() {
        tab = Shuffleboard.getTab("Arm");
        tab.addString("Lower Limit Switch",()-> Robot.arm.lowerLimitSwitchStatus()).withPosition(0, 0).withSize(3, 2);
        tab.addString("Upper Limit Switch",()-> Robot.arm.upperLimitSwitchStatus()).withPosition(0, 2).withSize(3, 2);
        tab.addDouble("Encoder Cnt", ()->Robot.arm.getEncoderCnt())                .withPosition(0, 4).withSize(3, 2);
        tab.addDouble("Arm Angle", ()->Robot.arm.getArmAngle())                    .withPosition(0, 6).withSize(3, 2);
        //WidgetsAndLayouts.TalonFXLayout("Motor", tab, Robot.arm.mArmMotor)         .withPosition(5, 0).withSize(6, 6);
    }
}
