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

    public static Command SetBrakeMode(){
        return new RunCommand(() -> Robot.swerve.setBrakeMode(true));
    }

    public static Command SetCoastMode(){
        return new RunCommand(() -> Robot.swerve.setBrakeMode(false));
    }
        
    public static Command FollowPath(PathPlannerTrajectory path, double time){
        return new FollowTrajectoryCmd(path).withTimeout(time);
    }


    public static Command DriveForTime(double time, double speed){
        //return new SwerveDrive(false, speed, 0).withTimeout(time);
        return new SwerveDrive2Cmd ( speed, 0.0,  0.0, false, true, ctrOfRot)
                .withTimeout(time);
    }


    public static Command IntializePathFollowing(PathPlannerTrajectory path){
        return new SequentialCommandGroup(
            SetBrakeMode().withTimeout(0.25), //set brake mode and pause for 1/4 second
            IntializeGyroAngle(path), //set gyro to initial heading
            ResetOdometry(path) //reset odometry to the initial position
        );
    }

    public static Command setGryoDegrees(double deg){
        return new InstantCommand(() -> Robot.swerve.setGyroDegrees(deg)).andThen(
            new PrintCommand("Gyro Degrees: " + Robot.swerve.getDegrees())
        );
    }

    public static Command IntializeGyroAngle(PathPlannerTrajectory path){
        PathPlannerState s = (PathPlannerState) path.getStates().get(0);
        return setGryoDegrees(s.holonomicRotation.getDegrees());
    }

    public static Command ResetOdometry(PathPlannerTrajectory path){
        Pose2d tempPose = path.getInitialPose();
        PathPlannerState s = (PathPlannerState) path.getStates().get(0) ;
        Pose2d tempPose2 = new Pose2d(tempPose.getTranslation(), s.holonomicRotation) ;
        return new InstantCommand(() -> Robot.swerve.resetOdometry(tempPose2));
    }

    // public static Command feed(double time){
    //     return BallPathCommands.feed().withTimeout(time);
    // }

    // public static Command llShotwithTimeout(double time){
    //     return BallPathCommands.llShotRPM().withTimeout(time);
    //     //return BallPathCommands.lowGoalShot().withTimeout(time);
    // }

    // public static Command autonLLAim(){
    //     return new LLAim().alongWith(
    //      new TeleopSwerve(Robot.swerve, true, true)   
    //     );
    // }
    
    // public static Command intake(){
    //     return BallPathCommands.intakeBalls();
    //     //return new WaitCommand(1);
    // }

    // public static Command intake(double time){
    //     return intake().withTimeout(time);
    // }

}
