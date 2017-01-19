package autonomous;

import org.usfirst.frc.team138.robot.Robot;

public class Elevation_State extends Template_State{

	boolean elevation;
	
	public Elevation_State(boolean target_elevation){
		this.elevation = target_elevation; 
	}
	
	boolean Update() {
		if (elevation) {
			Robot.shooter.elevationPneumaticsRaise();
		}
		else {
			Robot.shooter.elevationPneumaticsLower();
		}
		
		return true;
	}

}
