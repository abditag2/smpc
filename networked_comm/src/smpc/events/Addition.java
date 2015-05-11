package smpc.events;

import smpc.abstractlibrary.Parameters;
import smpc.library.OnlinePhaseSimulation;
import smpc.abstractlibrary.Event;

public class Addition extends Event {
	
	public Addition(OnlinePhaseSimulation onlinePhaseSimulation, double startTime, int hostID , int start, int end){

		this.onlinePhaseSimulation = onlinePhaseSimulation;
		this.startTime = startTime ; 
		this.hostID = hostID ;
		
		this.duration = 20.0 ;
		this.type = "aDDITION";
		
		this.start = start ; 
		this.end = end ; 

	}
	

	@Override
	public boolean execute() {
			/**
			 * ADD
			 */
			
			for (int i = 0; i < Parameters.getNumberOfParties(); i++) 
			{
				onlinePhaseSimulation.schedule(new Computation(onlinePhaseSimulation, startTime, i, Parameters.COMPUTATION_COST1));
			}

			return false ; 
	}

}
