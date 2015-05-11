package smpc.problemEvents;

import smpc.abstractlibrary.Parameters;
import smpc.abstractlibrary.Event;
import smpc.library.OnlinePhaseSimulation;

public class schedulePreProcessingForScMPC extends Event {
	
	public  schedulePreProcessingForScMPC(OnlinePhaseSimulation onlinePhaseSimulation, double startTime, int hostID ,  int start, int end)
	{
		this.onlinePhaseSimulation = onlinePhaseSimulation;
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
			
			onlinePhaseSimulation.schedule(new schedulePreProcessingForSPDZ(onlinePhaseSimulation, startTime, Parameters.VIRTUAL_HOST, i*Parameters.getClusterSize(), (i+1)*Parameters.getClusterSize()));
			onlinePhaseSimulation.schedule(new schedulePreProcessingForSPDZ(onlinePhaseSimulation, startTime, Parameters.VIRTUAL_HOST, i*Parameters.getClusterSize(), (i+1)*Parameters.getClusterSize()));
			
		}
		
		return false;
	}
	

}
