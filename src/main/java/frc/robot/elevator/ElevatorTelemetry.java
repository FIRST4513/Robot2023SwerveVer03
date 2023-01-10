//Created by Spectrum3847
//Based on Code from FRC# 4141 
package frc.robot.elevator;

import edu.wpi.first.wpilibj.shuffleboard.*;

import frc.robot.Robot;
import frc.lib.telemetry.WidgetsAndLayouts;

// The Shuffleboard Main tab.
public class ElevatorTelemetry {
    // ---------------------//
    // NetworkTableEntries //

    // ----------------//
    // Tab & Layouts //
    private static ShuffleboardTab m_tab;

    // ---------//
    // Widgets //

    // --------------//
    // Constructor //
    public ElevatorTelemetry() {
        m_tab = Shuffleboard.getTab("Elevator");
    }

    // ---------------------//
    // initialize //
    // Create all View Widgets, ones you can't edit, created after subsystem
    // instances are made
    public void initialize() {
        m_tab.addNumber("Elev-Motor Pwr", 
                        ()-> Robot.elevator.m_motor.get()).withPosition(0,0);
        m_tab.addNumber("Elev Encoder Raw", 
                        ()-> Robot.elevator.m_encoder.get()).withPosition(0,1);
        //m_tab.addNumber("Elev Encoder Height", 
        //                ()-> Robot.elevator.m_encoder.get()).withPosition(0,1);
    }

    // --------//
    // Update //
    public void update() { // This will be called in the robotPeriodic
        //ClimberLeader.update();
    }
}
