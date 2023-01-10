package frc.robot;

import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.lib.util.Network;
import frc.robot.auto.AutoSetup;
import frc.robot.elevator.ElevatorSubSys;
import frc.robot.elevator.commands.ElevatorCmds;
import frc.robot.grabber.GrabberCmds;
import frc.robot.grabber.GrabberSubSys;
import frc.robot.pilotGamepad.PilotGamepad;
import frc.robot.pilotGamepad.commands.PilotGamepadCmds;
import frc.robot.slider.SliderCmds;
import frc.robot.slider.SliderSubSys;
import frc.robot.swerve.SwerveSubSys;
import frc.robot.trajectories.Trajectories;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
    public static RobotConfig config;
    public static RobotTelemetry telemetry;
    public static SwerveSubSys swerve;
    public static Trajectories trajectories;
    public static PilotGamepad pilotGamepad;
    public static ElevatorSubSys elevator;
    public static GrabberSubSys grabber;
    public static SliderSubSys slider;

    public static Timer sysTimer = new Timer();
    public static String MAC = "";

    /**
     * This function is run when the robot is first started up and should be used for any
     * initialization code.
     */
    @Override
    public void robotInit() {
        // Start the DataLogger and log DS values
        DataLogManager.start();
        DriverStation.startDataLog(DataLogManager.getLog());

        sysTimer.reset();			// System timer for Competition run
    	sysTimer.start(); 

        // Set the MAC Address for this robot, useful for adjusting comp/practice bot settings*/
        MAC = Network.getMACaddress();
        Shuffleboard.getTab("Robot"); // Makes the Robot tab the first tab on the Shuffleboard
        intializeSubsystems();
    }

    // Intialize subsystems and run their setupDefaultCommand methods here
    private void intializeSubsystems() {
        // drivetrain = new DrivetrainSubSys();
        swerve = new SwerveSubSys();
        trajectories = new Trajectories();
        elevator = new ElevatorSubSys();
        pilotGamepad = new PilotGamepad();
        telemetry = new RobotTelemetry();
        slider = new SliderSubSys();

        // Set Default Commands, this method should exist for each subsystem that has commands
        //SwerveCmds.setupDefaultCommand();
        ElevatorCmds.setupDefaultCommand();
        PilotGamepadCmds.setupDefaultCommand();
        GrabberCmds.setupDefaultCommand();
        SliderCmds.setupDefaultCommand();
        //TrajectoriesCmds.setupDefaultCommand();
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
        resetCommandsAndButtons();
    }

    /** This function is called periodically during operator control. */
    @Override
    public void teleopPeriodic() {}

    @Override
    public void teleopExit() {}

    @Override
    public void testInit() {
        resetCommandsAndButtons();
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
