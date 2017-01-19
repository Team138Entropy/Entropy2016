/******************************************************************************
 * Filename : Winch.java
 * Author   : Team 138
 * Date     : 2/22/2016
 *****************************************************************************/
package manipulator;

import org.usfirst.frc.team138.robot.Robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Talon;

public class Winch
{
	// Declare the winch assembly components
	Talon winch_motor;
	DoubleSolenoid scaling_solenoid;
	
	// Define the component port numbers
	public final static int WINCH_MOTOR_PORT = 6;
	public final static int SCALING_SOLENOID_FORWARD_PORT = 2;
	public final static int SCALING_SOLENOID_REVERSE_PORT = 3;
	
	/**************************************************************************
	 * public Winch()
	 * 
	 * The constructor for the Winch class.
	 *************************************************************************/
	public Winch()
	{
		// Instantiate the winch assembly components
		this.winch_motor = new Talon(WINCH_MOTOR_PORT);
		this.scaling_solenoid = new DoubleSolenoid(SCALING_SOLENOID_FORWARD_PORT, SCALING_SOLENOID_REVERSE_PORT);
	}
	
	/**************************************************************************
	 * public void setWinchMotor(double speed, boolean armUp)
	 * 
	 * Sets the speed and direction of the motor that controls the winch for the
	 * arm used to scale the tower. The "speed" input must be within the range
	 * of 0-1.
	 * 
	 * @param speed     Specifies the speed of the winch motor
	 * @param shooterUp Specifies the direction of the winch motor
	 *************************************************************************/
	public void setWinchMotor(double speed, boolean armUp)
	{
		if (armUp)
		{
			this.winch_motor.set(Robot.truncateMotorSpeed(speed));
		}
		else
		{
			this.winch_motor.set(-1.0 * Robot.truncateMotorSpeed(speed));
		}
	}
	
	/**************************************************************************
	 * public void winchMotorHalt()
	 * 
	 * Stops the winch motor.
	 *************************************************************************/
	public void winchMotorHalt()
	{
		this.winch_motor.set(0.0);
	}
	
	/**************************************************************************
	 * public void scalingPneumaticsRaise()
	 * 
	 * Extends the pneumatic cylinders used for scaling the tower.
	 *************************************************************************/
	public void scalingPneumaticsRaise()
	{
		this.scaling_solenoid.set(DoubleSolenoid.Value.kForward);			
	}
	
	/**************************************************************************
	 * public void scalingPneumaticsLower()
	 * 
	 * Retracts the pneumatic cylinders used for scaling the tower.
	 *************************************************************************/
	public void scalingPneumaticsLower()
	{
		this.scaling_solenoid.set(DoubleSolenoid.Value.kReverse);			
	}
	
	/**************************************************************************
	 * public boolean getScalingSolenoidExtended()
	 * 
	 * Returns True if the scaling solenoid is extended and False if it is
	 * retracted.
	 * 
	 * @return The current state of the scaling solenoid
	 *************************************************************************/
	public boolean getScalingPneumaticsRaised()
	{
		return this.scaling_solenoid.get() == DoubleSolenoid.Value.kForward;
	}
}
