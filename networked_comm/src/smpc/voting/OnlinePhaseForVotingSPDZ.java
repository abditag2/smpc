package smpc.voting;

import smpc.abstractlibrary.Event;
import smpc.Parameters;
import smpc.abstractlibrary.Simulation;
import smpc.events.BroadCast;
import smpc.events.Computation;
import smpc.offlineEvents.MacCheck;

public class OnlinePhaseForVotingSPDZ extends Event{

	public OnlinePhaseForVotingSPDZ(Simulation simulation, double startTime, int hostID, int start, int end)
	{
		this.simulation = simulation;
		this.startTime = startTime ; 
		this.hostID = hostID ;
		
		this.duration = 20.0 ;
		this.type = "OnlinePhaseForVotingSPDZ";

		this.start = start;
		this.end = end ; 

	}
	
	
	
	@Override
	public boolean execute() {

		for(int i = 0 ; i < Parameters.getNumberOfParties() ; i++){
			simulation.schedule( new Computation(simulation, simulation.time, i, Parameters.ComputationType.ADDITION, 1));
		}

		simulation.doAllEvents() ;

		simulation.schedule(new MacCheck(simulation, startTime, Parameters.VIRTUAL_HOST, 0, Parameters.getNumberOfParties(), 0));
		return false;
	}

}
