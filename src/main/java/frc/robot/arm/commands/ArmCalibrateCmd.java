package frc.robot.arm.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.arm.ArmConfig;

/*
 * Command to Recalibrate Arm Encoder
 * Step 1. Retract Arm till hits Rear Limit Switch then
 * Step 2. Reset Encoder to correct -Degrees value then
 * Step 3. Drive Arm Forward till Arm is at 0 degrees Using MM (Straight Down) then
 * Step 4. End - Stop Motors
 */

public class ArmCalibrateCmd  extends CommandBase {
    double angle = 0;
    Timer delayTimer = new Timer();

    static enum ArmState {RETRACTING, EXTENDING, DONE};
    ArmState armState = ArmState.RETRACTING;
    public String armStateStr = "";

    public ArmCalibrateCmd() {
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(Robot.arm);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        armState = ArmState.RETRACTING;
        armStateStr = "RETRACTING";
        System.out.println("Arm Calibrate command called");
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if (armState == ArmState.RETRACTING) {
            // Check If we have reached the retract limit switch
            if (Robot.arm.isRetractLimitSwitchPressed() == false) {
                // Have Not reached limit sw keep retracting
                Robot.arm.setArmMotor(ArmConfig.kLowerSpeed);
                return;
            } else {
                // We Have reached limit sw
                Robot.arm.resetEncoderAngle(ArmConfig.ExtendLimitSwitchAngle);   // Reset Encoder to Back Angle
                armState = ArmState.EXTENDING;                                  // Update State
                armStateStr = "EXTENDING";
            }
        }
        if (armState == ArmState.EXTENDING) {
            if ( Robot.arm.getArmAngle() >= 0) {
                // we have reached botttom
                armState = ArmState.DONE;                                       // Update State
                armStateStr = "DONE";
                return;                                  
            } else {
                // We havent reached bottom yet so keep on driving
                Robot.arm.setMMangle( 0.0 );             // Lets keep driving forward   
            }
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        Robot.arm.stopArm();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        if (armState == ArmState.DONE) { return true; }
        return false;
    }
}