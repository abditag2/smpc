package smpc.events;

import smpc.abstractlibrary.Event;
import smpc.library.OnlinePhaseSimulation;

public class Computation extends Event {

	
	public Computation(OnlinePhaseSimulation onlinePhaseSimulation, double startTime, int hostID, double duration)
	{
		this.onlinePhaseSimulation = onlinePhaseSimulation;
		this.startTime = startTime ; 
		this.hostID = hostID ; 
		
		this.duration = duration ;
		this.type = "Computation";
	}
	
	
	@Override
	public boolean execute() {
		//THere is nothing to do
		//Basically at this execution the real machine will calculate something! we won't bcz 
		//we re just a onlinePhaseSimulation :))
		return false;
	}

}
