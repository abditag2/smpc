package smpc.events;

import smpc.abstractlibrary.Event;
import smpc.abstractlibrary.Simulation;
import smpc.library.OnlinePhaseSimulation;

public class OpenCommit extends Event {

	
	int start, end ; 
	public OpenCommit (Simulation simulation, double startTime, int hostID, int start, int end){
		this.simulation = simulation;
		this.startTime = startTime ; 
		this.hostID = hostID ;
		
		this.duration = 0.0 ;
		this.type = "OpenCommit";
		this.start = start ; 
		this.end = end ; 
	}
	

	@Override
	public boolean execute() {
		
		for (int i = start; i < end; i++) 
		{
			simulation.schedule(new Send(simulation, startTime, hostID, i)) ;
		}
		return false;
	}

}
