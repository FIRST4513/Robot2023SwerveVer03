package frc.robot;

import com.pathplanner.lib.server.PathPlannerServer;

import edu.wpi.first.wpilibj.Threads;
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
import frc.robot.arm.ArmSubSys;
import frc.robot.arm.commands.ArmCmds;
import frc.robot.auto.AutoSetup;
import frc.robot.elevator.ElevatorSubSys;
import frc.robot.elevator.commands.ElevatorCmds;
import frc.robot.intake.IntakeSubSys;
import frc.robot.intake.commands.IntakeCmds;
import frc.robot.logger.Logger;
import frc.robot.pilot.PilotGamepad;
import frc.robot.pilot.commands.PilotGamepadCmds;
import frc.robot.pose.Pose;
import frc.robot.swerveDrive.SwerveDrive;
import frc.robot.swerveDrive.commands.SwerveDriveCmds;
import frc.robot.trajectories.Trajectories;

public class Robot extends TimedRobot {
    public static RobotConfig config;
    public static Logger logger;
    public static SwerveDrive swerve;
    public static Pose pose;
    public static Trajectories trajectories;
    public static PilotGamepad pilotGamepad;
    public static ElevatorSubSys elevator;
    public static ArmSubSys arm;
    public static IntakeSubSys intake;
    public static RobotTelemetry telemetry;

    public static String MAC = "";
    public static Timer sysTimer = new Timer();

    // Intialize subsystems and run their setupDefaultCommand methods here
    private void intializeSubsystems() {
        logger = new Logger(); 
        logger.startTimer();
        swerve = new SwerveDrive();
        pose = new Pose();
        trajectories = new Trajectories();
        elevator = new ElevatorSubSys();
        pilotGamepad = new PilotGamepad();
        arm = new ArmSubSys();
        intake = new IntakeSubSys();
        telemetry = new RobotTelemetry();

        // Set Default Commands, this method should exist for each subsystem that has commands
        SwerveDriveCmds.setupDefaultCommand();
        ElevatorCmds.setupDefaultCommand();
        PilotGamepadCmds.setupDefaultCommand();
        ArmCmds.setupDefaultCommand();
        IntakeCmds.setupDefaultCommand();
        logger.startTimer();

    }
    /**
     * This function is run when the robot is first started up and should be used for any
     * initialization code.
     */
    @Override
    public void robotInit() {
        sysTimer.reset();			// System timer for Competition run
    	sysTimer.start();
        Timer.delay( 2.0 );     // Delay for 2 seconds for robot to come fully up
        // Set the MAC Address for this robot, useful for adjusting comp/practice bot settings*/
        MAC = Network.getMACaddress();
        //PathPlannerServer.startServer( 5811 ); // 5811 = port number. adjust this according to your needs
        intializeSubsystems();
    }

    @Override
    public void robotPeriodic() {
        // Ensures that the main thread is the highest priority thread
        Threads.setCurrentThreadPriority(true, 99);
        /**
         * Runs the Scheduler. This is responsible for polling buttons, adding newly-scheduled
         * commands, running already-scheduled commands, removing finished or interrupted commands,
         * and running subsystem periodic() methods. This must be called from the robot's periodic
         * block in order for anything in the Command-based framework to work.
         */
        CommandScheduler.getInstance().run();

        Threads.setCurrentThreadPriority(true, 10); // Set the main thread back to normal priority
    }

    /** This function is called once each time the robot enters Disabled mode. */
    @Override
    public void disabledInit() {
        resetCommandsAndButtons();
        //logger.saveLogFile();           // look for a way to verify we have a valid file
    }

    @Override
    public void disabledPeriodic() {
        
    }

    @Override
    public void disabledExit() {
        //
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
        //LiveWindow.setEnabled(false); // Disable Live Window we don't need that data being sent
        //LiveWindow.disableAllTelemetry();

        // Reset Config for all gamepads and other button bindings
        pilotGamepad.resetConfig();
    }

}
