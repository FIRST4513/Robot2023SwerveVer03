package frc.robot.arm;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRXConfiguration;
import com.ctre.phoenix.sensors.SensorInitializationStrategy;

public class ArmSRXMotorConfig {
    // TalonSRX Config object
    public static TalonSRXConfiguration config = new TalonSRXConfiguration();

    /* Motor Control Sets */
    public static final NeutralMode armDefaultNeutralMode   = NeutralMode.Coast;
    public static final boolean     armMotorInvert          = true;
    public static final boolean     armEncoderInvert        = false;
    public static final boolean     armEnableCurrentLimit   = true;
    public static final SensorInitializationStrategy sensorStrat = SensorInitializationStrategy.BootToZero;

    // 76815 encoder Cnts = 42.5 degrees (1807.41176 cnts per degree)
    public static final int         allowableError          = 2000;
    // increase AllowableError to reduce jitter, (2048 * angleGearRatio) / 360.0) = 1 degree = 122 cnts

    /* Motor Limits (Encoder Cnts) */
    public static final boolean     armExtendSoftLimitEnable           = false;
    public static final boolean     armRetractSoftLimitEnable          = false;
    public static final int         armExtendSoftLimitThreshold        = +80000;   // TEST !!! 
    public static final int         armRetractSoftLimitThreshold       = -80000;   // TEST !!!

    /* Motion Magic Control Loop Constants */

    /*
     * Arbitrary Feed Forward needs to be scaled based on angle
     * Testing showed 45 degrees forward requires +0.22 pwr to hold
     *               -45 degrees reverse requires -0.25 pwr to hold
     * 
     * Cosine of 45 degrees = 0.707 so to get 0.25 power out kP scaler must be 0.3536
     */
    public final double arbitraryFeedForwardScaler = 0.3536;

    /*
     * Cruise velocity Calculation: 45 degrees in 3 seconds required an encoder cnt of 81,333
     *                              1 second of travel ( 15 degrees )= 27,111 counts
     *                              1/100 second of travel ( 1.5 degrees )= 2,711 counts
     */
    public final double kP = 0.1;     // Needs to be Determined by testing
    public final double kI = 0.0;     // could be 0
    public final double kD = 0.0;     // could be 0
    public final double kF = 0.0;     // 0 we will be using ArbitraryFeedForward for position control
    public final double kIz = 0.0;
    
    public final double motionCruiseVelocity        = 1500; // 15 degrres per second
    public final double motionAcceleration          = 1500; // 1 second to get up to cruise velocity
    public final int    motionCurveStrength         = 0;    // 0 no smoothing to 8 max smoothing
   
    /* Current Limiting */
    public static final int     continuousCurrentLimit   = 25;       // Amps
    public static final int     peakCurrentLimit         = 40;       // Amps
    public static final int     peakCurrentDuration      = 100;      // Time in milliseconds
    public final boolean        EnableCurrentLimit       = false;

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
        config.slot0.integralZone               = kIz;
        config.motionCruiseVelocity             = motionCruiseVelocity;
        config.motionAcceleration               = motionAcceleration;
        
        config.openloopRamp                     = openLoopRamp;
        config.closedloopRamp                   = closedLoopRamp;
        config.voltageCompSaturation            = voltageCompSaturation;

        config.continuousCurrentLimit           = continuousCurrentLimit;
        config.peakCurrentLimit                 = peakCurrentLimit;         
        config.peakCurrentDuration              = peakCurrentDuration;
        config.slot0.allowableClosedloopError   = allowableError;

        config.forwardSoftLimitEnable           = armExtendSoftLimitEnable;
        config.forwardSoftLimitThreshold        = armExtendSoftLimitThreshold;
        config.reverseSoftLimitEnable           = armRetractSoftLimitEnable;
        config.reverseSoftLimitThreshold        = armRetractSoftLimitThreshold;
    }

}

