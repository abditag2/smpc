package uiuc.smpc.events;

import uiuc.smpc.abstractlibrary.Event;
import uiuc.smpc.abstractlibrary.Parameters;
import uiuc.smpc.library.Simulation;

public class Addition extends Event{
	
	public Addition(Simulation simulation, double startTime, int hostID , int start, int end){

		this.simulation = simulation ;
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
				simulation.schedule(new Computation(simulation, startTime, i, Parameters.COMPUTATION_COST1));
			}

			return false ; 
	}

}
