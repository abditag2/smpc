package smpc.events;

import smpc.abstractlibrary.Parameters;
import smpc.abstractlibrary.Event;
import smpc.library.OnlinePhaseSimulation;

public class InputPhase extends Event {

	public InputPhase(OnlinePhaseSimulation onlinePhaseSimulation, double startTime, int hostID , int start, int end){

		this.onlinePhaseSimulation = onlinePhaseSimulation;
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
			onlinePhaseSimulation.schedule(new Computation(onlinePhaseSimulation, startTime, i, Parameters.COMPUTATION_COST1));
			onlinePhaseSimulation.schedule(new PartiallyOpen(onlinePhaseSimulation, startTime, Parameters.VIRTUAL_HOST , i, start, end));
		}
		
		
		
		onlinePhaseSimulation.doAllEvents();
		
		
		
		//Then all the event BroadCast their inputs
		for(int i = start ; i < end ; i++){
			//share all the inputs
			onlinePhaseSimulation.schedule(new BroadCast(onlinePhaseSimulation, startTime, i, start, end));
		}
		onlinePhaseSimulation.doAllEvents();
		
	

		return false;
	}

}
