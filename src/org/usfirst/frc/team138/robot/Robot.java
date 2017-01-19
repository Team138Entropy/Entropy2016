/******************************************************************************
 * Filename : Robot.java
 * Author   : Team 138
 * Date     : 2/17/2016
 *****************************************************************************/
package org.usfirst.frc.team138.robot;

import autonomous.Autonomous;
import drivetrain.Drivetrain;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import manipulator.Shooter;
import manipulator.Winch;

import org.usfirst.frc.team138.robot.VisionDataListener;

/******************************************************************************
 * JOYSTICK AXIS MAPPING
 * 
 * NYKO JOYSTICKS
 *   Left Joystick X => Axis #0, Max Left => -1.00, Max Right => 1.00
 *   Left Joystick Y => Axis #1, Max Up   => -1.00, Max Down  => 1.00
 *   Right Joystick X => Axis #3, Max Left => -1.00, Max Right => 1.00
 *   Right Joystick Y => Axis #2, Max Up   => -1.00, Max Down  => 1.00
 * 
 * XBOX360 JOYSTICKS
 *   Left Joystick X => Axis #0, Max Left => -1.00, Max Right => 1.00
 *   Left Joystick Y => Axis #1, Max Up   => -1.00, Max Down  => 1.00
 *   Right Joystick X => Axis #4, Max Left => -1.00, Max Right => 1.00
 *   Right Joystick Y => Axis #5, Max Up   => -1.00, Max Down  => 1.00
 *****************************************************************************/

public class Robot extends IterativeRobot
{
	// Declare the joysticks
	public Joystick driver_joystick_left;
	public Joystick driver_joystick_right;
	public Joystick operator_joystick;
	
	// Declare the robot components
	public Winch winch;
	public static Shooter shooter;
	public static Drivetrain drivetrain;
	
	// Declare Autonomous
	Autonomous autonomous;
	
	// Enumerated Type for Input Controller Modes
	public enum ControllerType {
		Xbox_Controller (0),
		Dual_Joystick (1);
		
		private int Controller_Mode;
		
		private ControllerType(int initConfig) {
			Controller_Mode = initConfig;
		}
	};
	
	public ControllerType Controller_Config = ControllerType.Xbox_Controller;

	// Define the joystick ports
	public final static int DRIVER_JOYSTICK_XBOX_USB_PORT = 0;
	public final static int OPERATOR_JOYSTICK_USB_PORT = 1;
	public final static int DRIVER_JOYSTICK_LEFT_USB_PORT = 2;
	public final static int DRIVER_JOYSTICK_RIGHT_USB_PORT = 3;
	
	// Define drive constants
	public final static double JOYSTICK_DEADBAND = 0.05;
	public final static double SPEED_SCALING = 0.8;
	public final static double TURNING_SCALING = 0.8;	
	
	// Define the xbox joystick axes
	public final static int DRIVER_LEFT_STICK_X_AXIS = 0;
	public final static int DRIVER_LEFT_STICK_Y_AXIS = 1;
	public final static int DRIVER_LEFT_TRIGGER = 2;
	public final static int DRIVER_RIGHT_TRIGGER = 3;
	public final static int DRIVER_RIGHT_STICK_X_AXIS = 4;
	public final static int DRIVER_RIGHT_STICK_Y_AXIS = 5;
	
	// Define the NYKO joystick axes
	public final static int OPERATOR_LEFT_STICK_X_AXIS = 0;
	public final static int OPERATOR_LEFT_STICK_Y_AXIS = 1;
	public final static int OPERATOR_RIGHT_STICK_X_AXIS = 3;
	public final static int OPERATOR_RIGHT_STICK_Y_AXIS = 2;
	
	// Tower scaling variables
	boolean startScalingPhaseOne = false;
	boolean scalingPhaseOneComplete = false;
	boolean startScalingPhaseTwo = false;
	boolean scalingPhaseTwoComplete = false;
	boolean hookHeld = false;
	boolean shooterHeld = false;
	boolean hookState = false;
	boolean shooterState = true;	
	int beltMotorCounter = 0;
	int winchMotorCounter = 0;
	int firingWheelsCounter = 0;	
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit()
    {
    	// Instantiate the joysticks
    	
    	if (Controller_Config == ControllerType.Xbox_Controller) 
    	{
    		this.driver_joystick_left = new Joystick(DRIVER_JOYSTICK_XBOX_USB_PORT);
    	}
    	else if (Controller_Config == ControllerType.Dual_Joystick)
    	{
    		this.driver_joystick_left  = new Joystick(DRIVER_JOYSTICK_LEFT_USB_PORT);
    		this.driver_joystick_right = new Joystick(DRIVER_JOYSTICK_RIGHT_USB_PORT); 
    	}
    	
    	this.operator_joystick = new Joystick(OPERATOR_JOYSTICK_USB_PORT);
    	
    	// Instantiate the robot components
    	drivetrain = new Drivetrain();
    	shooter = new Shooter();
    	this.winch = new Winch();
    	
    	// Instantiate the Autonomous
    	this.autonomous = new Autonomous();
    	
    }
    
