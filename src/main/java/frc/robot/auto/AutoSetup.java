package frc.robot.auto;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import frc.robot.Robot;
import frc.robot.auto.commands.delayCmd;

public class AutoSetup {
    public static final SendableChooser<Command> autoChooser = new SendableChooser<>();

    // A chooser for autonomous commands
    public static void setupSelectors() {
        autoChooser.setDefaultOption("Do Nothing", new PrintCommand("DO NOTHING AUTO RUNNING"));
        autoChooser.addOption("Delay 10 Sec", new delayCmd(10));
        autoChooser.addOption("Nothing", new PrintCommand("DO NOTHING AUTON RUNNING"));
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public static Command getAutonomousCommand() {
        // return new CharacterizeLauncher(Robot.launcher);
        return autoChooser.getSelected();
    }
}
