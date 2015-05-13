package smpc.events;

import smpc.Parameters;
import smpc.abstractlibrary.*;

public class Send extends Event {

	int destinationID ; 

	

	public Send (Simulation simulation,  double startTime , int hostID , int destinaionID)
	{
	 this.simulation = simulation;
	 this.destinationID = destinaionID;	
	 this.startTime = startTime ; 
	 this.hostID  = hostID;
	 
	 this.duration  = Parameters.getDuration(Parameters.ComputationType.SEND)  ;
	 this.type = "Send";
	}
	


	@Override
	public boolean execute() {

	//	System.out.println("Send executed at" + this.startTime + " and finished at:" + this.getFinishingTime() + "on host: "  + this.hostID);

//		System.out.println("receve time is " + this.getFinishingTime() + Parameters.getNetworkDelay());
		Recieve recieve = new Recieve(this.simulation, this.getFinishingTime() + Parameters.getNetworkDelay() , this.destinationID) ;
		this.simulation.schedule(recieve);
		

		return true ; 
		
	}

	

}
