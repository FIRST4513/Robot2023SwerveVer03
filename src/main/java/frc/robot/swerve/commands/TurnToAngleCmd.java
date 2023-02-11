//Created by Spectrum3847
package frc.robot.swerve.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.ProfiledPIDCommand;
import frc.robot.Robot;

public class TurnToAngleCmd extends ProfiledPIDCommand {

  public static double kP = 0.01;
  public static double kI = 0; // 0.00015
  public static double kD = 0.000; // 0.0005

  double fwdSpeedMPS =  0.0;
  double leftSpeedMPS = 0.0;
  double rotSpeedRPS =  0.0;
  boolean fPOV = false;
  boolean closedLoop = true;
  Translation2d ctrOfRot = new Translation2d( 0, 0 );

  boolean hasTarget = false;

  // Basic: turn-in-place; fwd and left assumed 0
  public TurnToAngleCmd(DoubleSupplier targetAngleSupplier) {
    super(
        // The ProfiledPIDController used by the command
        new ProfiledPIDController( kP, kI, kD,
                                   new TrapezoidProfile.Constraints(360, 1080)),
                                  Robot.swerve::getGyroYawDegrees,     // This gets the current heading
                                  targetAngleSupplier.getAsDouble(),                        // This is the target heading
                                  // This uses the output
                                  //(output, setpoint) -> Robot.swerve.useOutput(output));
                                  (output, setpoint) -> Robot.swerve.drive( 0.0, 0.0, output, false, false,
                                                                             new Translation2d( 0.0, 0.0) )
    );

    addRequirements(Robot.swerve);

    // Configure additional PID options by calling `getController'

    double differance = targetAngleSupplier.getAsDouble() - Robot.swerve.getGyroYawDegrees();
    if (differance > 180) {
      differance = (360 - differance) * -1;
    }
    getController().setTolerance(1);    // Tolerence is 1 degree accuracy
    getController().enableContinuousInput(0, 360);
    getController().setGoal(differance);

  }

  // More advanced: assisted/auto angle, but manual moving
  public TurnToAngleCmd(
                        DoubleSupplier fwdSpeedSupplier,
                        DoubleSupplier leftSpeedSupplier,
                        DoubleSupplier targetAngleSupplier) {
    super(
        // The ProfiledPIDController used by the command
        new ProfiledPIDController( kP, kI, kD,
                                   new TrapezoidProfile.Constraints(360, 1080)),
                                  Robot.swerve::getGyroYawDegrees,     // This gets the current heading
                                  // () -> Robot.swerve.getGyroYawDegrees(),
                                  targetAngleSupplier.getAsDouble(),                        // This is the target heading
                                  // This uses the output
                                  //(output, setpoint) -> Robot.swerve.useOutput(output));
                                  (output, setpoint) -> Robot.swerve.drive( fwdSpeedSupplier.getAsDouble(),
                                                                            leftSpeedSupplier.getAsDouble(),
                                                                            output, false, false,
                                                                            new Translation2d( 0.0, 0.0) )
    );

    addRequirements(Robot.swerve);

    // Configure additional PID options by calling `getController'

    double differance = targetAngleSupplier.getAsDouble() - Robot.swerve.getGyroYawDegrees();
    if (differance > 180) {
      differance = (360 - differance) * -1;
    }
    getController().setTolerance(1);    // Tolerence is 1 degree accuracy
    getController().enableContinuousInput(0, 360);
    getController().setGoal(differance);

  }

  @Override
  public void initialize() {
    super.initialize();
  }

  @Override
  public void execute() {
    super.execute();
    Robot.swerve.drive( 0.0,
                        0.0,
                        0.0,
                        true,
                        false,
                        new Translation2d( 0.0, 0.0));
  }

  @Override
  public void end(boolean interrupted) {
    super.end(interrupted);
    Robot.swerve.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return getController().atGoal();
  }
}
