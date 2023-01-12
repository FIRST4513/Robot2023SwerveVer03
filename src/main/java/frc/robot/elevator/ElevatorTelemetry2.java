//Created by Spectrum3847
//Based on Code from FRC# 4141 
package frc.robot.elevator;

import edu.wpi.first.wpilibj.shuffleboard.*;

// The Shuffleboard Main tab.
public class ElevatorTelemetry2 {
    private ElevatorSubSys elevator;

    // ---------------------//
    // NetworkTableEntries //

    // ----------------//
    // Tab & Layouts //
    private static ShuffleboardTab m_tab;

    // ---------//
    // Widgets //

    // --------------//
    // Constructor //
    public ElevatorTelemetry2(ElevatorSubSys s) {
        m_tab = Shuffleboard.getTab("Elevator");
        elevator = s;
    }

    // ---------------------//
    // initialize //
    // Create all View Widgets, ones you can't edit, created after subsystem
    // instances are made
    public void initialize() {
        m_tab.addNumber("Elev-Motor Pwr", 
                        ()-> elevator.m_motor.get()).withPosition(0,0);
        m_tab.addNumber("Elev Encoder Raw", 
                        ()-> elevator.m_encoder.get()).withPosition(0,1);
        //m_tab.addNumber("Elev Encoder Height", 
        //                ()-> elevator.m_encoder.get()).withPosition(0,1);
    }

}
