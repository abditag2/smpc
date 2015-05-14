package smpc.OnlineEvents;

import smpc.Parameters;
import smpc.events.Computation;
import smpc.library.OnlinePhaseSimulation;
import smpc.abstractlibrary.Event;
import smpc.offlineEvents.ProtCommitOpen;

public class Multiplication extends Event {

	public Multiplication(OnlinePhaseSimulation onlinePhaseSimulation, double startTime, int hostID , int start, int end){

		this.simulation = onlinePhaseSimulation;
		this.startTime = startTime ; 
		this.hostID = hostID ;
		
		this.duration = 20.0 ;
		this.type = "Multiplication";
		
		this.start = start ; 
		this.end = end ; 

	}
	

	@Override
	public boolean execute() {
			/**
			 * Multiplication
			 */

		for (int i = 0 ;i < Parameters.getNumberOfParties() ; i++){
			simulation.schedule( new ProtCommitOpen(simulation, startTime, i, 0, Parameters.getNumberOfParties()));
			simulation.schedule( new Computation(simulation, simulation.time, i, Parameters.ComputationType.ADDITION, 2 + 3));
		}
		return false;
	}

}
