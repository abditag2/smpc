package uiuc.smpc.events;

import uiuc.smpc.abstractlibrary.Event;
import uiuc.smpc.library.Simulation;

public class Computation extends Event{

	
	public Computation(Simulation simulation , double startTime, int hostID, double duration)
	{
		this.simulation = simulation ;
		this.startTime = startTime ; 
		this.hostID = hostID ; 
		
		this.duration = duration ;
		this.type = "Computation";
	}
	
	
	@Override
	public boolean execute() {
		//THere is nothing to do
		//Basically at this execution the real machine will calculate something! we won't bcz 
		//we re just a simulation :))
		return false;
	}

}
