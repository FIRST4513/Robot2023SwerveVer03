// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.trajectories.commands;


import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.PathPoint;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import java.util.LinkedList;

public class FollowOnTheFlyPath extends CommandBase {

    private LinkedList<PathPoint> endPoints = new LinkedList<>();
    private PathPlannerTrajectory path;

    private double maxVelocity;
    private double maxAcceleration;

    private double startXPos;
    private double startYPos;
    private double startHeading;
    private double startRotation;
    private double startVelocity;

    private Command pathFollowingCommmand = null;

    /*Creates a new GeneratePath.
     * * @param firstPoints*/
    public FollowOnTheFlyPath(LinkedList<PathPoint> fullPath) {
        endPoints = new LinkedList<PathPoint>();
        endPoints = fullPath;
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(Robot.swerve);
    }

    // Called when the command is initially scheduled.
    @Override
    @SuppressWarnings("unchecked")
    public void initialize() {
        // LinkedList<PathPoint> fullPath = new LinkedList<>();
        // fullPath = (LinkedList<PathPoint>) endPoints.clone();

        // maxVelocity = TrajectoriesConfig.kGenPathMaxSpeed;
        // maxAcceleration = TrajectoriesConfig.kGenPathMaxAccel;

        // startXPos = Robot.pose.getPosition().getX();
        // startYPos = Robot.pose.getPosition().getY();
        // startHeading = Robot.swerve.getFieldRelativeHeading().getDegrees();
        // startRotation = Robot.swerve.getRotation().getDegrees();
        // startVelocity = Robot.swerve.getFieldRelativeMagnitude();

        // double constantRotation;
        // double constantHeading;
        // double constantYPos;

        // constantRotation = TrajectoriesConfig.constantRotation;
        // constantHeading = TrajectoriesConfig.constantHeading;

        // constantYPos = TrajectoriesConfig.bumpYPosition;

        // if (startYPos < TrajectoriesConfig.changeYPositionLine) {
        //     for (int i = 0; i < TrajectoriesConfig.xPositions.length; i++) {
        //         fullPath.set(
        //                 i,
        //                 new PathPoint(
        //                         new Translation2d(TrajectoriesConfig.xPositions[i], constantYPos),
        //                         Rotation2d.fromDegrees(constantHeading),
        //                         Rotation2d.fromDegrees(constantRotation)));
        //     }
        // }

        // for (int i = fullPath.size() - 3; i >= 0; i--) {
        //     if (TrajectoriesConfig.xPositions[i] > startXPos) {
        //         fullPath.remove(i);
        //     }
        // }

        // fullPath.add(
        //         0,
        //         new PathPoint(
        //                 new Translation2d(startXPos, startYPos),
        //                 Rotation2d.fromDegrees(startHeading),
        //                 Rotation2d.fromDegrees(startRotation),
        //                 startVelocity) // position, heading(direction of
        //         // travel), holonomic rotation,
        //         // velocity override
        //         );

        // path =
        //         PathPlanner.generatePath(
        //                 new PathConstraints(maxVelocity, maxAcceleration),
        //                 fullPath // position, heading(direction of travel),
        //                 // holonomic
        //                 // rotation
        //                 );
        // pathFollowingCommmand = PathBuilderEstimatedPose.pathBuilder.followPath(path);
        // pathFollowingCommmand.initialize();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // pathFollowingCommmand.execute();
        // pathFollowingCommmand.end(isFinished());
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        //pathFollowingCommmand.end(interrupted);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        // return pathFollowingCommmand.isFinished();
        return true;
    }
}
