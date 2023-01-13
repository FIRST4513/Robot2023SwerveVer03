// Created by Spectrum3847
package frc.robot.swerveDrive.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import frc.robot.Robot;
import frc.robot.pilot.commands.PilotGamepadCmds;

public class SwerveDriveCmds {
    public static void setupDefaultCommand() {
        Robot.swerve.setDefaultCommand(PilotGamepadCmds.fpvPilotSwerveCmd());
    }

    public static Command lockSwerveCmd() {
        return brakeModeCmd().alongWith(new SetModulesToAngleCmd(225, 135, 315, 45));
    }

    public static Command testWheelFwdCmd() {
        return brakeModeCmd().alongWith(new SetModulesToAngleCmd(0, 0, 0, 0));
    }

    public static Command testWheelFwdLeftCmd() {
        return brakeModeCmd().alongWith(new SetModulesToAngleCmd(45, 45, 45, 45));
    }
    public static Command testWheelFwdRightCmd() {
        return brakeModeCmd().alongWith(new SetModulesToAngleCmd(-45, -45, -45, -45));
    }
    public static Command brakeModeCmd() {
        return new StartEndCommand(
                () -> Robot.swerve.setBrakeMode(true), () -> Robot.swerve.setBrakeMode(false));
    }
}
