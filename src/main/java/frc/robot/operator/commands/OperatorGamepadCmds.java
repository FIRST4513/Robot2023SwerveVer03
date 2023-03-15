package frc.robot.operator.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.Robot;
import frc.robot.arm.ArmConfig;
import frc.robot.arm.commands.ArmCmds;
import frc.robot.arm.commands.ArmToStowPosCmd;
import frc.robot.elevator.ElevatorConfig;
import frc.robot.elevator.commands.ElevatorCmds;
import frc.robot.operator.OperatorGamepad;

public class OperatorGamepadCmds {
    /** Set default command to turn off the rumble */
    public static void setupDefaultCommand() {
    //    Robot.pilotGamepad.setDefaultCommand(ProcessAndSetRumbleCmd());
    }

    // ------------- Arm/Elev by TeleOp Commands ---------------
    public static Command ControlArmByJoysticksCmd()  { return ArmCmds.ArmByJoystickCmd(); }

    public static Command ControlElevByJoysticksCmd() { return ElevatorCmds.ElevByJoystickCmd(); }

    public static Command ControlArmElevByJoysticksCmd() {
        return new ParallelCommandGroup(
            ArmCmds.ArmByJoystickCmd(),
            ElevatorCmds.ElevByJoystickCmd()
        );
    }

    public static Command StopArmElevCmd() {
        return new ParallelCommandGroup(
            ArmCmds.StopArmCmd(),
            ElevatorCmds.ElevStopCmd()
        );
    }

    // --------------------------- Arm/Elev Auto Position (NORMAL MODE) --------------------
    public static Command SetArmElevToEjectLowPosCmd() {
        return ArmElevGoToPosParallelCmd(ArmConfig.ArmAngleEjectLowPos, ElevatorConfig.ElevEjectLowHt, 4.0);
    }
    
    public static Command SetArmElevToEjectMidPosCmd() {
        return ArmElevGoToPosParallelCmd(ArmConfig.ArmAngleEjectMidPos, ElevatorConfig.ElevEjectMidHt, 4.0);
    }
    
    public static Command SetArmElevToEjectHighPosCmd() {
        return ArmElevGoToPosParallelCmd(ArmConfig.ArmAngleEjectHighPos, ElevatorConfig.ElevEjectHighHt, 4.0);
    }

    // --------------------------- Arm/Elev Auto Position (SAFE MODE) --------------------    
    public static Command RunArmElevToStowPosCmd() {
        return new ArmToStowPosCmd(10);
    }

    public static Command RunArmElevToIntakePosCmd() {
        return new ArmElevDriveSafeCmd(ArmConfig.ArmAngleIntakePos, ElevatorConfig.ElevBumperClearHt, ElevatorConfig.ElevIntakeHt, 10.0);
    }

    public static Command RunArmElevToMidPosCmd() {
        return new ArmElevDriveSafeCmd(ArmConfig.ArmAngleEjectMidPos, ElevatorConfig.ElevBumperClearHt, ElevatorConfig.ElevEjectMidHt, 10.0);
    }

    public static Command RunArmElevToHighPosCmd() {
        return new ArmElevDriveSafeCmd(ArmConfig.ArmAngleEjectHighPos, ElevatorConfig.ElevBumperClearHt, ElevatorConfig.ElevEjectHighHt, 10.0);
    }

    public static Command ArmElevGoToPosParallelCmd(double armPos, double elevPos, double timeout) {
        // return new ArmElevDriveCmd(armPos, elevPos, timeout);
        return new ParallelCommandGroup(  
            new RunCommand(() -> Robot.arm.setMMTargetAngle(armPos), Robot.arm),
            new RunCommand(() -> Robot.elevator.setMMheight(elevPos), Robot.elevator)
        ).until(() -> Robot.operatorGamepad.isArmAndElevAtPos()).withTimeout(timeout);
    }

    // -------------------- Rumble Controller  ---------------

    public static Command RumbleOperatorCmd(double intensity) {
        return new RunCommand(() -> Robot.operatorGamepad.rumble(intensity), Robot.operatorGamepad);
    }

    public static Command ProcessAndSetRumbleCmd() {
        return new RunCommand(() -> Robot.operatorGamepad.rumble(() -> OperatorGamepad.getLeftRumble(), () -> OperatorGamepad.getRightRumble()), Robot.operatorGamepad);
    }
}
