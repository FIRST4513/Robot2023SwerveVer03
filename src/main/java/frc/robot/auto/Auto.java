package frc.robot.auto;

import java.util.HashMap;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import frc.robot.trajectories.commands.PathBuilder;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;
import frc.robot.RobotTelemetry;
import frc.robot.auto.commands.AutoCmds;
import frc.robot.swerve.commands.SwerveCmds;
import frc.robot.trajectories.commands.TrajectoriesCmds;

public class Auto {
    public static final SendableChooser<String> scoreChooser = new SendableChooser<>();
    public static final SendableChooser<String> positionChooser = new SendableChooser<>();
    public static final SendableChooser<String> crossChooser = new SendableChooser<>();
    public static final SendableChooser<String> dockChooser = new SendableChooser<>();
    public static final SendableChooser<String> testChooser = new SendableChooser<>();

    public static HashMap<String, Command> eventMap = new HashMap<>();
    public static String scoreSelect;
    public static String positionSelect;
    public static String crossSelect;
    public static String dockSelect;
    public static String testSelect;
    public static double armPosition;
    public static double elevStartPos;
    public static double elevEndPos;
    public static Pose2d startPose;
    private static boolean autoMessagePrinted = true;
    private static double autoStart = 0;

    // Setup Needed PathPlaner Paths
    static PathPlannerTrajectory    blueRightCubeCrossPath  = PathPlanner.loadPath(
                                    "BlueRightCubeCross", AutoConfig.kMaxSpeed, AutoConfig.kMaxAccel);        
    static PathPlannerTrajectory    blueLeftCubeCrossPath  = PathPlanner.loadPath(
                                    "BlueLeftCubeCross", AutoConfig.kMaxSpeed, AutoConfig.kMaxAccel);     
    static PathPlannerTrajectory    blueCenterScalePath  = PathPlanner.loadPath(
                                    "BlueCenterScale", AutoConfig.kMaxSpeed, AutoConfig.kMaxAccel);
    static PathPlannerTrajectory    redRightCubeCrossPath  = PathPlanner.loadPath(
                                    "RedRightCubeCross", AutoConfig.kMaxSpeed, AutoConfig.kMaxAccel);        
    static PathPlannerTrajectory    redLeftCubeCrossPath  = PathPlanner.loadPath(
                                    "RedLeftCubeCross", AutoConfig.kMaxSpeed, AutoConfig.kMaxAccel);     
    static PathPlannerTrajectory    redCenterScalePath  = PathPlanner.loadPath(
                                    "RedCenterScale", AutoConfig.kMaxSpeed, AutoConfig.kMaxAccel);


    static PathPlannerTrajectory    blue1MeterPath  = PathPlanner.loadPath(
                                    "blue1Meter", AutoConfig.kMaxSpeed, AutoConfig.kMaxAccel);
    static PathPlannerTrajectory    red1MeterPath  = PathPlanner.loadPath(
                                    "red1Meter", AutoConfig.kMaxSpeed, AutoConfig.kMaxAccel);

    // static PathPlannerTrajectory    testTurnPath  = PathPlanner.loadPath(
    //                                 "TestTurn", AutoConfig.kMaxSpeed, AutoConfig.kMaxAccel);
    // static PathPlannerTrajectory    testTurn2Path  = PathPlanner.loadPath(
    //                                 "TestTurn2", AutoConfig.kMaxSpeed, AutoConfig.kMaxAccel);

    // -----------------------------  Constructor ----------------------------
    public Auto(){
        setupSelectors();       // Setup on screen slection menus
        setupEventMap();
    }

    // ------------------------------------------------------------------------
    //          Setup Shuffle Board Selection Menus for Autonomous
    // ------------------------------------------------------------------------

