package uiuc.smpc.voting;

import uiuc.smpc.abstractlibrary.Event;
import uiuc.smpc.abstractlibrary.Parameters;
import uiuc.smpc.events.Computation;
import uiuc.smpc.events.InputPhase;
import uiuc.smpc.events.OutputPhase;
import uiuc.smpc.events.ZeroKnowledgeProver;
import uiuc.smpc.library.Simulation;
import uiuc.smpc.problemEvents.ClusterCoinToss;
import uiuc.smpc.problemEvents.schedulePreProcessingForSPDZ;
import uiuc.smpc.problemEvents.schedulePreProcessingForScMPC;

public class votingSPDZ extends Event{

	public  votingSPDZ(Simulation simulation, double startTime, int hostID ,  int start, int end)
	{
		this.simulation = simulation ;
		this.startTime = startTime ; 
		this.hostID = hostID ;
		
		this.duration = 20.0 ;
		this.type = "votingSPDZ";
		
		this.start = start ; 
		this.end = end ; 

	}
	
	
	
	@Override
	public boolean execute() {
		/**
		 * Do the preprocessing step required for SPDZ
		 */
		
		simulation.schedule(new schedulePreProcessingForSPDZ(simulation, startTime, Parameters.VIRTUAL_HOST , 0, Parameters.getNumberOfParties()));
		
		simulation.doAllEvents() ; 
		
		/**
		 * write the simulations here 
		 */
		
		simulation.schedule(new votingCluster(simulation, startTime, Parameters.VIRTUAL_HOST, 0, Parameters.getNumberOfParties()));
		simulation.doAllEvents() ; 
		
		/**
		 * output
		 */
		
		simulation.schedule(new OutputPhase(simulation, startTime,Parameters.VIRTUAL_HOST, 0, Parameters.getNumberOfParties()));
		
		return false;
	}

}
