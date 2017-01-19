package autonomous;

public class Idle_State extends Template_State{

	int counter;
	
	public Idle_State(int input_counter) {
		this.counter = input_counter;
	}
	
	boolean Update() {
		if(counter < 0) {
			return false;
		}
		else if(counter == 0) {
			return true;
		}
		else {
			counter--;
			return false;
		}
		
	}

}