    public static void setupSelectors() {

        // Selector for Robot Position on field
        positionChooser.setDefaultOption("Left",        AutoConfig.kLeftSelect);
        positionChooser.addOption(       "Center",      AutoConfig.kCenterSelect);
        positionChooser.addOption(       "Right",       AutoConfig.kRightSelect);

        // Selector Score by placing Cube/Cone on selected Position
        scoreChooser.setDefaultOption(  "Do Nothing",   AutoConfig.kNoSelect);
        scoreChooser.addOption(         "Cube Low",     AutoConfig.kLowSelect);
        scoreChooser.addOption(         "Cube Mid",     AutoConfig.kMidSelect);

        // Selector for Getting on Charge Platform
        dockChooser.setDefaultOption(   "Do Nothing",   AutoConfig.kNoSelect);
        dockChooser.addOption(          "Dock",         AutoConfig.kYesSelect);

        // Selector for Crossing the Line to score
        crossChooser.setDefaultOption(  "Do Nothing",   AutoConfig.kNoSelect);
        crossChooser.addOption(         "Cross Line",   AutoConfig.kYesSelect);

        // Selector for Test
        testChooser.setDefaultOption(  "Do Nothing",   AutoConfig.kNoSelect);
        testChooser.addOption(         "Red 1 Meter",  "Red1Meter");
        testChooser.addOption(         "Blue 1 Meter", "Blue1Meter");
    }


    // ------ Get operator selected responses from shuffleboard -----
    public static void getAutoSelections() {
        scoreSelect =       scoreChooser.getSelected();
        positionSelect =    positionChooser.getSelected();
        crossSelect =       crossChooser.getSelected();
        dockSelect =        dockChooser.getSelected();
        testSelect =        testChooser.getSelected();
    }
    

    // ------------------------------------------------------------------------
    //            Get selected Autonomous Command Routine
    // ------------------------------------------------------------------------

