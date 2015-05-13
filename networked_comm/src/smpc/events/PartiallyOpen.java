package smpc.events;

import smpc.abstractlibrary.Event;
import smpc.abstractlibrary.Simulation;
import smpc.library.OnlinePhaseSimulation;

public class PartiallyOpen extends Event {

	
	int openDestination ; 
	int start, end ; 
	public PartiallyOpen (Simulation simulation, double startTime, int hostID, int openDestination, int start, int end){
		this.simulation = simulation;
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
