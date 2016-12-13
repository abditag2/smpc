package uiuc.smpc.events;

import uiuc.smpc.abstractlibrary.Event;
import uiuc.smpc.abstractlibrary.Parameters;
import uiuc.smpc.library.Simulation;

public class BroadCast extends Event{


	int start, end ; 

	public BroadCast(Simulation simulation,  double startTime , int hostID, int start, int end)
	{
	
		 this.simulation = simulation;
		 this.startTime = startTime ; 
		 this.hostID  = hostID; 
		 
		 this.duration = 10.0 ; 
		 this.type = "Share";
		 this.start = start ; 
		 this.end = end ; 
	}
	
	

	@Override
	public boolean execute() {
		
		double time = this.getFinishingTime() ; 

//		System.out.println("Share Started at" + this.startTime + " and finished at:" + this.getFinishingTime() + "on host: "  + this.hostID);

		
		for (int i = start ; i < end  ; i++)
		{
				Send sendEvent = new Send(simulation, time  , hostID, i);
				simulation.schedule(sendEvent);
				time = sendEvent.getFinishingTime();
		}


		
		return false;
	}

}
