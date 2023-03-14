package frc.robot.leds;

import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LedSubSys extends SubsystemBase {
    public LedConfig config;
    
    // Devices
    public PWMVictorSPX lightStrip;

    // Constructor
    public LedSubSys() { 
        config = new LedConfig();
        lightStrip = new PWMVictorSPX(5);
    }

    public void setLEDs(double value) {
        lightStrip.set(value);
    }
}
