package frc.robot.auto.commands;

import com.pathplanner.lib.PathPlannerTrajectory;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;
import frc.robot.arm.commands.ArmCmds;
// import frc.robot.arm.commands.ArmHoldPositionCmd;
// import frc.robot.arm.commands.ArmReleaseCmd;
import frc.robot.auto.Auto;
import frc.robot.autoBalance.commands.AutoBalanceCommand;
import frc.robot.elevator.commands.ElevatorCmds;
import frc.robot.elevator.commands.ElevatorHoldPosCmd;
import frc.robot.intake.commands.IntakeCmds;
import frc.robot.operator.commands.ArmElevComboMoveCmds;
import frc.robot.operator.commands.OperatorGamepadCmds;
import frc.robot.swerve.commands.LockSwerve;
import frc.robot.trajectories.commands.TrajectoriesCmds;

public class AutoCmds {

    // --------------- Do Nothing ---------------------------
    public static Command DoNothingCmd() {
        // Set position and Gyro Heading based on position
        return IntializeRobotFromPoseCmd(Auto.startPose);
    }
    
    // --------------- Place Cube Only Command ------------------
    public static Command PlaceOnlyCmd( String level ) {
        return new SequentialCommandGroup(           
            IntializeRobotFromPoseCmd(Auto.startPose),
            PlaceCmd( level )
        );
    }

    // ----------------- Place Cube Commands ---------------------
    public static Command PlaceCmd( String level ) {
        if (level == "Low") {
            // Set position and Gyro Heading based on position
            return new SequentialCommandGroup(
                // ArmParkedToStorePosCmd().withTimeout(6.0),  // Move Arm from parked to store pos
                // OperatorGamepadCmds.SetArmElevToEjectLowPosSafeCmd(),
                // IntakeCmds.IntakeEjectUntilNoGamepieceCmd().raceWith(IntakeCmds.holdArmAndElevCmd()),
                // IntakeCmds.IntakeStopCmd(),
                // OperatorGamepadCmds.SetArmElevToStorePosFromLowSafeCmd()
                IntakeCmds.IntakeEjectRunCmd().withTimeout(0.25)
            );
        }
        if (level == "Mid") {
            // Set position and Gyro Heading based on position
            return new SequentialCommandGroup(           
                // ArmParkedToStorePosCmd().withTimeout(6.0),  // Move Arm from parked to store pos
                // OperatorGamepadCmds.SetArmElevToEjectMidPosSafeCmd(),
                // IntakeCmds.IntakeEjectUntilNoGamepieceCmd().raceWith(IntakeCmds.holdArmAndElevCmd()),
                // IntakeCmds.IntakeStopCmd(),
                // OperatorGamepadCmds.SetArmElevToStorePosFromMidSafeCmd()
                IntakeCmds.IntakeHoldRunCmd().withTimeout(.01),
                ArmElevComboMoveCmds.SetArmRunElevMidPosCmd(),
                new DelayCmd(3.0).until(() -> Robot.arm.isMMtargetReached()),
                new DelayCmd(.25),
                IntakeCmds.IntakeEjectRunCmd().withTimeout(0.25),
                IntakeCmds.IntakeStopCmd(),
                ArmCmds.ArmRunToZeroCmd().until(() -> Robot.arm.isMMtargetReached()),
                OperatorGamepadCmds.RunArmElevToStowPosCmd()
            );
        }
        if (level == "High") {
            return new SequentialCommandGroup(
                IntakeCmds.IntakeHoldRunCmd().withTimeout(.01),
                ArmElevComboMoveCmds.SetArmRunElevHighPosCmd(),
                new DelayCmd(3.0).until(() -> Robot.arm.isMMtargetReached()),
                new DelayCmd(.25),
                IntakeCmds.IntakeEjectRunCmd().withTimeout(0.25),
                IntakeCmds.IntakeStopCmd(),
                ArmCmds.ArmRunToZeroCmd().until(() -> Robot.arm.isMMtargetReached()),
                OperatorGamepadCmds.RunArmElevToStowPosCmd()
            );
        }
        return new PrintCommand("Error on auto place only paramter");
    }

    // -------------- Cross Line Only Comands ---------------------
    public static Command CrossLineOnlyCmd( PathPlannerTrajectory path) {
        return new SequentialCommandGroup(
            TrajectoriesCmds.IntializeRobotAndFollowPathCmd(path, 10.0)
        );
    }

    // -------------- Place and Cross Line Comands ---------------------
    public static Command PlaceAndCrossCmd( String level, PathPlannerTrajectory path ) {
        return new SequentialCommandGroup(  
            PlaceOnlyCmd(level),
            CrossLineOnlyCmd(path)
        );
    }

