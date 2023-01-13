package frc.robot.limelight;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTable;

public class limelightSubSys extends SubsystemBase {
    
    String line = "";
    public static enum LLSmardashState {UPDATE, NOUPDATE};
    public static LLSmardashState lLSmardashState = LLSmardashState.UPDATE;

    private double displayCnt = 0;

    public final static int kDefaultPipeline = 0;
    public final static int kSortTopPipeline = 1;

    // limelight
    public static final double kHorizontalFOV = 59.6; // degrees
    public static final double kVerticalFOV = 49.7; // degrees
    public static final double kVPW = 2.0 * Math.tan(Math.toRadians(kHorizontalFOV / 2.0));
    public static final double kVPH = 2.0 * Math.tan(Math.toRadians(kVerticalFOV / 2.0));
    public static final double kImageCaptureLatency = 11.0 / 1000.0; // seconds
    public static final double kCameraFrameRate = 90.0;

    //private NetworkTable mNetworkTable;
    private NetworkTableInstance inst = NetworkTableInstance.getDefault();
    private NetworkTable mNetworkTable = inst.getTable("limelight");

    //public static class PeriodicIO {
    // INPUTS
    public double latency = 0;
    public int givenLedMode = 0;
    public int givenPipeline = 0;
    public double xOffset = 0;
    public double yOffset = 0;
    public double area = 0;

    // OUTPUTS
    public int ledMode = 1; // 0 - use pipeline mode, 1 - off, 2 - blink, 3 - on
    public int camMode = 0; // 0 - vision processing, 1 - driver camera
    public int pipeline = 0; // 0 - 9
    public int stream = 2; // sets stream layout if another webcam is attached
    public int snapshot = 0; // 0 - stop snapshots, 1 - 2 Hz

    //private PeriodicIO mPeriodicIO = new PeriodicIO();
    private boolean mOutputsHaveChanged = true;
    private boolean mSeesTarget = false;

    public limelightSubSys() {
    }

    @Override
    public void periodic() {
        // Put code here to be run every loop

         // This will read the network table for Limelight data Not used if no vision automation
         readPeriodicInputs();
         //writePeriodicOutputs();
         updateSmartDashboard();  

    }

   // Return Vision Target data
   public boolean  isValidVisionTarget()     { return mSeesTarget; }
   public double   getXangle()               { return xOffset; }
   public double   getYangle()               { return yOffset; }
   public double   getVisionLatency()        { return latency; }
   public double   getLatency()              { return latency; }
   public double   getArea()                 { return area; }

   //@Override
   public synchronized void readPeriodicInputs() {
       latency = mNetworkTable.getEntry("tl").getDouble(0) / 1000.0 + kImageCaptureLatency;
       givenLedMode = (int) mNetworkTable.getEntry("ledMode").getDouble(0.0);

       givenPipeline = (int) mNetworkTable.getEntry("pipeline").getDouble(0);
       xOffset = mNetworkTable.getEntry("tx").getDouble(0.0);
       yOffset = mNetworkTable.getEntry("ty").getDouble(0.0);
       area = mNetworkTable.getEntry("ta").getDouble(0.0);
       mSeesTarget = mNetworkTable.getEntry("tv").getDouble(0) == 1.0;
   }

   //@Override
   public synchronized void writePeriodicOutputs() {
       if (givenLedMode != ledMode || givenPipeline != pipeline) {
           System.out.println("Table has changed from expected, retrigger!!");
           mOutputsHaveChanged = true;
       }

       if (mOutputsHaveChanged) {
           mNetworkTable.getEntry("ledMode").setNumber(ledMode);
           mNetworkTable.getEntry("camMode").setNumber(camMode);
           mNetworkTable.getEntry("pipeline").setNumber(pipeline);
           mNetworkTable.getEntry("stream").setNumber(stream);
           mNetworkTable.getEntry("snapshot").setNumber(snapshot);
           mOutputsHaveChanged = false;
       }
   }

   // -- Pipeline Setting Control
   public synchronized void setPipeline(int mode) {
       if (pipeline != mode) {
           pipeline = mode;
           mOutputsHaveChanged = true;
           mNetworkTable.getEntry("pipeline").setNumber(pipeline);
       }
   }  

   public synchronized int getPipeline() { return pipeline; }
       

   // -- Stream Mode Control
   public synchronized void setStreamModetoStandard() {
       if (stream != 0) {
           stream = 0;
           mOutputsHaveChanged = true;
       }
   }    

