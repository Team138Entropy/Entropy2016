package autonomous;

import org.usfirst.frc.team138.robot.Robot;

public class Shoot_State extends Template_State {

	int iterations;
	
	public Shoot_State() {
		iterations = -1;
		
	}
	
	boolean Update() {
		iterations++;
		if (iterations < 100) {
			Robot.shooter.shooterWheelsShoot();
			return false;
		}
		else if (iterations < 125) {
			Robot.shooter.shooterPneumaticSet(true);
			return false;
		}
		else {
			Robot.shooter.shooterPneumaticSet(false);
			Robot.shooter.shooterWheelsHalt();
			return true;
		}
	}

}
