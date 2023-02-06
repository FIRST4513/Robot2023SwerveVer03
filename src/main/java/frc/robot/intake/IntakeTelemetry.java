package frc.robot.intake;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.lib.telemetry.WidgetsAndLayouts;

public class IntakeTelemetry {
    protected ShuffleboardTab tab;

    public IntakeTelemetry( IntakeSubSys intake) {
        tab = Shuffleboard.getTab("Intake");
        tab.addString("Cube Detect",()-> intake.cubeDetectStatus())  .withPosition(0, 0).withSize(3, 2);
        tab.addString("Cone Detect",()-> intake.coneDetectStatus())  .withPosition(0, 3).withSize(3, 2);
        WidgetsAndLayouts.TalonFXLayout("Motor", tab, intake.intakeUpperMotor)    .withPosition(4, 0).withSize(3, 7);
        WidgetsAndLayouts.TalonFXLayout("Motor", tab, intake.intakeUpperMotor)    .withPosition(8, 0).withSize(3, 7);
    }
}
