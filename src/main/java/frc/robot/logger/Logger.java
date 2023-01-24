package frc.robot.logger;

import frc.robot.Robot;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.io.File;
import java.util.Vector;
import edu.wpi.first.wpilibj.Timer;

public class Logger extends SubsystemBase {
    String fileName;
	String base = "/home/lvuser/logs/logfile";	// Files of format logfile01.csv to logfile15.csv
	String msg;  
	java.io.PrintStream outFile;
	String current, last, logDateTime;
	File currentFile, lastFile, logDateTimeFile;
	boolean bool = false;
	String line;
	Vector<String> lines = new Vector<>();
	public Timer logTimer = new Timer();
	public int logFlag = 0;
	double time, lastTime, leftLastDist, rightLastDist, avgLastDist, LogFlag;

    public Logger() {
        createFile();
		//LogFlag = 1;		// Start with detail logging turned off 1=on
		time = 0;
		lastTime = 0;
		leftLastDist = 0;
		rightLastDist = 0;
		avgLastDist = 0;
    }

    @Override
    public void periodic() {

    }

    // ------------------------------------------------------------------------
    // ------------------------- Subsystem Methods ----------------------------
    public void createFile(){
    	System.out.println("Logger Creating file");
    	// Remove log file 15 to make room
    	last = new String(base + "15.csv"); 
    	lastFile = new File(last);

		// -------- Delete oldest file 15 -----------
	    System.out.println("attempting logfile15.csv delete");
   		bool = lastFile.delete();
   		if (bool)
   		    System.out.println("logfile15.csv delete SUCCESS");
   		else
   		    System.out.println("NO logfile15.csv to delete!");
   		
		// ----------------------------------------------------------------
    	// Rename remaining files 9-1 to 10-2 (incrementing the file count)
	    System.out.println("\nRenaming files 1-15\n");
    	for (int i = 14; i > 0; i--){
    		if (i < 10) {
    			current = new String(base + "0" + i + ".csv");
    		} else {
    			current = new String(base + i + ".csv");
    		}
    		System.out.println("i =" + i + " current=" + current + "   rename to " + last);
    		currentFile = new File(current);
   			bool = currentFile.renameTo(lastFile);	// Rename files 9-1
   	   		if (bool) {
   	    		System.out.println("Renamed current file = " + current + "  to " + last);   	   			
   	   		    System.out.println(current + " rename SUCCESS");
            } else {
                      System.out.println("NO " + current + " to rename!");
            }
			//System.out.println("Logger Create file completed ! Current =" + current );
    		last = new String(current);
    		lastFile = new File(last);
			//System.out.println("Logger Create file completed ! last =" + last );
    	}
		System.out.println("Logger Create file completed ! last =" + last );
    	// Open Log file for Output
		// what happens if no file exists !!! empty folder !!!!!
    	try{  
    		// Create a PrintStream attached to a file named last log1.csv.
    		outFile = new java.io.PrintStream(last);
    		System.out.println("success in opening new log file..." + last);
			startTimer();
    	}
    	catch(Exception e){
    		System.out.println("Error in opening new log file..." + last);         	
    		System.out.println(e);
    	}
    	appendLog("****** LOG FILE ( " + last + " )    Created at " + logDateTime + " *******");
    }

    public void saveLogFile(){
    	System.out.println("Logger printlog function called"); 
		System.out.println("  ----------- Lines in log file = "+ lines.size());

		// Write out individual lens to file
    	for (String element : lines) {
			//System.out.println("  ----------- Line = "+ element);
			try{  
				outFile.println(element);	
			}
			catch(Exception e) {
				System.out.println("Error in saving log file..." + last);         	
				System.out.println(e);
			} 	
    	}
		// Flush out all remaing line to disk
		try{ 
			System.out.println("  ----------- Trying to flush out file buffer");
			outFile.flush();    	// Flush all buffered data to the file.
		}
		catch(Exception e) {
			System.out.println("Error in flushing log file..." + last);         	
			System.out.println(e);
		}
		lines.clear();
		logFlag = 0;			// Show that there is nothing in buffer	
    }

    public void startTimer(){
    	logTimer.reset();
    	logTimer.start();
    }

    public void appendLog(String s){
    	logFlag = 1;						// we have log entry in buffer
    	time = Robot.sysTimer.get();
    	lines.add(String.valueOf(time)+", " + s);
    }

    public void appendLog(String s, double d){
    	logFlag = 1;						// we have log entry in buffer
    	time = Robot.sysTimer.get();
    	lines.add(String.valueOf(time)+", " + s + ", " + String.valueOf(d));
    }

    public void clearLog()				{  	lines.clear();  }

}
