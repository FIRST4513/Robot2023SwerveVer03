package frc.robot.trajectories.commands;

import com.pathplanner.lib.PathPlannerTrajectory;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;
import frc.robot.swerve.commands.SwerveCmds;

public class TrajectoriesCmds {
    
    public static Command IntializeRobotAndFollowPathCmd(PathPlannerTrajectory path, double time) {
        return new SequentialCommandGroup(
            InitializeRobotFromPathCmd( path),
            FollowPathCmd(path, time)
            // new LockSwerve()
        );
    }

    public static Command FollowPathCmd(PathPlannerTrajectory path, double time) {
        return new FollowTrajectoryCmd(path).withTimeout(time);
    }

    public static Command InitializeRobotFromPathCmd(PathPlannerTrajectory path) { 
        return new SequentialCommandGroup(
            SwerveCmds.SetBrakeModeOnCmd().withTimeout(0.25),  // set brake mode On and pause for 1/4 second
            SwerveCmds.IntializeGyroAngleCmd(path),            // set gyro to Path starting holonomicRotation Heading +-180 degrees
            SwerveCmds.ResetOdometryCmd(path)                  // reset odometry to the Path Starting position (x,y)
        );
    }

    public static Command ResetThetaControllerCmd() {
        return new InstantCommand(() -> Robot.trajectories.resetTheta(), Robot.trajectories);
    }

}
