package smpc.library;

import smpc.abstractlibrary.Simulation;

public class OnlinePhaseSimulation extends Simulation{
	
	ListQueue eventQueue ;
	public double time;
	
	public OnlinePhaseSimulation(){
		eventQueue = new ListQueue() ;
		this.time = 0;
	}

}
