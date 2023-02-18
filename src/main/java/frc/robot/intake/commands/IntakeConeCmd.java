package frc.robot.intake.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.intake.IntakeConfig;

public class IntakeConeCmd extends CommandBase{
    
    static enum IntakeState {EMPTY, INTAKE, TRANSIT, STAGED, INPLACE};

    public static String intakeStateStr = "Empty";

    IntakeState intakeState;

    double upperSpeed = 0.0;
    double lowerSpeed = 0.0;

    public IntakeConeCmd() {
        addRequirements(Robot.intake);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        intakeState = IntakeState.EMPTY;
        intakeStateStr = "Empty";
        System.out.println("cone command called");
        // System.out.println(intakeState);
        upperSpeed = IntakeConfig.coneRetractUpperSpeed;
        lowerSpeed = IntakeConfig.coneRetractLowerSpeed;
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // System.out.println("Executing, state = " + intakeStateStr);
        if (intakeState == IntakeState.EMPTY) {
            if (Robot.intake.isConeDetected()) {
                intakeState = IntakeState.INTAKE;
                // intakeStateStr = "Staged";
                upperSpeed = -1.0;
                lowerSpeed = 1.0;
            }
        }
        if (intakeState == IntakeState.INTAKE) {
            if (!Robot.intake.isConeDetected()) {
                intakeState = IntakeState.TRANSIT;
                // intakeStateStr = "Staged";
                upperSpeed = -0.8;
                lowerSpeed = 0.8;
            }
        }
        if (intakeState == IntakeState.TRANSIT) {
            if (Robot.intake.isConeDetected()) {
                intakeState = IntakeState.STAGED;
                // intakeStateStr = "Staged";
                upperSpeed = 0.25;
                lowerSpeed = 0.25;
            }
        }
        if (intakeState == IntakeState.STAGED) {
            if (Robot.intake.isConeDetected()) {
                upperSpeed = 0.25;
                lowerSpeed = 0.25;
            } else {
                intakeState = IntakeState.INPLACE;
                intakeStateStr = "In Place";
                upperSpeed = 0.0;
                lowerSpeed = 0.0;
                return;
            }
        }

        Robot.intake.setLowerMotor(lowerSpeed);
        Robot.intake.setUpperMotor(upperSpeed);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        Robot.intake.stopMotors();
    }

    @Override
    public boolean isFinished() {
        if (intakeState == IntakeState.INPLACE){
            System.out.println("Cone Intake Finished");
            return true;
        }
        return false;
    }

    public static String getStateString() {
        return intakeStateStr;
    }
}