   public synchronized void setStreamModetoPipMain() {
       if (stream != 1) {
           stream = 1;
           mOutputsHaveChanged = true;
       }
   }    
   public synchronized void setStreamModetoPiPSecond() {
       if (stream != 2) {
           stream = 2;
           mOutputsHaveChanged = true;
       }
   }    

   public synchronized int getStreamMode() { return stream; }



   // -- Snapshot Mode Control
   public synchronized void setSnapshotOn() {
       if (snapshot != 1) {
           snapshot = 1;
           mOutputsHaveChanged = true;
       }
   }
   public synchronized void setSnapshotOff() {
       if (snapshot != 0) {
           snapshot = 0;
           mOutputsHaveChanged = true;
       }
   }

   public synchronized int getSnapshotMode() { return snapshot; }

   
   // -- CAM Mode Control --
   public synchronized void setCamModetoVision() {
       if (camMode != 0) {
           camMode = 0;
           mOutputsHaveChanged = true;
           mNetworkTable.getEntry("camMode").setNumber(camMode);
           setPipeline(0);             // Pipeline 0
       }
   }

   public synchronized void setCamModetoDriver() {
       if (camMode != 1) {
           camMode = 1;
           mOutputsHaveChanged = true;
           mNetworkTable.getEntry("camMode").setNumber(camMode);
           setPipeline(1);             // Pipeline 0
       }
   }
   public synchronized int getCamMode() { return camMode; }


   // -- Limelight LED Cotrol Methods --
   public enum LedMode {
       PIPELINE, OFF, BLINK, ON
   }

   public synchronized void setLedOn() {
       if (ledMode != 3) {
           ledMode = 3;
           mOutputsHaveChanged = true;
           mNetworkTable.getEntry("ledMode").setNumber(ledMode);
       }
   }

   public synchronized void setLedOff() {
       if (ledMode != 1) {
           ledMode = 1;
           mOutputsHaveChanged = true;
           mNetworkTable.getEntry("ledMode").setNumber(ledMode);
       }
   }
   public synchronized void setLedBlink() {
       if (ledMode != 2) {
           ledMode = 2;
           mOutputsHaveChanged = true;
       }
   }

   public synchronized void setLedPipeline() {
       if (ledMode != 0) {
           ledMode = 0;
           mOutputsHaveChanged = true;
       }
   }

   public synchronized void setLed(LedMode mode) {
       if (mode.ordinal() != ledMode) {
           ledMode = mode.ordinal();
           mOutputsHaveChanged = true;
       }
   }


   
   public synchronized void triggerOutputs() {
       mOutputsHaveChanged = true;
   }


   private void updateSmartDashboard(){
       if ( (displayCnt % 10) != 0) {
           // only update dashboard every 200 ms
           displayCnt++;
           return;
       }
       displayCnt = 0;

    //    if (mSeesTarget == true){
    //        SmartDashboard.putString("Vision Target", "Vision Target Acquired");
    //    } else {
    //        SmartDashboard.putString("Vision Target", "NO Vision Target Acquired");
    //    }

    //    SmartDashboard.putNumber("Vision X Angle", getXangle());
    //    SmartDashboard.putNumber("Vision Y Angle", getYangle());
        SmartDashboard.putNumber("Vision Latency", getVisionLatency());
    //    SmartDashboard.putNumber("Vision Area", getArea());
       SmartDashboard.putNumber("Vision Pipline", getPipeline());

       //    LED =    PIPELINE, OFF, BLINK, ON
       if(ledMode == 0 )      {SmartDashboard.putString("Vision LED Status", "Pipeline");}
       else if(ledMode == 1 ) {SmartDashboard.putString("Vision LED Status", "OFF");}
       else if(ledMode == 2 ) {SmartDashboard.putString("Vision LED Status", "BLINK");}
       else if(ledMode == 3 ) {SmartDashboard.putString("Vision LED Status", "ON");}

       if (camMode == 0)      {SmartDashboard.putString("Vision Cam Mode", "VISION");}
       else if (camMode == 1) {SmartDashboard.putString("Vision Cam Mode", "DRIVER");}

       if (stream == 0)      {SmartDashboard.putString("Vision Stream Mode", "STANDARD");}
       else if (stream == 1) {SmartDashboard.putString("Vision Stream Mode", "PiPMain");}
       else if (stream == 2) {SmartDashboard.putString("Vision Stream Mode", "PiPSecond");}
   }

}

