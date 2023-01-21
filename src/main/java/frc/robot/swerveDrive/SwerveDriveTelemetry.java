package frc.robot.swerveDrive;

import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.SuppliedValueWidget;
import java.util.Map;

public class SwerveDriveTelemetry {
    protected ShuffleboardTab tab;
    private SwerveDrive swerve;
    String Mod0Name;
    String Mod1Name;
    String Mod2Name;
    String Mod3Name;

    public SwerveDriveTelemetry(SwerveDrive swerve) {
        this.swerve = swerve;
        tab = Shuffleboard.getTab("Swerve");
        tab.addNumber("Heading", () -> swerve.getHeading().getDegrees()).withPosition(12, 0).withSize(2, 1);
        tab.addNumber("Odometry X", () -> swerve.getPoseMeters().getX()).withPosition(12, 1).withSize(2, 1);
        tab.addNumber("Odometry Y", () -> swerve.getPoseMeters().getY()).withPosition(12, 2).withSize(2, 1);

        Mod0Name = swerve.mSwerveMods[0].getName();
        Mod1Name = swerve.mSwerveMods[1].getName();
        Mod2Name = swerve.mSwerveMods[2].getName();
        Mod3Name = swerve.mSwerveMods[3].getName();

        moduleLayout(Mod0Name, 0, tab).withPosition(2, 0).withSize(3, 3);
        moduleLayout(Mod1Name, 1, tab).withPosition(5, 0).withSize(3, 3);
        moduleLayout(Mod2Name, 2, tab).withPosition(2, 3).withSize(3, 3);
        moduleLayout(Mod3Name, 3, tab).withPosition(5, 3).withSize(3, 3);
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

        // mod Cancoder Angle
        SuppliedValueWidget<Double> modCancoderAngleWidget =
                modLayout.addNumber(
                        "ABS Raw",
                        () -> swerve.mSwerveMods[moduleNum].getCANcoderAngleAbsolute());
        modCancoderAngleWidget.withPosition(0, 0).withSize(2, 2);

        // mod Cancoder Angle with Offset
        SuppliedValueWidget<Double> modCancoderAngleWOWidget =
        modLayout.addNumber(
                "ABS Angle",
                () -> swerve.mSwerveMods[moduleNum].getCANcoderAngle180());
        modCancoderAngleWOWidget.withPosition(2, 0).withSize(2, 2);

        // mod Integrated Angle
        SuppliedValueWidget<Double> modIntegratedAngleWidget =
                modLayout.addNumber(
                        "Fal Angle",
                        () -> swerve.mSwerveMods[moduleNum].getFalconAngle().getDegrees());
        modIntegratedAngleWidget.withPosition(0, 4).withSize(2, 2);
      
        // mod TargetAngle
        SuppliedValueWidget<Double> modTargetAngleWidget =
                modLayout.addNumber(
                        "Tgt Angle", () -> swerve.mSwerveMods[moduleNum].getTargetAngle());
        modTargetAngleWidget.withPosition(2, 2).withSize(2, 2);

        // mod Velocity
        SuppliedValueWidget<Double> modVelocityWidget =
                modLayout.addNumber(
                        "Fal Vel",
                        () -> swerve.mSwerveMods[moduleNum].getState().speedMetersPerSecond);
        modVelocityWidget.withPosition(2, 0).withSize(2, 2);

        return modLayout;
    }

    public void update(){
        //LiveWindow.disableAllTelemetry();
        //update();

    }
}
