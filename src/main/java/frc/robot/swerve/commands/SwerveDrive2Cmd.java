package frc.robot.swerve.commands;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.swerve.Swerve;

public class SwerveDrive2Cmd extends CommandBase {

    private boolean fieldRelative;
    private boolean openLoop;

    private Swerve swerve;
    private Double fwdPositiveMPS;
    private Double leftPositiveMPS;
    private Double ccwPositiveRPS;
    private Translation2d centerOfRotationMeters;

    // -------------- Constuctor ---------------
    public SwerveDrive2Cmd( Double fwdPositiveMPS,
                            Double leftPositiveMPS,
                            Double ccwPositiveRPS,
                            boolean fieldRelative,
                            boolean openLoop,
                            Translation2d centerOfRotationMeters) {
        this.swerve = Robot.swerve;
        addRequirements(swerve);

        this.fieldRelative = fieldRelative;
        this.openLoop = openLoop;
        this.fwdPositiveMPS = fwdPositiveMPS;
        this.leftPositiveMPS = leftPositiveMPS;
        this.ccwPositiveRPS = ccwPositiveRPS;
        this.centerOfRotationMeters = centerOfRotationMeters;
    }

    // -------------------- Command Methods --------------------
    public void intialize() {
    }

    @Override
    public void execute() {
        swerve.setBrakeMode(true);
        swerve.drive(
            fwdPositiveMPS,
            leftPositiveMPS,
            ccwPositiveRPS,
            fieldRelative,
            openLoop,
            centerOfRotationMeters);
    }

    public void end(boolean interrupted) {
        swerve.stop();
    }
}