    /**************************************************************************
     * AUTONOMOUS MODE FUNCTIONS
     *************************************************************************/
    public void autonomousInit()
    {
    	// TBD
    	// Remember to look back at default code for how to choose auto program
    	// from SmartDashboard
    	autonomous.Init();
    	this.winch.scalingPneumaticsLower();
    }
    
    public void autonomousPeriodic()
    {
    	autonomous.Update();
    }
    
    /**************************************************************************
     * TELE-OPERATED MODE FUNCTIONS
     *************************************************************************/
    public void teleopInit()
    {
    	
    }
    
	public void teleopPeriodic()
    {
    	Scheduler.getInstance().run();

		SmartDashboard.putNumber("Left Encoder:", Robot.drivetrain.leftEncoderGet());
		SmartDashboard.putNumber("Right Encoder:", Robot.drivetrain.rightEncoderGet());
		    	
		//Arcade Drive
		drivetrain.driveRobot(driver_joystick_left.getRawAxis(1), driver_joystick_right.getRawAxis(0));
		//drivetrain.driveRobot(-0.5, 0.0);
    	
    	//Tank Drive
//		drivetrain.driveTank(compensateForDeadband(SPEED_SCALING * driver_joystick_left.getRawAxis(1)),
//				compensateForDeadband(SPEED_SCALING * driver_joystick_right.getRawAxis(1)));
		
    	// Shooter elevation control
    	if ((this.driver_joystick_left.getRawButton(13) || this.operator_joystick.getRawButton(8)) && !shooterHeld)
    	{
    		shooterHeld = true;
    		shooterState = !shooterState;
    		if (shooterState) {
				shooter.elevationPneumaticsRaise();
			}
			else 
			{
				shooter.elevationPneumaticsLower();
			}
    	} 
    	else if (!(this.driver_joystick_left.getRawButton(13) || this.operator_joystick.getRawButton(8)))
    	{
    		shooterHeld = false;
    	}
    	if (this.operator_joystick.getRawAxis(OPERATOR_RIGHT_STICK_Y_AXIS) > JOYSTICK_DEADBAND)
    	{
    		shooter.elevationMotorLower();
    	}
    	else if (this.operator_joystick.getRawAxis(OPERATOR_RIGHT_STICK_Y_AXIS) < (-1.0 * JOYSTICK_DEADBAND))
    	{
    		shooter.elevationMotorRaise();
    	}
    	else
    	{
    		shooter.elevationMotorHalt();
    	}
    	
    	//
    	// Shooter wheels control
    	//    	
    	if (this.operator_joystick.getRawButton(7))
    	{
    		shooter.shooterWheelsShoot();
    		if (firingWheelsCounter == 150) 
    		{
                shooter.shooterPneumaticSet(this.operator_joystick.getRawButton(5));
    		}
    		else
    		{
    			firingWheelsCounter++;
    			shooter.shooterPneumaticSet(false);
    		}
    	}
    	else if (this.driver_joystick_left.getRawButton(1) || this.operator_joystick.getRawButton(4))
    	{
    		firingWheelsCounter = 0;
    		shooter.shooterWheelsAcquire();
    	}
    	else
    	{
    		if (firingWheelsCounter > 0)
    		{
    			firingWheelsCounter--;
    		}
    		shooter.shooterPneumaticSet(this.operator_joystick.getRawButton(5));
    		shooter.shooterWheelsHalt();
    	}		
		
		// Tower scaling controls
    	if (this.driver_joystick_left.getRawButton(3) && this.operator_joystick.getRawButton(3) && !scalingPhaseOneComplete)
    	{
    		startScalingPhaseOne = true;
    	}
    	else if (this.driver_joystick_left.getRawButton(2) && this.operator_joystick.getRawButton(2))// && scalingPhaseOneComplete && !scalingPhaseTwoComplete)
    	{
    		startScalingPhaseTwo = true;
    	}
    	
    	// Emergency stop for automated tower scaling
    	if (this.driver_joystick_left.getRawButton(1) || this.operator_joystick.getRawButton(1))
    	{
    		startScalingPhaseOne = false;
    		startScalingPhaseTwo = false;
    		scalingPhaseOneComplete = false;
    		scalingPhaseTwoComplete = false;
    		
    		this.winch.winchMotorHalt();
    		shooter.elevationMotorHalt();
    	}
    	
    	if (startScalingPhaseOne)
    	{
    		this.winch.scalingPneumaticsRaise();
    		
    		if (beltMotorCounter < 75)
    		{
    			shooter.elevationMotorLower();
    			beltMotorCounter++;
    		}
    		else
    		{
    			shooter.elevationMotorHalt();
    		}
    		
    		if (winchMotorCounter < 200)
    		{
				// Let the winch cable out
				this.winch.setWinchMotor(0.8, false);
				winchMotorCounter++;
    		}
    		else
    		{
    			this.winch.winchMotorHalt();
    			shooter.elevationPneumaticsRaise();
    			startScalingPhaseOne = false;
    			scalingPhaseOneComplete = true;
    			winchMotorCounter = 0;
    		}
    	}
    	
    	if (startScalingPhaseTwo)
    	{
    		if (winchMotorCounter < 300)
    		{
    			// Pull the winch cable in
    			this.winch.setWinchMotor(1.0, true);
    			winchMotorCounter++;
    			
    			if (winchMotorCounter == 100)
    			{
    				this.winch.scalingPneumaticsLower();
    				shooter.elevationPneumaticsLower();
    			}
    		}
    		else
    		{
    			this.winch.winchMotorHalt();
    			startScalingPhaseTwo = false;
    			scalingPhaseTwoComplete = true;
    		}
    	}
    	
    	SmartDashboard.putBoolean("Hook Position Raised", this.winch.getScalingPneumaticsRaised());
    	
    	if (!startScalingPhaseOne && !startScalingPhaseTwo)
    	{
    		if (operator_joystick.getRawAxis(OPERATOR_LEFT_STICK_Y_AXIS) > JOYSTICK_DEADBAND)
    		{
    			// Scale the tower
    			if (operator_joystick.getRawAxis(OPERATOR_LEFT_STICK_Y_AXIS) < 0.8)
    			{
    			
    				this.winch.setWinchMotor(0.4, true);
    			}
    			else
    			{
    				this.winch.setWinchMotor(0.7, true);
    			}
    		}
    		else if (operator_joystick.getRawAxis(OPERATOR_LEFT_STICK_Y_AXIS) < (-1.0 * JOYSTICK_DEADBAND))
    		{
    			// Reset the tower scaling mechanism

    				this.winch.setWinchMotor(0.4, false);
    				
    		}
    		else
    		{
    			this.winch.winchMotorHalt();
    		}
    	
    		if (operator_joystick.getRawButton(6) && !hookHeld)
    		{
    			hookHeld = true;
    			hookState = !hookState;
    			if (hookState) {
    				this.winch.scalingPneumaticsRaise();
    			}
    			else 
    			{
    				this.winch.scalingPneumaticsLower();
    			}
    		}
    		else if (!operator_joystick.getRawButton(6))
    		{
    			hookHeld = false;
    		}
    	}
    }
    
