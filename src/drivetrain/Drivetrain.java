/******************************************************************************
 * Filename : Drivetrain.java
 * Author   : Team 138
 * Date     : 2/19/2016
 *****************************************************************************/
package drivetrain;

import com.kauailabs.navx.frc.AHRS;

//import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.CANJaguar.JaguarControlMode;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.Encoder;

public class Drivetrain
{
	
	// Declare the drive motors
//	public CANTalon Left_Motor_Leader;
//	public CANTalon Left_Motor_Follower;
//	public CANTalon Right_Motor_Leader;
//	public CANTalon Right_Motor_Follower;
	
	public CANJaguar Left_Motor_Leader;
	public CANJaguar Left_Motor_Follower;
	public CANJaguar Right_Motor_Leader;
	public CANJaguar Right_Motor_Follower;
	
	// Define the drive motor CAN ports
	public final static int LEFT_MOTOR_LEADER_CHANNEL = 2;
	public final static int LEFT_MOTOR_FOLLOWER_CHANNEL = 3;
	public final static int RIGHT_MOTOR_LEADER_CHANNEL = 4;
	public final static int RIGHT_MOTOR_FOLLOWER_CHANNEL = 5;
	
	// Define the encoder ports
	public final static int LEFT_ENCODER_PORT_A = 0;
	public final static int LEFT_ENCODER_PORT_B = 1;
	public final static int RIGHT_ENCODER_PORT_A = 3;
	public final static int RIGHT_ENCODER_PORT_B = 2;
	
	// Compensation Ratios
	public final static double COMPENSATE_SPEED_UP    = 0.99;
	public final static double COMPENSATE_SPEED_DOWN  = 0.60;
	public final static double COMPENSATE_ROTATE_UP   = 0.99;
	public final static double COMPENSATE_ROTATE_DOWN = 0.99;
	
	// Declare the encoders
	public Encoder Left_Encoder;
	public Encoder Right_Encoder;
	
	// Drive table index
	public int Drive_Table_Index_X;
	public int Drive_Table_Index_Y;
	
	// Dead Zone Constant Band
	public double Controller_Dead_Zone = .05;
	
	// Declare the IMU
	AHRS IMU;
	
	// Declare the Drivetrain
	RobotDrive Drivetrain;
	
	/**************************************************************************
	 * public Drivetrain()
	 * 
	 * The constructor for the Drivetrain class.
	 *************************************************************************/
	
	public Drivetrain()
	{
		// Instantiate the drive motors
		this.Left_Motor_Leader = new CANJaguar(LEFT_MOTOR_LEADER_CHANNEL);
    	this.Left_Motor_Follower = new CANJaguar(LEFT_MOTOR_FOLLOWER_CHANNEL);
    	this.Right_Motor_Leader = new CANJaguar(RIGHT_MOTOR_LEADER_CHANNEL);
    	this.Right_Motor_Follower = new CANJaguar(RIGHT_MOTOR_FOLLOWER_CHANNEL);
    	
    	// Configure the follower motors
    	//this.Left_Motor_Follower.changeControlMode(JaguarControlMode.Follower);
    	//this.Left_Motor_Follower.set(LEFT_MOTOR_LEADER_CHANNEL);
//    	this.Right_Motor_Follower.changeControlMode(JaguarControlMode.);
//    	this.Right_Motor_Follower.set(RIGHT_MOTOR_LEADER_CHANNEL);
    	
    	
    	// Instantiate the encoders
    	this.Left_Encoder = new Encoder(LEFT_ENCODER_PORT_A, LEFT_ENCODER_PORT_B);
    	this.Right_Encoder = new Encoder(RIGHT_ENCODER_PORT_A, RIGHT_ENCODER_PORT_B);
    	this.Left_Encoder.setDistancePerPulse(0.1);
    	this.Right_Encoder.setDistancePerPulse(0.1);
    	resetEncoders();
    	
    	// Instantiate the IMU
    	this.IMU = new AHRS(SPI.Port.kMXP);
    	this.IMU.reset();
    	
    	// Instantiate the Drivetrain
    	this.Drivetrain = new RobotDrive(this.Left_Motor_Leader, this.Left_Motor_Follower, this.Right_Motor_Leader, this.Right_Motor_Follower);
	}
	
	/**************************************************************************
	 * public enum DriveMode
	 * 
	 * This enum specifies whether the drive mode is in rotate mode or radius mode
	 * 
	 * 
	 * 
	 *
	 *************************************************************************/
	private enum DriveMode {
	
		Rotate_Mode (0),
		Radius_Mode (1);
		
		private int Current_Mode;
		
