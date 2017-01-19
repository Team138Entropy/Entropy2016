package autonomous;

import java.util.LinkedList;
import java.util.Queue;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Autonomous {
	
	// Starts in spy box, shoots, goes back under low bar, grabs a ball, goes under low bar, shoots
	Queue<Template_State> Super_Secret_Strategy = new LinkedList<Template_State>();
	
	// Approach the defenses, but do not cross them
	Queue<Template_State> Approach = new LinkedList<Template_State>();

	// Drive backwards under low bar and shoot
	Queue<Template_State> LowBar = new LinkedList<Template_State>();
	
	// Drive backwards over static obstacle in position 2 and shoot
	Queue<Template_State> Position2ShootB = new LinkedList<Template_State>();
	
	// Drive forwards over static obstacle in position 2 and shoot
	Queue<Template_State> Position2ShootF = new LinkedList<Template_State>();
	
	// Drive backwards over static obstacle in position 3 and shoot
	Queue<Template_State> Position3ShootB = new LinkedList<Template_State>();
	
	// Drive forwards over static obstacle in position 3 and shoot
	Queue<Template_State> Position3ShootF = new LinkedList<Template_State>();
	
	// Drive backwards over static obstacle in position 4 and shoot
	Queue<Template_State> Position4ShootB = new LinkedList<Template_State>();
	
	// Drive forwards over static obstacle in position 4 and shoot
	Queue<Template_State> Position4ShootF = new LinkedList<Template_State>();
	
	// Drive backwards over static obstacle in position 5 and shoot
	Queue<Template_State> Position5ShootB = new LinkedList<Template_State>();
	
	// Drive forwards over static obstacle in position 5 and shoot
	Queue<Template_State> Position5ShootF = new LinkedList<Template_State>();
	
	// Drive backwards under the low bar
	Queue<Template_State> LowBarNoShot = new LinkedList<Template_State>();
	
	// Drive backwards over static obstacles and NO shot
	Queue<Template_State> StaticObstaclesNoShotB = new LinkedList<Template_State>();
	
	// Drive over the moat, rock wall, etc. forwards
	Queue<Template_State> StaticObstaclesNoShotF = new LinkedList<Template_State>();
	
	// For testing only
	Queue<Template_State> Test_Queue = new LinkedList<Template_State>();
	
	// This is the queue that will hold the desired autonomous mode from the SmartDashboard
	Queue<Template_State> Execute = new LinkedList<Template_State>();
	
	// The object that displays the autonomous mode choices on the SmartDashboard
	SendableChooser autoChooser;
	
	public Autonomous()
	{
		autoChooser = new SendableChooser();
		autoChooser.addDefault("Low Bar and Shoot", LowBar);
		autoChooser.addObject("Position 2 Backwards and Shoot", Position2ShootB);
		autoChooser.addObject("Position 2 Forwards and Shoot", Position2ShootF);
		autoChooser.addObject("Position 3 Backwards and Shoot", Position3ShootB);
		autoChooser.addObject("Position 3 Forwards and Shoot", Position3ShootF);
		autoChooser.addObject("Position 4 Backwards and Shoot", Position4ShootB);
		autoChooser.addObject("Position 4 Forwards and Shoot", Position4ShootF);
		autoChooser.addObject("Position 5 Backwards and Shoot", Position5ShootB);
		autoChooser.addObject("Position 5 Forwards and Shoot", Position5ShootF);
		autoChooser.addObject("Low bar and No Shot", LowBarNoShot);
		autoChooser.addObject("Static Obstacles Backwards and No Shot", StaticObstaclesNoShotB);
		autoChooser.addObject("Static Obstacles Forwards and No Shot", StaticObstaclesNoShotF);
		autoChooser.addObject("Approach", Approach);
		SmartDashboard.putData("Autonomous Mode Chooser", autoChooser);
	}

	public void Init()
	{
		Super_Secret_Strategy.add(new Elevation_State(false));
		Super_Secret_Strategy.add(new Elevation_Motor_State(true));
		Super_Secret_Strategy.add(new Idle_State(50));
		Super_Secret_Strategy.add(new Drive_State(-0.6, 140.0));
		Super_Secret_Strategy.add(new Elevation_State(true));
		//Super_Secret_Strategy.add(new Rotate_State (false, [angle1]));
		//Super_Secret_Strategy.add(new Aim_State());
		Super_Secret_Strategy.add(new Shoot_State());
		Super_Secret_Strategy.add(new Elevation_State(false));
		//Super_Secret_Strategy.add(new Rotate_State (false, [-angle1]));
		Super_Secret_Strategy.add(new Drive_State(-0.6, 96.0)); //?
		//Locate, Pick Up, Return
		Super_Secret_Strategy.add(new Drive_State(-0.6, 96.0)); //?
		//Super_Secret_Strategy.add(new Rotate_State (false, [angle1]));
		Super_Secret_Strategy.add(new Elevation_State(true));
		//Super_Secret_Strategy.add(new Rotate_State (false, [angle1]));
		//Super_Secret_Strategy.add(new Aim_State());
		Super_Secret_Strategy.add(new Shoot_State());
		Super_Secret_Strategy.add(new Idle_State(-1));
		
		Approach.add(new Elevation_State(true));
		Approach.add(new Elevation_Motor_State(true));
		Approach.add(new Idle_State(25));
		Approach.add(new Drive_State(-0.8, 44.0));
		Approach.add(new Idle_State(-1));
		
		LowBar.add(new Elevation_State(false));
		LowBar.add(new Elevation_Motor_State(true));
		LowBar.add(new Idle_State(50));
		LowBar.add(new Drive_State(-0.6, 140.0)); //?
		LowBar.add(new Elevation_State(true));
		//Rotate
		//Drive
		//Rotate
		//Aim
		//Shoot
		LowBar.add(new Idle_State(-1));
		
		Position2ShootB.add(new Elevation_State(true));
		Position2ShootB.add(new Elevation_Motor_State(true));
		Position2ShootB.add(new Idle_State(50));
		Position2ShootB.add(new Drive_State(-0.6, 140.0));
		//Rotate
		//Aim
		//Shoot
		Position2ShootB.add(new Idle_State(-1));
		
		Position2ShootF.add(new Elevation_State(true));
		Position2ShootF.add(new Elevation_Motor_State(true));
		Position2ShootF.add(new Idle_State(50));
		Position2ShootF.add(new Drive_State(0.6, 140.0));
		//Rotate
		//Aim
		//Shoot
		Position2ShootF.add(new Idle_State(-1));
		
		Position3ShootB.add(new Elevation_State(true));
		Position3ShootB.add(new Elevation_Motor_State(true));
		Position3ShootB.add(new Idle_State(50));
		Position3ShootB.add(new Drive_State(-0.6, 140.0));
		//Rotate
		//Aim
		//Shoot
		Position3ShootB.add(new Idle_State(-1));
		
		Position3ShootF.add(new Elevation_State(true));
		Position3ShootF.add(new Elevation_Motor_State(true));
		Position3ShootF.add(new Idle_State(50));
		Position3ShootF.add(new Drive_State(0.6, 140.0));
		//Rotate
		//Aim
		//Shoot
		Position3ShootF.add(new Idle_State(-1));
		
		Position4ShootB.add(new Elevation_State(true));
		Position4ShootB.add(new Elevation_Motor_State(true));
		Position4ShootB.add(new Idle_State(50));
		Position4ShootB.add(new Drive_State(-0.6, 140.0));
		//Rotate
		//Aim
		//Shoot
		Position4ShootB.add(new Idle_State(-1));
		
		Position4ShootF.add(new Elevation_State(true));
		Position4ShootF.add(new Elevation_Motor_State(true));
		Position4ShootF.add(new Idle_State(50));
		Position4ShootF.add(new Drive_State(0.6, 140.0));
		//Rotate
		//Aim
		//Shoot
		Position4ShootF.add(new Idle_State(-1));
		
		Position5ShootB.add(new Elevation_State(true));
		Position5ShootB.add(new Elevation_Motor_State(true));
		Position5ShootB.add(new Idle_State(50));
		Position5ShootB.add(new Drive_State(-0.6, 140.0));
		//Rotate
		//Aim
		//Shoot
		Position5ShootB.add(new Idle_State(-1));
		
		Position5ShootF.add(new Elevation_State(true));
		Position5ShootF.add(new Elevation_Motor_State(true));
		Position5ShootF.add(new Idle_State(50));
		Position5ShootF.add(new Drive_State(0.6, 140.0));
		//Rotate
		//Aim
		//Shoot
		Position5ShootF.add(new Idle_State(-1));
		
		LowBarNoShot.add(new Elevation_State(false));
		LowBarNoShot.add(new Elevation_Motor_State(true));
		LowBarNoShot.add(new Idle_State(50));
		LowBarNoShot.add(new Drive_State(-0.6, 140.0));
		LowBarNoShot.add(new Idle_State(250));
		LowBarNoShot.add(new Elevation_State(true));
		LowBarNoShot.add(new Idle_State(-1));
		
		StaticObstaclesNoShotB.add(new Elevation_State(true));
		StaticObstaclesNoShotB.add(new Elevation_Motor_State(true));
		StaticObstaclesNoShotB.add(new Idle_State(50));
		StaticObstaclesNoShotB.add(new Drive_State(-0.6, 140.0));
		StaticObstaclesNoShotB.add(new Idle_State(-1));
		
		StaticObstaclesNoShotF.add(new Elevation_State(true));
		StaticObstaclesNoShotF.add(new Elevation_Motor_State(true));
		StaticObstaclesNoShotF.add(new Idle_State(50));
		StaticObstaclesNoShotF.add(new Drive_State(0.6, 140.0));
		StaticObstaclesNoShotF.add(new Idle_State(-1));
		
		//Test_Queue.add(new Rotate_State(0.8, true, 90));
		Test_Queue.add(new Idle_State(-1));
	}
	
	@SuppressWarnings("unchecked")
	public void Update()
	{		
		Execute = (LinkedList<Template_State>) autoChooser.getSelected();
		if(Execute.peek().Update())
		{
			Execute.remove();
		}		
	}	
}
