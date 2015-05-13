package smpc.events;

import smpc.abstractlibrary.Event;
import smpc.abstractlibrary.Simulation;
import smpc.library.OnlinePhaseSimulation;

public class BroadCast extends Event {


	int start, end ; 

	public BroadCast(Simulation simulation,  double startTime , int hostID, int start, int end)
	{
	
		 this.simulation = simulation;
		 this.startTime = startTime ; 
		 this.hostID  = hostID; 
		 
		 this.duration = 0.0 ;
		 this.type = "BroadCast";
		 this.start = start ; 
		 this.end = end ; 
	}
	
	

	@Override
	public boolean execute() {
		
		double time = this.getFinishingTime() ; 

//		System.out.println("Share Started at" + this.startTime + " and finished at:" + this.getFinishingTime() + "on host: "  + this.hostID);

		for (int i = start ; i < end  ; i++)
		{
//			System.out.println("bc send time:" + time);
			int destID = (i + hostID) % end;
			Send sendEvent = new Send(simulation, time  , hostID, destID);
			simulation.schedule(sendEvent);
			time = sendEvent.getFinishingTime();
		}
		return false;
	}

}
