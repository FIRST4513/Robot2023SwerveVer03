package frc.robot.arm;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.sensors.SensorInitializationStrategy;

import frc.robot.RobotConfig.LimitSwitches;
import frc.robot.RobotConfig.Motors;

public class ArmConfig {

    // ------ Device Port Constants ------
    public final static int kMotorPort =            Motors.armMotorID;
    public final static int kLowerLimitSwitchPort = LimitSwitches.armLowerLimitSw;
    public final static int kUpperLimitSwitchPort = LimitSwitches.armUpperLimitSw;

    // ------ Speed Constants ------
    public final static double kRaiseSpeed = 0.5;
    public final static double kLowerSpeed = -0.35;
    public final static double kHoldSpeed = 0.1;

    // ------ Limit Switch True States ------
    public final static boolean lowerLimitSwitchTrue = true;
    public final static boolean upperLimitSwitchTrue = true;

    /* Neutral Modes */
    public static final NeutralMode armNeutralMode = NeutralMode.Coast;
    
    /* Inverts */
    public static final boolean armMotorInvert = false;
    
    /* Arm Motor PID Values */
    public static final double armKP = 0.1;
    public static final double armKI = 0.0;
    public static final double armKD = 0.0;
    public static final double armKF = 0.0;

    /* Arm Motor Characterization Values */
    public static final double armKS = ( 0.32 / 12.0 );  // /12 to convert from volts to %output
    public static final double armKV = ( 3.00 / 12.0 );
    public static final double armKA = ( 0.27 / 12.0 );

    // increase to reduce jitter, (2048 * angleGearRatio) / 360.0) = 1 degree = 122 cnts
    public static final int armAngleAllowableError = 122;  // 1 degree is close enough ????

    
    public static TalonFXConfiguration armFXConfig;     // TalonFX Config objetct

    /* Arm Motor Current Limiting */
    public static final int angleContinuousCurrentLimit = 25;
    public static final int anglePeakCurrentLimit = 40;
    public static final double anglePeakCurrentDuration = 0.1;
    public static final boolean angleEnableCurrentLimit = true;


    // --------------- Constuctor Setting Up Motor Config values -------------
    public ArmConfig() {
        /* Arm Motor Configurations */
        armFXConfig = new TalonFXConfiguration();
        SupplyCurrentLimitConfiguration armSupplyLimit =
                new SupplyCurrentLimitConfiguration(
                    ArmConfig.angleEnableCurrentLimit,
                    ArmConfig.angleContinuousCurrentLimit,
                    ArmConfig.anglePeakCurrentLimit,
                    ArmConfig.anglePeakCurrentDuration);

        armFXConfig.slot0.kP = ArmConfig.armKP;
        armFXConfig.slot0.kI = ArmConfig.armKI;
        armFXConfig.slot0.kD = ArmConfig.armKD;
        armFXConfig.slot0.kF = ArmConfig.armKF;
        armFXConfig.slot0.allowableClosedloopError = ArmConfig.armAngleAllowableError;
        armFXConfig.supplyCurrLimit = armSupplyLimit;
        armFXConfig.initializationStrategy = SensorInitializationStrategy.BootToZero;
    }
}
