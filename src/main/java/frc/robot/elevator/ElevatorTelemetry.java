// Creaqted by Circuit Breakers Team 4513
// Based on code from Spectrum3847 and FRC# 4141 
package frc.robot.elevator;

import java.util.Map;

import edu.wpi.first.wpilibj.shuffleboard.*;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.Robot;
import frc.robot.elevator.commands.ElevatorCmds;

// --------------------------------------------------------
// ------------   Elevator Telemetry Class   --------------
// --------------------------------------------------------

// The Shuffleboard Elevator tab.

public class ElevatorTelemetry {
    // Declare needed class objects
    private ElevatorSubSys elevator;
    
    // --- NetworkTableEntries ---

    // --- Tab & Layouts ---
    private static ShuffleboardTab    tab;
    private static ShuffleboardLayout elevEncoderLayout;
    private static ShuffleboardLayout elevMotorLayout;
    private static ShuffleboardLayout elevStatusLayout;

    // --- Widgets ---
    SuppliedValueWidget<Double>  m_motorPwrWidget;

    SuppliedValueWidget<Double>  m_encoderCntWidget;
    SuppliedValueWidget<Double>  m_heightWidget;
    SuppliedValueWidget<Double>  m_posWidget;

    SuppliedValueWidget<Double>  m_PIDtargetTestPosWidget;
    SuppliedValueWidget<Double>  m_PIDtargetPosWidget;
    SuppliedValueWidget<Double>  m_PIDtargetHtWidget;
    SuppliedValueWidget<Double>  m_PIDoutWidget;

    SuppliedValueWidget<String>  m_lowerLimitSwWidget;
    SuppliedValueWidget<String>  m_upperLimitSwWidget;
    SuppliedValueWidget<String>  m_ModeWidget;


    // ---------------------------------------------------------------
    // ------------   Elevator Telemetry Constructor    --------------
    // ---------------------------------------------------------------

    public ElevatorTelemetry(ElevatorSubSys el) {
        // Inialize needed class objects
        elevator = el;
        tab = Shuffleboard.getTab("Elevator");
        initialize();
    }

    // -------------------------------------------------------------
    // --------------   Elevator Telemetry Methods    --------------
    // -------------------------------------------------------------

    public void initialize() {
        // Create all View Widgets, ones you can't edit, created after subsystem instances are made
        elevMotorLayout()       .withPosition(0, 0)  ;
        elevStatusLayout()      .withPosition(0, 2)  ;
        elevEncoderLayout()     .withPosition(2, 0)  ;
        tab.add("Elev Commands",elevator).withPosition(0, 5).withSize(5, 2);
        tab.addNumber("Input from Operator Joystick", () -> Robot.operatorGamepad.getElevInput());

        tab.add("MM To 5.0", 
            new RunCommand( () -> Robot.elevator.setMMheight( 5.0 ), Robot.elevator))
            .withSize(3, 2)    .withPosition(14, 1);

        tab.add("MM To 10.0", 
            new RunCommand( () -> Robot.elevator.setMMheight( 10.0 ), Robot.elevator))
            .withSize(3, 2)    .withPosition(14, 2);

        tab.add("MM To 15.0", 
            new RunCommand( () -> Robot.elevator.setMMheight( 15.0 ), Robot.elevator))
            .withSize(3, 2)    .withPosition(14, 3);
            
        tab.add("MM To 20.0", 
            new RunCommand( () -> Robot.elevator.setMMheight( 20.0 ), Robot.elevator))
            .withSize(3, 2)    .withPosition(14, 4);

        tab.add("Goto Bottom", 
            new RunCommand( () -> Robot.elevator.elevLower(), Robot.elevator)
                                .until(() ->Robot.elevator.isLowerLimitSwitchPressed()))
            .withSize(3, 2)    .withPosition(14, 5);

        tab.addBoolean("MM Tgt Reached", () ->Robot.elevator.isMMtargetReached())
            .withSize(2, 2)    .withPosition(14, 7);

        tab.add("Brake On",
            new InstantCommand(() -> Robot.elevator.setBrakeMode(true)))
            .withSize(3, 2).withPosition(16, 0);

        tab.add("Brake Off",
            new InstantCommand(() -> Robot.elevator.setBrakeMode(false)))
            .withSize(3, 2).withPosition(16, 0);

    }

    // ------------ Elev Motor Data Layout ---------
    public ShuffleboardLayout elevMotorLayout() {
        elevMotorLayout = tab.getLayout("Motor", BuiltInLayouts.kGrid);
        elevMotorLayout.withProperties(Map.of("Label position", "TOP"));
        elevMotorLayout.withSize(2, 2);

        // Motor Power Widget
        m_motorPwrWidget = elevMotorLayout.addNumber("Power",()-> elevator.m_motor.get())
            .withPosition(0,0).withSize(2, 1);

        return elevMotorLayout;
    }

    // ------------ Elev Encoder Data Layout ---------
    public ShuffleboardLayout elevEncoderLayout() {
        elevEncoderLayout = tab.getLayout("Encoder", BuiltInLayouts.kGrid);
        elevEncoderLayout.withProperties(Map.of("Label position", "TOP"));
        elevEncoderLayout.withSize(2, 4);

        // Encoder Cnt Widget
        m_encoderCntWidget = elevEncoderLayout.addNumber("Cnt", ()-> elevator.getElevEncoderCnt())
            .withPosition(0,0).withSize(2, 1);

        // Elev Height Widget
        m_heightWidget = elevEncoderLayout.addNumber("Height", ()-> elevator.getElevHeightInches())
            .withPosition(0,2).withSize(2, 1);

        return elevEncoderLayout;
    }


    // ------------ Elevator Status Layout ---------
    public ShuffleboardLayout elevStatusLayout() {
        elevStatusLayout = tab.getLayout("Status", BuiltInLayouts.kGrid);
        elevStatusLayout.withProperties(Map.of("Label position", "TOP"));
        elevStatusLayout.withSize(2, 2);

        // Lower Limit Switch Widget
        m_lowerLimitSwWidget = elevStatusLayout.addString("Limit Lower",()-> elevator.getLowerLimitSwStatus())
            .withPosition(0,0).withSize(2, 1);
        // Upper Limit Switch Widget
        m_upperLimitSwWidget = elevStatusLayout.addString("Limit Upper",()-> elevator.getUpperLimitSwStatus())
            .withPosition(0,2).withSize(2, 1);
            
        return elevStatusLayout;
    }
        
}
