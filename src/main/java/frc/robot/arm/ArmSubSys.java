package frc.robot.arm;

import java.util.function.DoubleSupplier;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.AnalogInput;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.Rmath;
import frc.robot.Robot;
import frc.robot.RobotConfig.AnalogPorts;
import frc.robot.RobotConfig.LimitSwitches;
import frc.robot.RobotConfig.Motors;

public class ArmSubSys extends SubsystemBase {
    public  static ArmSRXMotorConfig    motorConfig;
    public  WPI_TalonSRX                mArmMotor;
    private DigitalInput                extendLimitSwitch;
    private DigitalInput                retractLimitSwitch;
    // public  AnalogInput                 armAbsoluteAngleSensor;

    // Arm Variables
    public double mCurrArmPwr       = 0;
    public double mTargetArmAngle   = 0;       // PID Target Angle

    public double mCurrEncoderCnt   = 0; 
    public double mCurrArmAngle     = 0;
    // public double mCurrAbsoluteArmAngle = 0;

    public static enum ArmStates { STOPPED, RELAX, RUNNING }
    public ArmStates armState;

    public static String armBrakeStatus = "";
    // public double hold_pwr = 0;

    // ------------- Constructor ----------
    public ArmSubSys() {
        motorConfig                 = new ArmSRXMotorConfig();
        mArmMotor                   = new WPI_TalonSRX(Motors.armMotorID);
        extendLimitSwitch           = new DigitalInput(LimitSwitches.armExtendLimitSw);
        retractLimitSwitch          = new DigitalInput(LimitSwitches.armRetractLimitSw);
        // armAbsoluteAngleSensor      = new AnalogInput(AnalogPorts.armAngleSensor);
        armMotorConfig();
        stopArm();
        armState = ArmStates.STOPPED;
        setBrakeMode(true);
        mArmMotor.configForwardSoftLimitThreshold(mCurrArmAngle);
    }

    // ------------- Periodic -------------
    public void periodic() {
        updateCurrentArmPosition();
        if (isRetractLimitSwitchPressed() == true) { resetEncoderAngle(ArmConfig.RetractLimitSwitchAngle); }
        if (isExtendLimitSwitchPressed() == true)  { resetEncoderAngle(ArmConfig.ExtendLimitSwitchAngle); }
        mCurrArmPwr = mArmMotor.get();  // not sure if this works?
        driveMotor();
    }

    // -------------------------------------------------
    // --------------- Main Drive Method ---------------
    // -------------------------------------------------
    public void driveMotor() {
        // if arm is in stopped state, stop motor
        if (armState == ArmStates.STOPPED) {
            stopMotor();
            setBrakeMode(false);
            return;
        }
        // if arm is has target to go to and needs to, check conditions
        if ((armState == ArmStates.RUNNING) || (armState == ArmStates.RELAX)) {
            // first, make sure angle is capped/limited
            mTargetArmAngle = limitArmAngle(mTargetArmAngle);
            // next, check for inside conditions
            if (isArmInside()) {
                // stop motor if elevator is down, set to "relax" state
                if (Robot.elevator.mCurrElevHt < 5) {
                    setArmState(ArmStates.RELAX);
                    return;
                }
                // move arm if elevator is high enough, set to normal running state
                else {
                    setArmState(ArmStates.RUNNING);
                    driveMotorByMM();
                    return;
                }
            }
            // else, check for outside conditions
            else {
                setArmState(ArmStates.RUNNING);  // arm outside robot, make sure state is normal running not relax
                // if target is inside robot
                if (mTargetArmAngle < 0) {
                    // check if elevator is too low, stop/"relax" arm
                    if (Robot.elevator.mCurrElevHt < 5) {
                        setArmState(ArmStates.RELAX);
                        return;
                    }
                    // otherwise, elevator is high enough, drive arm back
                    else {
                        driveMotorByMM();
                        return;
                    }
                }
                // target outside robot, drive arm
                driveMotorByMM();
                return;
            }
        }
    }

    // ---------------------------------------------
    // --------------- Motor Methods ---------------
    // ---------------------------------------------
    private void stopMotor() {
        mArmMotor.stopMotor();
    }

