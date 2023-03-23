package frc.robot.elevator;

import java.util.function.DoubleSupplier;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.Rmath;
import frc.robot.RobotConfig.LimitSwitches;
import frc.robot.RobotConfig.Motors;

public class ElevatorSubSys extends SubsystemBase {
    public ElevatorConfig           config;
    public static ElevFXMotorConfig configFX;
    public ElevFXMotorConfig        motorConfig;

    // Devices
    public final WPI_TalonFX m_motor;
    public final DigitalInput elevLowerLimitSw, elevUpperLimitSw;

    // Elevator Variables
    public double target_height;
    public double mCurrElevPwr;

    public double mCurrEncoderCnt = 0;
    public double mCurrElevHt;      // Elevator Height Zero at bottom (Inches)

    public static String elevBrakeStatus = "";

    // -----------  Constructor --------------------
    public ElevatorSubSys() {
        configFX            = new ElevFXMotorConfig();
        m_motor             = new WPI_TalonFX(Motors.elevatorMotorID);
        elevLowerLimitSw    = new DigitalInput(LimitSwitches.elevatorLowerLimitSw);
        elevUpperLimitSw    = new DigitalInput(LimitSwitches.elevatorUpperLimitSw);
        elevatorMotorConfig();
        setBrakeMode(true);
    }

   @Override
   public void periodic() {
       updateCurrentElevPosition();
       System.out.println(m_motor.get());
    }

    // --------------------------------------------
    // ---------   Elevator Methods   -------------
    // --------------------------------------------

    // ------------ Lower Elevator ----------
    public void elevLower() {
        elevSetSpeed( ElevatorConfig.KLowerSpeedDefault ); 
    }

    // ------------ Raise Elevator ----------
    public void elevRaise() {
        elevSetSpeed( ElevatorConfig.KRaiseSpeedDefault ); 
    }

    // ------------ Hold Elevator Position ----------
    public void elevHoldMtr(){
        //elevSetSpeed( ElevatorConfig.KHoldSpeedDefault );
        //mCurrElevPwr = ElevatorConfig.KHoldSpeedDefault;
        setMMheight(mCurrElevHt);
    }

    // ------------ Stop Elevator Motor  ----------
    public void elevStop() {
        mCurrElevPwr = 0;        
        m_motor.stopMotor();
    }

    // ------------  Set Elev to Height by Motion Magic  ----------
    public void setMMheight(double height) {
        height = limit_target_ht(height);
        target_height = height;
        m_motor.set( ControlMode.MotionMagic, convertHeightToFalconCnt(height),
                     DemandType.ArbitraryFeedForward, ElevFXMotorConfig.arbitraryFeedForward);
    }

    public void setMMheight(DoubleSupplier height) {
        setMMheight(height.getAsDouble());
    }

    // ------------ This Drives the Elevator Manually during TeleOp ----------
    public void elevSetSpeed(double speed){
        // Cap speed to max
        if ( speed > ElevatorConfig.raiseMaxPwr )  { speed = ElevatorConfig.raiseMaxPwr; }
        if ( speed < ElevatorConfig.lowerMaxPwr )  { speed = ElevatorConfig.lowerMaxPwr; }

        // Were Raising the elevator
        if ( speed > ElevatorConfig.KHoldSpeedDefault ) {
            // Test for hitting Upper Limits
            if ( isUpperLimitReached() ) {
                elevHoldMtr();
                return;
            }
            //  This is for slowing down as we approach the top
            if ( mCurrElevHt >= ElevatorConfig.KLimitElevTopSlowHt )  {
                speed = ElevatorConfig.KRaiseSlowSpeed;
            }
        }

        // Were Lowering the elevator
        if ( speed <= ElevatorConfig.KHoldSpeedDefault) {
            // Test if hitting Bottom limit switch
            if ( isLowerLimitSwitchPressed() ) {
                elevStop();
                return;
            }
            //  This is for slowing down as we approach the bottom    		
            if (mCurrElevHt <= ElevatorConfig.KLimitElevBottomSlowHt1) { speed = ElevatorConfig.KLowerSlowSpeed1; }
            if (mCurrElevHt <= ElevatorConfig.KLimitElevBottomSlowHt2) { speed = ElevatorConfig.KLowerSlowSpeed2; }
            if (mCurrElevHt <= ElevatorConfig.KLimitElevBottomSlowHt3) { speed = ElevatorConfig.KLowerSlowSpeed3; }
            // if ( mCurrElevHt <= ElevatorConfig.KLimitElevBottomSlowHt ) {
            //     speed = ElevatorConfig.KLowerSlowSpeed;
            // }
        }

        mCurrElevPwr = speed;
        m_motor.set(mCurrElevPwr);              // Send Power to motor 
        
        // System.out.println("Speed = " + speed);
    }

