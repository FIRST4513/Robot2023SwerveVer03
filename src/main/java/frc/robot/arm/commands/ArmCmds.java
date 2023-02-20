package frc.robot.arm.commands;

import java.util.function.DoubleSupplier;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.Robot;

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

    public static Command ResetArmEncoderCmd() {
        return new InstantCommand( () -> Robot.arm.resetEncoder(), Robot.arm);
    }

    public static Command SetArmBrakeCmd() {
        return new InstantCommand( () -> Robot.arm.setBrakeMode(true), Robot.arm);
    }
    public static Command SetArmCoastCmd() {
        return new InstantCommand( () -> Robot.arm.setBrakeMode(false), Robot.arm);
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
        return new RunCommand( () -> Robot.arm.lowerArm(), Robot.arm)
            .until(() ->Robot.arm.isLowerLimitSwitchPressed())
            .withName("armToBottomCmd");
    }

    public static Command ArmToPIDPositionCmd(double angle) {
        return new InstantCommand(() -> Robot.arm.setMMangle(angle), Robot.arm )
            .withName("ArmToPIDPosistionCmd");
    }

    public static Command ArmByJoystickCmd() {
        return new RunCommand(
        () -> Robot.arm.setArmMotor(() -> Robot.operatorGamepad.getArmInput()), Robot.arm);
    }

// ----------------- Motion Magic Commands ----------------------
    public static Command setMMPosition(double position) {
        return new RunCommand(() -> Robot.arm.setMMangle(position), Robot.arm);
    }

    public static Command setMMPercent(double percent) {
        return new RunCommand(() -> Robot.arm.setMMangle(percent), Robot.arm);
    }


}
