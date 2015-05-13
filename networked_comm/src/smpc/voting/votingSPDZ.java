package smpc.voting;

import smpc.abstractlibrary.Event;
import smpc.Parameters;
import smpc.abstractlibrary.Simulation;

public class votingSPDZ extends Event{

	public  votingSPDZ(Simulation simulation, double startTime, int hostID, int start, int end)
	{
		this.simulation = simulation;
		this.startTime = startTime ; 
		this.hostID = hostID ;
		
		this.duration = 20.0 ;
		this.type = "votingSPDZ";

		this.start = start;
		this.end = end ; 

	}
	
	
	
	@Override
	public boolean execute() {
		/**
		 * Do the preprocessing step required for SPDZ
		 */
		
//		simulation.schedule(new schedulePreProcessingForSPDZ(simulation, startTime, Parameters.VIRTUAL_HOST , 0, Parameters.getNumberOfParties()));
//
//		simulation.doAllEvents() ;
//
		/**
		 * write the simulations here 
		 */
		
		simulation.schedule(new votingCluster(simulation, startTime, Parameters.VIRTUAL_HOST, 0, Parameters.getNumberOfParties()));
		simulation.doAllEvents() ;
		
		/**
		 * output
		 */
		
//		simulation.schedule(new OutputPhase(simulation, startTime,Parameters.VIRTUAL_HOST, 0, Parameters.getNumberOfParties()));
		
		return false;
	}

}
