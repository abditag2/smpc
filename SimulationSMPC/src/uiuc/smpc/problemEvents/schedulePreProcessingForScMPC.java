package uiuc.smpc.problemEvents;

import uiuc.smpc.abstractlibrary.Event;
import uiuc.smpc.abstractlibrary.Parameters;
import uiuc.smpc.library.Simulation;

public class schedulePreProcessingForScMPC extends Event{
	
	public  schedulePreProcessingForScMPC(Simulation simulation, double startTime, int hostID ,  int start, int end)
	{
		this.simulation = simulation ;
		this.startTime = startTime ; 
		this.hostID = hostID ;
		
		this.duration = 20.0 ;
		this.type = "schedulePreProcessingForScMPC";
		
		this.start = start ; 
		this.end = end ; 

	}

	@Override
	public boolean execute() {

		int totalNumberOfClusters = Parameters.getNumberOfParties() / Parameters.getClusterSize() ; 

		for (int i = start; i < totalNumberOfClusters ; i++) 
		{
			
			//Do the preproccessing for all the clusters in tree. 
			
			simulation.schedule(new schedulePreProcessingForSPDZ(simulation, startTime, Parameters.VIRTUAL_HOST, i*Parameters.getClusterSize(), (i+1)*Parameters.getClusterSize()));
			simulation.schedule(new schedulePreProcessingForSPDZ(simulation, startTime, Parameters.VIRTUAL_HOST, i*Parameters.getClusterSize(), (i+1)*Parameters.getClusterSize()));
			
		}
		
		return false;
	}
	

}
