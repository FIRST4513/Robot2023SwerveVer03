package frc.robot.intake.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;

public class ConeIntakeCmd extends CommandBase{
    
    static enum IntakeState {EMPTY, STAGED, INPLACE};

    IntakeState intakeState;

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        intakeState = IntakeState.EMPTY;
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if (intakeState == IntakeState.EMPTY) {
            if (Robot.intake.isConeDetected()) {
                intakeState = IntakeState.STAGED;
            } else {
                Robot.intake.setMotorsConeRetract();
            }
        }
        if (intakeState == IntakeState.STAGED) {
            if (Robot.intake.isConeDetected()) {
                Robot.intake.setMotorsConeRetractSlow();
            } else {
                intakeState = IntakeState.INPLACE;
            }
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        Robot.intake.stopMotors();
    }

    @Override
    public boolean isFinished() {
        if (intakeState == IntakeState.INPLACE){
            return true;
        }
        return false;
    }

}
