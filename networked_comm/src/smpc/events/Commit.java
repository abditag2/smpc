package smpc.events;

import smpc.abstractlibrary.Parameters;
import smpc.abstractlibrary.Event;
import smpc.library.OnlinePhaseSimulation;


public class Commit extends Event {

	int start, end ; 
	
	public Commit (OnlinePhaseSimulation onlinePhaseSimulation, double startTime, int hostID, int start, int end){
		this.onlinePhaseSimulation = onlinePhaseSimulation;
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
					onlinePhaseSimulation.schedule(new Computation(onlinePhaseSimulation, startTime, j, Parameters.COMMIT_COMPUTATION_COST));
					onlinePhaseSimulation.schedule(new Send(onlinePhaseSimulation, startTime, j, i)) ;
				}
			}
		
		return false;
	}

}
