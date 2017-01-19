package autonomous;

import org.usfirst.frc.team138.robot.Robot;

public class Elevation_Motor_State extends Template_State{

	// True = lower
	// False = raise
	boolean inOut;
	
	public Elevation_Motor_State(boolean inOrOut) {
		this.inOut = inOrOut;
	}
	
	boolean Update() {
		if (inOut) 
		{
			Robot.shooter.elevationMotorLower();
		}
		else
		{
			Robot.shooter.elevationMotorRaise();
		}
		
		return true;
	}

}
