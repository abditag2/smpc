package smpc.events.OnlineEvents;

import smpc.abstractlibrary.Event;
import smpc.Parameters;
import smpc.abstractlibrary.Simulation;
import smpc.events.Computation;
import smpc.events.offlineEvents.MacCheck;

public class OnlinePhaseForVotingSPDZ extends Event{

	public OnlinePhaseForVotingSPDZ(Simulation simulation, double startTime, int hostID, int start, int end)
	{
		this.simulation = simulation;
		this.startTime = startTime ; 
		this.hostID = hostID ;
		
		this.duration = 0.0 ;
		this.type = "OnlinePhaseForVotingSPDZ";

		this.start = start;
		this.end = end ; 

	}
	
	
	
	@Override
	public boolean execute() {


		for(int i = 0 ; i < Parameters.getNumberOfParties() ; i++){
			simulation.schedule( new Computation(simulation, simulation.time, i, Parameters.ComputationType.ADDITION, Parameters.N_A));
		}
		simulation.doAllEvents() ;
		double before = simulation.getLastFinishingTimeForAllNodes();
		simulation.schedule(new Multiplication(simulation, startTime, Parameters.VIRTUAL_HOST, 0, Parameters.getNumberOfParties()));
		simulation.doAllEvents() ;
		double after = simulation.getLastFinishingTimeForAllNodes();

		simulation.setAllFinishingTimes(simulation.getLastFinishingTimeForAllNodes() + (after - before)* Parameters.getNumberOfMultiplications());



		simulation.schedule(new MacCheck(simulation, startTime, Parameters.VIRTUAL_HOST, 0, Parameters.getNumberOfParties(), Parameters.getNumberOfMultiplications()));
		simulation.doAllEvents() ;
		return false;
	}

}