    // -------------- Place, Cross Short Line and get on Charging Station Comands ---------------------
    // This only used for short cross Red and Blue
    public static Command PlaceAndCrossShortAndScaleCmd( String level,
                                                          PathPlannerTrajectory pathA,
                                                          PathPlannerTrajectory pathB ) {
        return new SequentialCommandGroup(  
            PlaceOnlyCmd(level),
            new SequentialCommandGroup(  
                TrajectoriesCmds.IntializeRobotAndFollowPathCmd(pathA, 10.0),
                TrajectoriesCmds.IntializeRobotAndFollowPathCmd(pathB, 5.0),
                AutoBalanceCmd().withTimeout(3.0),
                new LockSwerve()
            )
        );
    }

    // ----------------------- Get on Charging Platform  -------------------
    public static Command GetOnChargingTableCmd( PathPlannerTrajectory pathA, PathPlannerTrajectory pathB, boolean autoLevel ) {
        if (autoLevel) {
            return new SequentialCommandGroup(  
                TrajectoriesCmds.IntializeRobotAndFollowPathCmd(pathA, 10.0),
                TrajectoriesCmds.IntializeRobotAndFollowPathCmd(pathB, 10.0),
                AutoBalanceCmd().withTimeout(3.0),
                new LockSwerve()
            );
        }
        else {
            return new SequentialCommandGroup(  
                TrajectoriesCmds.IntializeRobotAndFollowPathCmd(pathA, 10.0),
                TrajectoriesCmds.IntializeRobotAndFollowPathCmd(pathB, 10.0),
                new LockSwerve()
            );
        }
    }

    // ----------------------- Score and Get on Charging Platform  -------------------
    public static Command PlaceAndChargingTableCmd( String level, PathPlannerTrajectory pathA, PathPlannerTrajectory pathB, boolean autoLevel ) {
        return new SequentialCommandGroup(  
            PlaceCmd(level),
            GetOnChargingTableCmd( pathA, pathB, autoLevel )
        );
    }

    public static Command IntializeRobotFromPoseCmd( Pose2d pose){
        return new SequentialCommandGroup(
            //  //holonomicRotation Heading +-180 degrees
            new InstantCommand(() -> Robot.swerve.resetGyro(pose.getRotation().getDegrees())),
            new InstantCommand(() -> Robot.swerve.resetOdometry(pose))
        );
    }

    public static Command AutoBalanceCmd(){
        return new SequentialCommandGroup(
            new AutoBalanceCommand(),
            new LockSwerve()
        ); 
    }

    
    // -------------------- Arm/Elev To Eject Positions -------------------

    // public static Command SetArmElevToEjectLowPosCmd() {
    //     // sequential
    //     //      move elev to safe height for low while holding arm at retract pos
    //     //      move arm to eject low pos
    //     //      move elev to eject low pos
    //     return new SequentialCommandGroup(
    //         // 1. Move Elev up to safe height while holding Arm
    //         new ParallelCommandGroup(
    //             ElevatorCmds.ElevToEjectCubeLowSafePosCmd(),
    //             new ArmHoldPositionCmd().until(() -> Robot.elevator.isMMtargetReached())
    //             ),
    //         // 2. Move ArnOut to Low Cube release point, while elevator continues to hold
    //         new ParallelCommandGroup(
    //             ArmCmds.ArmToEjectLowPosCmd(),
    //             new ElevatorHoldPosCmd().until(() -> Robot.arm.isMMtargetReached())
    //             ),
    //         // 3. Move Elev down to eject position while hold arm at 
    //         new ParallelCommandGroup(
    //             ElevatorCmds.ElevToEjectLowPosCmd(),
    //             new ArmHoldPositionCmd().until(() -> Robot.elevator.isMMtargetReached())
    //         )
    //     ); 
    // }

    // public static Command SetArmElevToEjectMidPosCmd() {
    //     // sequential
    //     //      move elev to safe height for low while holding arm at retract pos
    //     //      move arm to eject low pos
    //     //      move elev to eject low pos
    //     return new SequentialCommandGroup(
    //         // 1. Move Elev up to safe height while holding Arm
    //         new ParallelCommandGroup(
    //             ElevatorCmds.ElevToEjectCubeMidSafePosCmd(),
    //             new ArmHoldPositionCmd().until(() -> Robot.elevator.isMMtargetReached())
    //             ),
    //         // 2. Move ArnOut to Low Cube release point, while elevator continues to hold
    //         new ParallelCommandGroup(
    //             ArmCmds.ArmToEjectMidPosCmd(),
    //             new ElevatorHoldPosCmd().until(() -> Robot.arm.isMMtargetReached())
    //             ),
    //         // 3. Move Elev down to eject position while hold arm at 
    //         new ParallelCommandGroup(
    //             ElevatorCmds.ElevToEjectMidPosCmd(),
    //             new ArmHoldPositionCmd().until(() -> Robot.elevator.isMMtargetReached())
    //         )
    //     ); 
    // }


}
