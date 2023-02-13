package frc.robot.arm.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;

public class ArmDelay extends CommandBase {
    private double safePercent;
    private double finalPercent;
    private double conditionalPos;
    /**
     * Creates a new FourBarDelay. FourBar will move to safePos, wait for Elevator to be at
     * conditionalPos, then move to finalPos
     *
     * @param safePos FourBar position that won't hit anything
     * @param finalPos position that FourBar will go to after Elevator is at conditionalPos
     * @param conditionalPos position that Elevator must be at before FourBar will move to finalPos
     */
    public ArmDelay(double safePercent, double finalPercent, double conditionalPos) {
        // Use addRequirements() here to declare subsystem dependencies.
        this.safePercent = safePercent;
        this.finalPercent = finalPercent;
        this.conditionalPos = conditionalPos;
        addRequirements(Robot.arm);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        // may need to be in execute
        Robot.arm.setMMPercent(safePercent);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if (Robot.arm.getEncoderCnt() < Robot.arm.percentToFalcon(safePercent)) {
            Robot.arm.setMMPercent(safePercent);
        }
        if (Robot.elevator.getElevEncoderCnt() >= conditionalPos) {
            Robot.arm.setMMPercent(finalPercent);
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {}

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
