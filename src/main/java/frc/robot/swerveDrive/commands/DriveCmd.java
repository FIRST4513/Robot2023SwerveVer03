// Created by Spectrum3847
// Based on Code from Team364 - BaseFalconSwerve
// https://github.com/Team364/BaseFalconSwerve/tree/338c0278cb63714a617f1601a6b9648c64ee78d1

package frc.robot.swerveDrive.commands;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.swerveDrive.SwerveDriveSubSys;

import java.util.function.DoubleSupplier;

public class DriveCmd extends CommandBase {

    private boolean fieldRelative;
    private boolean openLoop;

    private SwerveDriveSubSys swerve;
    private DoubleSupplier fwdPositiveSupplier;
    private DoubleSupplier leftPositiveSupplier;
    private DoubleSupplier ccwPositiveSupplier;
    private Translation2d centerOfRotationMeters;

    /**
     * Creates a SwerveDrive command that allows for simple x and y translation of the robot and r
     * rotation.
     *
     * @param fieldRelative
     * @param leftPositiveSupplier
     * @param fwdPositiveSupplier
     */

    // -------------- Overloaded Constuctor -------------------
    // openLoop assumed false, center of rotation assummed (0,0)
    public DriveCmd(
            DoubleSupplier fwdPositiveSupplier,
            DoubleSupplier leftPositiveSupplier,
            DoubleSupplier ccwPositiveSupplier,
            boolean fieldRelative) {
        // call full constructor below
        this(
            fwdPositiveSupplier,
            leftPositiveSupplier,
            ccwPositiveSupplier,
            fieldRelative,
            true,  // false,
            new Translation2d());
    }

    // -------------- Overloaded Constuctor -------------------
    // center of rotation assummed (0,0)    
    public DriveCmd(
            DoubleSupplier fwdPositiveSupplier,
            DoubleSupplier leftPositiveSupplier,
            DoubleSupplier ccwPositiveSupplier,
            boolean fieldRelative,
            boolean openLoop) {
        // call full constructor below
        this(
            fwdPositiveSupplier,
            leftPositiveSupplier,
            ccwPositiveSupplier,
            fieldRelative,
            openLoop,
            new Translation2d());
    }

    // -------------- Main Full Constuctor ---------------
    public DriveCmd(
            DoubleSupplier fwdPositiveSupplier,
            DoubleSupplier leftPositiveSupplier,
            DoubleSupplier ccwPositiveSupplier,
            boolean fieldRelative,
            boolean openLoop,
            Translation2d centerOfRotationMeters) {
        this.swerve = Robot.swerve;
        addRequirements(swerve);
        this.fieldRelative = fieldRelative;
        this.openLoop = openLoop;
        this.leftPositiveSupplier = leftPositiveSupplier;
        this.fwdPositiveSupplier = fwdPositiveSupplier;
        this.ccwPositiveSupplier = ccwPositiveSupplier;
        this.centerOfRotationMeters = centerOfRotationMeters;
    }


    // -------------- Overloaded Constuctor -------------------
    // fieldRelative assumed true
    // openLoop assumed false, center of rotation assummed (0,0)
    public DriveCmd(
        DoubleSupplier fwdPositiveSupplier,
        DoubleSupplier leftPositiveSupplier,
        DoubleSupplier ccwPositiveSupplier) {
    this(fwdPositiveSupplier, leftPositiveSupplier, ccwPositiveSupplier, true, false);
}

// -------------------- Command Methods --------------------
    public void intialize() {}

    @Override
    public void execute() {
        double fwdPositive = fwdPositiveSupplier.getAsDouble();
        double leftPositive = leftPositiveSupplier.getAsDouble();
        double ccwPositive = ccwPositiveSupplier.getAsDouble();

        // this does the work - Drive wheels as needed
        swerve.drive(
                fwdPositive,
                leftPositive,
                ccwPositive,
                fieldRelative,
                openLoop,
                centerOfRotationMeters);
    }

    public void end(boolean interrupted) {
        swerve.stop();
    }
}