    public static Command getAutonomousCommand() {
        getAutoSelections();
        setStartPose();

        // ---------------------- Test ------------------
        if ( testSelect == "Red1Meter") {
            return TrajectoriesCmds.IntializeRobotAndFollowPathCmd(red1MeterPath, 5.0);
        }
        if ( testSelect == "Blue1Meter") {
            return TrajectoriesCmds.IntializeRobotAndFollowPathCmd(blue1MeterPath, 5.0);
        }


        // ----------------------- Do Nothing -------------------
        if (doNothing()) {
            // Set position and Gyro Heading based on position
            return new SequentialCommandGroup(            
                AutoCmds.IntializeRobotFromPoseCmd(startPose)
            );
        }

        // ----------------------- Score Only -------------------
        if (placeOnly()) {
            if ( low() ) {
                // Set position and Gyro Heading based on position
                return new SequentialCommandGroup(           
                    AutoCmds.IntializeRobotFromPoseCmd(startPose),
                    AutoCmds.PlaceCubeLowCmd()
                );
            }
            if ( mid() ) {
                // Set position and Gyro Heading based on position
                return new SequentialCommandGroup(           
                    AutoCmds.IntializeRobotFromPoseCmd(startPose),
                    AutoCmds.PlaceCubeMidCmd()
                );
            }
            return new PrintCommand("ERROR: Invalid place only auto command");
        }

        // ----------------------- Cross Line Only -------------------
        if (crossOnly()) {
            if (redRight()) {
                return TrajectoriesCmds.IntializeRobotAndFollowPathCmd(redRightCubeCrossPath, 5.0);
            }
            if (redLeft()) {
                return TrajectoriesCmds.IntializeRobotAndFollowPathCmd(redLeftCubeCrossPath, 5.0);
            }
            if (blueRight()) {
                return TrajectoriesCmds.IntializeRobotAndFollowPathCmd(blueRightCubeCrossPath, 5.0);
            }
            if (blueLeft()) {
                return TrajectoriesCmds.IntializeRobotAndFollowPathCmd(blueLeftCubeCrossPath, 5.0);
            }
            return new PrintCommand("ERROR: Invalid cross only auto command");
        }
    
        // ----------------------- Place and Cross Line  -------------------
        if ( place() && cross() ) {
            if ( low() ){
                if ( redRight()) {
                    // Set position and Gyro Heading based on position
                    return new SequentialCommandGroup(           
                        AutoCmds.IntializeRobotFromPoseCmd(startPose),
                        AutoCmds.PlaceCubeLowCmd(), // Returns to store position at completion
                        TrajectoriesCmds.IntializeRobotAndFollowPathCmd(redRightCubeCrossPath, 5.0)
                    );
                }
                if ( redLeft()) {
                    // Set position and Gyro Heading based on position
                    return new SequentialCommandGroup(           
                        AutoCmds.IntializeRobotFromPoseCmd(startPose),
                        AutoCmds.PlaceCubeLowCmd(), // Returns to store position at completion
                        TrajectoriesCmds.IntializeRobotAndFollowPathCmd(redLeftCubeCrossPath, 5.0)
                    );
                }
                if ( blueRight()) {
                    // Set position and Gyro Heading based on position
                    return new SequentialCommandGroup(           
                        AutoCmds.IntializeRobotFromPoseCmd(startPose),
                        AutoCmds.PlaceCubeLowCmd(), // Returns to store position at completion
                        TrajectoriesCmds.IntializeRobotAndFollowPathCmd(blueRightCubeCrossPath, 5.0)
                    );
                }
                if ( blueLeft()) {
                    // Set position and Gyro Heading based on position
                    return new SequentialCommandGroup(           
                        AutoCmds.IntializeRobotFromPoseCmd(startPose),
                        AutoCmds.PlaceCubeLowCmd(), // Returns to store position at completion
                        TrajectoriesCmds.IntializeRobotAndFollowPathCmd(blueLeftCubeCrossPath, 5.0)
                    );
                }
            }
            if ( mid() ){
                if ( redRight()) {
                    // Set position and Gyro Heading based on position
                    return new SequentialCommandGroup(           
                        AutoCmds.IntializeRobotFromPoseCmd(startPose),
                        AutoCmds.PlaceCubeMidCmd(), // Returns to store position at completion
                        TrajectoriesCmds.IntializeRobotAndFollowPathCmd(redRightCubeCrossPath, 5.0)
                    );
                }
                if ( redLeft()) {
                    // Set position and Gyro Heading based on position
                    return new SequentialCommandGroup(           
                        AutoCmds.IntializeRobotFromPoseCmd(startPose),
                        AutoCmds.PlaceCubeMidCmd(), // Returns to store position at completion
                        TrajectoriesCmds.IntializeRobotAndFollowPathCmd(redLeftCubeCrossPath, 5.0)
                    );
                }
                if ( blueRight()) {
                    // Set position and Gyro Heading based on position
                    return new SequentialCommandGroup(           
                        AutoCmds.IntializeRobotFromPoseCmd(startPose),
                        AutoCmds.PlaceCubeMidCmd(), // Returns to store position at completion
                        TrajectoriesCmds.IntializeRobotAndFollowPathCmd(blueRightCubeCrossPath, 5.0)
                    );
                }
                if ( blueLeft()) {
                    // Set position and Gyro Heading based on position
                    return new SequentialCommandGroup(           
                        AutoCmds.IntializeRobotFromPoseCmd(startPose),
                        AutoCmds.PlaceCubeMidCmd(), // Returns to store position at completion
                        TrajectoriesCmds.IntializeRobotAndFollowPathCmd(blueLeftCubeCrossPath, 5.0)
                    );
                }
            }
        }

        // ----------------------- Get on Charging Station Only -------------------
        if (dockOnly()) {
            if ( red() ) {
                return TrajectoriesCmds.IntializeRobotAndFollowPathCmd(redCenterScalePath, 5.0);
            }
            if ( blue() ) {
                return TrajectoriesCmds.IntializeRobotAndFollowPathCmd(blueCenterScalePath, 5.0);
            }
            return new PrintCommand("ERROR: Invalid dock only auto command");
        }

        // ----------------------- Score and Get on Charging Platform  -------------------
        if (place() && !cross() && dock()) {
            if ( low() ) {
                if ( red() ) {
                    return new SequentialCommandGroup(  
                        AutoCmds.PlaceCubeLowCmd(), // Returns to store position at completion
                        TrajectoriesCmds.IntializeRobotAndFollowPathCmd(redCenterScalePath, 5.0)
                    );
                }
                if ( blue() ) {
                    return new SequentialCommandGroup(  
                        AutoCmds.PlaceCubeLowCmd(), // Returns to store position at completion
                        TrajectoriesCmds.IntializeRobotAndFollowPathCmd(blueCenterScalePath, 5.0)
                    );
                }
            }
            if ( mid() ) {
                if ( red() ) {
                    return new SequentialCommandGroup(  
                        AutoCmds.PlaceCubeMidCmd(), // Returns to store position at completion
                        TrajectoriesCmds.IntializeRobotAndFollowPathCmd(redCenterScalePath, 5.0)
                    );
                }
                if ( blue() ) {
                    return new SequentialCommandGroup(  
                        AutoCmds.PlaceCubeMidCmd(), // Returns to store position at completion
                        TrajectoriesCmds.IntializeRobotAndFollowPathCmd(blueCenterScalePath, 5.0)
                    );
                }
            }

        }         
        return new PrintCommand("Invalid AutoChoose");
    }

