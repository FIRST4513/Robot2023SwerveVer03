package frc.robot.auto;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Robot;
import frc.robot.RobotTelemetry;
import frc.robot.auto.commands.delayCmd;

public class AutoSetup {
    public static final SendableChooser<Command> autoChooser = new SendableChooser<>();
    private static boolean autoMessagePrinted = true;
    private static double autoStart = 0;

    // A chooser for autonomous commands
    public static void setupSelectors() {
        autoChooser.setDefaultOption(
                "Nothing", new PrintCommand("Doing Nothing in Auton").andThen(new WaitCommand(5)));
        autoChooser.setDefaultOption("Do Nothing", new PrintCommand("DO NOTHING AUTO RUNNING"));
        autoChooser.addOption("Delay 10 Sec", new delayCmd(10));    
        }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public static Command getAutonomousCommand() {
        // return new CharacterizeLauncher(Robot.launcher);
        Command auton = autoChooser.getSelected();
        if (auton != null) {
            return auton;
        } else {
            return new PrintCommand("*** AUTON COMMAND IS NULL ***");
        }
    }

    /** This method is called in AutonInit */
    public static void startAutonTimer() {
        autoStart = Timer.getFPGATimestamp();
        autoMessagePrinted = false;
    }

    /** Called in RobotPeriodic and displays the duration of the auton command Based on 6328 code */
    public static void printAutoDuration() {
        Command autoCommand = AutoSetup.getAutonomousCommand();
        if (autoCommand != null) {
            if (!autoCommand.isScheduled() && !autoMessagePrinted) {
                if (DriverStation.isAutonomousEnabled()) {
                    RobotTelemetry.print(
                            String.format(
                                    "*** Auton finished in %.2f secs ***",
                                    Timer.getFPGATimestamp() - autoStart));
                } else {
                    RobotTelemetry.print(
                            String.format(
                                    "*** Auton CANCELLED in %.2f secs ***",
                                    Timer.getFPGATimestamp() - autoStart));
                }
                autoMessagePrinted = true;
            }
        }
    }
}
