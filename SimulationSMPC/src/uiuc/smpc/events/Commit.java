package uiuc.smpc.events;

import uiuc.smpc.abstractlibrary.Event;
import uiuc.smpc.abstractlibrary.Parameters;
import uiuc.smpc.library.Simulation;


public class Commit extends Event{

	int start, end ; 
	
	public Commit (Simulation simulation , double startTime, int hostID, int start, int end){
		this.simulation = simulation ;
		this.startTime = startTime ; 
		this.hostID = hostID ;
		
		this.duration = 0.0 ;
		this.type = "Commit";
		this.start = start;
		this.end = end ; 

	}
	
	
	@Override
	public boolean execute() {

		//Complexity n^3
			for (int i = start ; i < end; i++ ) {
				for (int j = start; j < end; j++) {

					//COMMIT WITH N^2 COST!
					simulation.schedule(new Computation(simulation, startTime, j, Parameters.COMMIT_COMPUTATION_COST));
					simulation.schedule(new Send(simulation, startTime, j, i)) ; 
				}
			}
		
		return false;
	}

}
