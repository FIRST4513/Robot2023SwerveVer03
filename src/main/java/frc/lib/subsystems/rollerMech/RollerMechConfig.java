// Created by Spectrum3847

package frc.lib.subsystems.rollerMech;

import edu.wpi.first.math.util.Units;
import frc.lib.subsystems.MotorFXSubsystemConfig;

public abstract class RollerMechConfig extends MotorFXSubsystemConfig {
    // Physical Constants
    public double diameterInches = 2;
    public double gearRatio = 1;

    public double maxRPM = 6000;

    /* Motor Characterization Values */
    public double kS = 0;
    public double kV = 0;
    public double kA = 0;

    public double getDiameterMeters() {
        return Units.inchesToMeters(diameterInches);
    }

    public double getCirumferrenceInches() {
        return diameterInches * Math.PI;
    }

    public double getCircumferenceMeters() {
        return getDiameterMeters() * Math.PI;
    }

    public RollerMechConfig(String name) {
        super(name);
        updateTalonFXConfig();
    }
}