    private void driveMotorByMM() {
        // System.out.println("ARM - Moving arm by Motion Magic");
        setBrakeMode(true);
        mArmMotor.set( ControlMode.MotionMagic,         convertAngleToCnt(mTargetArmAngle),
                       DemandType.ArbitraryFeedForward, getHoldPwr());
    }

    // ---------------------------------------------
    // --------------- State Methods ---------------
    // ---------------------------------------------
    public void setArmState(ArmStates newArmState) {
        if (newArmState == ArmStates.STOPPED) { stopMotor(); }
        else if (newArmState == ArmStates.RELAX) { stopMotor(); }
        else if (newArmState == ArmStates.RUNNING) { }  // do nothing, this does not have to be here but is :)
        armState = newArmState;
    }

    // to be called by other subsystems to set arm to a stopped state without possible setArmState() confusion
    public void stopArm() {
        setArmState(ArmStates.STOPPED);
    }

    public String getArmState() {
        if (armState == ArmStates.STOPPED) { return "STOPPED"; }
        else { return "RUNNING"; }
    }

    // ----------------------------------------------------
    // --------------- Motion Magic Methods ---------------
    // ----------------------------------------------------
    public void holdArmMM() {
        setMMTargetAngle(mCurrArmAngle);
    }

    public void adjustMMTarget(double adjustAmount) {
        double newTarget = mTargetArmAngle + adjustAmount;
        newTarget = limitArmAngle(newTarget);
        mTargetArmAngle = newTarget;
    }

    public void adjustMMTarget(DoubleSupplier adjustAmount) {
        adjustMMTarget(adjustAmount.getAsDouble());
    }

    public void setMMTargetAngle(double angle) {
        setArmState(ArmStates.RUNNING);  // set state to running in order to allow drive method to run arm if possible
        angle = limitArmAngle( angle );     // Limit range to max allowed
        mTargetArmAngle = angle;
    }

    public void setMMTargetAngle(DoubleSupplier angle) {
        setMMTargetAngle(angle.getAsDouble());
    }

    public boolean isMMtargetReached(){
        // If we are within the deadband of our target we can stop
        if (Math.abs(mTargetArmAngle-mCurrArmAngle) <= ArmConfig.KAngleDeadBand) { return true; }
        return false;
    }

    // --------------------------------------------------
    // --------------- Brake Mode Methods ---------------
    // --------------------------------------------------
    public void setBrakeMode(Boolean enabled) {
        if (enabled) {
            mArmMotor.setNeutralMode(NeutralMode.Brake);
            armBrakeStatus = "Arm Brake On";
        } else {
            mArmMotor.setNeutralMode(NeutralMode.Coast);
            armBrakeStatus = "Arm Brake Off";
        }
    }

    public String getBrakeStatus() {
        return armBrakeStatus;
    }

    // ----------------------------------------------------
    // --------------- Limit Switch Methods ---------------
    // ----------------------------------------------------
    public boolean isRetractLimitSwitchPressed() {
        if (retractLimitSwitch.get() == ArmConfig.RetractLimitSwitchTrue) { return true; }
        return false;
    }

    public boolean isExtendLimitSwitchPressed() {
        if (extendLimitSwitch.get() == ArmConfig.ExtendLimitSwitchTrue)   { return true; }
        return false;
    }

    public String retractLimitSwitchStatus(){
        if (retractLimitSwitch.get() == ArmConfig.RetractLimitSwitchTrue) { return "Pressed"; }
        return "Not Pressed";
    }

    public String extendLimitSwitchStatus() {
        if (extendLimitSwitch.get() == ArmConfig.ExtendLimitSwitchTrue)   { return "Pressed"; }
        return "Not Pressed";
    }

    // -----------------------------------------------
    // --------------- Set Soft Limits ---------------
    // -----------------------------------------------
    public void softLimitsTrue() {
        mArmMotor.configReverseSoftLimitEnable(true);   // ????????????
        mArmMotor.configForwardSoftLimitEnable(true);   // ????????????
    }

    public void softLimitsFalse() {
        mArmMotor.configReverseSoftLimitEnable(false);  // ?????????????
        mArmMotor.configForwardSoftLimitEnable(false);  // ?????????????
    }

