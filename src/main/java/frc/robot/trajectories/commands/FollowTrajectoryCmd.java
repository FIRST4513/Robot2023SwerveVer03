// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.trajectories.commands;

import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.commands.PPSwerveControllerCommand;
import frc.robot.Robot;
import frc.robot.swerve.SwerveConfig;

public class FollowTrajectoryCmd extends PPSwerveControllerCommand {

    /** Creates a new FollowTrajectory. */
    public FollowTrajectoryCmd(PathPlannerTrajectory trajectory) {
        super(
                trajectory,                          // a path to follow
                Robot.swerve::getPoseMeters,         // getter for current position
                SwerveConfig.swerveKinematics,       // chassis dimensions/geometry
                Robot.trajectories.xController,      // PID controller X
                Robot.trajectories.yController,      // PID controller Y
                Robot.trajectories.thetaController,  // PID controller rotation
                Robot.swerve::setModuleStates,       // setter for driving wheels
                Robot.swerve);                       // subsystem required for the command

        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(Robot.swerve);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        super.initialize();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        super.execute();
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return super.isFinished();
    }
}
