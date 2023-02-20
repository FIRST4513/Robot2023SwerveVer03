package frc.robot.elevator.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.arm.ArmConfig;
import frc.robot.elevator.ElevatorConfig;

public class ElevReleaseArmCmd extends CommandBase {
    
    Timer delayTimer = new Timer();

    // Command Constructor
    public ElevReleaseArmCmd() {
        addRequirements(Robot.elevator);
    }

    @Override
    public void initialize() {
        Robot.elevator.setMMheight(ElevatorConfig.ElevInitReleaseHt);
        delayTimer.reset();
        delayTimer.start();
    }

    @Override
    public void execute() {
        // Raise Elevator to clear arm and let fall
        Robot.elevator.setMMheight(ElevatorConfig.ElevInitReleaseHt);

        // Lets look to see when it passes the back limit switch
        // And the set counter for that position
        if (Robot.arm.isLowerLimitSwitchPressed()) {
            Robot.arm.resetEncoder(ArmConfig.lowerSoftLimitPos);
        }
    }

    @Override
    public void end(boolean interrupted) {}

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        if (Robot.elevator.getElevHeightInches() >= ElevatorConfig.ElevInitReleaseHt) { return true; }
        return false;
    }
}