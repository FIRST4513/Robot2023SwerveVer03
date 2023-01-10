// Created by Spectrum3847
package frc.robot.swerve.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import frc.robot.Robot;
import frc.robot.pilotGamepad.commands.PilotGamepadCmds;

public class SwerveCmds {
    public static void setupDefaultCommand() {
        Robot.swerve.setDefaultCommand(PilotGamepadCmds.pilotSwerve());
    }

    public static Command lockSwerve() {
        return brakeMode().alongWith(new SetModulesToAngle(225, 135, 315, 45));
    }

    public static Command brakeMode() {
        return new StartEndCommand(
                () -> Robot.swerve.setBrakeMode(true), () -> Robot.swerve.setBrakeMode(false));
    }
}
