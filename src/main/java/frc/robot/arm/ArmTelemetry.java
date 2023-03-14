package frc.robot.arm;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.Robot;
import frc.robot.arm.ArmSubSys.ArmStates;
// import frc.robot.arm.commands.ArmCalibrateCmd;
import frc.robot.elevator.commands.ElevReleaseArmCmd;

public class ArmTelemetry {
    protected ShuffleboardTab tab;

    public ArmTelemetry( ArmSubSys arm) {
        tab = Shuffleboard.getTab("Arm");
        tab.addString("Retract Limit Switch",()-> arm.retractLimitSwitchStatus()).withPosition(0, 0).withSize(2, 2);
        tab.addString("Extend Limit Switch", ()-> arm.extendLimitSwitchStatus()) .withPosition(0, 2).withSize(2, 2);

        tab.add("Arm Commands",arm)                                          .withPosition(0, 4).withSize(4, 2);

        tab.addDouble("Encoder Cnt", ()->arm.getEncoderCnt())                .withPosition(2, 0).withSize(2, 2);
        tab.addDouble("Arm Angle",   ()->arm.getArmAngle())                  .withPosition(2, 2).withSize(2, 2);

        // tab.addDouble("Abs Angle", ()->arm.getAbsoluteArmAngle())         .withPosition(3, 6).withSize(3, 2);        
        // tab.addDouble("Abs Volt",  ()->arm.getAbsoluteArmVolt())          .withPosition(6, 6).withSize(3, 2);

        // tab.addDouble("Abs Angle Raw", ()->arm.getAbsoluteArmAngleRaw())  .withPosition(3, 8).withSize(3, 2);
        //tab.addDouble("Abs Volt",  ()->arm.getAbsoluteArmVolt())          .withPosition(6, 8).withSize(3, 2);

        tab.addNumber("Motor Pwr",   () -> arm.mArmMotor.get())              .withPosition(4, 0).withSize(2, 2);

        tab.addNumber("Input from Operator Joystick",
                        () -> Robot.operatorGamepad.getArmInput())           .withPosition(4, 2).withSize(2, 2);

        tab.addNumber("Arb FF Val", () -> arm.getHoldPwr())                  .withPosition(4, 4).withSize(2, 2);
        
        tab.addNumber("MM Target", () -> arm.getTargetAngle())                  .withPosition(4, 6).withSize(2, 2);
        
        SmartDashboard.putData("Release Arm", new ElevReleaseArmCmd());

        tab.add("Set Mode STOP", new RunCommand(() -> Robot.arm.setArmState(ArmStates.STOPPED))).withPosition(6, 0);
        tab.add("Set Mode RUN", new RunCommand(() -> Robot.arm.setArmState(ArmStates.RUNNING))).withPosition(6, 2);
        tab.add("Arm State", Robot.arm.getArmState()).withPosition(6, 4).withSize(2, 1);

        tab.add("MM To 0", 
        new RunCommand( () -> Robot.arm.setMMTargetAngle( 0.0 ), Robot.arm))      .withPosition(8, 0).withSize(2, 1);

        tab.add("MM To 30", 
        new RunCommand( () -> Robot.arm.setMMTargetAngle( 30.0 ), Robot.arm))     .withPosition(8, 2).withSize(2, 1);

        tab.add("MM To 45", 
        new RunCommand( () -> Robot.arm.setMMTargetAngle( 45.0 ), Robot.arm))     .withPosition(8, 4).withSize(2, 1);

        tab.add("MM To 90", 
        new RunCommand( () -> Robot.arm.setMMTargetAngle( 90.0 ), Robot.arm))    .withPosition(10, 0).withSize(2, 1);

        tab.add("MM To -25", 
        new RunCommand( () -> Robot.arm.setMMTargetAngle( -25.0 ), Robot.arm))    .withPosition(10, 2).withSize(2, 1);
        
        // SmartDashboard.putData("Recal Arm", new ArmCalibrateCmd());
    }
}
