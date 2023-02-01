package frc.robot;

//import edu.wpi.first.wpilibj.DataLogManager;
//import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.lib.util.Network;
import frc.robot.auto.AutoSetup;
import frc.robot.elevator.ElevatorSubSys;
import frc.robot.elevator.commands.ElevatorCmds;
import frc.robot.logger.Logger;
import frc.robot.pilot.PilotGamepad;
import frc.robot.pilot.commands.PilotGamepadCmds;
import frc.robot.swerveDrive.SwerveDrive;
import frc.robot.swerveDrive.commands.SwerveDriveCmds;
import frc.robot.trajectories.Trajectories;

public class Robot extends TimedRobot {
    public static RobotConfig config;
    //public static RobotTelemetry telemetry;
    public static Logger logger;
    public static SwerveDrive swerve;
    public static Trajectories trajectories;
    public static PilotGamepad pilotGamepad;
    public static ElevatorSubSys elevator;

    public static Timer sysTimer = new Timer();
    public static String MAC = "";

    /**
     * This function is run when the robot is first started up and should be used for any
     * initialization code.
     */
    @Override
    public void robotInit() {
        sysTimer.reset();			// System timer for Competition run
    	sysTimer.start();

        // Set the MAC Address for this robot, useful for adjusting comp/practice bot settings*/
        MAC = Network.getMACaddress();
        Shuffleboard.getTab("Robot"); // Makes the Robot tab the first tab on the Shuffleboard
        intializeSubsystems();
    }

    // Intialize subsystems and run their setupDefaultCommand methods here
    private void intializeSubsystems() {
        logger = new Logger(); 
        logger.startTimer();
        swerve = new SwerveDrive();
        trajectories = new Trajectories();
        elevator = new ElevatorSubSys();
        pilotGamepad = new PilotGamepad();
        //telemetry = new RobotTelemetry();


        // Set Default Commands, this method should exist for each subsystem that has commands
        SwerveDriveCmds.setupDefaultCommand();
        ElevatorCmds.setupDefaultCommand();
        PilotGamepadCmds.setupDefaultCommand();
        logger.startTimer();

    }

    /**
     * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
     * that you want ran during disabled, autonomous, teleoperated and test.
     *
     * <p>This runs after the mode specific periodic functions, but before LiveWindow and
     * SmartDashboard integrated updating.
     */
    @Override
    public void robotPeriodic() {
        // Runs the Scheduler. This is responsible for polling buttons, adding
        // newly-scheduled
        // commands, running already-scheduled commands, removing finished or
        // interrupted commands,
        // and running subsystem periodic() methods. This must be called from the
        // robot's periodic
        // block in order for anything in the Command-based framework to work.
        CommandScheduler.getInstance().run();
    }

    /** This function is called once each time the robot enters Disabled mode. */
    @Override
    public void disabledInit() {
        resetCommandsAndButtons();
        //logger.saveLogFile();           // look for a way to verify we have a valid file
    }

    @Override
    public void disabledPeriodic() {
        CommandScheduler.getInstance().enable();
    }

    @Override
    public void autonomousInit() {
        resetCommandsAndButtons();
        sysTimer.reset();			// System timer for Competition run
    	sysTimer.start();
        //logger.startTimer();

        Command autonCommand = AutoSetup.getAutonomousCommand();
        if (autonCommand != null) {
            autonCommand.schedule();
        }
    }

    /** This function is called periodically during autonomous. */
    @Override
    public void autonomousPeriodic() {}

    @Override
    public void autonomousExit() {}

    @Override
    public void teleopInit() {
        // System.out.print("----------\nTeleop Init!\n");
        resetCommandsAndButtons();
        logger.startTimer();
        // Set Pilot Teleop Speeds to those selected on smartdashboard
        pilotGamepad.setMaxSpeeds(pilotGamepad.getSelectedSpeed());
        swerve.resetFalconAngles(); // reset falcon angle motors to absolute encoder
    }

    /** This function is called periodically during operator control. */
    @Override
    public void teleopPeriodic() {}

    @Override
    public void teleopExit() {}

    @Override
    public void testInit() {
        resetCommandsAndButtons();
        //logger.startTimer();
        swerve.resetFalconAngles(); // reset falcon angle motors to absolute encoder
    }

    /** This function is called periodically during test mode. */
    @Override
    public void testPeriodic() {}

    /** This function is called once when a simulation starts */
    public void simulationInit() {}

    /** This function is called periodically during a simulation */
    public void simulationPeriodic() {
        // PhysicsSim.getInstance().run();
    }

    public static void resetCommandsAndButtons() {
        CommandScheduler.getInstance().cancelAll(); // Disable any currently running commands
        CommandScheduler.getInstance().getActiveButtonLoop().clear();
        LiveWindow.setEnabled(false); // Disable Live Window we don't need that data being sent
        LiveWindow.disableAllTelemetry();

        // Reset Config for all gamepads and other button bindings
        pilotGamepad.resetConfig();
    }

}
