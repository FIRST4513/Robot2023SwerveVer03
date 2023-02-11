//Created by Spectrum3847
package frc.robot.auto.commands;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.swerve.commands.SwerveDrive2Cmd;

// Wait 5 seconds
// Then drive forward at 0.2 MPS
// Stop after 1.5 seconds of motion

public class TaxiSimplePCmdGrp extends ParallelCommandGroup {
  /** Creates a new TestPathFollowing. */
  public TaxiSimplePCmdGrp() {
    double fwdSpeedMPS = 0.2;
    double leftSpeedMPS = 0.0;
    double rotSpeedRPS = 0.0;
    boolean fPOV = false;
    boolean closedLoop = true;
    Translation2d ctrOfRot = new Translation2d( 0, 0);
    
    addCommands(
      new WaitCommand(5),
      new SwerveDrive2Cmd ( fwdSpeedMPS,
                            leftSpeedMPS,
                            rotSpeedRPS,
                            fPOV,
                            closedLoop,
                            ctrOfRot)
          .withTimeout(1.5));
  }

  // ???????????
  Rotation2d finalRotation() {
    return new Rotation2d(Math.PI);
  }
}
