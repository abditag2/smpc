package uiuc.smpc.events;

import uiuc.smpc.abstractlibrary.Event;
import uiuc.smpc.abstractlibrary.Parameters;
import uiuc.smpc.library.Simulation;

public class PartiallyOpen extends Event{

	
	int openDestination ; 
	int start, end ; 
	public PartiallyOpen (Simulation simulation , double startTime, int hostID, int openDestination, int start, int end){
		this.simulation = simulation ;
		this.startTime = startTime ; 
		this.hostID = hostID ;
		this.openDestination = openDestination ; 
		
		this.duration = 0.0 ;
		this.type = "Open";
		this.start = start;
		this.end = end ; 
				
	}
	

	@Override
	public boolean execute() {

		for (int sendHostID = start ; sendHostID < end ; sendHostID ++)
		{
			simulation.schedule(new Send(simulation, startTime, sendHostID, openDestination));
		}
		
		return true;
	}

}
