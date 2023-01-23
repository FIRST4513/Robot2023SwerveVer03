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
import frc.robot.swerveDrive.commands.SwerveDriveCmds;
import frc.robot.trajectories.commands.FollowTrajectoryCmd;

public class AutoCmds {

    private static Translation2d ctrOfRot = new Translation2d( 0, 0);

    public static Command feedCmd(double time){
        //return BallPathCommands.feed().withTimeout(time);
        return SwerveDriveCmds.DriveForTimeCmd( 2.5, 1.5).withTimeout(time);    // Dummy placeholder
    }

    public static Command llShotwithTimeoutCmd(double time){
        //return BallPathCommands.llShotRPM().withTimeout(time);    // Bring up to RPM
        //return BallPathCommands.lowGoalShot().withTimeout(time);  // Shoot Low Goal
        return SwerveDriveCmds.DriveForTimeCmd( 2.5, 1.5).withTimeout(time);    // Dummy placeholder
    }

    public static Command autoLLAimCmd(){
        // Keeps robot aligned with target for shooring
        //return new LLAim().alongWith(
        // new TeleopSwerve(Robot.swerve, true, true));
        return SwerveDriveCmds.DriveForTimeCmd( 2.5, 1.5).withTimeout(5.0);    // Dummy placeholder
    }
    
    public static Command intakeCmd(){
        //return BallPathCommands.intakeBalls();
        //return new WaitCommand(1);
        return SwerveDriveCmds.DriveForTimeCmd( 2.5, 1.5).withTimeout(5.6);    // Dummy placeholder
    }

    public static Command intakeCmd(double time){
        //return intake().withTimeout(time);
        return SwerveDriveCmds.DriveForTimeCmd( 2.5, 1.5).withTimeout(5.5);    // Dummy placeholder
    }

    public static Command followPathAndIntakeCmd(PathPlannerTrajectory path, double time){
        return new FollowTrajectoryCmd(path).deadlineWith(intakeCmd()).withTimeout(time);
    }

}
