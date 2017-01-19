package autonomous;

import org.usfirst.frc.team138.robot.Robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Rotate_State extends Template_State {
	
	boolean direction;
	double angle;
	double difference;
	double scale_factor = 0.005;
	double right_speed;
	double modified_speed;
	
	/*
	 direction
	 true = left
	 false = right
	 */
	
	public Rotate_State(double target_rotation_speed, double target_angle) {
		this.angle = target_angle;
		
		if (target_angle > (Robot.drivetrain.azimithAngleGet() + 180) % 360) {
			direction = true;
			right_speed = target_rotation_speed;
			modified_speed = -1.0 * target_rotation_speed;
		}
		else {
			direction = false;
			right_speed = -1.0 * target_rotation_speed;
			modified_speed = target_rotation_speed;
		}
		
	}
	
	boolean Update() {
		if((Robot.drivetrain.azimithAngleGet() <= angle - 360 && direction) || Robot.drivetrain.azimithAngleGet() >= angle && !direction) {
			Robot.drivetrain.drive(0, 0);
			
			SmartDashboard.putNumber("Left Encoder:", Robot.drivetrain.leftEncoderGet());
			SmartDashboard.putNumber("Right Encoder:", Robot.drivetrain.rightEncoderGet());
			
			return true;
		}
		else {
			Robot.drivetrain.autoDrive(modified_speed, right_speed);
			
			// difference returns + if left is going faster than right
			difference = Math.abs(Robot.drivetrain.leftEncoderGet()) - Math.abs(Robot.drivetrain.rightEncoderGet());
		
			if (right_speed > 0)
			{
				modified_speed = -right_speed + (difference * scale_factor);
			}
			else
			{
				modified_speed = -right_speed - (difference * scale_factor);
			}
			
			SmartDashboard.putNumber("Angle", Robot.drivetrain.azimithAngleGet());
			SmartDashboard.putNumber("Left Encoder:", Robot.drivetrain.leftEncoderGet());
			SmartDashboard.putNumber("Right Encoder:", Robot.drivetrain.rightEncoderGet());
			
			return false;
		}
		
	}
	
}
