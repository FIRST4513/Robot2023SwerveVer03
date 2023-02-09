package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lib.telemetry.Alert;
import frc.lib.telemetry.Alert.AlertType;
import frc.lib.telemetry.TelemetrySubsystem;
import frc.lib.util.Network;
import frc.lib.util.Util;
import frc.robot.arm.ArmTelemetry;
import frc.robot.auto.AutoSetup;
import frc.robot.elevator.ElevatorTelemetry;
import frc.robot.intake.IntakeTelemetry;
import frc.robot.operator.OperatorGamepadTelemetry;
import frc.robot.pilot.PilotGamepadTelemetry;
import frc.robot.swerveDrive.SwerveDriveTelemetry;

import java.util.Map;

public class RobotTelemetry extends TelemetrySubsystem {
    private static boolean disablePrints = false;

    // Additional Shuffleboard Tabs
    private static ArmTelemetry             m_ArmTelemetry;
    private static ElevatorTelemetry        m_ElevTelemetry;
    private static IntakeTelemetry          m_IntakeTelemetry;
    private static SwerveDriveTelemetry     m_SwerveDriveTelemetry;
    private static PilotGamepadTelemetry    m_PilotTelemetry;
    private static OperatorGamepadTelemetry m_OperatorTelemetry;

    // Alerts
    private static Alert batteryAlert = new Alert("Low Battery < 12v", AlertType.WARNING);
    private static Alert FMSConnectedAlert = new Alert("FMS Connected", AlertType.INFO);

    private String IPaddress = "UNKOWN";

    // ------------- Constructor --------------
    public RobotTelemetry() {
        // Call Super constuctor first - 
        super("Robot");     // Set tab name to Robot

        layoutRobotTelemtryTab();

        // Setup the rest of the Shuffleboard Tabs
        m_ArmTelemetry =            new ArmTelemetry(Robot.arm);
        m_ElevTelemetry =           new ElevatorTelemetry(Robot.elevator); ;
        m_IntakeTelemetry =         new IntakeTelemetry(Robot.intake); ;
        m_SwerveDriveTelemetry =    new SwerveDriveTelemetry(Robot.swerve); ;
        m_PilotTelemetry =          new PilotGamepadTelemetry(Robot.pilotGamepad); ;
        m_OperatorTelemetry =       new OperatorGamepadTelemetry(Robot.operatorGamepad); ;
    }

    @Override
    public void periodic() {
        checkFMSalert();
        checkBatteryWhenDisabledalert();
    }

    // public static void createTab(String name) {
    //     Shuffleboard.getTab(name);
    // }

    public void layoutRobotTelemtryTab(){
        // Column 0 - Setup the autonomous selector to display on shuffleboard
        AutoSetup.setupSelectors();
        tab.add("Auton Selection", AutoSetup.autoChooser).withPosition(0, 0).withSize(2, 1);
        tab.add("Position Selection", AutoSetup.positionChooser).withPosition(0, 1).withSize(2, 1);
        tab.add("Object Selection", AutoSetup.objectChooser).withPosition(0, 2).withSize(2, 1);
        tab.add("Height Selection", AutoSetup.heightChooser).withPosition(0, 3).withSize(2, 1);

        // Column 2
        tab.addBoolean("Connected?", () -> flash())
                .withPosition(2, 0)
                .withSize(1, 1)
                .withProperties(
                        Map.of("Color when true", "#300068", "Color when false", "#FFFFFF"));

        tab.addNumber("Match Time", () -> Timer.getMatchTime())
                .withPosition(2, 1)
                .withSize(2, 2)
                .withWidget("Simple Dial")
                .withProperties(Map.of("Min", 0, "Max", 135));

        // Column 4
        tab.add("Alerts", SmartDashboard.getData("Alerts")).withPosition(4, 0).withSize(2, 2);
        tab.add("MAC Address", Robot.MAC).withPosition(4, 2).withSize(2, 1);
        tab.addString("IP Address", () -> getIP()).withPosition(4, 3).withSize(2, 1);
        tab.addNumber("ElevPos", () -> Robot.elevator.getElevPosInches()).withPosition(6, 0).withSize(2, 1);
    }

    public String getIP() {
        if (IPaddress == "UNKOWN") {
            IPaddress = Network.getIPaddress();
        }
        return IPaddress;
    }

    public void checkFMSalert() {
        FMSConnectedAlert.set(DriverStation.isFMSAttached());
    }

    public void checkBatteryWhenDisabledalert() {
        batteryAlert.set(DriverStation.isDisabled() && Util.checkBattery(12.0));
    }

    public boolean flash() {
        return (int) Timer.getFPGATimestamp() % 2 == 0;
    }

    public static void print(String output) {
        if (!disablePrints) {
            System.out.println(output);
        }
    }

}
