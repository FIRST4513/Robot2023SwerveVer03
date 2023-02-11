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
import frc.robot.auto.AutoConfig;
import frc.robot.auto.Auto;
import frc.robot.elevator.commands.ElevatorCmds;
import frc.robot.intake.commands.IntakeCmds;
import frc.robot.trajectories.commands.TrajectoriesCmds;

public class AutoCmds {

    public static Command PlaceObjectCmd() {
        return new ParallelCommandGroup(
            // 1. raise elev to start pos
            ElevatorCmds.ElevGoToPIDPosCmd(Auto.elevStartPos).withTimeout(2),
            // 2. raise arm to target pos
            ArmCmds.ArmToPIDPositionCmd(Auto.armPosition).withTimeout(2),
            // 3. elev lower to final target pos
            ElevatorCmds.ElevGoToPIDPosCmd(Auto.elevEndPos).withTimeout(2),
            // 4. eject
            IntakeCmds.IntakeEjectCmd(),
            // Intake Arm to get ready for following path
            ElevatorCmds.ElevGoToPIDPosCmd(AutoConfig.kElevTop).withTimeout(2),
            ArmCmds.ArmToPIDPositionCmd(ArmConfig.ArmAngleStorePos),
            ElevatorCmds.ElevGoToBottomCmd().withTimeout(2)
        );
    }

    public static Command PlaceObjectRunPathCmd( PathPlannerTrajectory path, double time){
        return new SequentialCommandGroup(
            PlaceObjectCmd(),
            TrajectoriesCmds.IntializeRobotAndFollowPathCmd(path , time)
        );
    }

    public static Command IntializeRobotPoseCmd( Pose2d pose){
        return new SequentialCommandGroup(
            //  //holonomicRotation Heading +-180 degrees
            new InstantCommand(() -> Robot.swerve.setGyroYawAngle(pose.getRotation().getDegrees())),
            new InstantCommand(() -> Robot.swerve.resetOdometry(pose)) 
        );
    }

}
