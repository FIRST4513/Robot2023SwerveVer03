package frc.lib.util;

import java.lang.Math;

public class Rmath {	

	// ***************************************************
	//	              Hdg Routines
	// ***************************************************
	
	// Returns Hdg (0->360) from (+ -) angle/Yaw
	public static double convYawToHdg(double yaw)		{ return constrainDeg0To360(yaw + 360); }
	public static double convAngleToHdg(double angle)	{ return constrainDeg0To360(angle + 360); }

	// Returns Yaw (+ -) 180 from hdg (0->360)
	public static double convHdgToYaw(double Hdg)		{ return constrainDeg0To180(Hdg); }
	public static double convHdgToAngle(double hdg)		{ return constrainDeg0To180(hdg); }	

	// Constraint routines
	public static double constrainDeg0To360(double deg){
		while (deg >= 360) 	{ deg -= 360; }
		while (deg < 0)		{ deg += 360; }
		return deg;
	}
	public static double constrainDeg0To180(double deg){
		return constrainDeg0To360(deg + 180) - 180;
	}
	
	// Heading Add / Subtract routines
	public static double addAngleToHdg(double hdg1, double angle){
		// Returns new Hdg (0-360) from sum of hdg & angle
		return (constrainDeg0To360(hdg1 + angle));
	}
	public static double calcHdgSum(double hdg1, double hdg2){
	    // ********** Calculate new hdg from old heading and angle **************
		// Returns new Hdg 0-360 angle between 2 headings
	    double hdgSum;
	    hdgSum = hdg1 + hdg2;
	    hdgSum = constrainDeg0To360(hdgSum);
	    return(hdgSum);
	}
	
	public static double calcHdgDiff(double hdg1, double hdg2){
		// ********** Calculate difference in 2 compass hdgs **************
		// Returns angle between 2 headings
		double hdgDiff;
	    if (hdg2 >= hdg1)
	        hdgDiff = hdg2 - hdg1;
	    else
	        hdgDiff = (hdg2 - hdg1) + 360;
	    hdgDiff = constrainDeg0To180(hdgDiff);
	    return(hdgDiff);
	}


	// ***************************************************
	//	              Math Routines
	// ***************************************************
	public static double mRound(double nmbr, int digits){
		// Returns rounded nmbr to no of digits
		int mult = 1;
		if (digits == 0) mult = 1;
		else if (digits == 1) mult = 10;
		else if (digits == 2) mult = 100;
		else if (digits == 3) mult = 1000;
		else if (digits == 4) mult = 10000;
		else if (digits == 5) mult = 100000;
		else if (digits == 6) mult = 1000000;
		else if (digits == 7) mult = 10000000;
		else if (digits == 8) mult = 100000000;
		else if (digits == 9) mult = 1000000000;
		return (double) Math.round(nmbr * mult) / mult;
	}

	public static int mGetQuad(double angle){
		// Returns Quad 1-4 from any angle
		int quad = 0;
		while (angle < 0) angle += 360;				// Make sure angle is converted to (0-359.999999)
		while (angle >= 360) angle -= 360;

	    if ((angle > 0) && (angle <= 90))
	        quad = 1;
	    else if ((angle > 90) && (angle <= 180))
	        quad = 2;
	    else if ((angle > 180) && (angle <= 270))
	        quad = 3;
	    else 				//	(angle > 270)
	        quad = 4;
		return quad;
	}

	public static double mQuad(double a, double b, double c){
		// Calculates the quadratic formula
		double x;
	    if (a == 0) a = .0000000001;
	    x = (-b + Math.sqrt(b*b - (4 * a * c))) / (2 * a);
	    return(x);
	}

	// **** Trig Functions *******
	public static double cnvrtDegToRad(double degree){
		return Math.toRadians(degree);
	}

	public static double cnvrtRadToDeg(double radian){
		return Math.toDegrees(radian);
	}

	public static double mCos(double angle){
		// Calculates the cos of an angle, Take in an angle in degrees and returns a cos value
		double x;
		if ((angle == 90) || (angle == 270)){
			// Python didn't return exactly zero .. just carry over
		    x = 0;
		}
		else{
		    x = Math.cos(cnvrtDegToRad(angle));
		}
		return x;
	}

	public static double mSin(double angle){
		// Calculates the sin of an angle, Takes in an angle in degrees and returns a sin value
		double x;
		if (angle == 180){
			// pyton fails to give zero just close to zero
		      x = 0;
		} else {
			x = Math.sin(cnvrtDegToRad(angle));
		}
	    return (x);
	}

	public static double mTan(double angle){
		// Calculates the tan of an angle, Takes in an angle and returns a tan to the angle
		double x;
		if ((angle >= 89.999999) && (angle <= 90.000001)){
			// avoid return infinity/error
		    x = Math.tan(cnvrtDegToRad(89.99999));
		} else if ((angle >= 179.999999) && (angle <= 180.000001)) {
			// python fails to give zero just close to zero
		    x = 0;
		} else {
	        x = Math.tan(cnvrtDegToRad(angle));
		}
		return x;
	}

	public static double mAtan(double opo, double adj){
		// Calculates the Atan of a ratio (opo/adj) or (rise/run)
		// Takes in two sides  of a triangle and returns a radius and angle value
		double angle = 0;
		if ((adj == 0) && (opo >= 0))
				return (90);
		else if ((adj == 0) && (opo <0))
			return (270);

		if 	((opo >= 0) && (adj >= 0)){
			// Quad 1
			angle = cnvrtRadToDeg(Math.atan(opo/adj));
		}
		else if ((opo >= 0) && (adj <= 0)){
			// Quad 2
			angle = cnvrtRadToDeg(Math.atan(opo/adj)) + 180;
		}
		else if ((opo <= 0) && (adj <= 0)){
			// Quad 3
			angle = cnvrtRadToDeg(Math.atan(opo/adj)) + 180;
		}else if ((opo <= 0) && (adj >= 0)){
			// Quad 4
			angle = cnvrtRadToDeg(Math.atan(opo/adj)) + 360;
		}
		return (angle);
	}

}
