package frc.robot.elevator;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.sensors.SensorInitializationStrategy;

public class ElevFXMotorConfig {
    // TalonSRX Config object
    public static TalonFXConfiguration config = new TalonFXConfiguration();

    /* Motor Control Sets */
    public static final NeutralMode elevNeutralMode          = NeutralMode.Brake;
    public static final boolean     elevMotorInvert          = false;
    public static final SensorInitializationStrategy sensorStrat = SensorInitializationStrategy.BootToZero;
    public static final int         allowableError           = 122;


    /* Control Loop Constants */
    /*
     * Cruise velocity Calculation: 30 inch in 3 seconds (elev 30" length = 99,480 cnts)
     *                              10 inch in 1 second  (elev 10" lenght = 33,160 cnts)
     *                              1  inch in 1/100 second ( 100 ms = 3,316 cnts)
     *                              1 inch = 3,316 sensor units
     */
    public final double motionCruiseVelocity        = 30000; // 10 inches per second
    public final double motionAcceleration          = 30000; // 1 second to get up to cruise velocity
    public final int    motionCurveStrength         = 0;    // 0 no smoothing to 8 max smoothing

    /*
     * kP Calculation Example:
     *      if Error after ArbitraryFeedForwrd = 1 inch short we wish to add .02 pwr to get there
     *      1023 * 0.02 = 20.46 / 3316 = kP 0.0067
     *      ( 1023 = full Pwr, 3316 = sensor units of error)
     *      controller will add 0.02 pwr for each inch we are off (up to the max velocity specified)
     */
    public final double kP = 0.08;    // Determined by testing on 2/19/23
    public final double kI = 0.0;     // could be 0
    public final double kD = 0.0;     // could be 0
    public final double kF = 0.0;     // 0 we will be using ArbitraryFeedForward for position control
    public final double kIz = 0.0;
    public final static double arbitraryFeedForward = 0.08; // Measured value to hold elev

    /* Current Limiting */
    public final int            currentLimit            = 40;
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
        config.motionCurveStrength            = motionCurveStrength;

        config.openloopRamp                   = openLoopRamp;
        config.closedloopRamp                 = closedLoopRamp;
        config.voltageCompSaturation          = voltageCompSaturation;
        config.supplyCurrLimit                = supplyLimit;
        config.slot0.allowableClosedloopError = allowableError;

        config.forwardSoftLimitEnable = ElevatorConfig.LowerSoftLimitSwitchEnable;
        // config.forwardSoftLimitThreshold = 
        //         (int) Robot.elevator.convertHeightToFalconCnt( ElevatorConfig.LowerSoftLimitSwitchHt );

        config.reverseSoftLimitEnable = ElevatorConfig.UpperSoftLimitSwitchEnable;
        // config.reverseSoftLimitThreshold =
        //         (int) Robot.elevator.convertHeightToFalconCnt( ElevatorConfig.UpperSoftLimitSwitchHt );
    }

}