    public void elevSetSpeed(DoubleSupplier speed) {
        elevSetSpeed(speed.getAsDouble());
    }



// -----------------  Encoder Sensor Methods --------------------
    public double getElevEncoderCnt()               { return mCurrEncoderCnt;}
    public double getElevHeightInches()             { return mCurrElevHt; }

    public void updateCurrentElevPosition() {
        // Called in subsystem periodic so only one CAN call needed per command loop 20ms
        mCurrEncoderCnt = m_motor.getSelectedSensorPosition();
        mCurrElevHt = Rmath.mRound(convertFalconCntToHeight(mCurrEncoderCnt) , 2);
    }

    public double convertHeightToFalconCnt( double height) {
        return ( height * ElevatorConfig.kCntsPerInch );
    }

    public double convertFalconCntToHeight( double cnt) {
        return ( cnt * ElevatorConfig.kInchesPerCnt );
    }

    public void resetEncoder(){
        m_motor.setSelectedSensorPosition(0);
        mCurrEncoderCnt = 0.0 ;
        mCurrElevHt = 0.0;
    }

    public void resetEncoder( double position ){
        m_motor.setSelectedSensorPosition(position);
        mCurrEncoderCnt = position;
        mCurrElevHt = Rmath.mRound(convertFalconCntToHeight(position) , 2);
    }

    // ------------- Other Misc Methods  ---------------
    public double getTargetHeight()         { return target_height; }
    public double getElevMotorPwr()         { return mCurrElevPwr; }

    public double limit_target_ht( double ht ){
        if (ht > ElevatorConfig.KElevMaxTopHt)     { ht = ElevatorConfig.KElevMaxTopHt; }
        if (ht < 0)                        { ht = 0; }
        return ht;
    }

    public boolean isMMtargetReached(){
        // If we are within the deadband of our target we can stop
        if (Math.abs(target_height-mCurrElevHt) <= ElevatorConfig.KheightDeadBand) { return true; }
        return false;
    }

    public void setBrakeMode( boolean value){
        if (value) {
            m_motor.setNeutralMode(NeutralMode.Brake);
            elevBrakeStatus = "Elev Brake On";
        } else {
            m_motor.setNeutralMode(NeutralMode.Coast);
            elevBrakeStatus = "Intake Brake Off";
        }
    }

    public String getBrakeStatus(){
       return elevBrakeStatus;
    }

    // -----------------  Lower/Upper Limits ----------------
    public boolean isLowerLimitSwitchPressed() {
        if (elevLowerLimitSw.get() == ElevatorConfig.LowerLimitSwitchTrue) { return true; }
        return false;
    }

    public boolean isUpperLimitSwitchPressed() {
        if (elevUpperLimitSw.get() == ElevatorConfig.UpperLimitSwitchTrue) { return true; }
        return false;
    }

    public boolean isUpperLimitReached() {
        if ( mCurrElevHt >= ElevatorConfig.KElevMaxTopHt )           { return true; }
        return false;
    }

    public boolean isLowerLmtSwNotPressed() { return !isLowerLimitSwitchPressed(); }
    public boolean isUpperLmtNotReached()   { return !isUpperLimitSwitchPressed(); }

    public String getUpperLimitSwStatus(){
        if ( isUpperLimitSwitchPressed() )  { return "Pressed"; }
        return "Not Pressed";
    }

    public String getLowerLimitSwStatus(){
        if ( isLowerLimitSwitchPressed() ) { return "Pressed"; }
        return "Not Pressed";
    }

    public void softLimitsTrue() {
        m_motor.configReverseSoftLimitEnable(true);
    }

    public void softLimitsFalse() {
        m_motor.configReverseSoftLimitEnable(false);
    }

    // --------------------------------------------------------
    // ---------------- Configure Elev Motor ------------------
    // --------------------------------------------------------
    public void elevatorMotorConfig(){
        // This config is for the Talon FX Controller
        m_motor.configFactoryDefault();
        m_motor.configAllSettings(ElevFXMotorConfig.config);
        m_motor.setInverted(ElevFXMotorConfig.elevMotorInvert);
        m_motor.setNeutralMode(ElevFXMotorConfig.elevNeutralMode);
        m_motor.setSelectedSensorPosition(0);                     // Reset Encoder to zero
        m_motor.configMotionSCurveStrength(configFX.motionCurveStrength); // Possible redundant in config
    }
}
