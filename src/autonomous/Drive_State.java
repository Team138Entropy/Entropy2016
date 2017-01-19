package autonomous;

import org.usfirst.frc.team138.robot.Robot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drive_State extends Template_State {
	
	double distance;
	double right_speed;
	double modified_speed;
	double difference;
	double scale_factor = 0.005;
	
	double lastRightDistance = 0.0;
	double lastLeftDistance = 0.0;
	int stallCounter = 0;
	boolean areMotorsStalled = false;
	
	public Drive_State(double input_speed, double input_distance)
	{
		this.distance = input_distance;
		this.right_speed = input_speed;
		this.modified_speed = input_speed;
	}

	boolean Update()
	{	
		if (areMotorsStalled) 
		{
			Robot.drivetrain.drive(0.0, 0.0);
			return false;
		}
		else
		{
			if ((Math.abs(Robot.drivetrain.leftEncoderGet()) + Math.abs(Robot.drivetrain.rightEncoderGet())) / 2 >= distance )
			{
				Robot.drivetrain.drive(0.0, 0.0);
				Robot.drivetrain.resetEncoders();
			
				SmartDashboard.putNumber("Left Encoder:", Robot.drivetrain.leftEncoderGet());
				SmartDashboard.putNumber("Right Encoder:", Robot.drivetrain.rightEncoderGet());
			
				return true;
			}		
			else
			{
				Robot.drivetrain.autoDrive(modified_speed, right_speed);
			
				difference = Robot.drivetrain.leftEncoderGet() - Robot.drivetrain.rightEncoderGet();
			
				if (right_speed > 0)
				{
					modified_speed = right_speed - (difference * scale_factor);
				}
				else
				{
					modified_speed = right_speed + (difference * scale_factor);
				}		
				
				if (lastRightDistance == Robot.drivetrain.rightEncoderGet() || lastLeftDistance == Robot.drivetrain.leftEncoderGet()) 
				{
					if (stallCounter == 75) 
					{
						areMotorsStalled = true;
					}
					stallCounter++;
				}
				else
				{
					stallCounter = 0;
				}
				
				lastRightDistance = Robot.drivetrain.rightEncoderGet();
				lastLeftDistance = Robot.drivetrain.leftEncoderGet();
				
				SmartDashboard.putNumber("Distance", (Math.abs(Robot.drivetrain.leftEncoderGet()) + Math.abs(Robot.drivetrain.rightEncoderGet())) / 2);
				SmartDashboard.putNumber("Left Speed:", modified_speed);
				SmartDashboard.putNumber("Right Speed:", right_speed);
				SmartDashboard.putNumber("Left Encoder:", Robot.drivetrain.leftEncoderGet());
				SmartDashboard.putNumber("Right Encoder:", Robot.drivetrain.rightEncoderGet());
			
				return false;
			}
		}
	}
}