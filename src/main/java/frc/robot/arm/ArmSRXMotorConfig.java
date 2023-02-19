package frc.robot.arm;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRXConfiguration;
import com.ctre.phoenix.sensors.SensorInitializationStrategy;

public class ArmSRXMotorConfig {
    // TalonSRX Config object
    public static TalonSRXConfiguration config = new TalonSRXConfiguration();

    /* Motor Control Sets */
    public static final NeutralMode armNeutralMode          = NeutralMode.Coast;
    public static final boolean     armMotorInvert          = false;
    public static final boolean     armEncoderInvert        = true;
    public static final boolean     armEnableCurrentLimit   = true;
    public static final SensorInitializationStrategy sensorStrat = SensorInitializationStrategy.BootToZero;
    public static final int         allowableError          = 1000;
    // increase AllowableError to reduce jitter, (2048 * angleGearRatio) / 360.0) = 1 degree = 122 cnts

    /* Motor Limits (Encoder Cnts) */
    public final int min = 0;
    public final int max = 75000;
    public final int mid = 37000;

    public static final boolean     armForwardSoftLimitEnable          = false;
    public static final boolean     armReverseSoftLimitEnable          = false;
    public static final int         armForwardSoftLimitThreshold       = 45000;    
    public static final int         armReverseSoftLimitThreshold       = 100;

    public final int armMaxFalcon = 60000;


    /* Control Loop Constants */
    // Pid and Motion Magic
    public final double kP = 0.5;   // not accurate value, just testing
    public final double kI = 0;     // could be 0
    public final double kD = 0;     // could be 0
    public final double kF = 0.3;   
    public final double kIz = 0;

    public final double motionCruiseVelocity = 0;
    public final double motionAcceleration = 0;
    public final double arbitraryFeedForwardScaler = 0.08;
    
    /* Current Limiting */
    public static final int     continuousCurrentLimit   = 25;       // Amps
    public static final int     peakCurrentLimit         = 40;       // Amps
    public static final int     peakCurrentDuration      = 100;      // Time in milliseconds
    public final boolean        EnableCurrentLimit          = false;

    /* Voltage Compensation */
    public final double voltageCompSaturation = 12;

    /* Ramp Rate */
    public final double openLoopRamp = 0;
    public final double closedLoopRamp = 0;

    /* Motor Characterization Values */
    public final double kS = 0;
    public final double kV = 0; 
    public final double kA = 0;


    // --------------- Constuctor Setting Up Motor Config values -------------
    public ArmSRXMotorConfig(){
        config.slot0.kP = kP;
        config.slot0.kI = kI;
        config.slot0.kD = kD;
        config.slot0.kF = kF;
        config.slot0.integralZone   = kIz;
        config.motionCruiseVelocity = motionCruiseVelocity;
        config.motionAcceleration   = motionAcceleration;
        
        config.openloopRamp             = openLoopRamp;
        config.closedloopRamp           = closedLoopRamp;
        config.voltageCompSaturation    = voltageCompSaturation;

        config.continuousCurrentLimit         = continuousCurrentLimit;
        config.peakCurrentLimit               = peakCurrentLimit;         
        config.peakCurrentDuration            = peakCurrentDuration;
        config.slot0.allowableClosedloopError = allowableError;

        config.forwardSoftLimitEnable           = armForwardSoftLimitEnable;
        config.reverseSoftLimitEnable           = armReverseSoftLimitEnable;
        config.forwardSoftLimitThreshold        = armForwardSoftLimitThreshold;
        config.reverseSoftLimitThreshold        = armReverseSoftLimitThreshold;
    }

}