    // -----------------------------------------------
    // --------------- Encoder Methods ---------------
    // -----------------------------------------------
    public void updateCurrentArmPosition() {
        // Called from Periodic so only 1 CAN call is needed per command loop 20ms
        mCurrEncoderCnt = mArmMotor.getSelectedSensorPosition();
        mCurrArmAngle = Rmath.mRound((convertCntToAngle(mCurrEncoderCnt)) , 1);
        // mCurrAbsoluteArmAngle = getAbsoluteArmAngle() ;
     }

    public double getEncoderCnt()   { return mCurrEncoderCnt; }
    public double getArmAngle()     { return mCurrArmAngle;   }

    public double convertAngleToCnt( double angle )   { return angle * ArmConfig.kCntsPerDeg; }
    public double convertCntToAngle( double cnt )     { return cnt * ArmConfig.kDegsPerCnt; }


    public void resetEncoder()                    { mArmMotor.setSelectedSensorPosition(0); }
    public void resetEncoder( double position )   { mArmMotor.setSelectedSensorPosition(position); }
    public void resetEncoderAngle( double angle ) { mArmMotor.setSelectedSensorPosition(convertAngleToCnt(angle)); }

    // --------------------------------------------------
    // --------------- Other Misc Methods ---------------
    // --------------------------------------------------
    public double getTargetAngle()         { return mTargetArmAngle; }
    public double getArmMotorPwr()         { return mCurrArmPwr; }

    public double limitArmAngle( double angle ){
        if (angle > ArmConfig.ExtendLimitSwitchAngle)       { angle = ArmConfig.ExtendLimitSwitchAngle; }
        if (angle < ArmConfig.RetractLimitSwitchAngle)      { angle = ArmConfig.RetractLimitSwitchAngle; }
        return angle;
    }

