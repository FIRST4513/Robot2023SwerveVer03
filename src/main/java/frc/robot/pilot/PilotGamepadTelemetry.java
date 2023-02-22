//Created by Spectrum3847

package frc.robot.pilot;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.SimpleWidget;
import frc.lib.telemetry.CustomLayout;
import frc.robot.Robot;

public class PilotGamepadTelemetry {
    public final PilotGamepad gamepad; 

    // Tab & Layouts  //
    private static ShuffleboardTab m_tab;
    private gamepadLayout driver;
    //private gamepadLayout operator;
    
    public static SimpleWidget m_EnableWidget;


    // -------------  Constructor     ---------------
    public PilotGamepadTelemetry(PilotGamepad gp) {
        m_tab = Shuffleboard.getTab("Gamepad Pilot");
        gamepad = gp;
    }

    public void initialize() {
        driver = new gamepadLayout("Driver 0", m_tab, Robot.pilotGamepad);
        driver.initialize();
        m_EnableWidget = m_tab.add("Update Enable", false)
                            .withWidget(BuiltInWidgets.kToggleButton).withPosition(5, 0);
    }

    public void update() {
        if (m_EnableWidget.getEntry().getBoolean(false)) {
            driver.update();
        }
    }

    private class gamepadLayout extends CustomLayout{
        //public GenericEntry yLeftDeadbandEntry2;
        //public NetworkTableEntry xLeftDeadbandEntry;
        public GenericEntry yLeftDeadbandEntry;
        public GenericEntry xLeftDeadbandEntry;
        public GenericEntry yRightDeadbandEntry;
        public GenericEntry xRightDeadbandEntry;
        public GenericEntry yLeftExpValEntry;
        public GenericEntry xLeftExpValEntry;
        public GenericEntry yRightExpValEntry;
        public GenericEntry xRightExpValEntry;
        public GenericEntry yLeftScalarValEntry;
        public GenericEntry xLeftScalarValEntry;
        public GenericEntry yRightScalarValEntry;
        public GenericEntry xRightScalarValEntry;
        private PilotGamepad gamepad;

        public gamepadLayout(String name, ShuffleboardTab tab, PilotGamepad gamepad){
            super(name, tab);
            this.gamepad = gamepad;
            setColumnsAndRows(2, 6);
            setSize(2, 4);
        }

        public void initialize() {
            yLeftDeadbandEntry = quickAddPersistentWidget("yLeftDeadband", gamepad.gamepad.leftStick.expXCurve.getDeadzone(), 0, 0);
            xLeftDeadbandEntry = quickAddPersistentWidget("xLeftDeadband", gamepad.gamepad.leftStick.expXCurve.getDeadzone(), 0, 1);
            yRightDeadbandEntry = quickAddPersistentWidget("yRightDeadband", gamepad.gamepad.rightStick.expYCurve.getDeadzone(), 1, 0);
            xRightDeadbandEntry = quickAddPersistentWidget("xRightDeadband", gamepad.gamepad.rightStick.expXCurve.getDeadzone(), 1, 1);
            yLeftExpValEntry = quickAddPersistentWidget("yLeftExpVal", gamepad.gamepad.leftStick.expYCurve.getExpVal(), 0, 2);
            xLeftExpValEntry = quickAddPersistentWidget("xLeftExpVal", gamepad.gamepad.leftStick.expXCurve.getExpVal(), 0, 3);
            yRightExpValEntry = quickAddPersistentWidget("yRightExpVal", gamepad.gamepad.rightStick.expYCurve.getExpVal(), 1, 2);
            xRightExpValEntry = quickAddPersistentWidget("xRightExpVal", gamepad.gamepad.rightStick.expXCurve.getExpVal(), 1, 3);
            yLeftScalarValEntry = quickAddPersistentWidget("yLeftScalar", gamepad.gamepad.leftStick.expYCurve.getScalar(), 0, 4);
            xLeftScalarValEntry = quickAddPersistentWidget("xLeftScalar", gamepad.gamepad.leftStick.expXCurve.getScalar(), 0, 5);
            yRightScalarValEntry = quickAddPersistentWidget("yRightScalar", gamepad.gamepad.rightStick.expYCurve.getScalar(), 1, 4);
            xRightScalarValEntry = quickAddPersistentWidget("xRightScalar", gamepad.gamepad.rightStick.expXCurve.getScalar(), 1, 5);
        }

        public void update(){
            gamepad.gamepad.leftStick.expYCurve.setDeadzone(yLeftDeadbandEntry.getDouble(0));
            gamepad.gamepad.leftStick.expXCurve.setDeadzone(xLeftDeadbandEntry.getDouble(0));
            gamepad.gamepad.rightStick.expYCurve.setDeadzone(yRightDeadbandEntry.getDouble(0));
            gamepad.gamepad.rightStick.expXCurve.setDeadzone(xRightDeadbandEntry.getDouble(0));
            
            gamepad.gamepad.leftStick.expYCurve.setExpVal(yLeftExpValEntry.getDouble(0));
            gamepad.gamepad.leftStick.expXCurve.setExpVal(xLeftExpValEntry.getDouble(0));
            gamepad.gamepad.rightStick.expYCurve.setExpVal(yRightExpValEntry.getDouble(0));
            gamepad.gamepad.rightStick.expXCurve.setExpVal(xRightExpValEntry.getDouble(0));
            
            gamepad.gamepad.leftStick.expYCurve.setScalar(yLeftScalarValEntry.getDouble(0));
            gamepad.gamepad.leftStick.expXCurve.setScalar(xLeftScalarValEntry.getDouble(0));
            gamepad.gamepad.rightStick.expYCurve.setScalar(yRightScalarValEntry.getDouble(0));
            gamepad.gamepad.rightStick.expXCurve.setScalar(xRightScalarValEntry.getDouble(0));
        }

    }
}

