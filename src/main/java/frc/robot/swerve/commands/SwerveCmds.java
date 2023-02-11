// Created by Spectrum3847
package frc.robot.swerve.commands;

import frc.robot.Robot;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.PathPlannerTrajectory.PathPlannerState;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.pilot.commands.PilotGamepadCmds;

public class SwerveCmds {
    private static Translation2d ctrOfRot = new Translation2d( 0, 0);
    
    public static void setupDefaultCommand() {
        Robot.swerve.setDefaultCommand(PilotGamepadCmds.FpvPilotSwerveCmd());
    }

    public static Command DriveForTimeCmd(double time, double speed){
        //return new SwerveDrive(false, speed, 0).withTimeout(time);
        return new SwerveDrive2Cmd ( speed, 0.0,  0.0, false, true, ctrOfRot)
                .withTimeout(time);
    }
    public static Command LockSwerveCmd() {
        return SetBrakeModeOnCmd().alongWith(new SetModulesToAngleCmd(225, 135, 315, 45));
    }

    public static Command TestWheelFwdCmd() {
        return SetBrakeModeOnCmd().alongWith(new SetModulesToAngleCmd(0, 0, 0, 0));
    }

    public static Command TestWheelFwdLeftCmd() {
        return SetBrakeModeOnCmd().alongWith(new SetModulesToAngleCmd(45, 45, 45, 45));
    }
    public static Command TestWheelFwdRightCmd() {
        return SetBrakeModeOnCmd().alongWith(new SetModulesToAngleCmd(-45, -45, -45, -45));
    }

    //public static Command brakeModeCmd() {
    //    return new StartEndCommand(
    //            () -> Robot.swerve.setBrakeMode(true), () -> Robot.swerve.setBrakeMode(false));
    //}
    
    public static Command SetBrakeModeOnCmd(){
        return new RunCommand(() -> Robot.swerve.setBrakeMode(true));
    }
    
    public static Command SetBrakeModeOffCmd(){
        return new RunCommand(() -> Robot.swerve.setBrakeMode(false));
    }

    // public static Command SetBrakeModeCmd(boolean state )(
    //     return new ConditionalCommand(
    //         new RunCommand(() -> Robot.swerve.setBrakeMode(true)),
    //         new RunCommand(() -> Robot.swerve.setBrakeMode(false)),
    //         state);
    // }
    
    public static Command SetGyroYawCmd(double deg){
        return new InstantCommand(() -> Robot.swerve.setGyroYawAngle(deg)).andThen(
            new PrintCommand("Gyro Degrees: " + Robot.swerve.getDegrees())
        );
    }

    public static Command IntializeGyroAngleCmd(PathPlannerTrajectory path){
        PathPlannerState s = (PathPlannerState) path.getStates().get(0);    // Starting pose
        return SetGyroYawCmd(s.holonomicRotation.getDegrees());             // Set Gyro to Starting holonomicRotation Hdg +-180
    }

    public static Command ResetOdometryCmd(PathPlannerTrajectory path){
        Pose2d tempPose = path.getInitialPose();
        PathPlannerState s = (PathPlannerState) path.getStates().get(0) ;
        Pose2d tempPose2 = new Pose2d(tempPose.getTranslation(), s.holonomicRotation) ;
        return new InstantCommand(() -> Robot.swerve.resetOdometry(tempPose2));
    }


    public static Command SetCoastModeCmd(){
        return new RunCommand(() -> Robot.swerve.setBrakeMode(false));
    }

    public static Command ResetFalconAnglesCmd() {
        return new InstantCommand( () -> Robot.swerve.resetFalconAngles(), Robot.swerve)
            .withName("ResetFalconAnglesCmd");
    }

    public static Command ZeroGyroHeadingCmd() {
        return new InstantCommand(
            () -> Robot.swerve.resetGyro()
        );
    }

    public static Command SnapTurnCmd() {
        return new TurnToAngleCmd(() -> Robot.swerve.getSnap90Angle());
    }
}
