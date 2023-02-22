// Based on code from - Spectrum 3847
package frc.lib.gamepads;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.lib.gamepads.XboxGamepad.XboxAxis;

public class Dpad {
    public final Joystick joy;

    public Trigger Left;
    public Trigger Right;
    public Trigger Up;
    public Trigger Down;
    public Trigger DownLeft;
    public Trigger DownRight;
    public Trigger UpLeft;
    public Trigger UpRight;

    public Dpad(Joystick joystick) {
        this.joy = joystick;

        this.Up =           new Trigger(() ->(joy.getPOV() == 0));
        this.Right =        new Trigger(() ->(joy.getPOV() == 45));
        this.Right =        new Trigger(() ->(joy.getPOV() == 90));
        this.DownRight =    new Trigger(() ->(joy.getPOV() == 135));
        this.Down =         new Trigger(() ->(joy.getPOV() == 180));
        this.DownLeft =     new Trigger(() ->(joy.getPOV() == 225));
        this.Left =         new Trigger(() ->(joy.getPOV() == 270));
        this.UpLeft =       new Trigger(() ->(joy.getPOV() == 315));

    }

    public double getValue() {
        return joy.getRawAxis(XboxAxis.DPAD.value);
    }
}
