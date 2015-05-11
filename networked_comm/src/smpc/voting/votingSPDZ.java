package smpc.voting;

import smpc.abstractlibrary.Event;
import smpc.abstractlibrary.Parameters;
import smpc.library.OnlinePhaseSimulation;

public class votingSPDZ extends Event{

	public  votingSPDZ(OnlinePhaseSimulation onlinePhaseSimulation, double startTime, int hostID ,  int start, int end)
	{
		this.onlinePhaseSimulation = onlinePhaseSimulation;
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
		
//		onlinePhaseSimulation.schedule(new schedulePreProcessingForSPDZ(onlinePhaseSimulation, startTime, Parameters.VIRTUAL_HOST , 0, Parameters.getNumberOfParties()));
//
//		onlinePhaseSimulation.doAllEvents() ;
//
		/**
		 * write the simulations here 
		 */
		
		onlinePhaseSimulation.schedule(new votingCluster(onlinePhaseSimulation, startTime, Parameters.VIRTUAL_HOST, 0, Parameters.getNumberOfParties()));
		onlinePhaseSimulation.doAllEvents() ;
		
		/**
		 * output
		 */
		
//		onlinePhaseSimulation.schedule(new OutputPhase(onlinePhaseSimulation, startTime,Parameters.VIRTUAL_HOST, 0, Parameters.getNumberOfParties()));
		
		return false;
	}

}
