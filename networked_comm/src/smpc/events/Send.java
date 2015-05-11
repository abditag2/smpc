package smpc.events;

import smpc.abstractlibrary.*;
import smpc.library.OnlinePhaseSimulation;

public class Send extends Event {

	int destinationID ; 

	

	public Send (OnlinePhaseSimulation onlinePhaseSimulation,  double startTime , int hostID , int destinaionID)
	{
	 this.onlinePhaseSimulation = onlinePhaseSimulation;
	 this.destinationID = destinaionID;	
	 this.startTime = startTime ; 
	 this.hostID  = hostID;
	 
	 this.duration  = 2.0  ;
	 this.type = "Send";
	}
	


	@Override
	public boolean execute() {

	//	System.out.println("Send executed at" + this.startTime + " and finished at:" + this.getFinishingTime() + "on host: "  + this.hostID);
		
		Recieve recieve = new Recieve(this.onlinePhaseSimulation, this.getFinishingTime() + Parameters.getNetworkDelay() , this.destinationID) ;
		this.onlinePhaseSimulation.schedule(recieve);
		

		return true ; 
		
	}

	

}