		private DriveMode(int input_Mode) {
			setCurrent_Mode(input_Mode);
		}

		public void setCurrent_Mode(int current_Mode) {
			Current_Mode = current_Mode;
		}	
			
	};
	
	/**************************************************************************
	 * public void drive(double forward_speed, double rotation_speed)
	 * 
	 * Takes in a forward speed and a rotation speed and sets the robots motors
	 * to move at those speeds using the arcadeDrive WPILib class. The inputs
	 * must be in the range of -1.0 to 1.0 or they will be truncated to fit that
	 * range.
	 * 
	 * -1.00 is full reverse and full <left/right>
	 * 1.00 is full forward and full <left/right>
	 * 
	 * @param forward_speed   The desired robot forward speed
	 * @param rotation_speed  The desired robot rotation speed
	 *************************************************************************/
	public void drive(double forward_speed, double rotation_speed)
	{
		this.Drivetrain.arcadeDrive(-1.0 * truncate_speed(forward_speed),
				                    -1.0 * truncate_speed(rotation_speed));
	}
	
	public void driveTank(double left_Track, double right_Track) 
	{
		this.Drivetrain.tankDrive(-1.0 * truncate_speed(left_Track), -1.0 * truncate_speed(right_Track));
		
	}
	
	public void autoDrive(double left_speed, double right_speed) 
	{
		this.Drivetrain.setLeftRightMotorOutputs(left_speed, right_speed);
	}
	
	public void driveRobot(double input_Speed, double input_Rotate)
	{
		// Motor Speeds on both the left and right sides
		double Left_Motor_Speed  = 0;
		double Right_Motor_Speed = 0;
		
		// Filter input speed
		input_Speed  = truncate_speed(input_Speed);
		input_Rotate = truncate_speed(input_Rotate);
		
		input_Speed = applyDeadZone(input_Speed);
		input_Rotate = applyDeadZone(input_Rotate);
		
//		Left_Motor_Speed  = Update_Speed_Left(input_Rotate, input_Speed, DriveMode.Rotate_Mode);
//		Right_Motor_Speed = Update_Speed_Right(input_Rotate, input_Speed, DriveMode.Rotate_Mode);
		
		Left_Motor_Speed  = left_scale(input_Rotate, input_Speed, DriveMode.Rotate_Mode);
		Right_Motor_Speed = right_scale(input_Rotate, input_Speed, DriveMode.Rotate_Mode);
				
		//this.Drivetrain.tankDrive(Left_Motor_Speed, Right_Motor_Speed);
		this.Drivetrain.setLeftRightMotorOutputs( Left_Motor_Speed, 0.96 * Right_Motor_Speed);
	}
	
	/**************************************************************************
	 * applyDeadZone
	 * 
	 * 
	 * 
	 * 
	 * @param speed
	 * @return
	 *************************************************************************/
	public double applyDeadZone(double speed)
	{
		double final_Speed;
		
		if ( Math.abs(speed) < Controller_Dead_Zone) {
			
			final_Speed = 0;
		}
		else {
			final_Speed = speed;
		}
		return final_Speed;
	}
	
	public double left_scale(double rotateValue, double moveValue, DriveMode mode)
	{
		 Drive_Table_Index_X = 0;
		 Drive_Table_Index_Y = 0;
		double tempDrive = 0;
		int x_idx = 0;
		double absRotate = rotateValue;

		if(mode == DriveMode.Radius_Mode)
		{
			absRotate = Math.abs(rotateValue);
		}	

		get_index(moveValue, absRotate, mode);
			
		if(mode == DriveMode.Rotate_Mode)
		{
			x_idx = Drive_Table_Index_X;
		}
		else
		{
			if(rotateValue < 0)
			{
				x_idx = 32-Drive_Table_Index_X;
			}
			else
			{
				x_idx = Drive_Table_Index_X;
			}

		}

		tempDrive = EntropyDriveTable.Drive_Matrix[Drive_Table_Index_Y][x_idx];


		return tempDrive;
	}
	
	boolean range(double x, double y, double z) 
	{  
		return (((y <= x) && (x <= z)) || ((y >= x) && (x >= z)));
	}
	
	double drive_table_limit(double x, double max, double min)
	{
		if(x > max)
		{
			return max;
		}
		else if(x < min)
		{
			return min;
		}
		else
		{
			return x;
		}
	}	
	
	void get_index1(double moveValue, double rotateValue, DriveMode mode) {
		moveValue = truncate_speed(moveValue);
		Drive_Table_Index_X = (int)(rotateValue * 16) + 16;
		Drive_Table_Index_Y = (int)(moveValue * 16) + 16;		
	}
		
