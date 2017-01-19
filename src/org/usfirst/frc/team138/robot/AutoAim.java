package org.usfirst.frc.team138.robot;

import org.json.JSONArray;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import manipulator.Shooter;

public class AutoAim {
	
	VisionDataListener camera_info = VisionDataListener.getInstance();
	double x;
	double y;
	double constantX = 1;
	double constantY = (constantX * .75);
	
	public AutoAim() {
		x = camera_info.v_aim.getInt(0);
		y = camera_info.v_aim.getInt(1);
		int iterationsX = -1;
		int iterationsY = -1;
		
		if(x > 0.05)
		{
			if (iterationsX == -1)
			{
				iterationsX = (int) (constantX * x);
			}
			else if(iterationsX > 0){
				
				iterationsX--;
			}
			else
			{
				Robot.shooter.elevationMotorHalt();
				if (x > 0.04 || x < -0.04)
				{
					iterationsX--;
				}
			}
		}
		else if(x < -0.05)
		{
			
		}
		if(y > 0.04)
		{
			if (iterationsY == -1)
			{
				iterationsY = (int) (constantY * y);
			}
			else if(iterationsY > 0){
				Robot.shooter.elevationMotorLower();
				iterationsY--;
			}
			else
			{
				Robot.shooter.elevationMotorHalt();
				if (y > 0.04 || y < -0.04)
				{
					iterationsY--;
				}
			}
		}
		else if(y < -0.04)
		{
			if (iterationsY == -1)
			{
				iterationsY = (int) (constantY * -y);
			}
			else if(iterationsY > 0){
				Robot.shooter.elevationMotorRaise();
				iterationsY--;
			}
			else
			{
				Robot.shooter.elevationMotorHalt();
				if (y > 0.04 || y < -0.04)
				{
					iterationsY--;
				}
			}
		}
		
	}
}
