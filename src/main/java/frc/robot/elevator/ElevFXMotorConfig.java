package frc.robot.elevator;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.sensors.SensorInitializationStrategy;

public class ElevFXMotorConfig {
    // TalonSRX Config object
    public static TalonFXConfiguration config;

    /* Motor Control Sets */
    public static final NeutralMode elevNeutralMode          = NeutralMode.Brake;
    public static final boolean     elevMotorInvert          = false;
    public static final SensorInitializationStrategy sensorStrat = SensorInitializationStrategy.BootToZero;
    public static final int         allowableError           = 122;

    // Soft Limit Switches - TODO Update as need    
    public static final boolean     elevForwardSoftLimitEnable          = false;
    public static final boolean     elevReverseSoftLimitEnable          = false;
    public static final int         elevForwardSoftLimitThreshold       = 45000;    
    public static final int         elevReverseSoftLimitThreshold       = 100;

    /* Motor Limits (Encoder Cnts) */
    public final int min = 0;
    public final int max = 75000;
    public final int mid = 37000;
    
    public final int elevMaxFalcon = 60000;

    /* Control Loop Constants */
    public final double kP = 0.5;   // not accurate value, just testing
    public final double kI = 0;     // could be 0
    public final double kD = 0;     // could be 0
    public final double kF = 0.05;   
    public final double kIz = 0;
    public final double motionCruiseVelocity = 0;
    public final double motionAcceleration = 0;
    
    /* Current Limiting */
    public final int            currentLimit            = 30;
    public final int            triggerThresholdLimit   = 0;
    public static final int     peakCurrentDuration     = 100;      // Time in milliseconds
    public final boolean        enableCurrentLimit      = true;

    public final SupplyCurrentLimitConfiguration supplyLimit = new SupplyCurrentLimitConfiguration(
                                                enableCurrentLimit,
                                                currentLimit,
                                                triggerThresholdLimit,
                                                peakCurrentDuration);

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
    public ElevFXMotorConfig(){
        config.slot0.kP = kP;
        config.slot0.kI = kI;
        config.slot0.kD = kD;
        config.slot0.kF = kF;
        config.slot0.integralZone = kIz;
        config.motionCruiseVelocity           = motionCruiseVelocity;
        config.motionAcceleration             = motionAcceleration;

        config.openloopRamp                   = openLoopRamp;
        config.closedloopRamp                 = closedLoopRamp;
        config.voltageCompSaturation          = voltageCompSaturation;
        config.supplyCurrLimit                = supplyLimit;
        config.slot0.allowableClosedloopError = allowableError;

        config.forwardSoftLimitEnable           = elevForwardSoftLimitEnable;
        config.reverseSoftLimitEnable           = elevReverseSoftLimitEnable;

        config.forwardSoftLimitThreshold        = elevForwardSoftLimitThreshold;
        config.reverseSoftLimitThreshold        = elevReverseSoftLimitThreshold;

    
    }

}
