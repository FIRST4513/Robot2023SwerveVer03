package frc.robot.pilot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.Robot;
import frc.robot.pilot.PilotGamepadConfig;
import frc.robot.swerve.commands.SwerveDriveCmd;
import frc.robot.swerve.commands.TurnToAngleCmd;
import frc.robot.trajectories.commands.TrajectoriesCmds;

/** Add your docs here. */
public class PilotGamepadCmds {

    /** Set default command to turn off the rumble */
    public static void setupDefaultCommand() {
        Robot.pilotGamepad.setDefaultCommand(RumblePilotCmd(0));
    }

    public static Command BasicSnapCmd() {
        return new TurnToAngleCmd(() -> Robot.swerve.getSnap90Angle())
            .withName("BasicSnapCmd")
            .withTimeout(5.0);
    }

    /** Set default command to turn off the rumble */
    //public static void setupDefaultCommand() {
    //    Robot.pilotGamepad.setDefaultCommand(
    //            rumble(0, 9999).repeatedly().withName("DisablePilotRumble"));
    //}

    /** Field Oriented Drive */
    public static Command FpvPilotSwerveCmd() {
        return new SwerveDriveCmd(
                        () -> Robot.pilotGamepad.getDriveFwdPositive(),
                        () -> Robot.pilotGamepad.getDriveLeftPositive(),
                        () -> Robot.pilotGamepad.getDriveRotationCCWPositive(),
                        true )
                .withName("FpvPilotSwerveCmd");
    }

    /** Field Oriented Drive */
    public static Command FpvDriveAndAutoRotateCmd() {
        return new TurnToAngleCmd(
                        () -> Robot.pilotGamepad.getDriveFwdPositive(),
                        () -> Robot.pilotGamepad.getDriveLeftPositive(),
                        () -> Robot.swerve.getSnap90Angle())
                .withName("FpvDriveAndAutoRotateCmd")
                .withTimeout(5.0);
    }

    /** Robot Oriented Drive */
    public static Command RpvPilotSwerveCmd() {
        return new SwerveDriveCmd(
                        () -> Robot.pilotGamepad.getDriveFwdPositive(),
                        () -> Robot.pilotGamepad.getDriveLeftPositive(),
                        () -> Robot.pilotGamepad.getDriveRotationCCWPositive(),
                        false)
                .withName("RpvPilotSwerveCmd");
    }

    public static Command snakeDriveCmd() {
        return TrajectoriesCmds.ResetThetaControllerCmd()
                .andThen(
                        new SwerveDriveCmd(
                                () -> Robot.pilotGamepad.getDriveFwdPositive(),
                                () -> Robot.pilotGamepad.getDriveLeftPositive(),
                                Robot.trajectories.calculateThetaSupplier(
                                        () -> Robot.pilotGamepad.getDriveAngle()),
                                true,
                                false,
                                PilotGamepadConfig.intakeCoRmeters))
                .withName("SnakeDrive");
    }
    
        /**
         * Drive the robot and control orientation using the right stick
         *
         * @return
         */
        // public static Command stickSteerCmd() {
        //     return aimPilotDriveCmd(() -> Robot.pilotGamepad.getRightStickAngle()).withName("StickSteer");
        // }
    
        // /** Drive while aiming to a specific angle, uses theta controller from Trajectories */
        // public static Command aimPilotDriveCmd(double goalAngleRadians) {
        //     return aimPilotDriveCmd(() -> goalAngleRadians);
        // }
    
        // /** Reset the Theata Controller and then run the SwerveDrive command and pass a goal Supplier */
        // public static Command aimPilotDriveCmd(DoubleSupplier goalAngleSupplierRadians) {
        //     return TrajectoriesCmds.resetThetaControllerCmd()
        //             .andThen(
        //                     new SwerveDriveCmd(
        //                             () -> Robot.pilotGamepad.getDriveFwdPositive(),
        //                             () -> Robot.pilotGamepad.getDriveLeftPositive(),
        //                             Robot.trajectories.calculateThetaSupplier(goalAngleSupplierRadians),
        //                             true,
        //                             false))
        //             .withName("AimPilotDrive");
        // }



    /** Command that can be used to rumble the pilot controller */
    public static Command RumblePilotCmd(double intensity) {
        return new RunCommand(() -> Robot.pilotGamepad.rumble(intensity), Robot.pilotGamepad);
    }
}
