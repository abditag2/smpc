package uiuc.smpc.events;

import uiuc.smpc.abstractlibrary.Event;
import uiuc.smpc.abstractlibrary.Parameters;
import uiuc.smpc.library.Simulation;

public class InputPhase extends Event{

	public InputPhase(Simulation simulation, double startTime, int hostID , int start, int end){

		this.simulation = simulation ;
		this.startTime = startTime ; 
		this.hostID = hostID ;
		
		this.duration = 20.0 ;
		this.type = "InputPhase";
		
		this.start = start ; 
		this.end = end ; 

	}

	
	@Override
	public boolean execute() {
		/**
		 * Input phase
		 */
		
		//First all the nodes open their input to the one node
		for (int i = start ; i < end ; i ++)
		{
			simulation.schedule(new Computation(simulation, startTime, i, Parameters.COMPUTATION_COST1));
			simulation.schedule(new PartiallyOpen(simulation, startTime, Parameters.VIRTUAL_HOST , i, start, end));
		}
		
		
		
		simulation.doAllEvents();
		
		
		
		//Then all the event BroadCast their inputs
		for(int i = start ; i < end ; i++){
			//share all the inputs
			simulation.schedule(new BroadCast(simulation, startTime, i, start, end));
		}
		simulation.doAllEvents();
		
	

		return false;
	}

}
