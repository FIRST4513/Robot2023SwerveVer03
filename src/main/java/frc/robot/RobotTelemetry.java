package frc.robot;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.ComplexWidget;
import edu.wpi.first.wpilibj.shuffleboard.SimpleWidget;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.lib.telemetry.Alert;
import frc.lib.telemetry.Alert.AlertType;
import frc.lib.telemetry.TelemetrySubsystem;
import frc.lib.util.Network;
import frc.lib.util.Util;
import frc.robot.arm.ArmTelemetry;
import frc.robot.auto.AutoSetup;
import frc.robot.elevator.ElevatorTelemetry;
import frc.robot.intake.IntakeTelemetry;
import frc.robot.pilot.PilotGamepadTelemetry;
import frc.robot.swerveDrive.SwerveDriveTelemetry;

import java.util.Map;

public class RobotTelemetry extends TelemetrySubsystem {
    
    private static boolean disablePrints = false;

    // Shuffleboard Tabs
    // private static ElevatorTelemetry m_ElevTelemetry;
    // private static SwerveDriveTelemetry m_SwerveDriveTelemetry;
    // private static IntakeTelemetry m_IntakeTelemetry;
    // private static ArmTelemetry m_ArmTelemetry;
    // private static PilotGamepadTelemetry m_PilotTelemetry;

    // NetworkTableEntries
    public static NetworkTableEntry flashEntry;

    // Widgets
    public static SimpleWidget m_flashWidget;
    public static SimpleWidget m_limelightLEDenable;
    public static SimpleWidget m_enableTabsWidget;
    public static ComplexWidget m_autonSelectorWidget;
    public static ComplexWidget m_autonPositionWidget;
    public static ComplexWidget m_autonColorWidget;

    // Alerts
    private static Alert batteryAlert = new Alert("Low Battery < 12v", AlertType.WARNING);
    private static Alert FMSConnectedAlert = new Alert("FMS Connected", AlertType.INFO);

    private String IPaddress = "UNKOWN";

    public RobotTelemetry() {
        super("Robot");     // Set tab name to Robot

        // Allows us to see all running commands on the robot
        SmartDashboard.putData(CommandScheduler.getInstance());

        // Column 0
        // Setup the auton selector to display on shuffleboard
        AutoSetup.setupSelectors();
        tab.add("Auton Selection", AutoSetup.autoChooser).withPosition(0, 0).withSize(2, 1);

        // Column 2
        tab.addBoolean("Connected?", () -> flash())
                .withPosition(2, 0)
                .withSize(1, 1)
                .withProperties(
                        Map.of("Color when true", "#300068", "Color when false", "#FFFFFF"));

        // tab.addBoolean("Romi Connected", () -> Robot.isRomiConnected())
        //         .withPosition(3, 0)
        //         .withSize(1, 1);

        tab.addNumber("Match Time", () -> Timer.getMatchTime())
                .withPosition(2, 1)
                .withSize(2, 2)
                .withWidget("Simple Dial")
                .withProperties(Map.of("Min", 0, "Max", 135));

        // Column 4
        tab.add("Alerts", SmartDashboard.getData("Alerts")).withPosition(4, 0).withSize(2, 2);
        tab.add("MAC Address", Robot.MAC).withPosition(4, 2).withSize(2, 1);
        tab.addString("IP Address", () -> getIP()).withPosition(4, 3).withSize(2, 1);

        // Intiate the Subystem Telemetry Classes
    }

    @Override
    public void periodic() {
        checkFMSalert();
        checkBatteryWhenDisabledalert();
    }


    // ---------------------//
    // initialize //
    // ---------------------//
    // Create all View Widgets, ones you can't edit, created after subsystem
    // instances are made
    public void initialize() {

        
        //matchTimeWidget().withPosition(0, 1);
        //flashWidget().withPosition(0, 0);
        //m_tab.addBoolean("Compressor on?", () -> Robot.pneumatics.isCompressorEnabled()).withPosition(1, 1);
        //m_tab.addNumber("Pressure", () -> Robot.pneumatics.getPressure()).withPosition(1, 0);
        //m_tab.addNumber("FPGA timestamp", () -> Timer.getFPGATimestamp()).withPosition(0, 4);
        //m_limelightLEDenable = m_tab.add("Limelight LED Enable", true).withWidget(BuiltInWidgets.kToggleButton)
        //        .withPosition(2, 0);
        //m_tab.addNumber("LL-Distance", () -> Robot.visionLL.getLLDistance()).withPosition(2, 1);
        //m_tab.addNumber("Target Distance", () -> Robot.visionLL.getActualDistance()).withPosition(2, 2);
        //m_enableTabsWidget = m_tab.add("Update Enable", false).withWidget(BuiltInWidgets.kToggleButton).withPosition(3,
        //        0).withSize(1, 1);


        //AutonSetup.setupSelectors();
        //m_autonSelectorWidget = m_tab.add(AutonSetup.chooser).withPosition(4, 0).withSize(3, 1);
        //m_autonPositionWidget = m_tab.add(AutonSetup.posChooser).withPosition(4, 1).withSize(3, 1);
        //m_autonColorWidget = m_tab.add(AutonSetup.colorChooser).withPosition(4, 2).withSize(3, 1);
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


    // Flash
    public SimpleWidget flashWidget() {
        //m_flashWidget = m_tab.add("Flash", false);
        //flashEntry = m_flashWidget.getEntry();
        //m_flashWidget.withWidget(BuiltInWidgets.kBooleanBox);
        //m_flashWidget.withProperties(Map.of("Color when true", "#1a0068", "Color when false", "#FFFFFF"));
        return m_flashWidget;
    }


    // --------//
    // Update //
    // --------//
    static boolean b = true;

    public void update() { // This will be called at a rate setup in ShufflbeboardTabs
        b = !b;
        flashEntry.setBoolean(b);
    }

    public static boolean getLimeLightToggle() {
        return m_limelightLEDenable.getEntry().getBoolean(true);
    }

    public static void print(String output) {
        if (!disablePrints) {
            System.out.println(output);
        }
    }
}
