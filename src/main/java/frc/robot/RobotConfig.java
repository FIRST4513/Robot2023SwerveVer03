package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class RobotConfig {
    private static final boolean FAKE_FMS = true;
    public static final boolean ENABLE_DASHBOARD = true;

    public final Motors motors = new Motors();
    public final Encoders encoders = new Encoders();
    public final LimitSwitches limitSwitches = new LimitSwitches();

    public final String praticeBotMAC = "18:66:DA:19:D4:41";

    public static final double loopPeriodSecs = 0.02;
    public static final boolean TUNING_MODE = false;

    public final class Motors {
        public final static int FLdriveMotorID =     1;       // Can ID Talon SRX
        public final static int FRdriveMotorID =     2;       // Can ID Talon SRX
        public final static int BLdriveMotorID =     3;       // Can ID Talon SRX
        public final static int BRdriveMotorID =     4;       // Can ID Talon SRX

        public final static int FLangleMotorID =     5;       // Can ID Talon SRX
        public final static int FRangleMotorID =     6;       // Can ID Talon SRX
        public final static int BLangleMotorID =     7;       // Can ID Talon SRX
        public final static int BRangleMotorID =     8;       // Can ID Talon SRX
        public final static int armMotorID =         13;      // Can ID Talon SRX
        public final static int intakeUpperMotorID = 14;      // Can ID Talon SRX
        public final static int intakeLowerMotorID = 15;      // Can ID Talon SRX

        public final static int elevatorMotorID =     7;      // PWM Port 
    }

    public final class Encoders {
        public final static int FLcanCoderID =        9;       // Cancorder CAN ID
        public final static int FRcanCoderID =       10;       // Cancorder CAN ID
        public final static int BLcanCoderID =       11;       // Cancorder CAN ID
        public final static int BRcanCoderID =       12;       // Cancorder CAN ID

        public final static int elevMotorEncoderA =    4;      // DIO Port
        public final static int elevMotorEncoderB =    5;      // DIO Port
    }

    public final class LimitSwitches {
        public final static int elevatorLowerLimitSw =     0;   // DIO Port
        public final static int armUpperLimitSw =          1;   // DIO Port
        public final static int armLowerLimitSw =          2;   // DIO Port
    } 

    public final class AnalogPorts {
        public final static int intakeConeDetectPort =   0;   //ANALOG Port
        public final static int intakeCubeDetectPort =   1;   //ANALOG Port
    }

    //Check if we are FMSattached or Faking it
    public static boolean isFMSEnabled (){
        return ((DriverStation.isFMSAttached() || FAKE_FMS));
      }
}