    /**************************************************************************
     * TEST MODE FUNCTIONS
     *************************************************************************/
    public void testInit() {}
    public void testPeriodic() {}
    
    /**************************************************************************
     * DISABLED MODE FUNCTIONS
     *************************************************************************/
    public void disabledInit() {}
    public void disabledPeriodic() {}
    
    /**************************************************************************
     * public void spawnDataGramListener()
     * 
     * TBD Description
     *************************************************************************/
    public void spawnDataGramListener()
    {
    	VisionDataListener theListener = VisionDataListener.getInstance();
    	theListener.initialize();
    	// Spawn off the VisionDataListener as a thread
    	(new Thread(theListener)).start();
    }
    
    /**************************************************************************
     * private double compensateForDeadband(double joystick_position)
     * 
     * Compensates for the deadband in the values returned from the joystick.
     * 
     * @param joystick_position The joystick value to adjust
     * @return                  The adjusted joystick value
     *************************************************************************/
    private double compensateForDeadband(double joystick_position)
    {
    	if(joystick_position < JOYSTICK_DEADBAND && joystick_position > (-1.0 * JOYSTICK_DEADBAND))
    	{
    		joystick_position = 0.0;
    	}
    	
    	return joystick_position;
    }
    
    /**************************************************************************
	 * private double truncateMotorSpeed(double speed)
	 * 
	 * Takes in a motor speed and truncates it to within the 0.0 to 1.0 range.
	 * 
	 * @param speed The speed to truncate
	 * @return      The truncated speed
	 *************************************************************************/
	public static double truncateMotorSpeed(double speed)
	{
		if (speed > 1.0)
		{
			speed = 1.0;
		}
		if (speed < 0.0)
		{
			speed = 0.0;
		}
		return speed;
	}
}
