package smpc.events;

import smpc.abstractlibrary.Event;
import smpc.library.OnlinePhaseSimulation;

public class BroadCast extends Event {


	int start, end ; 

	public BroadCast(OnlinePhaseSimulation onlinePhaseSimulation,  double startTime , int hostID, int start, int end)
	{
	
		 this.onlinePhaseSimulation = onlinePhaseSimulation;
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
				Send sendEvent = new Send(onlinePhaseSimulation, time  , hostID, i);
				onlinePhaseSimulation.schedule(sendEvent);
				time = sendEvent.getFinishingTime();
		}


		
		return false;
	}

}
