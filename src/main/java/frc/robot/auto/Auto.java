package frc.robot.auto;

import java.util.HashMap;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.auto.commands.AutoCmds;
import frc.robot.trajectories.commands.TrajectoriesCmds;

public class Auto {
    public static final SendableChooser<String> scoreChooser = new SendableChooser<>();
    public static final SendableChooser<String> positionChooser = new SendableChooser<>();
    public static final SendableChooser<String> crossChooser = new SendableChooser<>();
    public static final SendableChooser<String> dockChooser = new SendableChooser<>();
    public static final SendableChooser<String> levelChooser = new SendableChooser<>();
    public static final SendableChooser<String> testChooser = new SendableChooser<>();
    public static final SendableChooser<String> cubeChooser = new SendableChooser<>();

    public static HashMap<String, Command> eventMap = new HashMap<>();
    public static String scoreSelect;
    public static String positionSelect;
    public static String crossSelect;
    public static String dockSelect;
    public static String levelSelect;
    public static String testSelect;
    public static String cubeSelect;
    public static String level = "";

    public static double armPosition;
    public static double elevStartPos;
    public static double elevEndPos;
    public static Pose2d startPose;
    private static boolean autoMessagePrinted = true;
    private static double autoStart = 0;

    // Setup Needed PathPlaner Paths
    static PathPlannerTrajectory    blueRightCubeLongPath  = PathPlanner.loadPath(
                                    "BlueRightCubeLong", AutoConfig.kMaxSpeed, AutoConfig.kMaxAccel);        
    static PathPlannerTrajectory    blueLeftCubeShortPath  = PathPlanner.loadPath(
                                    "BlueLeftCubeShort", AutoConfig.kMaxSpeed, AutoConfig.kMaxAccel);    
    // ----- Center Scale Paths ------                                     
    // static PathPlannerTrajectory    CenterScalePathA  = PathPlanner.loadPath(
    //                                 "CtrScale1", 3.0, 4.0);
    // static PathPlannerTrajectory    CenterScalePathB  = PathPlanner.loadPath(
    //                                 "CtrScale2", 2.0, 1.0);
    static PathPlannerTrajectory    CenterScalePath = PathPlanner.loadPath(
                                    "CtrScale", 1.0, 1.0);

    // ----- Blue Short Cross and Scale Paths ------
    static PathPlannerTrajectory    BlueShortCrossScalePathA  = PathPlanner.loadPath(
                                    "BlueShortCrossScale1", 4.5, 4.5);
    static PathPlannerTrajectory    BlueShortCrossScalePathB  = PathPlanner.loadPath(
                                    "BlueShortCrossScale2", 0.5, 0.5);
    static PathPlannerTrajectory    BlueConeShortCrossScalePathA  = PathPlanner.loadPath(
                                    "BlueConeShortCrossScale1", 4.5, 4.5);
    // ----- Red Short Cross and Scale Paths ------
    static PathPlannerTrajectory    RedShortCrossScalePathA  = PathPlanner.loadPath(
                                    "RedShortCrossScale1", 4.5, 4.5);
    static PathPlannerTrajectory    RedShortCrossScalePathB  = PathPlanner.loadPath(
                                    "RedShortCrossScale2", 0.5, 0.5);
    static PathPlannerTrajectory    RedConeShortCrossScalePathA  = PathPlanner.loadPath(
                                   "RedConeShortCrossScale1", 4.5, 4.5);

    // ----- red Right Cube Old Paths ----
    static PathPlannerTrajectory    redRightCubeShortPath  = PathPlanner.loadPath(
                                    "RedRightCubeShort", AutoConfig.kMaxSpeed, AutoConfig.kMaxAccel);        
    static PathPlannerTrajectory    redLeftCubeLongPath  = PathPlanner.loadPath(
                                    "RedLeftCubeLong", AutoConfig.kMaxSpeed, AutoConfig.kMaxAccel);     

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
        scoreChooser.addOption(         "Low",     AutoConfig.kLowSelect);
        scoreChooser.addOption(         "Mid",     AutoConfig.kMidSelect);
        scoreChooser.addOption(         "High",     AutoConfig.kHighSelect);

        // Selector for Getting on Charge Platform
        dockChooser.setDefaultOption(   "Do Nothing",   AutoConfig.kNoSelect);
        dockChooser.addOption(          "Dock",         AutoConfig.kYesSelect);

        // Selector for leveling on charge platform
        levelChooser.setDefaultOption("No Auto Level", AutoConfig.kNoSelect);
        levelChooser.addOption("Auto Level", AutoConfig.kYesSelect);

        // Selector for Crossing the Line to score
        crossChooser.setDefaultOption(  "Do Nothing",   AutoConfig.kNoSelect);
        crossChooser.addOption(         "Cross Line",   AutoConfig.kYesSelect);

        // Selector for Test
        testChooser.setDefaultOption(  "Do Nothing",   AutoConfig.kNoSelect);
        testChooser.addOption(         "Red 1 Meter",  "Red1Meter");
        testChooser.addOption(         "Blue 1 Meter", "Blue1Meter");

