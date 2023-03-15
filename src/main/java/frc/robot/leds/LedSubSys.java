package frc.robot.leds;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LedSubSys extends SubsystemBase {
    public LedConfig config;
    private Timer alternatingTimer;
    
    // Devices
    public PWMVictorSPX lightStrip;

    // Constructor
    public LedSubSys() { 
        config = new LedConfig();
        lightStrip = new PWMVictorSPX(0);
        alternatingTimer = new Timer();
        alternatingTimer.reset();
        alternatingTimer.start();
    }

    public void periodic() {
        setLEDs(0.67);
    }

    public void setLEDs(double value) {
        lightStrip.set(value);
    }
}
