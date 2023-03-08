package frc.robot.operator.commands;

import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;
import frc.robot.arm.ArmConfig;
import frc.robot.arm.commands.ArmCmds;
import frc.robot.auto.AutoConfig;
import frc.robot.auto.commands.ArmElevDriveCmd;
import frc.robot.elevator.ElevatorConfig;
import frc.robot.elevator.commands.ElevatorCmds;
import frc.robot.intake.commands.IntakeCmds;
import frc.robot.trajectories.commands.TrajectoriesCmds;

public class OperatorGamepadCmds {
    static PathPlannerTrajectory testPath = PathPlanner.loadPath(
        "215Meter", AutoConfig.kMaxSpeed, AutoConfig.kMaxAccel);

    /** Set default command to turn off the rumble */
    public static void setupDefaultCommand() {
       // Robot.pilotGamepad.setDefaultCommand(RumbleOperatorCmd(0));
    }

    // ------------- Arm/Elev by TeleOp Commands ---------------
    public static Command ControlArmByJoysticksCmd()    { return ArmCmds.ArmByJoystickCmd(); }

    public static Command ControlElevByJoysticksCmd()   { return ElevatorCmds.ElevByJoystickCmd(); }

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
    public static Command SetArmElevToEjectLowPosSafeCmd() {
        return new SequentialCommandGroup(
            // Raise Elevator to Low safe height while holding arm at store position
            ArmElevGoToPosParallelCmd(ArmConfig.ArmAngleStorePos, ElevatorConfig.ElevEjectCubeLowSafeHt, 4.0),
            // Move Arm Out While Holding Elevator at Low safe height
            ArmElevGoToPosParallelCmd(ArmConfig.ArmAngleEjectLowPos, ElevatorConfig.ElevEjectCubeLowSafeHt, 4.0),
            // Move Elevator Down to correct height while holding arm at low eject position
            ArmElevGoToPosParallelCmd(ArmConfig.ArmAngleEjectLowPos, ElevatorConfig.ElevEjectLowHt, 4.0)
        );
    }
    
    public static Command SetArmElevToEjectMidPosSafeCmd() {
        return new SequentialCommandGroup(
            // Raise Elevator to safe height while holding arm at store position
            ArmElevGoToPosParallelCmd(ArmConfig.ArmAngleStorePos, ElevatorConfig.ElevEjectCubeMidSafeHt, 4.0),
            // Move Arm Out While Holding Elevator at safe height
            ArmElevGoToPosParallelCmd(ArmConfig.ArmAngleEjectMidPos, ElevatorConfig.ElevEjectCubeMidSafeHt, 4.0),
            // Move Elevator Down to correct height while holding arm at low eject position
            ArmElevGoToPosParallelCmd(ArmConfig.ArmAngleEjectMidPos, ElevatorConfig.ElevEjectMidHt, 4.0)
        );
    }
    
    public static Command SetArmElevToEjectHighPosSafeCmd() {
        return new SequentialCommandGroup(
            // Raise Elevator to safe height while holding arm at store position
            ArmElevGoToPosParallelCmd(ArmConfig.ArmAngleStorePos, ElevatorConfig.ElevEjectCubeHighSafeHt, 4.0),
            // Move Arm Out While Holding Elevator at safe height
            ArmElevGoToPosParallelCmd(ArmConfig.ArmAngleEjectMidPos, ElevatorConfig.ElevEjectCubeHighSafeHt, 4.0),
            // Move Elevator Down to correct height while holding arm at low eject position
            ArmElevGoToPosParallelCmd(ArmConfig.ArmAngleEjectHighPos, ElevatorConfig.ElevEjectHighHt, 4.0)
        );
    }

    
    public static Command SetArmElevToStorePosFromLowSafeCmd() {
        return new SequentialCommandGroup(
            // Retract Arm while holding elev
            ArmElevGoToPosParallelCmd(ArmConfig.ArmAngleStorePos, ElevatorConfig.ElevEjectCubeLowSafeHt, 4.0),
            // Lower Elevator while holding arm at store pos
            ArmElevGoToPosParallelCmd(ArmConfig.ArmAngleStorePos, ElevatorConfig.ElevStoreHt, 4.0)
        );
    }
    
    public static Command SetArmElevToStorePosFromMidSafeCmd() {
        return new SequentialCommandGroup(
            // Retract Arm while holding elev
            ArmElevGoToPosParallelCmd(ArmConfig.ArmAngleStorePos, ElevatorConfig.ElevEjectCubeMidSafeHt, 4.0),
            // Lower Elevator while holding arm at store pos
            ArmElevGoToPosParallelCmd(ArmConfig.ArmAngleStorePos, ElevatorConfig.ElevStoreHt, 4.0)
        );
    }

    public static Command ArmElevGoToPosParallelCmd(double armPos, double elevPos, double timeout) {
        return new ArmElevDriveCmd(armPos, elevPos, timeout);
        //return new ParallelCommandGroup(  
        //  new RunCommand(() -> Robot.arm.setMMangle(armPos), Robot.arm),
        //  new RunCommand(() -> Robot.elevator.setMMheight(elevPos), Robot.elevator)
        // ).until(() -> Robot.operatorGamepad.isArmAndElevAtPos()).withTimeout(timeout);
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

    // ------------------- Test Path Follow cmd ---------
    public static Command runTestPathCmd() {
        return TrajectoriesCmds.IntializeRobotAndFollowPathCmd(testPath, 5);
    }





    // -------------------- Arm/Elev To Eject Positions -------------------


    // ----------- Generic Parallel command for go to position ----------

    // public static Command ArmElevGoToPosParallelCmd(DoubleSupplier armPos, double elevPos, double timeout) {
    //     return new ParallelCommandGroup(
    //         new RunCommand(() -> Robot.arm.setMMangle(armPos), Robot.arm),
    //         new RunCommand(() -> Robot.elevator.setMMheight(elevPos), Robot.elevator)
    //     ).until(() -> Robot.operatorGamepad.isArmAndElevAtPos()).withTimeout(timeout);
    // }

    // public static Command ArmElevGoToPosParallelCmd(double armPos, DoubleSupplier elevPos, double timeout) {
    //     return new ParallelCommandGroup(
    //         new RunCommand(() -> Robot.arm.setMMangle(armPos), Robot.arm),
    //         new RunCommand(() -> Robot.elevator.setMMheight(elevPos), Robot.elevator)
    //     ).until(() -> Robot.operatorGamepad.isArmAndElevAtPos()).withTimeout(timeout);
    // }

    // public static Command ArmElevGoToPosParallelCmd(DoubleSupplier armPos, DoubleSupplier elevPos, double timeout) {
    //     return new ParallelCommandGroup(
    //         new RunCommand(() -> Robot.arm.setMMangle(armPos), Robot.arm),
    //         new RunCommand(() -> Robot.elevator.setMMheight(elevPos), Robot.elevator)
    //     ).until(() -> Robot.operatorGamepad.isArmAndElevAtPos()).withTimeout(timeout);
    // }

    // -------------------- Rumble Controller  ---------------

    public static Command RumbleOperatorCmd(double intensity) {
        return new RunCommand(() -> Robot.operatorGamepad.rumble(intensity), Robot.operatorGamepad);
    }
}
