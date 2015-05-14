package smpc.events.OnlineEvents;

import smpc.Parameters;
import smpc.events.Computation;
import smpc.library.OnlinePhaseSimulation;
import smpc.abstractlibrary.Event;

public class Addition extends Event {
	
	public Addition(OnlinePhaseSimulation onlinePhaseSimulation, double startTime, int hostID , int start, int end){

		this.simulation = onlinePhaseSimulation;
		this.startTime = startTime ; 
		this.hostID = hostID ;
		
		this.duration = 0.0 ;
		this.type = "Addition";
		
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
				simulation.schedule(new Computation(simulation, startTime, i, Parameters.ComputationType.ADDITION,1));
			}

			return false ; 
	}

}
