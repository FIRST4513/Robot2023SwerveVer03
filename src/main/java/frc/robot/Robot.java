package frc.robot;

import edu.wpi.first.wpilibj.Threads;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.lib.util.Network;
import frc.robot.arm.ArmSubSys;
import frc.robot.arm.commands.ArmCmds;
import frc.robot.auto.Auto;
import frc.robot.elevator.ElevatorSubSys;
import frc.robot.elevator.commands.ElevatorCmds;
import frc.robot.intake.IntakeSubSys;
import frc.robot.intake.commands.IntakeCmds;
import frc.robot.logger.Logger;
import frc.robot.operator.OperatorGamepad;
import frc.robot.operator.commands.OperatorGamepadCmds;
import frc.robot.pilot.PilotGamepad;
import frc.robot.pilot.commands.PilotGamepadCmds;
import frc.robot.pose.Pose;
import frc.robot.swerve.Swerve;
import frc.robot.swerve.commands.SwerveCmds;
import frc.robot.trajectories.Trajectories;
import com.pathplanner.lib.server.PathPlannerServer;

// ------------------- Constructor -----------------
public class Robot extends TimedRobot {
    public static RobotConfig config;
    public static Logger logger;
    public static Swerve swerve;
    public static Pose pose;
    public static Trajectories trajectories;
    public static PilotGamepad pilotGamepad;
    public static OperatorGamepad operatorGamepad;
    public static ElevatorSubSys elevator;
    public static ArmSubSys arm;
    public static IntakeSubSys intake;
    public static RobotTelemetry telemetry;
    public static Auto autoSetup;

    public static String MAC = "";
    public static Timer sysTimer = new Timer();


    // -----------------  Robot General Methods ------------------
    @Override
    public void robotInit() {
        sysTimer.reset();			// System timer for Competition run
    	sysTimer.start();
        Timer.delay( 2.0 );         // Delay for 2 seconds for robot to come fully up
        MAC = Network.getMACaddress();
        PathPlannerServer.startServer( 5811 ); // 5811 = port number. adjust this according to your needs
        intializeSubsystems();
    }

    @Override
    public void robotPeriodic() {
        // Ensures that the main thread is the highest priority thread
        Threads.setCurrentThreadPriority(true, 99);
        CommandScheduler.getInstance().run();       // Make sure scheduled commands get run
        Auto.printAutoDuration();                   // displays the duration of the auto command
        Threads.setCurrentThreadPriority(true, 10); // Set the main thread back to normal priority
    }

    // Intialize subsystems and run their setupDefaultCommand methods here
    private void intializeSubsystems() {
        logger = new Logger(); 
        logger.startTimer();
        swerve = new Swerve();
        pose = new Pose();
        trajectories = new Trajectories();
        elevator = new ElevatorSubSys();
        pilotGamepad = new PilotGamepad();
        operatorGamepad = new OperatorGamepad();
        arm = new ArmSubSys();
        intake = new IntakeSubSys();
        telemetry = new RobotTelemetry();
        autoSetup = new Auto();

        // Set Default Commands, this method should exist for each subsystem that has commands
        SwerveCmds.setupDefaultCommand();
        ElevatorCmds.setupDefaultCommand();
        PilotGamepadCmds.setupDefaultCommand();
        OperatorGamepadCmds.setupDefaultCommand();
        ArmCmds.setupDefaultCommand();
        IntakeCmds.setupDefaultCommand();
        logger.startTimer();
    }

    
    // -----------------  Robot Disabled Mode Methods ------------------
    @Override
    public void disabledInit() {
        resetCommandsAndButtons();
        //logger.saveLogFile();           // look for a way to verify we have a valid file
    }

    @Override
    public void disabledPeriodic()  { }

    @Override
    public void disabledExit()  { }


    // -----------------  Autonomous Mode Methods ------------------
    @Override
    public void autonomousInit() {
        resetCommandsAndButtons();
        sysTimer.reset();			// System timer for Competition run
    	sysTimer.start();
        //logger.startTimer();

        Command autonCommand = Auto.getAutonomousCommand();
        if (autonCommand != null) {
            autonCommand.schedule();
        }
    }

    @Override
    public void autonomousPeriodic() {}

    @Override
    public void autonomousExit() {}


    // -----------------  TeleOp Mode Methods ------------------
    @Override
    public void teleopInit() {
        resetCommandsAndButtons();
        logger.startTimer();
        // Set Pilot Teleop Speeds to those selected on smartdashboard
        pilotGamepad.setMaxSpeeds(pilotGamepad.getSelectedSpeed());
        swerve.resetFalconAngles(); // Set falcon angle motors to absolute encoder
    }

    @Override
    public void teleopPeriodic() {}

    @Override
    public void teleopExit() {}

    // -----------------  Test Mode Methods ------------------
    @Override
    public void testInit() {
        resetCommandsAndButtons();
    }

    @Override
    public void testPeriodic() {}


    // -----------------  Simulation Mode Methods ------------------
    public void simulationInit() {}

    public void simulationPeriodic() {
        // PhysicsSim.getInstance().run();
    }


    // ------------------------  Misc Methods ---------------------
    public static void resetCommandsAndButtons() {
        CommandScheduler.getInstance().cancelAll(); // Disable any currently running commands
        CommandScheduler.getInstance().getActiveButtonLoop().clear();
        pilotGamepad.resetConfig();     // Reset Config for all gamepads and other button bindings
        //LiveWindow.setEnabled(false); // Disable Live Window we don't need that data being sent
        //LiveWindow.disableAllTelemetry();
    }

}
