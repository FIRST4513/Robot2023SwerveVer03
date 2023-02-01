package frc.robot.intake;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IntakeSubSys extends SubsystemBase {
    
    //Devices
    private WPI_TalonFX intakeUpperMotor = new WPI_TalonFX(IntakeConfig.intakeUpperMotorCANID);
    private WPI_TalonFX intakeLowerMotor = new WPI_TalonFX(IntakeConfig.intakeLowerMotorCANID);
    private DigitalInput coneDetectSwitch = new DigitalInput(IntakeConfig.coneDetectSwitchID);
    private DigitalInput cubeDetectSwitch = new DigitalInput(IntakeConfig.cubeDetectSwitchID);

    //contructor
    public IntakeSubSys() { 
        stopMotors();
    } 

    //Methods
    public void setMotors(double speed) {
        intakeUpperMotor.set(speed);
        intakeLowerMotor.set(speed); 
    }

    public void stopMotors() {
        intakeUpperMotor.stopMotor();
        intakeLowerMotor.stopMotor();
    }

    public void setMotorsConeRetract(){
        intakeUpperMotor.set(IntakeConfig.coneRetractSpeed);
        intakeLowerMotor.set(IntakeConfig.coneRetractSpeed);
    }

    public void setMotorsCubeRetract(){
        intakeUpperMotor.set(IntakeConfig.cubeRetractSpeed);
        intakeLowerMotor.set(IntakeConfig.cubeRetractSpeed);
    }

    public void setMotorsCubeEject(){
        intakeUpperMotor.set(IntakeConfig.cubeEjectSpeed);
        intakeLowerMotor.set(IntakeConfig.cubeEjectSpeed);
    }

    public void setMotorsConeEject(){
        intakeUpperMotor.set(IntakeConfig.coneEjectSpeed);
        intakeLowerMotor.set(IntakeConfig.coneEjectSpeed);
    }

    public boolean isConeDetectSwitchPressed(){
        if(coneDetectSwitch.get() == IntakeConfig.coneDetectTrue) { return true; } 
        return false;
        }

     public boolean isCubeDetectSwitchPressed(){
        if(cubeDetectSwitch.get() == IntakeConfig.cubeDetectTrue) { return true; } 
        return false;
     }
     
}
