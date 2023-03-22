package frc.robot.swerve;

import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.SuppliedValueWidget;
import frc.robot.Robot;

import java.util.Map;

public class SwerveTelemetry {
    protected ShuffleboardTab tab;
    private Swerve swerve;
    String Mod0Name;
    String Mod1Name;
    String Mod2Name;
    String Mod3Name;

    public SwerveTelemetry(Swerve swerve) {
        this.swerve = swerve;
        tab = Shuffleboard.getTab("Swerve");
        tab.addNumber("Heading from Pose", () -> swerve.odometry.getHeading().getDegrees()).withPosition(12, 0).withSize(2, 1);
        tab.addNumber("Odometry X", () -> swerve.getPoseMeters().getX()).withPosition(12, 1).withSize(2, 1);
        tab.addNumber("Odometry Y", () -> swerve.getPoseMeters().getY()).withPosition(12, 2).withSize(2, 1);
        tab.addNumber("Gyro Yaw", () -> swerve.getGyroYawDegrees()).withPosition(12, 3).withSize(2, 1);

        tab.addNumber("Gyro Incline Raw",   () -> swerve.gyro.getGyroInclineRaw()).withPosition(12, 4).withSize(2, 1);
        tab.addNumber("Gyro Incline Angle", () -> swerve.gyro.getGyroInclineAngle()).withPosition(12, 5).withSize(2, 1);

        tab.addNumber("Pilot Input X", () -> Robot.pilotGamepad.getDriveFwdPositive()).withPosition(2,10).withSize(2, 1);
        tab.addNumber("Pilot Input Y", () -> Robot.pilotGamepad.getDriveLeftPositive()).withPosition(2,11).withSize(2, 1);

        Mod0Name = swerve.mSwerveMods[0].getName();
        Mod1Name = swerve.mSwerveMods[1].getName();
        Mod2Name = swerve.mSwerveMods[2].getName();
        Mod3Name = swerve.mSwerveMods[3].getName();

        moduleLayout(Mod0Name, 0, tab).withPosition(1, 0).withSize(4, 3);
        moduleLayout(Mod1Name, 1, tab).withPosition(5, 0).withSize(4, 3);
        moduleLayout(Mod2Name, 2, tab).withPosition(1, 3).withSize(4, 3);
        moduleLayout(Mod3Name, 3, tab).withPosition(5, 3).withSize(4, 3);
    }

    public void testMode() {
        moduleLayout(Mod0Name, 0, tab).withPosition(1, 0);
        moduleLayout(Mod1Name, 1, tab).withPosition(2, 0);
        moduleLayout(Mod2Name, 2, tab).withPosition(3, 0);
        moduleLayout(Mod3Name, 3, tab).withPosition(4, 0);
    }

    public void logModuleStates(String key, SwerveModuleState[] states) {
       // Robot.log.logger.recordOutput(key, states);
    }

    public void logModuleAbsolutePositions() {
        // for (SwerveModule mod : swerve.mSwerveMods) {
        //      Robot.log.logger.recordOutput(
        //            "Mod " + mod.moduleNumber + " Absolute", mod.getAbsoluteAngle().getDegrees());
        // }
    }

    public ShuffleboardLayout moduleLayout(String name, int moduleNum, ShuffleboardTab tab) {
        ShuffleboardLayout modLayout = tab.getLayout(name, BuiltInLayouts.kGrid);
        // m_mod0Layout.withSize(1, 2);
        modLayout.withProperties(Map.of("Label position", "TOP"));

        // mod Cancoder raw angle
        SuppliedValueWidget<Double> modCanAbsRawWidget =
        modLayout.addNumber(
                "CAN ABS Raw",
                () -> swerve.mSwerveMods[moduleNum].getCanCoder().getDegrees());
        modCanAbsRawWidget.withPosition(0, 0).withSize(1, 2);

        // mod Cancoder angle with offset
        SuppliedValueWidget<Double> modCanAngleWithOffsetWidget =
        modLayout.addNumber(
                "CAN With Offset",
                () -> swerve.mSwerveMods[moduleNum].getCanCoderDegreesWithOffset());
        modCanAngleWithOffsetWidget.withPosition(2, 0).withSize(1, 2);

        // mod falcon angle
        SuppliedValueWidget<Double> modFalconAngleWidget =
        modLayout.addNumber(
                "Falcon Angle",
                () -> swerve.mSwerveMods[moduleNum].getFalconAngle().getDegrees());
        modFalconAngleWidget.withPosition(0, 1).withSize(1, 2);

        // mod target angle
        SuppliedValueWidget<Double> modTargetFalconAngle =
        modLayout.addNumber(
                "Target Angle",
                () -> swerve.mSwerveMods[moduleNum].getTargetAngle().getDegrees());
        modTargetFalconAngle.withPosition(2, 1).withSize(1, 2);

        // mod wheel velocity
        SuppliedValueWidget<Double> modWheelVelocity =
        modLayout.addNumber(
                "Wheel Vel.",
                () -> swerve.mSwerveMods[moduleNum].getDriveVelocityMPS());
        modWheelVelocity.withPosition(0, 2).withSize(1, 2);

        // mod wheel target velocity
        SuppliedValueWidget<Double> modWheelTargetVelocity =
        modLayout.addNumber(
                "Wheel Tgt. Vel.",
                () -> swerve.mSwerveMods[moduleNum].getState().speedMetersPerSecond);
        modWheelTargetVelocity.withPosition(2,2).withSize(1, 2);

        return modLayout;
    }

    public void update(){
        //LiveWindow.disableAllTelemetry();
        //update();

    }
}
