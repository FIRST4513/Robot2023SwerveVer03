package frc.robot.intake.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;

public class IntakeConeMainCmd extends CommandBase{
    
    static enum IntakeState {EMPTY, INTAKE, TRANSIT, STAGED, INPLACE};
    IntakeState intakeState;

    // Timer stallTimer = new Timer(); 

    public static String intakeStateStr = "Empty";

    double upperSpeed = 0.0;
    double lowerSpeed = 0.0;

    public IntakeConeMainCmd() {
        addRequirements(Robot.intake);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        intakeState = IntakeState.EMPTY;
        intakeStateStr = "EMPTY";
        System.out.println("cone command called");
        // System.out.println(intakeState);
        upperSpeed = -1.0;      // Pull it in strongly
        lowerSpeed =  1.0;      // Pull it in strongly
        // stallTimer.reset();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if (intakeState == IntakeState.EMPTY) {
            // Were waiting for the cone to make its first appearance
            if (Robot.intake.isConeDetected()) {
                // Its here, so keep pulling it through hard
                intakeState = IntakeState.INTAKE;
                intakeStateStr = "INTAKE";
                upperSpeed = -1.0;
                lowerSpeed =  1.0;
                System.out.println("Empty -> Intake");
                // stallTimer.reset();     // Lets start a timer in case the cone gets stuck and never makes it forward
                // stallTimer.start();
            }
        }
        if (intakeState == IntakeState.INTAKE) {
            // Keep on pulling it through
            if (!Robot.intake.isConeDetected()) {
                // The cone has now passed through on the low side, keep pulling
                intakeState = IntakeState.TRANSIT;
                intakeStateStr = "TRANSIT";
                upperSpeed = -0.9;  // -0.8;
                lowerSpeed =  0.9;   // 0.8;
                System.out.println("Intake -> Transit");
            }
        }
        if (intakeState == IntakeState.TRANSIT) {
            // Keep on pulling till we see it on the top side
            if (Robot.intake.isConeDetected()) {
                // We see it on the top now. Lets slow a little 
                intakeState = IntakeState.STAGED;
                intakeStateStr = "STAGED";
                upperSpeed = -0.3;
                lowerSpeed =  0.3;
                System.out.println("Transit -> Staged");
            }
        }
        if (intakeState == IntakeState.STAGED) {
            // Keep on pulling until it moves past sensor 
            if ( !Robot.intake.isConeDetected() ) {
                // The Cone is clear time to stop it hard
                intakeState = IntakeState.INPLACE;
                intakeStateStr = "INPLACE";
                Robot.intake.setBrakeMode(true);    // Will be turned back off on Eject
                upperSpeed = 0.0;
                lowerSpeed = 0.0;
                System.out.println("Staged -> Inplace");
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
        if (intakeState == IntakeState.INPLACE) { return true; }
        // if (stallTimer.get() > 5.0)             { return true; } // We should have staged by now
        return false;
    }

    public static String getStateString() {
        return intakeStateStr;
    }
}
