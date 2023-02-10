package frc.robot.auto;

import java.util.HashMap;

import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.commands.FollowPathWithEvents;

import edu.wpi.first.networktables.PubSubOption;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Robot;
import frc.robot.RobotTelemetry;
import frc.robot.auto.commands.AutoCmds;
import frc.robot.auto.commands.DelayCmd;
import frc.robot.trajectories.commands.TrajectoriesCmds;

public class AutoSetup {
    public static final SendableChooser<String> scoreChooser = new SendableChooser<>();
    public static final SendableChooser<String> positionChooser = new SendableChooser<>();
    public static final SendableChooser<String> crossChooser = new SendableChooser<>();
    public static final SendableChooser<String> dockChooser = new SendableChooser<>();
    private static boolean autoMessagePrinted = true;
    private static double autoStart = 0;
    public static HashMap<String, Command> eventMap = new HashMap<>();
    public static String scoreSelect;
    public static String positionSelect;
    public static String crossSelect;
    public static String dockSelect;
    public static double armPosition;
    public static double elevStartPos;
    public static double elevEndPos;

    static PathPlannerTrajectory crossShortPath     = PathPlanner.loadPath(
                            "CrossShort", AutoConfig.kMaxSpeed, AutoConfig.kMaxAccel);
    static PathPlannerTrajectory crossLongPath      = PathPlanner.loadPath(
                            "CrossLong", AutoConfig.kMaxSpeed, AutoConfig.kMaxAccel);
    static PathPlannerTrajectory crossLongDockPath  = PathPlanner.loadPath(
                            "CrossLongDock", AutoConfig.kMaxSpeed, AutoConfig.kMaxAccel);
    static PathPlannerTrajectory shortDockPath      = PathPlanner.loadPath(
                            "ShortDockPath", AutoConfig.kMaxSpeed, AutoConfig.kMaxAccel);
    static PathPlannerTrajectory longDockPath       = PathPlanner.loadPath(
                            "LongDockPath", AutoConfig.kMaxSpeed, AutoConfig.kMaxAccel);
    static PathPlannerTrajectory leftLongDockPath   = PathPlanner.loadPath(
                            "LeftLongDockPath", AutoConfig.kMaxSpeed, AutoConfig.kMaxAccel);
    static PathPlannerTrajectory rightLongDockPath  = PathPlanner.loadPath(
                            "RightLongDockPath", AutoConfig.kMaxSpeed, AutoConfig.kMaxAccel);

    public AutoSetup(){
        setupSelectors();
        setupEventMap();
    }

    // A chooser for autonomous commands
    public static void setupSelectors() {

        // Selector for Score Position
        scoreChooser.setDefaultOption("Do Nothing", AutoConfig.kNoSelect);
        scoreChooser.addOption("Low", AutoConfig.kLowSelect);
        scoreChooser.addOption("Mid", AutoConfig.kMidSelect);
        scoreChooser.addOption("High", AutoConfig.kHighSelect);

        // Selector for Robot Position
        positionChooser.setDefaultOption("Left", AutoConfig.kLeftSelect);
        positionChooser.addOption("Left", AutoConfig.kLeftSelect);
        positionChooser.addOption("Right", AutoConfig.kRightSelect);
        positionChooser.addOption("Center Left", AutoConfig.kCenterLeftSelect);
        positionChooser.addOption("Center Right", AutoConfig.kCenterRightSelect);

        // 
        dockChooser.setDefaultOption("Do Nothing", AutoConfig.kNoSelect);
        dockChooser.addOption("Dock", AutoConfig.kYesSelect);

        //
        crossChooser.setDefaultOption("Do Nothing", AutoConfig.kNoSelect);
        crossChooser.setDefaultOption("Cross Line", AutoConfig.kYesSelect);
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
        scoreSelect = scoreChooser.getSelected();
        positionSelect = positionChooser.getSelected();
        crossSelect = crossChooser.getSelected();
        dockSelect = dockChooser.getSelected();
    }

