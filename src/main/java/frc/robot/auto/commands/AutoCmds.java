package frc.robot.auto.commands;

import com.pathplanner.lib.PathPlannerTrajectory;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;
import frc.robot.arm.ArmConfig;
import frc.robot.arm.commands.ArmCmds;
import frc.robot.arm.commands.ArmHoldPositionCmd;
import frc.robot.arm.commands.ArmReleaseCmd;
import frc.robot.autoBalance.commands.AutoBalanceCommand;
import frc.robot.elevator.commands.ElevReleaseArmCmd;
import frc.robot.elevator.commands.ElevatorCmds;
import frc.robot.elevator.commands.ElevatorHoldPosCmd;
import frc.robot.intake.commands.IntakeCmds;
import frc.robot.operator.commands.OperatorGamepadCmds;
import frc.robot.swerve.commands.LockSwerve;
import frc.robot.trajectories.commands.TrajectoriesCmds;

public class AutoCmds {

    public static Command PlaceCubeLowCmd() {
        return new SequentialCommandGroup(
            // 1. Release Parked Arm
            ArmParkedToStorePosCmd().withTimeout(6.0),
            // 2. Set arm/elev to low score position
            OperatorGamepadCmds.SetArmElevToEjectLowPosCmd(),
            // 3. eject
            IntakeCmds.IntakeEjectCmd(),
            // 4. Store Arm Elev
            OperatorGamepadCmds.SetArmElevToStorePosCmd()
        );
    }

    public static Command PlaceCubeMidCmd() {
        return new SequentialCommandGroup(
            // 1. Release Parked Arm
            ArmParkedToStorePosCmd().withTimeout(6.0),
            // 2. Set arm/elev to mid score position
            OperatorGamepadCmds.SetArmElevToEjectMidPosCmd(),
            // 3. eject
            IntakeCmds.IntakeEjectCmd(),
            // 4. Store Arm Elev
            OperatorGamepadCmds.SetArmElevToStorePosCmd()
        );
    }


    public static Command PlaceCubeLowRunPathCmd( PathPlannerTrajectory path, double timeOut){
        return new SequentialCommandGroup(
            PlaceCubeLowCmd(),
            TrajectoriesCmds.IntializeRobotAndFollowPathCmd(path , timeOut)
        );
    }

    public static Command PlaceCubeMidRunPathCmd( PathPlannerTrajectory path, double timeOut){
        return new SequentialCommandGroup(
            PlaceCubeLowCmd(),
            TrajectoriesCmds.IntializeRobotAndFollowPathCmd(path , timeOut)
        );
    }

    public static Command IntializeRobotFromPoseCmd( Pose2d pose){
        return new SequentialCommandGroup(
            //  //holonomicRotation Heading +-180 degrees
            new InstantCommand(() -> Robot.swerve.resetGyro(pose.getRotation().getDegrees())),
            new InstantCommand(() -> Robot.swerve.resetOdometry(pose))
        );
    }

    public static Command ArmParkedToStorePosCmd(){
        return new ParallelCommandGroup(
            new ElevReleaseArmCmd(),
            new ArmReleaseCmd()
        );
    }

    public static Command AutoBalanceCmd(){
        return new SequentialCommandGroup(
            //new AutoBalanceCommand(),
            new LockSwerve()
        ); 
    }

    
    // -------------------- Arm/Elev To Eject Positions -------------------

    public static Command SetArmElevToEjectLowPosCmd() {
        // sequential
        //      move elev to safe height for low while holding arm at retract pos
        //      move arm to eject low pos
        //      move elev to eject low pos
        return new SequentialCommandGroup(
            // 1. Move Elev up to safe height while holding Arm
            new ParallelCommandGroup(
                ElevatorCmds.ElevToEjectCubeLowSafePosCmd(),
                new ArmHoldPositionCmd().until(() -> Robot.elevator.isMMtargetReached())
                ),
            // 2. Move ArnOut to Low Cube release point, while elevator continues to hold
            new ParallelCommandGroup(
                ArmCmds.ArmToEjectLowPosCmd(),
                new ElevatorHoldPosCmd().until(() -> Robot.arm.isMMtargetReached())
                ),
            // 3. Move Elev down to eject position while hold arm at 
            new ParallelCommandGroup(
                ElevatorCmds.ElevToEjectLowPosCmd(),
                new ArmHoldPositionCmd().until(() -> Robot.elevator.isMMtargetReached())
            )
        ); 
    }

    public static Command SetArmElevToEjectMidPosCmd() {
        // sequential
        //      move elev to safe height for low while holding arm at retract pos
        //      move arm to eject low pos
        //      move elev to eject low pos
        return new SequentialCommandGroup(
            // 1. Move Elev up to safe height while holding Arm
            new ParallelCommandGroup(
                ElevatorCmds.ElevToEjectCubeMidSafePosCmd(),
                new ArmHoldPositionCmd().until(() -> Robot.elevator.isMMtargetReached())
                ),
            // 2. Move ArnOut to Low Cube release point, while elevator continues to hold
            new ParallelCommandGroup(
                ArmCmds.ArmToEjectMidPosCmd(),
                new ElevatorHoldPosCmd().until(() -> Robot.arm.isMMtargetReached())
                ),
            // 3. Move Elev down to eject position while hold arm at 
            new ParallelCommandGroup(
                ElevatorCmds.ElevToEjectMidPosCmd(),
                new ArmHoldPositionCmd().until(() -> Robot.elevator.isMMtargetReached())
            )
        ); 
    }


}