        // Selector for Aliance Color
        cubeChooser.setDefaultOption( "Cube",          AutoConfig.kCubeSelect);
        cubeChooser.addOption(        "Cone",          AutoConfig.kConeSelect);
    }


    // ------ Get operator selected responses from shuffleboard -----
    public static void getAutoSelections() {
        scoreSelect =     scoreChooser.getSelected();
        positionSelect =  positionChooser.getSelected();
        crossSelect =     crossChooser.getSelected();
        dockSelect =      dockChooser.getSelected();
        levelSelect =     levelChooser.getSelected();
        // testSelect =      testChooser.getSelected();
        cubeSelect =      cubeChooser.getSelected();

        System.out.println("Score Select = " +     scoreSelect);
        System.out.println("Position Select = " +  positionSelect);
        System.out.println("Cross Select = " +     crossSelect);
        System.out.println("Dock Select = " +      dockSelect);
        System.out.println("Level Select = " +     levelSelect);
        System.out.println("Cube Select = " +       cubeSelect);
        // System.out.println("Test Select = " +      testSelect);
    }

    // ------------------------------------------------------------------------
    //            Get selected Autonomous Command Routine
    // ------------------------------------------------------------------------

    public static Command getAutonomousCommand() {
        getAutoSelections();
        setStartPose();

        // ---------------------- Test ------------------
        // if ( testSelect == "Red1Meter") {
        //     return TrajectoriesCmds.IntializeRobotAndFollowPathCmd(red1MeterPath, 5.0);
        // }
        // if ( testSelect == "Blue1Meter") {
        //     return TrajectoriesCmds.IntializeRobotAndFollowPathCmd(blue1MeterPath, 5.0);
        // }

        // ----------------------- Do Nothing -------------------
        if (doNothing()) {
            System.out.println("********* DO Nothing Selection *********");
            return AutoCmds.DoNothingCmd();
        }

        // ----------------------- Place Only -------------------
        if (placeOnly()) {
            System.out.println("********* Place Only Selection *********");
            if ( low() ) {
                return AutoCmds.PlaceOnlyCmd("Low");
            } 
            if ( mid() ) {
                return AutoCmds.PlaceOnlyCmd("Mid");
            }
            if ( high() ) {
                return AutoCmds.PlaceOnlyCmd("High");
            }
            return new PrintCommand("ERROR: Invalid place only auto command");
        }

        // ----------------------- Cross Line Only -------------------
        if (crossOnly()) {
            System.out.println("********* Cross Line Only Selection *********");
            if (redRight())     { return AutoCmds.CrossLineOnlyCmd(redRightCubeShortPath); }
            if (redLeft())      { return AutoCmds.CrossLineOnlyCmd(redLeftCubeLongPath);   }
            if (blueRight())    { return AutoCmds.CrossLineOnlyCmd(blueRightCubeLongPath); }
            if (blueLeft())     { return AutoCmds.CrossLineOnlyCmd(blueLeftCubeShortPath); }
            return new PrintCommand("ERROR: Invalid cross only auto command");
        }
    
        // ----------------------- Place and Cross Line  -------------------
        if ( place() && cross() && !dock() ) {
            System.out.println("********* Place and Cross Selection *********");
            if ( low() ){
                if ( redRight() )    { return AutoCmds.PlaceAndCrossCmd( "Low", redRightCubeShortPath); }
                if ( redLeft() )     { return AutoCmds.PlaceAndCrossCmd( "Low", redLeftCubeLongPath);   }
                if ( blueRight() )   { return AutoCmds.PlaceAndCrossCmd( "Low", blueRightCubeLongPath); }
                if ( blueLeft() )    { return AutoCmds.PlaceAndCrossCmd( "Low", blueLeftCubeShortPath); }
                return new PrintCommand("ERROR: Invalid place and cross auto command");
            }
            if ( mid() ){
                if ( redRight() )    { return AutoCmds.PlaceAndCrossCmd( "Mid", redRightCubeShortPath); }
                if ( redLeft() )     { return AutoCmds.PlaceAndCrossCmd( "Mid", redLeftCubeLongPath);   }
                if ( blueRight() )   { return AutoCmds.PlaceAndCrossCmd( "Mid", blueRightCubeLongPath); }
                if ( blueLeft() )    { return AutoCmds.PlaceAndCrossCmd( "Mid", blueLeftCubeShortPath); }
                return new PrintCommand("ERROR: Invalid place and cross auto command");
            }
            if ( high() ){
                if ( redRight() )    { return AutoCmds.PlaceAndCrossCmd( "High", redRightCubeShortPath); }
                if ( redLeft() )     { return AutoCmds.PlaceAndCrossCmd( "High", redLeftCubeLongPath);   }
                if ( blueRight() )   { return AutoCmds.PlaceAndCrossCmd( "High", blueRightCubeLongPath); }
                if ( blueLeft() )    { return AutoCmds.PlaceAndCrossCmd( "High", blueLeftCubeShortPath); }
                return new PrintCommand("ERROR: Invalid place and cross auto command");
            }
            return new PrintCommand("Error on auto place and cross only paramter");
        }

        // ----------------------- Get on Charging Station Only -------------------
        if (dockOnly()) {
            System.out.println("********* Dock only Selection *********");
            return new SequentialCommandGroup(
                // TrajectoriesCmds.IntializeRobotAndFollowPathCmd(CenterScalePath, 5.0),
                // TrajectoriesCmds.IntializeRobotAndFollowPathCmd(CenterScalePathA, 5.0),
                // TrajectoriesCmds.IntializeRobotAndFollowPathCmd(CenterScalePathB, 5.0)
                // AutoCmds.AutoBalanceCmd()
                AutoCmds.GetOnChargingTableCmd(CenterScalePath, autoLevel())
            );
        }

        // ----------------------- Score and Get on Charging Platform (Ctr Only)  -------------------
        if (place() && !cross() && dock()) {
            System.out.println("********* Score and get on Platform from CTR Only Selection *********");
            // if auto level
            if ( low() )    { return AutoCmds.PlaceAndChargingTableCmd( "Low",  CenterScalePath, autoLevel()); }
            if ( mid() )    { return AutoCmds.PlaceAndChargingTableCmd( "Mid",  CenterScalePath, autoLevel()); }
            if ( high() )   { return AutoCmds.PlaceAndChargingTableCmd( "High", CenterScalePath, autoLevel()); }
            return new PrintCommand("Error on auto place only paramter");
        }

        // ----------------------- No Score, Short Cross Line and Get on Charging Platform -------------------
        if (!place() && cross() && dock()) {
            // This is only setup for the Sort line BlueLeft and RedRight position
            // to avoid 4 mps over cable tray on long cross
            System.out.println("********* Score cross line and get on Platform Selection *********");
            if ( blueLeft())  {
                if ( cube() ) {
                    return AutoCmds.CrossShortAndScaleCmd( level,  BlueShortCrossScalePathA);
                } else {
                    return AutoCmds.CrossShortAndScaleCmd( level,  BlueConeShortCrossScalePathA);
                }
            }
            if ( redRight())  {
                if ( cube() ){
                    return AutoCmds.CrossShortAndScaleCmd( level,  RedShortCrossScalePathA);
                } else {
                    return AutoCmds.CrossShortAndScaleCmd( level,  RedConeShortCrossScalePathA);
                }
            }
            return new PrintCommand("Error on auto place cross short and dock paramter");
        }

        // ----------------------- Score, Short Cross Line and Get on Charging Platform -------------------
        if (place() && cross() && dock()) {
            // This is only setup for the Sort line BlueLeft and RedRight position
            // to avoid 4 mps over cable tray on long cross
            System.out.println("********* Score cross line and get on Platform Selection *********");
            if ( low() )      {level = "Low";}
            if ( mid() )      {level = "Mid";}
            if ( high() )     {level = "High";}
            if ( blueLeft())  {
                if ( cube() ) {
                    return AutoCmds.PlaceAndCrossShortAndScaleCmd( level,  BlueShortCrossScalePathA);
                } else {
                    return AutoCmds.PlaceAndCrossShortAndScaleCmd( level,  BlueConeShortCrossScalePathA);
                }
            }
            if ( redRight())  {
                if ( cube() ){
                    return AutoCmds.PlaceAndCrossShortAndScaleCmd( level,  RedShortCrossScalePathA);
                } else {
                    return AutoCmds.PlaceAndCrossShortAndScaleCmd( level,  RedConeShortCrossScalePathA);
                }
            }
            return new PrintCommand("Error on auto place cross short and dock paramter");
        }

        return new PrintCommand("Error on auto Commands Selection");
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

    private static boolean autoLevel() {
        if (levelSelect.equals(AutoConfig.kYesSelect)) { return true; }
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
        if (scoreSelect.equals(AutoConfig.kLowSelect)) { return true; }
        return false;
    }

    private static boolean mid() {
        if (scoreSelect.equals(AutoConfig.kMidSelect)) { return true; }
        return false;
    }

    private static boolean high() {
        if (scoreSelect.equals(AutoConfig.kHighSelect)) { return true; }
        return false;
    }

    private static boolean cube() {
        if (cubeSelect.equals(AutoConfig.kCubeSelect)) { return true; }
        return false;
    }
    private static boolean cone() {
        if (cubeSelect.equals(AutoConfig.kConeSelect)) { return true; }
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
        //Command autoCommand = Auto.getAutonomousCommand();
    //     if (autoCommand != null) {
    //         if (!autoCommand.isScheduled() && !autoMessagePrinted) {
    //             if (DriverStation.isAutonomousEnabled()) {
    //                 RobotTelemetry.print(
    //                         String.format(
    //                                 "*** Auton finished in %.2f secs ***",
    //                                 Timer.getFPGATimestamp() - autoStart));
    //             } else {
    //                 RobotTelemetry.print(
    //                         String.format(
    //                                 "*** Auton CANCELLED in %.2f secs ***",
    //                                 Timer.getFPGATimestamp() - autoStart));
    //             }
    //             autoMessagePrinted = true;
    //         }
    //     }
    }
}
