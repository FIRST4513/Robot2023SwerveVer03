//Created by Spectrum3847
//Based on Code from FRC# 4141 
package frc.robot.elevator;

import java.util.Map;

import edu.wpi.first.wpilibj.shuffleboard.*;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.elevator.commands.ElevToPosCmd;

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
    private static ShuffleboardLayout elevPIDLayout;
    private static ShuffleboardLayout elevStatusLayout;
    private static ShuffleboardLayout elevCmdsLayout;
    private static ShuffleboardLayout elevPIDCmdsLayout;

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
        elevPIDLayout()         .withPosition(4, 0)  ;
        elevCmdsLayout()        .withPosition(6, 0)  ;
        elevPIDCmdsLayout()     .withPosition(9, 0)  ;
        tab.add("Elev Commands",elevator).withPosition(0, 5).withSize(5, 2);
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

        // Elev Position Widget
        m_posWidget = elevEncoderLayout.addNumber("Pos", ()-> elevator.getElevPosInches())
            .withPosition(0,1).withSize(2, 1);

        // Elev Height Widget
        m_heightWidget = elevEncoderLayout.addNumber("Height", ()-> elevator.getElevHeightInches())
            .withPosition(0,2).withSize(2, 1);

        return elevEncoderLayout;
    }

    // ------------ PID Data Layout ---------
    public ShuffleboardLayout elevPIDLayout() {
        elevPIDLayout = tab.getLayout("PID", BuiltInLayouts.kGrid);
        elevPIDLayout.withProperties(Map.of("Label position", "TOP"));
        elevPIDLayout.withSize(2, 4);

        // Target Pos Widget
        m_PIDtargetPosWidget = elevPIDLayout.addNumber("Tgt Pos.",()-> elevator.getTargetPos())
            .withPosition(0,0).withSize(2, 1);
            
        // Target Height Widget
        m_PIDtargetHtWidget = elevPIDLayout.addNumber("Tgt Ht.",()-> elevator.getTargetHeight())
            .withPosition(0,1).withSize(2, 1);        

        // PID Calc Output Widget
        m_PIDoutWidget = elevPIDLayout.addNumber("PID 14.0 Test",()-> elevator.getPidCalcTestOut( 14.0 ))
            .withPosition(0,2).withSize(2, 1);
                
        return elevPIDLayout;
    }

    // --------------- Elev Commands Layout -------------
    public ShuffleboardLayout elevPIDCmdsLayout(){
        elevPIDCmdsLayout = tab.getLayout("Elev PID Cmds", BuiltInLayouts.kList);
        elevPIDCmdsLayout.withProperties(Map.of("Label position", "TOP"));
        elevPIDCmdsLayout.withSize(3, 5);

        elevPIDCmdsLayout.add("PID To 14.0", 
            new RunCommand( () -> elevator.setPIDposition( 14.0 ), elevator))
            .withPosition(0, 0)    .withSize(2, 1);
        elevPIDCmdsLayout.add("PID To 0.0", 
            new RunCommand( () -> elevator.setPIDposition( 0.0 ), elevator))
            .withPosition(0, 1)    .withSize(2, 1);
        elevPIDCmdsLayout.add("PID To 35.0", 
            new RunCommand( () -> elevator.setPIDposition( 35.0 ), elevator))
            .withPosition(0, 2)    .withSize(2, 1);

        return elevPIDCmdsLayout;            
    }
    // --------------- Elev Commands Layout -------------
    public ShuffleboardLayout elevCmdsLayout(){
        elevCmdsLayout = tab.getLayout("Elev Cmds", BuiltInLayouts.kList);
        elevCmdsLayout.withProperties(Map.of("Label position", "TOP"));
        elevCmdsLayout.withSize(3, 6);

        elevCmdsLayout.add("TO Pos 5.0", new ElevToPosCmd( 5.0,    "Elev", 5.0))
            .withPosition(0, 0)    .withSize(2, 1);
        elevCmdsLayout.add("TO Pos 10.0", new ElevToPosCmd( 10.0 , "Elev", 5.0))
            .withPosition(0, 1)    .withSize(2, 1);
        elevCmdsLayout.add("TO Pos 99.0", new ElevToPosCmd( 99.0 , "Elev", 5.0))
            .withPosition(0, 2)    .withSize(2, 1);
        elevCmdsLayout.add("TO Pos 0.0", new ElevToPosCmd( 0.0 ,  "Elev", 5.0)) 
            .withPosition(0, 3)    .withSize(2, 1);

        elevCmdsLayout.add("TO Bottom", new RunCommand( () -> elevator.elevLower(), elevator)
            .until(() ->elevator.isLowerLimitSwitchPressed()))
            .withPosition(0, 4)    .withSize(2, 1);

        return elevCmdsLayout;
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
