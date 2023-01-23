package frc.robot.trajectories.commands;

import com.pathplanner.lib.PathPlannerTrajectory;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;
import frc.robot.swerveDrive.commands.SwerveDriveCmds;

public class TrajectoriesCmds {

    public static Command resetThetaControllerCmd() {
        return new InstantCommand(() -> Robot.trajectories.resetTheta(), Robot.trajectories);
    }
            
    public static Command FollowPathCmd(PathPlannerTrajectory path, double time){
        return new FollowTrajectoryCmd(path).withTimeout(time);
    }

    public static Command IntializePathFollowingCmd(PathPlannerTrajectory path){
        return new SequentialCommandGroup(
            SwerveDriveCmds.SetBrakeModeOnCmd().withTimeout(0.25), //set brake mode and pause for 1/4 second
            SwerveDriveCmds.IntializeGyroAngleCmd(path), //set gyro to initial heading
            SwerveDriveCmds.ResetOdometryCmd(path) //reset odometry to the initial position
        );
    }

}
