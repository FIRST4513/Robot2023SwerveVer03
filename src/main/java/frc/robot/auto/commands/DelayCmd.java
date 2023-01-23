package frc.robot.auto.commands;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;


public class DelayCmd extends CommandBase {
    private double m_timeOut;
    Timer delayTimer = new Timer();

    public DelayCmd(double timeOut) {
        m_timeOut = timeOut;
    }

    @Override
    public void initialize() {
        delayTimer.reset();
        delayTimer.start();
        System.out.println("---------- delayCmd Has Started");
    }

    @Override
    public void execute() {
    }

    @Override
    public void end(boolean interrupted) {
    }

     @Override
    public boolean isFinished() {
        if (delayTimer.get() > m_timeOut) { System.out.println("---------- delayCmd Has Ended"); return true; }
        return false;
    }

    @Override
    public boolean runsWhenDisabled() {
        return false;
    }
}
