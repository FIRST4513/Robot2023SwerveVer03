// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.elevator.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.elevator.ElevatorConfig;

public class ZeroElevator extends CommandBase {
    /** Creates a new ZeroElevator. */
    public ZeroElevator() {
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(Robot.elevator);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        // Turn off soft limits
        Robot.elevator.softLimitsFalse();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // Set elevator to slowly lower
        Robot.elevator.elevSetSpeed(ElevatorConfig.zeroSpeed);
        //Robot.elevator.zeroElevator();
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        // Set elevator position to zero
        // enable soft limits
        //Robot.elevator.resetSensorPosition(-500);
        Robot.elevator.softLimitsTrue();
        Robot.elevator.setMMheight(0);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
