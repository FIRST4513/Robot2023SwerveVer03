package frc.lib.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonSRXConfiguration;
import com.ctre.phoenix.sensors.SensorInitializationStrategy;

public class MotorSRXSubsystemConfig {
    public String name;
    /* Inverted */
    public boolean kInverted = false;
    public boolean kFollowerInverted = true;

    /* Neutral Modes */
    public NeutralMode kNeutralMode = NeutralMode.Brake;

    /* Control Loop Constants */
    public double kP = 0.0;
    public double kI = 0;
    public double kD = 0;
    public double kF = 0.05;
    public double kIz = 150;
    public double motionCruiseVelocity = 0;
    public double motionAcceleration = 0;

    /* Current Limiting */
    public int currentLimit = 40;
    public int tirggerThresholdLimit = 45;
    public double PeakCurrentDuration = 0.5;
    public boolean EnableCurrentLimit = true;
    public SupplyCurrentLimitConfiguration supplyLimit =
            new SupplyCurrentLimitConfiguration(
                    EnableCurrentLimit, currentLimit, tirggerThresholdLimit, PeakCurrentDuration);

    /* Voltage Compensation */
    public double voltageCompSaturation = 12;

    /* Ramp Rate */
    public double openLoopRamp = 0;
    public double closedLoopRamp = 0;

    /* Intialization Strategy */
    public SensorInitializationStrategy sensorStrat = SensorInitializationStrategy.BootToZero;

    // Falcon Setup
    public TalonSRXConfiguration TalonSRXConfig = new TalonSRXConfiguration();

    public MotorSRXSubsystemConfig(String name) {
        this.name = name;
        updateTalonSRXConfig();
    }

    public void updateTalonSRXConfig() {
        TalonSRXConfig.slot0.kP = kP;
        TalonSRXConfig.slot0.kI = kI;
        TalonSRXConfig.slot0.kD = kD;
        TalonSRXConfig.slot0.kF = kF;
        TalonSRXConfig.slot0.integralZone = kIz;
        TalonSRXConfig.motionCruiseVelocity = motionCruiseVelocity;
        TalonSRXConfig.motionAcceleration = motionAcceleration;

        //TalonSRXConfig.supplyCurrLimit = supplyLimit;                 // Not available in SRX
        TalonSRXConfig.openloopRamp = openLoopRamp;
        TalonSRXConfig.closedloopRamp = closedLoopRamp;
        TalonSRXConfig.voltageCompSaturation = voltageCompSaturation;
        //TalonSRXConfig.initializationStrategy = sensorStrat;          // Not available in SRX
    }
}
