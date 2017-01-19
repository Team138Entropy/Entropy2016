/******************************************************************************
 * Filename : Shooter.java
 * Author   : Team 138
 * Date     : 2/19/2016
 *****************************************************************************/
package manipulator;

import org.usfirst.frc.team138.robot.Robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;

public class Shooter
{
	// Declare the shooter components
	Jaguar elevation_motor;
	DoubleSolenoid elevation_solenoid;
	Talon left_shooter_wheels;
	Talon right_shooter_wheels;
	Solenoid shooter_solenoid;
	
	// Define the component port numbers
	public final static int ELEVATION_MOTOR_PORT = 7;
	public final static int ELEVATION_SOLENOID_FORWARD_PORT = 0;
	public final static int ELEVATION_SOLENOID_REVERSE_PORT = 1;
	public final static int LEFT_SHOOTER_WHEELS_PORT = 8;
	public final static int RIGHT_SHOOTER_WHEELS_PORT = 9;
	public final static int SHOOTER_SOLENOID_PORT = 4;
	
	// Define desired speeds for the shooter wheel and elevation motors
	public final static double SHOOTER_WHEELS_ACQUIRE_SPEED = 0.9;
	public final static double SHOOTER_WHEELS_SHOOT_SPEED = 0.9;
	public final static double ELEVATION_MOTOR_RAISE_SPEED = 0.6;
	public final static double ELEVATION_MOTOR_LOWER_SPEED = 0.4;
	
	/**************************************************************************
	 * public Shooter()
	 * 
	 * The constructor for the Shooter class.
	 *************************************************************************/
	public Shooter()
	{
		// Instantiate the shooter components
		this.elevation_motor = new Jaguar(ELEVATION_MOTOR_PORT);
		this.elevation_solenoid = new DoubleSolenoid(ELEVATION_SOLENOID_FORWARD_PORT, ELEVATION_SOLENOID_REVERSE_PORT);
		this.left_shooter_wheels = new Talon(LEFT_SHOOTER_WHEELS_PORT);
		this.right_shooter_wheels = new Talon(RIGHT_SHOOTER_WHEELS_PORT);
		this.shooter_solenoid = new Solenoid(SHOOTER_SOLENOID_PORT);
		
		// Invert the direction of the left side shooter wheels
		this.left_shooter_wheels.setInverted(true);				
	}
	
	/**************************************************************************
	 * public void elevationPneumaticsRaise()
	 * 
	 * Extends the two pneumatic cylinders used for coarse elevation control of
	 * the shooter.
	 *************************************************************************/
	public void elevationPneumaticsRaise()
	{
		this.elevation_solenoid.set(DoubleSolenoid.Value.kForward);		
	}
	
	/**************************************************************************
	 * public void elevationPneumaticsLower()
	 * 
	 * Retracts the two pneumatic cylinders used for coarse elevation control of
	 * the shooter.
	 *************************************************************************/
	public void elevationPneumaticsLower()
	{
		this.elevation_solenoid.set(DoubleSolenoid.Value.kReverse);		
	}
	
	/**************************************************************************
	 * public void setShooterPneumatic(boolean state)
	 * 
	 * Sets the state of the pneumatic cylinder used for pushing the ball into
	 * the shooter wheels. Passing in True will extend the cylinder, passing in
	 * False will retract the cylinder.
	 * 
	 * @param state The desired state of the pneumatic cylinder
	 *************************************************************************/
	public void shooterPneumaticSet(boolean state)
	{
		this.shooter_solenoid.set(state);
	}
	
	/**************************************************************************
	 * public void elevationMotorRaise()
	 * 
	 * Raises the shooter using the elevation motor.
	 *************************************************************************/
	public void elevationMotorRaise()
	{
		// The elevation motor cannot be driven unless the elevation pneumatics are extended
		//if (getElevationPneumaticsRaised())
		//{
			this.elevation_motor.set(-1.0 * Robot.truncateMotorSpeed(ELEVATION_MOTOR_RAISE_SPEED));
		//}
	}
	
	/**************************************************************************
	 * public void elevationMotorLower()
	 * 
	 * Lowers the shooter using the elevation motor.
	 *************************************************************************/
	public void elevationMotorLower()
	{
		// The elevation motor cannot be driven unless the elevation pneumatics are extended
		//if (getElevationPneumaticsRaised())
		//{
			this.elevation_motor.set(Robot.truncateMotorSpeed(ELEVATION_MOTOR_LOWER_SPEED));
		//}
	}
	
	/**************************************************************************
	 * public void haltShooterMotor()
	 * 
	 * Stops the elevation motor.
	 *************************************************************************/
	public void elevationMotorHalt()
	{
		this.elevation_motor.set(0.0);
	}
	
	/**************************************************************************
	 * public void shooterWheelsAcquire()
	 * 
	 * Sets the shooter wheels to acquire the ball.
	 *************************************************************************/
	public void shooterWheelsAcquire()
	{
		this.left_shooter_wheels.set(Robot.truncateMotorSpeed(SHOOTER_WHEELS_ACQUIRE_SPEED));
		this.right_shooter_wheels.set(Robot.truncateMotorSpeed(SHOOTER_WHEELS_ACQUIRE_SPEED));
	}
	
	/**************************************************************************
	 * public void shooterWheelsShoot()
	 * 
	 * Sets the shooter wheels to shoot the ball.
	 *************************************************************************/
	public void shooterWheelsShoot()
	{
		this.left_shooter_wheels.set(-1.0 * Robot.truncateMotorSpeed(SHOOTER_WHEELS_SHOOT_SPEED));
		this.right_shooter_wheels.set(-1.0 * Robot.truncateMotorSpeed(SHOOTER_WHEELS_SHOOT_SPEED));		
	}
	
	/**************************************************************************
	 * public void haltShooterWheels()
	 * 
	 * Stops the shooter wheels motors.
	 *************************************************************************/
	public void shooterWheelsHalt()
	{
		this.left_shooter_wheels.set(0.0);
		this.right_shooter_wheels.set(0.0);		
	}
	
	/**************************************************************************
	 * public boolean getElevationSolenoidExtended()
	 * 
	 * Returns True if the elevation pneumatics are extended and False if they
	 * are retracted.
	 * 
	 * @return The current state of the elevation pneumatics
	 *************************************************************************/
	public boolean getElevationPneumaticsRaised()
	{
		return this.elevation_solenoid.get() == DoubleSolenoid.Value.kForward;
	}
}
