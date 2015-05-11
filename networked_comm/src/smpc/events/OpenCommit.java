package smpc.events;

import smpc.abstractlibrary.Event;
import smpc.library.OnlinePhaseSimulation;

public class OpenCommit extends Event {

	
	int start, end ; 
	public OpenCommit (OnlinePhaseSimulation onlinePhaseSimulation, double startTime, int hostID, int start, int end){
		this.onlinePhaseSimulation = onlinePhaseSimulation;
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
			onlinePhaseSimulation.schedule(new Send(onlinePhaseSimulation, startTime, hostID, i)) ;
		}
		return false;
	}

}
