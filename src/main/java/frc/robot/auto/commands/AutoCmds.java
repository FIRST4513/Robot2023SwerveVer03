package frc.robot.auto.commands;

import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.PathPlannerTrajectory.PathPlannerState;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;
import frc.robot.swerveDrive.commands.SwerveDrive2Cmd;
import frc.robot.trajectories.commands.FollowTrajectoryCmd;

public class AutoCmds {

    private static Translation2d ctrOfRot = new Translation2d( 0, 0);

    public static Command SetBrakeModeCmd(){
        return new RunCommand(() -> Robot.swerve.setBrakeMode(true));
    }

    public static Command SetCoastModeCmd(){
        return new RunCommand(() -> Robot.swerve.setBrakeMode(false));
    }
        
    public static Command FollowPathCmd(PathPlannerTrajectory path, double time){
        return new FollowTrajectoryCmd(path).withTimeout(time);
    }

    public static Command DriveForTimeCmd(double time, double speed){
        //return new SwerveDrive(false, speed, 0).withTimeout(time);
        return new SwerveDrive2Cmd ( speed, 0.0,  0.0, false, true, ctrOfRot)
                .withTimeout(time);
    }

    public static Command IntializePathFollowingCmd(PathPlannerTrajectory path){
        return new SequentialCommandGroup(
            SetBrakeModeCmd().withTimeout(0.25), //set brake mode and pause for 1/4 second
            IntializeGyroAngleCmd(path), //set gyro to initial heading
            ResetOdometryCmd(path) //reset odometry to the initial position
        );
    }

    public static Command setGryoDegreesCmd(double deg){
        return new InstantCommand(() -> Robot.swerve.setGyroDegrees(deg)).andThen(
            new PrintCommand("Gyro Degrees: " + Robot.swerve.getDegrees())
        );
    }

    public static Command IntializeGyroAngleCmd(PathPlannerTrajectory path){
        PathPlannerState s = (PathPlannerState) path.getStates().get(0);
        return setGryoDegreesCmd(s.holonomicRotation.getDegrees());
    }

    public static Command ResetOdometryCmd(PathPlannerTrajectory path){
        Pose2d tempPose = path.getInitialPose();
        PathPlannerState s = (PathPlannerState) path.getStates().get(0) ;
        Pose2d tempPose2 = new Pose2d(tempPose.getTranslation(), s.holonomicRotation) ;
        return new InstantCommand(() -> Robot.swerve.resetOdometry(tempPose2));
    }

    public static Command feedCmd(double time){
        //return BallPathCommands.feed().withTimeout(time);
        return DriveForTimeCmd( 2.5, 1.5).withTimeout(time);    // Dummy placeholder
    }

    public static Command llShotwithTimeoutCmd(double time){
        //return BallPathCommands.llShotRPM().withTimeout(time);
        //return BallPathCommands.lowGoalShot().withTimeout(time);
        return DriveForTimeCmd( 2.5, 1.5).withTimeout(time);    // Dummy placeholder
    }

    public static Command autoLLAimCmd(){
        //return new LLAim().alongWith(
        // new TeleopSwerve(Robot.swerve, true, true));
        return DriveForTimeCmd( 2.5, 1.5).withTimeout(5.0);    // Dummy placeholder
    }
    
    public static Command intakeCmd(){
        //return BallPathCommands.intakeBalls();
        //return new WaitCommand(1);
        return DriveForTimeCmd( 2.5, 1.5).withTimeout(5.6);    // Dummy placeholder
    }

    public static Command intakeCmd(double time){
        //return intake().withTimeout(time);
        return DriveForTimeCmd( 2.5, 1.5).withTimeout(5.5);    // Dummy placeholder
    }

    public static Command followPathAndIntakeCmd(PathPlannerTrajectory path, double time){
        return new FollowTrajectoryCmd(path).deadlineWith(intakeCmd()).withTimeout(time);
    }

}
