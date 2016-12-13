package uiuc.smpc.events;
import uiuc.smpc.abstractlibrary.Event;
import uiuc.smpc.events.Recieve;
import uiuc.smpc.library.Simulation;
import uiuc.smpc.abstractlibrary.*;

public class Send extends Event {

	int destinationID ; 

	

	public Send (Simulation simulation,  double startTime , int hostID , int destinaionID)
	{
	 this.simulation = simulation;
	 this.destinationID = destinaionID;	
	 this.startTime = startTime ; 
	 this.hostID  = hostID;
	 
	 this.duration  = 2.0  ;
	 this.type = "Send";
	}
	


	@Override
	public boolean execute() {

	//	System.out.println("Send executed at" + this.startTime + " and finished at:" + this.getFinishingTime() + "on host: "  + this.hostID);
		
		Recieve recieve = new Recieve(this.simulation, this.getFinishingTime() + Parameters.getNetworkDelay() , this.destinationID) ;
		this.simulation.schedule(recieve);
		

		return true ; 
		
	}

	

}
