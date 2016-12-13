package uiuc.smpc.events;

import uiuc.smpc.abstractlibrary.Event;
import uiuc.smpc.abstractlibrary.Parameters;
import uiuc.smpc.library.Simulation;

public class Recieve extends Event{
	
	
	
	public Recieve (Simulation simulation , double startTime, int hostID){
		this.simulation = simulation ;
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
