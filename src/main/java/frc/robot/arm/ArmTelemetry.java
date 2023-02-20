package frc.robot.arm;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.Robot;
import frc.robot.elevator.commands.ElevReleaseArmCmd;

public class ArmTelemetry {
    protected ShuffleboardTab tab;

    public ArmTelemetry( ArmSubSys arm) {
        tab = Shuffleboard.getTab("Arm");
        tab.addString("Lower Limit Switch",()-> arm.lowerLimitSwitchStatus()).withPosition(0, 0).withSize(3, 2);
        tab.addString("Upper Limit Switch",()-> arm.upperLimitSwitchStatus()).withPosition(0, 2).withSize(3, 2);

        tab.add("Arm Commands",arm)                                          .withPosition(0, 4).withSize(5, 2);

        tab.addDouble("Encoder Cnt", ()->arm.getEncoderCnt())                .withPosition(3, 0).withSize(3, 2);
        tab.addDouble("Arm Angle", ()->arm.getArmAngle())                    .withPosition(3, 2).withSize(3, 2);

        tab.addNumber("Motor Pwr", () -> arm.mArmMotor.get())                .withPosition(6, 0).withSize(3, 2);

        tab.addNumber("Input from Operator Joystick",
                        () -> Robot.operatorGamepad.getArmInput())           .withPosition(6, 2).withSize(3, 2);
        
        //SmartDashboard.putData("Release Arm", new ElevReleaseArmCmd());
        tab.add("Release Arm", new ElevReleaseArmCmd())                     .withPosition(16, 0).withSize(3, 2);

        tab.add("MM To 5", 
        new RunCommand( () -> Robot.arm.setMMangle( 5.0 ), Robot.arm))      .withPosition(10, 0).withSize(3, 2);

        tab.add("MM To 20", 
        new RunCommand( () -> Robot.arm.setMMangle( 20.0 ), Robot.arm))     .withPosition(10, 3).withSize(3, 2);

        tab.add("MM To 40", 
        new RunCommand( () -> Robot.arm.setMMangle( 40.0 ), Robot.arm))     .withPosition(10, 6).withSize(3, 2);

        tab.add("MM To -20", 
        new RunCommand( () -> Robot.arm.setMMangle( -20.0 ), Robot.arm))    .withPosition(13, 0).withSize(3, 2);

        tab.add("MM To -40", 
        new RunCommand( () -> Robot.arm.setMMangle( -40.0 ), Robot.arm))    .withPosition(13, 3).withSize(3, 2);

        tab.add("MM To 0", 
        new RunCommand( () -> Robot.arm.setMMangle( 0.0 ), Robot.arm))      .withPosition(13, 6).withSize(3, 2);
        
    }
}