    public static Command getAutonomousCommand() {
        getAutoSelections();
        setPlacePositions();

        // ----------------------- Do Nothing -------------------
        if (doNothing()) {
            return new PrintCommand("Do Nothing");
        }

        // ----------------------- Score Only -------------------
        if (placeOnly()) {
            return AutoCmds.PlaceObjectCmd();
        }

        // ----------------------- Cross Line Only -------------------
        if (crossOnly()) {
            if (redRight() || blueLeft()) {                     // Short Cross
                return TrajectoriesCmds.IntializePathFollowingCmd(crossShortPath, 5.0);
            }
            if (centerLeft() || centerRight()) {                // Long Dock Cross
                return TrajectoriesCmds.IntializePathFollowingCmd(crossLongDockPath, 5.0);
            }
            if (redLeft() && blueRight()) {                     // Long Cross
                return TrajectoriesCmds.IntializePathFollowingCmd(crossLongPath, 5.0);
            }
            return new PrintCommand("ERROR: Invalid cross only auto command");
        }

        // ----------------------- Get on Charging Station Only -------------------
        if (dockOnly()) {
            if (centerLeft() || centerRight()) {
                return TrajectoriesCmds.IntializePathFollowingCmd(shortDockPath, 5.0);
            }
            if (redRight() || blueLeft()) {
                return TrajectoriesCmds.IntializePathFollowingCmd(leftLongDockPath, 5.0);
            }
            if (redLeft() || blueRight()) {
                return TrajectoriesCmds.IntializePathFollowingCmd(rightLongDockPath, 5.0);
            }
            return new PrintCommand("ERROR: Invalid dock only auto command");
        }

        // ----------------------- Score and Cross Line -------------------
        if (place() && cross() && !dock()) {
            if (redRight() || blueLeft()) {                     // Short Cross
                return AutoCmds.PlaceObjectRunPathCmd(crossShortPath, 5.0);
            }
            if (centerLeft() || centerRight()) {                // Long Dock Cross
                return AutoCmds.PlaceObjectRunPathCmd(crossLongDockPath, 5.0);
            }
            if (redLeft() && blueRight()) {                     // Long Cross
                return AutoCmds.PlaceObjectRunPathCmd(crossLongPath, 5.0);
            }
            return new PrintCommand("ERROR: Invalid cross and place auto command");
        }

        // ----------------------- Score and Get on Charging Platform  -------------------
        if (place() && !cross() && dock()) {
            if (redRight() || blueLeft()) {                     // Short Cross
                return AutoCmds.PlaceObjectRunPathCmd(leftLongDockPath, 5.0);
            }
            if (centerLeft() || centerRight()) {                // Long Dock Cross
                return AutoCmds.PlaceObjectRunPathCmd(shortDockPath, 5.0);
            }
            if (redLeft() && blueRight()) {                     // Long Cross
                return AutoCmds.PlaceObjectRunPathCmd(rightLongDockPath, 5.0);
            }
            return new PrintCommand("ERROR: Invalid place and dock auto command");
        }

        // ----------------------- Cross Line and Get on Platform -------------------
        if (!place() && cross() && dock()) {
            if (redRight() || blueLeft()) {                     // Short Cross
                return new SequentialCommandGroup(
                    TrajectoriesCmds.IntializePathFollowingCmd(crossShortPath, 5.0) );
            }
            if (centerLeft() || centerRight()) {                // Long Dock Cross
                return new SequentialCommandGroup(
                    TrajectoriesCmds.IntializePathFollowingCmd(crossLongDockPath, 5.0) );
            }
            if (redLeft() && blueRight()) {                     // Long Cross
                return new SequentialCommandGroup(
                    TrajectoriesCmds.IntializePathFollowingCmd(crossLongPath, 5.0) );
            }
            return new PrintCommand("ERROR: Invalid cross and dock auto command");
        }
        
        // ----------------------- Score and Cross Line and Get on Platform -------------------    
        if (place() && cross() && dock()) {
            if (redRight() || blueLeft()) {                     // Short Cross
                return AutoCmds.PlaceObjectRunPathCmd(crossShortPath, 5.0);
            }
            if (centerLeft() || centerRight()) {                // Long Dock Cross
                return AutoCmds.PlaceObjectRunPathCmd(crossLongDockPath, 5.0);
            }
            if (redLeft() && blueRight()) {                     // Long Cross
                return AutoCmds.PlaceObjectRunPathCmd(crossLongPath, 5.0);
            }
            return new PrintCommand("ERROR: Invalid place and cross and dock auto command");
        }
        return new PrintCommand("ERROR: Invalid auto command");
    }


