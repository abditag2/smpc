package smpc.events;

import smpc.Parameters;
import smpc.abstractlibrary.Event;
import smpc.abstractlibrary.Simulation;

public class Recieve extends Event {
	
	
	
	public Recieve (Simulation simulation, double startTime, int hostID){
		this.simulation = simulation;
		this.startTime = startTime ; 
		this.hostID = hostID ;
		
		this.duration = Parameters.getDuration(Parameters.ComputationType.RECIEVE) ;
		this.type = "Recieve";
	}
	
	

	@Override
	public boolean execute() {
		Parameters.count = Parameters.count + 1 ; 
		//System.out.println("Recieve Started at" + this.startTime + " and finished at:" + this.getFinishingTime() + "on host: "  + this.hostID);
		return false;
	}


}
