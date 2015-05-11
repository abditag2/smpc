package smpc.events;

import smpc.abstractlibrary.Event;
import smpc.library.OnlinePhaseSimulation;

public class PartiallyOpen extends Event {

	
	int openDestination ; 
	int start, end ; 
	public PartiallyOpen (OnlinePhaseSimulation onlinePhaseSimulation, double startTime, int hostID, int openDestination, int start, int end){
		this.onlinePhaseSimulation = onlinePhaseSimulation;
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
			onlinePhaseSimulation.schedule(new Send(onlinePhaseSimulation, startTime, sendHostID, openDestination));
		}
		
		return true;
	}

}