    // ---------------------------------------------------------------
    public static void setPlacePositions() {
        if (cone() && low()) {
            elevStartPos = AutoConfig.coneLowElevStartPos;
            elevEndPos = AutoConfig.coneLowElevEndPos;
            armPosition = AutoConfig.coneLowArmPos;
            return;
        } if (cone() && mid()) {
            elevStartPos = AutoConfig.coneMidElevStartPos;
            elevEndPos = AutoConfig.coneMidElevEndPos;
            armPosition = AutoConfig.coneMidArmPos;
            return;
        } if (cone() && high()) {
            elevStartPos = AutoConfig.coneHighElevStartPos;
            elevEndPos = AutoConfig.coneHighElevEndPos;
            armPosition = AutoConfig.coneHighArmPos;
            return;
        } if (cube() && low()) {
            elevStartPos = AutoConfig.cubeLowElevStartPos;
            elevEndPos = AutoConfig.cubeLowElevEndPos;
            armPosition = AutoConfig.cubeLowArmPos;
            return;
        } if (cube() && mid()) {
            elevStartPos = AutoConfig.cubeMidElevStartPos;
            elevEndPos = AutoConfig.cubeMidElevEndPos;
            armPosition = AutoConfig.cubeMidArmPos;
            return;
        } if (cube() && high()) {
            elevStartPos = AutoConfig.cubeHighElevStartPos;
            elevEndPos = AutoConfig.cubeHighElevEndPos;
            armPosition = AutoConfig.cubeHighArmPos;
            return;
        }
        elevStartPos = 0;
        elevEndPos = 0;
        armPosition = 0;
    }
    
    /** This method is called in AutonInit */
    public static void startAutonTimer() {
        autoStart = Timer.getFPGATimestamp();
        autoMessagePrinted = false;
    }

    private static boolean doNothing() {
        if (!place() && !dock() && !cross()) { return true; }
        return false;
    }
    
    private static boolean place() {
        if (low() || mid() || high()) { return true; }
        return false;
    }

    private static boolean placeOnly() {
        if (place() && !dock() && !cross()) { return true; }
        return false;
    }

    private static boolean cross() {
        if (crossSelect.equals(AutoConfig.kYesSelect)) { return true; }
        return false;
    }

    private static boolean crossOnly() {
        if (cross() && !dock() && !place()) { return true; }
        return false;
    }

    private static boolean dock() {
        if (dockSelect.equals(AutoConfig.kYesSelect)) { return true; }
        return false;
    }

    private static boolean dockOnly() {
        if (dock() && !place() && !cross()) { return true; }
        return false;
    }

    private static boolean red() {
        if (DriverStation.getAlliance() == Alliance.Red) { return true; }
        return false;
    }

    private static boolean blue() {
        if (DriverStation.getAlliance() == Alliance.Blue) { return true; }
        return false;
    }

    private static boolean cube() {
        if (Robot.intake.isCubeDetected()) { return true; }
        return false;
    }

    private static boolean cone() {
        return !cube(); // NOT cube()
    }

    private static boolean left() {
        if (positionSelect.equals(AutoConfig.kLeftSelect)) { return true; }
        return false;
    }

    private static boolean right() {
        if (positionSelect.equals(AutoConfig.kRightSelect)) { return true; }
        return false;
    }

    private static boolean centerLeft() {
        if (positionSelect.equals(AutoConfig.kCenterLeftSelect)) { return true; }
        return false;
    }

    private static boolean centerRight() {
        if (positionSelect.equals(AutoConfig.kCenterRightSelect)) { return true; }
        return false;
    }

    private static boolean low() {
        if (positionSelect.equals(AutoConfig.kLowSelect)) { return true; }
        return false;
    }

    private static boolean mid() {
        if (positionSelect.equals(AutoConfig.kMidSelect)) { return true; }
        return false;
    }

    private static boolean high() {
        if (positionSelect.equals(AutoConfig.kHighSelect)) { return true; }
        return false;
    }

    private static boolean redLeft() {
        if (red() && left()) { return true; }
        return false;
    }

    private static boolean redRight() {
        if (red() && right()) { return true; }
        return false;
    }

    private static boolean blueLeft() {
        if (blue() && left()) { return true; }
        return false;
    }

    private static boolean blueRight() {
        if (blue() && right()) { return true; }
        return false;
    }



   
    /* ---------  Load Auto Path Group ----------------------
    This example will load a path file as a path group. The path group will be separated into paths
    based on which waypoints are marked stop points.


    ArrayList<PathPlannerTrajectory> pathGroup1 = PathPlanner.loadPathGroup(
                                                        "Example Path Group",
                                                        new PathConstraints(4, 3));

    // This will load the file "Example Path Group.path" and generate it with different path constraints for each segment
    ArrayList<PathPlannerTrajectory> pathGroup2 = PathPlanner.loadPathGroup(
        "Example Path Group", 
        new PathConstraints(4, 3), 
        new PathConstraints(2, 2), 
        new PathConstraints(3, 3));
    // -------------------------------------------------------
*/



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
