package smpc.events;

import smpc.abstractlibrary.Parameters;
import smpc.abstractlibrary.Event;
import smpc.library.OnlinePhaseSimulation;

public class Recieve extends Event {
	
	
	
	public Recieve (OnlinePhaseSimulation onlinePhaseSimulation, double startTime, int hostID){
		this.onlinePhaseSimulation = onlinePhaseSimulation;
		this.startTime = startTime ; 
		this.hostID = hostID ;
		
		this.duration = 20.0 ;
		this.type = "Recieve";
	}
	
	

	@Override
	public boolean execute() {
		Parameters.count = Parameters.count + 1 ; 
		//System.out.println("Recieve Started at" + this.startTime + " and finished at:" + this.getFinishingTime() + "on host: "  + this.hostID);
		return false;
	}


}
