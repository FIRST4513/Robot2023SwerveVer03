package frc.robot.operator.commands;

import java.util.function.DoubleSupplier;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;
import frc.robot.arm.ArmConfig;
import frc.robot.arm.commands.ArmCmds;
import frc.robot.elevator.ElevatorConfig;
import frc.robot.elevator.commands.ElevatorCmds;
import frc.robot.intake.commands.IntakeCmds;

public class OperatorGamepadCmds {
    
    /** Set default command to turn off the rumble */
    public static void setupDefaultCommand() {
       // Robot.pilotGamepad.setDefaultCommand(RumbleOperatorCmd(0));
    }

    // ------------- Arm/Elev by TeleOp Commands ---------------
    public static Command ControlArmByJoysticksCmd() {
        return ArmCmds.ArmByJoystickCmd();
    }

    public static Command ControlElevByJoysticksCmd() {
        return ElevatorCmds.ElevByJoystickCmd();
    }

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

    // ------------- Intake Commands ----------------------------
    public static Command IntakeCubeCmd(){
        return new ParallelCommandGroup(
            OperatorGamepadCmds.SetArmElevToIntakeCubePosCmd(),
            IntakeCmds.IntakeCubeCmd()
        );
    }

    public static Command IntakeConeCmd(){
        return new ParallelCommandGroup(
            OperatorGamepadCmds.SetArmElevToIntakeConePosCmd(),
            IntakeCmds.IntakeConeCmd()
        );
    }

    // GENERAL STRUCTURE OF MOVE TO POSITION COMMANDS:
    // If moving from out-out, then parallel motion used
    // If moving from in-to-out or out-to-in, then check for cube
    //    If cube there: avoid bumper collision
    //    If not, parallel motion used

    
    // --------------------------- Arm/Elev To Intake Positions --------------------

    public static Command SetArmElevToIntakeCubePosCmd() {
        return new ParallelCommandGroup(        
            ElevatorCmds.ElevToIntakeCubePosCmd(),
            ArmCmds.ArmToIntakeCubePosCmd());
    }
    
    public static Command SetArmElevToIntakeConePosCmd() {
        return new ParallelCommandGroup(        
            ElevatorCmds.ElevToIntakeConePosCmd(),
            ArmCmds.ArmToIntakeConePosCmd());
    }

    // -------------------- Arm/Elev to Retracted Positions ---------------

    public static Command SetArmElevToStorePosCmd() {
        // return new SequentialCommandGroup(
        //     ArmElevGoToPosParallelCmd( () -> Robot.arm.getArmAngle(), ElevatorConfig.ElevBumperClearHt, 4.0),
        //     ArmElevGoToPosParallelCmd( ArmConfig.ArmAngleStorePos, () -> Robot.elevator.getElevHeightInches(),4.0),
        //     ArmElevGoToPosParallelCmd( () -> Robot.arm.getArmAngle(),     ElevatorConfig.ElevStoreHt, 4.0)
        // );
        return ArmElevGoToPosParallelCmd(ArmConfig.ArmAngleStorePos, ElevatorConfig.ElevStoreHt, 4.0);
    }

    public static Command SetArmElevToFullRetractPosCmd() {
        return new ConditionalCommand(
            // True condition: arm inside robot, no worry of bumper collision
            new ParallelCommandGroup(
                ElevatorCmds.ElevToRetractPosCmd(),
                ArmCmds.ArmToFullRetractCmd()
            ),
            // False condition: arm outside robot, check for cube
            new ConditionalCommand(
                // True condition: no cube, good to go, parallel motion used
                new ParallelCommandGroup(
                    ElevatorCmds.ElevToRetractPosCmd(),
                    ArmCmds.ArmToFullRetractCmd()
                ),
                // False condition: cube, must avoid bumper collision;
                // raise elevator for clearance, set arm, then move elev back to correct pos
                new SequentialCommandGroup(
                    ElevatorCmds.ElevToBumperClearPosCmd(),
                    new ParallelCommandGroup(
                        ArmCmds.ArmToFullRetractCmd(),
                        ElevatorCmds.ElevHoldCmd().until(() -> Robot.arm.isMMtargetReached())
                    ),
                    // new DelayCmd(1.0),
                    ElevatorCmds.ElevToRetractPosCmd()
                ),
                () -> Robot.intake.isCubeEjectNotDetected()),
            // Condition: is arm outside?
            () -> Robot.arm.isArmInside()
        );
    }

    // -------------------- Arm/Elev To Eject Positions -------------------

