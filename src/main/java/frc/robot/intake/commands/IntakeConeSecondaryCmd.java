package frc.robot.intake.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;

public class IntakeConeSecondaryCmd extends CommandBase {
    static enum IntakeState {EMPTY, TRANSIT, INPLACE};
    IntakeState intakeState;

    double upperSpeed = 0.0;
    double lowerSpeed = 0.0;

    public IntakeConeSecondaryCmd() {
        addRequirements(Robot.intake);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        intakeState = IntakeState.EMPTY;
        System.out.println("Intake Cone Secondary Command Called");
        upperSpeed = -1.0;      // Pull it in strongly
        lowerSpeed =  1.0;      // Pull it in strongly
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // see nothing, see it, see nothing
        // full power, possible slow down, then stop
        if (intakeState == IntakeState.EMPTY) {
            if (Robot.intake.isConeDetected()) {
                intakeState = IntakeState.TRANSIT;
                upperSpeed = -1.0;
                lowerSpeed =  1.0;
                System.out.println("Empty -> Transit");
            }
        }
        if (intakeState == IntakeState.TRANSIT) {
            // Keep on pulling till we see it on the top side
            if (Robot.intake.isConeDetected()) {
                // We see it on the top now. Lets slow a little 
                intakeState = IntakeState.INPLACE;
                upperSpeed = -0;
                lowerSpeed =  0;
                Robot.intake.setBrakeMode(true);    // Will be turned back off on Eject
                System.out.println("Transit -> Inplace");
            }
        }

        Robot.intake.setLowerMotor(lowerSpeed);
        Robot.intake.setUpperMotor(upperSpeed);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        Robot.intake.stopMotors();
        System.out.println("STOPPED");
    }

    @Override
    public boolean isFinished() {
        if (intakeState == IntakeState.INPLACE) { return true; }
        // if (stallTimer.get() > 5.0)             { return true; } // We should have staged by now
        return false;
    }
}
