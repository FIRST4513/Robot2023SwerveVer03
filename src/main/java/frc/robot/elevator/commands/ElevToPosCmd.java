package frc.robot.elevator.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.elevator.ElevatorConfig;

public class ElevToPosCmd extends CommandBase {

    private ElevatorConfig config;
    private double tgt_pos, curr_pos, diff, speed;
    private String m_relative;
    private double m_timeOut;
    enum Dir {UP, DN, DONE}
    private Dir dir;
    private Timer commandTmr = new Timer();

    public ElevToPosCmd( double pos, String relative, double time_Out)  {
        //addRequirements(Robot.elevator);
        setName("ElevToPosCmd");
        // relative "Elev"  = Height Relative to Elevator 0 to Elev Ht
        //          "Floor" = Height Relative to the floor
        tgt_pos = pos;
        m_relative = relative;
        m_timeOut = time_Out;
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        config = new ElevatorConfig();              // Get all Constants for this subsystem
        addRequirements(Robot.elevator);
        commandTmr.reset();		// We are just starting out so start timer
    	commandTmr.start();
        System.out.println("");
        System.out.println("********* Starting ElevToPosCmd Target Pos = " + tgt_pos);
        curr_pos = Robot.elevator.getElevHeightInches();
        tgt_pos = Robot.elevator.limit_target_ht(tgt_pos);

        diff =  curr_pos - tgt_pos;
        dir = Dir.DONE;
        if ( diff > 0 )     { dir = Dir.DN; }
        if ( diff < 0 )     { dir = Dir.UP; }
        if ( Math.abs(diff) < config.KheightDeadBand ) {dir = Dir.DONE; }
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        curr_pos = Robot.elevator.getElevHeightInches(); 
        diff =  curr_pos - tgt_pos;
        if (dir == Dir.DN) {
            // Were Lowering
            if ( diff < 1.5) {
                speed = config.KLowerSlowSpeed;     // Were close so slow down
            } else {
                speed = config.KLowerSpeedDefault;
            }
        } else if (dir == Dir.UP) {
            // Were Raising
            if ( diff > -1.5) {
                speed = config.KRaiseSlowSpeed;     // Were close so slow down
            } else {
                speed = config.KRaiseSpeedDefault;
            }
        } else {
            speed = config.KHoldSpeedDefault;       // Were Holding
        }  
        Robot.elevator.elevSetSpeed( speed );
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        System.out.println("******** Ending ElevToPosCmd Pos");
        Robot.elevator.elevHoldMtr();
    }

    @Override
    public boolean isFinished() {
        if (commandTmr.get() > m_timeOut) {
            // (Were Done) Cmd has timed out
            System.out.println("******** ElevToPosCmd has timed out *********");
            return true;
        }
        
        // If going to bottom lets check if were there yet
        if (tgt_pos == 0.0) {
            if (Robot.elevator.isLowerLimitSwitchPressed()) {
                return true;
            } else {
                return false;
            }
        }

        // Check if were at our target (within a deadband zone anyway)
        if (Math.abs(diff) <= config.KheightDeadBand) {
            return true;    // Were there .... all done !
        }
        return false;
    }

    @Override
    public boolean runsWhenDisabled() {
        return false;
    }
}
