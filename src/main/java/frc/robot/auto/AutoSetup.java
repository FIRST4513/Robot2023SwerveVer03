package frc.robot.auto;

import java.util.HashMap;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import frc.robot.Robot;

public class AutoSetup {
    public static final SendableChooser<String> autoChooser = new SendableChooser<>();
    public static final SendableChooser<String> positionChooser = new SendableChooser<>();
    public static final SendableChooser<String> heightChooser = new SendableChooser<>();
    public static final SendableChooser<String> objectChooser = new SendableChooser<>();
    private static boolean autoMessagePrinted = true;
    private static double autoStart = 0;
    public static HashMap<String, Command> eventMap = new HashMap<>();
    public static String autoSelect;
    public static String positionSelect;
    public static String objectSelect;
    public static String heightSelect;

    public AutoSetup(){
        setupSelectors();
        setupEventMap();
    }

    // A chooser for autonomous commands
    public static void setupSelectors() {

        // Selector for Routine
        autoChooser.setDefaultOption("Nothing", "Nothing");
        autoChooser.addOption("Place", "Place");
        autoChooser.addOption("Place and Cross", "Place and Cross");
        autoChooser.addOption("Place and Charge", "Place and Charge");
        autoChooser.addOption("Place and Cross and Charge", "Place and Cross and Charge");
        autoChooser.addOption("Place and Cross and Pickup", "Place and Cross and Pickup");
        autoChooser.addOption("Place and Cross and Pickup and Charge", "Place and Cross and Pickup and Charge");

        // Selector for Position
        positionChooser.setDefaultOption("Left", "Left");
        positionChooser.addOption("Left", "Left");
        positionChooser.addOption("Center", "Center");
        positionChooser.addOption("Right", "Right");

        // Selector for Game Piece
        objectChooser.setDefaultOption("Cube", "Cube");
        objectChooser.addOption("Cube", "Cube");
        objectChooser.addOption("Cone", "Cone");

        // Selector for Height
        heightChooser.setDefaultOption("Low", "Low");
        heightChooser.addOption("Low", "Low");
        heightChooser.addOption("Mid", "Mid");
        heightChooser.addOption("High", "High");

        // Selector for starting Location on Field
        
    }

    // Adds event mapping to autonomous commands
    public static void setupEventMap(){
        eventMap.put("Marker1", new PrintCommand("Passed marker 1"));
        eventMap.put("Marker2", new PrintCommand("Passed marker 2"));
    }


    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public static void getAutoSelections() {
        // return new CharacterizeLauncher(Robot.launcher);
        autoSelect = autoChooser.getSelected();
        positionSelect = positionChooser.getSelected();
        objectSelect = objectChooser.getSelected();
        heightSelect = heightChooser.getSelected();
    }

    public static Command getAutonomousCommand() {
        if (autoSelect == "Nothing") {
            return new PrintCommand("Do Nothing");
        }
        if (autoSelect == "Place") {
            
        }

        return new PrintCommand("Do Nothing");
    }

    /** This method is called in AutonInit */
    public static void startAutonTimer() {
        autoStart = Timer.getFPGATimestamp();
        autoMessagePrinted = false;
    }

    /** Called in RobotPeriodic and displays the duration of the auton command Based on 6328 code */
    // public static void printAutoDuration() {
        // Command autoCommand = AutoSetup.getAutonomousCommand();
        // if (autoCommand != null) {
        //     if (!autoCommand.isScheduled() && !autoMessagePrinted) {
        //         if (DriverStation.isAutonomousEnabled()) {
        //             RobotTelemetry.print(
        //                     String.format(
        //                             "*** Auton finished in %.2f secs ***",
        //                             Timer.getFPGATimestamp() - autoStart));
        //         } else {
        //             RobotTelemetry.print(
        //                     String.format(
        //                             "*** Auton CANCELLED in %.2f secs ***",
        //                             Timer.getFPGATimestamp() - autoStart));
        //         }
        //         autoMessagePrinted = true;
        //     }
        // }
    // }
}