    public boolean isArmInside() {
        if (getArmAngle() < 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isArmOutside() { return !isArmInside(); }

    // some possible unnecessary methods
    public boolean isArmExtendedPast(double angle) {
        if (mCurrArmAngle > angle) { return true; }
        return false;
    }

    public boolean isArmRetractedPast(double angle) {
        if (mCurrArmAngle < angle) { return true; }
        return false;
    }

    public boolean isArmWithin(double deadband) {
        if (Math.abs(mCurrArmAngle - mTargetArmAngle) < deadband) {
            return true;
        }
        return false;
    }

    public double getHoldPwr(){
        double pwr = Math.sin(Math.toRadians(mCurrArmAngle)) * motorConfig.arbitraryFeedForwardScaler;
        if (mCurrArmAngle > 0) {
            pwr *= 0.95;
        }
        return pwr;
    }

    // ---------------------------------------------------
    // --------------- Configure Arm Motor ---------------
    // ---------------------------------------------------
    public void armMotorConfig(){
        // This config is for the Talon SRX Controller
        mArmMotor.configFactoryDefault();
        mArmMotor.configAllSettings(ArmSRXMotorConfig.config);
        mArmMotor.setInverted(ArmSRXMotorConfig.armMotorInvert);
        mArmMotor.setSensorPhase(ArmSRXMotorConfig.armEncoderInvert);
        mArmMotor.setNeutralMode(ArmSRXMotorConfig.armDefaultNeutralMode);
        mArmMotor.setSelectedSensorPosition(0); // Zero Encoder
        mArmMotor.enableCurrentLimit(true);
    }

    // commented code from old sub sys structure
        // -----------------------------------------------------
    // ---------------- Arm Motor Methods ------------------
    // -----------------------------------------------------
    // public void raiseArm() { setArmMotor(ArmConfig.kDefaultExtendPwr); }
    // public void lowerArm() { setArmMotor(ArmConfig.kDefaultRetractPwr); }

    // public void holdArm() {
    //      //setMMangle(mCurrArmAngle);  // TEST
    //     if ( Math.abs(mCurrArmAngle) < 8.0) {
    //         stopArm();
    //         return;
    //     }
    //     mCurrArmPwr = getHoldPwr();
    //     // mArmMotor.set(mCurrArmPwr);
    // }

    // ------------  Set Arm Manually during TeleOP  ----------
    // public void setArmMotor( double pwr ) {

    //     // Limit power if needed
    //     if ( pwr > ArmConfig.kExtendMaxPwr)  { pwr = ArmConfig.kExtendMaxPwr; }
    //     if ( pwr < ArmConfig.kRetractMaxPwr) { pwr = ArmConfig.kRetractMaxPwr; }

    //     if ( pwr > 0) {
    //         // Were extending so check if Extend Limit Switch has been hit
    //         if (isExtendLimitSwitchPressed()) {
    //             // resetEncoderToAbsolute();     // Reset Arm motor encoder to absolute
    //             holdArm();
    //             return;
    //         } else {
    //             if (mCurrArmAngle < -75) {
    //                 stopArm();
    //                 return;
    //             }
    //             // OK to move arm
    //             // mArmMotor.set(pwr);
    //             mCurrArmPwr = pwr;
    //             return;
    //         }
    //     }
        
    //     // Were retracting check if Retract Lower Limit Switch has been hit
    //     if (isRetractLimitSwitchPressed()) {
    //         // resetEncoderToAbsolute();     // Initialize Arm motor encoder to absolute
    //         //holdArm();            // We can back drive to -90 degrees
    //         //return;
    //     }
        
    //     if (getArmAngle() >= ArmConfig.ArmAngleStorePos) {
    //         // We are retracting and have Not gone past -45 degrees All OK
    //         // mArmMotor.set(pwr);
    //         mCurrArmPwr = pwr;
    //         return;
    //     }

    //     // at this point, we're coming past (less than) the store point

    //     if (Robot.elevator.getElevHeightInches() < 13.0) {
    //         // Were trying to go past -45 degrres and elevator is down
    //         holdArm();            // We can back drive to -90 degrees when elev is down
    //         return;
    //     }
        
    //     if ((getArmAngle() <= -85.0) && (Robot.elevator.getElevHeightInches() < 2.0)) {
    //         // We have parked Arm
    //         stopArm();
    //         return;
    //     }

    //     if (getArmAngle() <= -90) {
    //         holdArm();
    //         return;
    //     }

    //     // we're driving to parked position (no other conditions are met)
    //     // mArmMotor.set(pwr);
    //     mCurrArmPwr = pwr;
    // }

    // public void setArmMotor(DoubleSupplier pwr) {
    //     setArmMotor(pwr.getAsDouble());
    // }
 
    // -------- Set Brake Mode ----------


    // ------------------------------------------------------------
    // ---------------- Arm Limit Switch Methods ------------------
    // ------------------------------------------------------------
    




    // -------------------------------------------------------
    // ---------------- Arm Encoder Methods ------------------
    // -------------------------------------------------------
    




    // ------------ Absolute Arm Angle Encoder Methods -----------
    // public double getAbsoluteArmAngle(){
    //     // Convert from 0 to -360
    //     // Output :  Arm Down = 0 degree
    //     //          Arm Fully retracted to -90 degree
    //     //          Arm Fully Extended  to +90 degree
    //     // 
    //     double angle = getAbsoluteArmAngleRaw(); 
    //     angle += ArmConfig.kabsoluteAngleOffset;
    //     if (angle < -180)   { angle += 360.0; }     
    //     if (angle > +180 )  { angle -= 360.0; }     // Convert 
    //     angle = Rmath.mRound( angle , 2);
    //     return angle;
    // }
    // public double getAbsoluteArmVolt(){
    //     return armAbsoluteAngleSensor.getAverageVoltage();
    // }

    // public double getAbsoluteArmAngleRaw(){
    //     // Convert absolute encoder Volts 0 to +3.3 volts to 0 to -360 degrees (CW)
    //     double volts = armAbsoluteAngleSensor.getAverageVoltage();
    //     double angle = volts / ArmConfig.kAnalogVoltsToDegree;
    //     return angle;
    // }
    
    // public void resetEncoderToAbsolute() {
    //     // recalibrate encoder to absolut Encoder value
    //     resetEncoderAngle(getAbsoluteArmAngle());
    // }  

    // --------------- Misc Methods ------------------


    // -------------------------------------------------------
    // ---------------- Configure Arm Motor ------------------
    // -------------------------------------------------------
}

// -3317, -5.3 code, -2 arm
// 66314, 106.1 code, 84 arm
// 69631 dif, 86 arm dif
//  

// ground intake elev 0 arm 16