	void get_index(double moveValue, double rotateValue, DriveMode mode)
	{
		double rotate = 0;
		double move = 0;
		double minRotate = 0;
		double maxRotate = 0;
		final double[] arrayPtr;
        int arrayLength = 0;
		double diff1 = 0;
		double diff2 = 0;

		if(mode == DriveMode.Radius_Mode)
		{
			arrayPtr = EntropyDriveTable.Tank_Drive_Lookup;
			arrayLength = EntropyDriveTable.Tank_Drive_Lookup.length;
			minRotate = arrayPtr[16];
			maxRotate = arrayPtr[15];
		}
		else /*Rotate*/
		{
			arrayPtr = EntropyDriveTable.Drive_Lookup_X;
			arrayLength = EntropyDriveTable.Drive_Lookup_X.length;
			minRotate = arrayPtr[0];
			maxRotate = arrayPtr[arrayLength-1];
		}

		rotate = drive_table_limit(rotateValue, maxRotate, minRotate);

		for(int i = 0; i < arrayLength; i++) 
		{
			if(i+1 >= arrayLength || range(rotate, arrayPtr[i], arrayPtr[i+1]))
			{
				//Assume match found
				if((i + 1) >= arrayLength)
				{
					Drive_Table_Index_X = i;	
				}
				else
				{
					diff1 = Math.abs(rotate - arrayPtr[i]);
					diff2 = Math.abs(rotate - arrayPtr[i+1]);

					if(diff1 < diff2)
					{
						Drive_Table_Index_X = i;
					}
					else
					{
						Drive_Table_Index_X = i + 1;
					}
				}
				break;
			}
		}
		
		

		arrayLength = EntropyDriveTable.Drive_Lookup_Y.length;
		move = drive_table_limit(moveValue, EntropyDriveTable.Drive_Lookup_Y[32], EntropyDriveTable.Drive_Lookup_Y[0]);

		for( int i = 0; i < arrayLength; i++) 
		{
			if(i+1 >= arrayLength || range(move, EntropyDriveTable.Drive_Lookup_Y[i], EntropyDriveTable.Drive_Lookup_Y[i+1]))
			{
				//Assume match found
				if((i + 1) >= arrayLength)
				{
					Drive_Table_Index_Y = i;	
				}
				else
				{
					diff1 = Math.abs(move - EntropyDriveTable.Drive_Lookup_Y[i]);
					diff2 = Math.abs(move - EntropyDriveTable.Drive_Lookup_Y[i+1]);

					if(diff1 < diff2)
					{
						Drive_Table_Index_Y = i;
					}
					else
					{
						Drive_Table_Index_Y = i + 1;
					}
				}
				break;
			}
		}
	}
	
	double right_scale(double rotateValue, double moveValue, DriveMode mode)
	{
		Drive_Table_Index_X = 0;
		Drive_Table_Index_Y = 0;
		double temp_drive = 0;
		int x_idx = 0;
		double absRotate = rotateValue;

		if(mode == DriveMode.Radius_Mode)
		{
			absRotate = Math.abs(rotateValue);
		}

		get_index(moveValue, absRotate, mode);

		temp_drive = EntropyDriveTable.Drive_Matrix[Drive_Table_Index_Y][32-Drive_Table_Index_X];

		if(mode == DriveMode.Rotate_Mode)
		{
			x_idx = 32-Drive_Table_Index_X;
		}
		else
		{
			if(rotateValue < 0)
			{
				x_idx = Drive_Table_Index_X;
			}
			else
			{
				x_idx = 32-Drive_Table_Index_X;
			}
			
		}

		
		temp_drive = EntropyDriveTable.Drive_Matrix[Drive_Table_Index_Y][x_idx];

		return temp_drive;
	}
	
	/**************************************************************************
	 * private double truncate_speed(double speed)
	 * 
	 * Takes in a speed value and truncates it to fit the -1.0 to 1.0 range.
	 * 
	 * @param speed  A double representing the speed value
	 * @return       The truncated speed value
	 *************************************************************************/
	private double truncate_speed(double speed)
	{
		if (speed > 1.0)
		{
			speed = 1.0;
		}
		if (speed < -1.0)
		{
			speed = -1.0;
		}
		
		return speed;
	}
	
	public double leftEncoderGet() {
		return Left_Encoder.getDistance();
	}
	
	public double rightEncoderGet() {
		return Right_Encoder.getDistance();
	}
	
	public void resetEncoders() {
		Left_Encoder.reset();
		Right_Encoder.reset();
	}
	
	public double azimithAngleGet() {
		return IMU.getAngle();
	}
	
}
