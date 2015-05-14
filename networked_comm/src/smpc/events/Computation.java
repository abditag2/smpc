package smpc.events;

import smpc.Parameters;
import smpc.abstractlibrary.Event;
import smpc.abstractlibrary.Simulation;
import smpc.library.OnlinePhaseSimulation;

public class Computation extends Event {

	
	public Computation(Simulation simulation, double startTime, int hostID, Parameters.ComputationType computationType, int numberOfTimes)
	{
		this.simulation = simulation;
		this.startTime = startTime ; 
		this.hostID = hostID ; 
		
		this.duration = Parameters.getDuration(computationType);
		this.type = "Computation";
		this.numberOfTimes = numberOfTimes;
	}
	
	
	@Override
	public boolean execute() {
		//THere is nothing to do
		//Basically at this execution the real machine will calculate something! we won't bcz 
		//we re just a simulation :))
		return false;
	}

}
