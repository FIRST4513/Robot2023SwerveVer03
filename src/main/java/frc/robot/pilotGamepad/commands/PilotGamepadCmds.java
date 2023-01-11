package frc.robot.pilotGamepad.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.Robot;
import frc.robot.pilotGamepad.PilotGamepadConfig;
import frc.robot.swerve.commands.SwerveDriveCmd;
import frc.robot.trajectories.commands.TrajectoriesCmds;

/** Add your docs here. */
public class PilotGamepadCmds {

    /** Set default command to turn off the rumble */
    public static void setupDefaultCommand() {
        Robot.pilotGamepad.setDefaultCommand(rumblePilot(0));
    }


    /** Set default command to turn off the rumble */
    //public static void setupDefaultCommand() {
    //    Robot.pilotGamepad.setDefaultCommand(
    //            rumble(0, 9999).repeatedly().withName("DisablePilotRumble"));
    //}

    /** Field Oriented Drive */
    public static Command pilotSwerve() {
        return new SwerveDriveCmd(
                        () -> Robot.pilotGamepad.getDriveFwdPositive(),
                        () -> Robot.pilotGamepad.getDriveLeftPositive(),
                        () -> Robot.pilotGamepad.getDriveRotationCCWPositive())
                .withName("PilotSwerve");
    }

    /** Robot Oriented Drive */
    public static Command fpvPilotSwerve() {
        return new SwerveDriveCmd(
                        () -> Robot.pilotGamepad.getDriveFwdPositive(),
                        () -> Robot.pilotGamepad.getDriveLeftPositive(),
                        () -> Robot.pilotGamepad.getDriveRotationCCWPositive(),
                        false)
                .withName("fpvPilotSwerve");
    }

    public static Command snakeDrive() {
        return TrajectoriesCmds.resetThetaController()
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
        // public static Command stickSteer() {
        //     return aimPilotDrive(() -> Robot.pilotGamepad.getRightStickAngle()).withName("StickSteer");
        // }
    
        // /** Drive while aiming to a specific angle, uses theta controller from Trajectories */
        // public static Command aimPilotDrive(double goalAngleRadians) {
        //     return aimPilotDrive(() -> goalAngleRadians);
        // }
    
        // /** Reset the Theata Controller and then run the SwerveDrive command and pass a goal Supplier */
        // public static Command aimPilotDrive(DoubleSupplier goalAngleSupplierRadians) {
        //     return TrajectoriesCmds.resetThetaController()
        //             .andThen(
        //                     new SwerveDrive(
        //                             () -> Robot.pilotGamepad.getDriveFwdPositive(),
        //                             () -> Robot.pilotGamepad.getDriveLeftPositive(),
        //                             Robot.trajectories.calculateThetaSupplier(goalAngleSupplierRadians),
        //                             true,
        //                             false))
        //             .withName("AimPilotDrive");
        // }



    /** Command that can be used to rumble the pilot controller */
    public static Command rumblePilot(double intensity) {
        return new RunCommand(() -> Robot.pilotGamepad.rumble(intensity), Robot.pilotGamepad);
    }
}