        // ----------------------- Test Routines ----------------
        // if (testSelect == "1Meter") {      
        //    return TrajectoriesCmds.IntializeRobotAndFollowPathCmd(test1MeterPath, 10.0);        
        // }

        // if (testSelect == "Test Turn2") {
        //     return new SequentialCommandGroup(
        //         TrajectoriesCmds.IntializeRobotAndFollowPathCmd(testTurn2Path, 10.0),
        //         SwerveCmds.SetBrakeModeOnCmd()
        //     );
        // }

        // if (testSelect == "BlueCtrTable") {   
        //     return new SequentialCommandGroup(
        //         TrajectoriesCmds.IntializeRobotAndFollowPathCmd(blueCtrTablePath, 10.0), 
        //         SwerveCmds.LockSwerveCmd()
        //     );    
        // }
    // ------------------------------------------------------------------------
    //     Setup proper Arm/Elevator position to Place Cone/Cube
    // ------------------------------------------------------------------------

    public static void setStartPose() {
        // Set position and Gyro Heading based on position
        startPose = new Pose2d(new Translation2d(0,0), new Rotation2d(0));
        if (left())         startPose = AutoConfig.kLeftPose;
        if (right())        startPose =AutoConfig.kRightPose;
        if (center())       startPose = AutoConfig.kCenterPose;
    }          
    
    // ------------------------------------------------------------------------
    //                      Setup any Event Mapped Commands
    // ------------------------------------------------------------------------

    // Adds event mapping to autonomous commands
    public static void setupEventMap(){
        eventMap.put("Marker1", new PrintCommand("Passed marker 1"));
        eventMap.put("Marker2", new PrintCommand("Passed marker 2"));
    }


    // ------------------------------------------------------------------------
    //            Simple Checks to make above routines cleaner
    // ------------------------------------------------------------------------
    private static boolean doNothing() {
        if (!place() && !dock() && !cross()) { return true; }
        return false;
    }
    
    private static boolean place() {
        if (low() || mid() ) { return true; }
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

    private static boolean left() {
        if (positionSelect.equals(AutoConfig.kLeftSelect)) { return true; }
        return false;
    }

    private static boolean right() {
        if (positionSelect.equals(AutoConfig.kRightSelect)) { return true; }
        return false;
    }

    private static boolean center() {
        if (positionSelect.equals(AutoConfig.kCenterSelect)) { return true; }
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


    // ------------------------------ Print Auto Duration ---------------------------
    /** Called in RobotPeriodic and displays the duration of the auto command Based on 6328 code */
    public static void printAutoDuration() {
        Command autoCommand = Auto.getAutonomousCommand();
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
