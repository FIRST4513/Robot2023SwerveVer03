package frc.robot.arm.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.Robot;
import frc.robot.arm.ArmConfig;

//the funny code

public class ArmCmds {
    
    // Default Command
    /** Set default command to turn off the rumble */
    public static void setupDefaultCommand() {
        Robot.arm.setDefaultCommand(HoldArmCmd());
    }

    public static Command HoldArmCmd() {
        return new RunCommand(() -> Robot.arm.holdArm(), Robot.arm )
            .withName("HoldArmCmd");
    }

    public static Command StopArmCmd() {
        return new InstantCommand( () -> Robot.arm.stopArm(), Robot.arm)
            .withName("StopArmCmd");
    }

    public static Command RaiseArmCmd() {
        return new RunCommand(() -> Robot.arm.raiseArm(), Robot.arm )
            .withName("RaiseArmCmd");
    }

    public static Command LowerArmCmd() {
        return new RunCommand(() -> Robot.arm.lowerArm(), Robot.arm )
            .withName("LowerArmCmd");
    }
   
    public static Command setManualOutput(double speed) {
        return setManualOutput(speed);
    }

    public static Command setManualOutput(DoubleSupplier speed) {
        return new RunCommand(
                () -> Robot.arm.setArmMotor(speed.getAsDouble()), Robot.arm);
    }

    public static Command ArmToFullRetractCmd() {
        return new RunCommand( () -> Robot.arm.lowerArm(), Robot.elevator)
            .until(() ->Robot.arm.isLowerLimitSwitchPressed())
            .withName("armToBottomCmd");
    }

    public static Command ArmToPIDPositionCmd(double angle) {
        return new InstantCommand(() -> Robot.arm.setPIDArmToAngle(angle), Robot.arm )
            .withName("ArmToPIDPosistionCmd");
    }

    public static Command ArmByJoystickCmd() {
        return new RunCommand(
        () -> Robot.arm.setArmMotor(() -> Robot.operatorGamepad.getArmInput()), Robot.arm);
    }

// ----------------- Motion Magic Commands ----------------------
    public static Command setMMPosition(double position) {
        return new RunCommand(() -> Robot.arm.setMMPosition(position), Robot.arm);
    }

    public static Command setMMPercent(double percent) {
        return new RunCommand(() -> Robot.arm.setMMPercent(percent), Robot.arm);
    }

    public static Command coneTop() {
        return setMMPercent(ArmConfig.coneTop);
    }

    public static Command coneShelf() {
        return setMMPercent(ArmConfig.coneShelf);
    }

    public static Command cubeIntake() {
        return setMMPercent(ArmConfig.cubeIntake);
    }

    public static Command cubeMid() {
        return setMMPercent(ArmConfig.cubeMid);
    }

    public static Command cubeTop() {
        return setMMPercent(ArmConfig.cubeTop);
    }

    public static Command home() {
        return setMMPercent(0);
    }

}