    public static Command SetArmElevToEjectLowPosCmd() {
        // sequential
        //      move elev to safe height for low while holding arm at retract pos
        //      move arm to eject low pos
        //      move elev to eject low pos


        
        // return new ConditionalCommand(
        //     // True condition: arm outside robot, no worry of bumper collision (out-to-out movement)
        //     ArmElevGoToPosParallelCmd(ArmConfig.ArmAngleEjectLowPos, ElevatorConfig.ElevEjectLowHt, 4.0),
        //     // False condition: arm inside robot, check for cube
        //     new ConditionalCommand(
        //         // True condition: no cube, good to go, parallel motion used
        //         ArmElevGoToPosParallelCmd(ArmConfig.ArmAngleEjectLowPos, ElevatorConfig.ElevEjectLowHt, 4.0),
        //         // False condition: cube, must avoid bumper collision;
        //         // raise elevator for clearance, set arm, then move elev back to correct pos
        //         new SequentialCommandGroup(
        //             ArmElevGoToPosParallelCmd(() -> Robot.arm.getArmAngle(), ElevatorConfig.ElevBumperClearHt, 4.0),
        //             ArmElevGoToPosParallelCmd(ArmConfig.ArmAngleEjectLowPos, () -> Robot.elevator.getElevHeightInches(), 4.0),
        //             ArmElevGoToPosParallelCmd(() -> Robot.arm.getArmAngle(), ElevatorConfig.ElevEjectLowHt, 4.0)
        //         ).until(() -> Robot.operatorGamepad.isArmAndElevAtPos()),
        //         () -> Robot.intake.isCubeEjectNotDetected()),
        //     // Condition: is arm outside?
        //     () -> Robot.arm.isArmOutside()
        // );

        
        return ArmElevGoToPosParallelCmd(ArmConfig.ArmAngleEjectLowPos, ElevatorConfig.ElevEjectLowHt, 4.0);
    }

    public static Command SetArmElevToEjectMidPosCmd() {
        // return new SequentialCommandGroup(
        //     ArmElevGoToPosParallelCmd(() -> Robot.arm.getArmAngle(), ElevatorConfig.ElevEjectMidHt, 3.0),
        //     ArmElevGoToPosParallelCmd(ArmConfig.ArmAngleEjectMidPos, ElevatorConfig.ElevEjectMidHt, 4.0)
        // );
        return ArmElevGoToPosParallelCmd(ArmConfig.ArmAngleEjectMidPos, ElevatorConfig.ElevEjectMidHt, 4.0);
    }


    public static Command SetArmElevToEjectHighPosCmd() {
        // return new SequentialCommandGroup(
        //     ArmElevGoToPosParallelCmd(() -> Robot.arm.getArmAngle(), ElevatorConfig.ElevEjectHighHt, 2.0),
        //     ArmElevGoToPosParallelCmd(ArmConfig.ArmAngleEjectHighPos, ElevatorConfig.ElevEjectHighHt, 4.0)
        // );
        return ArmElevGoToPosParallelCmd(ArmConfig.ArmAngleEjectHighPos, ElevatorConfig.ElevEjectHighHt, 4.0);
    }

    // ----------- Generic Parallel command for go to position ----------
    public static Command ArmElevGoToPosParallelCmd(double armPos, double elevPos, double timeout) {
        return new ParallelCommandGroup(
            new RunCommand(() -> Robot.arm.setMMangle(armPos), Robot.arm),
            new RunCommand(() -> Robot.elevator.setMMheight(elevPos), Robot.elevator)
        ).until(() -> Robot.operatorGamepad.isArmAndElevAtPos()).withTimeout(timeout);
    }

    public static Command ArmElevGoToPosParallelCmd(DoubleSupplier armPos, double elevPos, double timeout) {
        return new ParallelCommandGroup(
            new RunCommand(() -> Robot.arm.setMMangle(armPos), Robot.arm),
            new RunCommand(() -> Robot.elevator.setMMheight(elevPos), Robot.elevator)
        ).until(() -> Robot.operatorGamepad.isArmAndElevAtPos()).withTimeout(timeout);
    }

    public static Command ArmElevGoToPosParallelCmd(double armPos, DoubleSupplier elevPos, double timeout) {
        return new ParallelCommandGroup(
            new RunCommand(() -> Robot.arm.setMMangle(armPos), Robot.arm),
            new RunCommand(() -> Robot.elevator.setMMheight(elevPos), Robot.elevator)
        ).until(() -> Robot.operatorGamepad.isArmAndElevAtPos()).withTimeout(timeout);
    }

    public static Command ArmElevGoToPosParallelCmd(DoubleSupplier armPos, DoubleSupplier elevPos, double timeout) {
        return new ParallelCommandGroup(
            new RunCommand(() -> Robot.arm.setMMangle(armPos), Robot.arm),
            new RunCommand(() -> Robot.elevator.setMMheight(elevPos), Robot.elevator)
        ).until(() -> Robot.operatorGamepad.isArmAndElevAtPos()).withTimeout(timeout);
    }

    // -------------------- Rumble Controller  ---------------

    public static Command RumbleOperatorCmd(double intensity) {
        return new RunCommand(() -> Robot.operatorGamepad.rumble(intensity), Robot.operatorGamepad);
    }
}
