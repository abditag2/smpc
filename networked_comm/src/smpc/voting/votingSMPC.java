package smpc.voting;

import smpc.abstractlibrary.Event;
import smpc.abstractlibrary.Parameters;
import smpc.events.OutputPhase;
import smpc.library.OnlinePhaseSimulation;
import smpc.problemEvents.ClusterCoinToss;
import smpc.problemEvents.schedulePreProcessingForScMPC;

public class votingSMPC extends Event{

	public  votingSMPC(OnlinePhaseSimulation onlinePhaseSimulation, double startTime, int hostID ,  int start, int end)
	{
		this.onlinePhaseSimulation = onlinePhaseSimulation;
		this.startTime = startTime ; 
		this.hostID = hostID ;
		
		this.duration = 20.0 ;
		this.type = "votingSMPC";
		
		this.start = start ; 
		this.end = end ; 

	}
	
	@Override
	public boolean execute() {

		/**
		 * Tossing random Coin to find the clusters
		 */
		
		onlinePhaseSimulation.schedule(new ClusterCoinToss(onlinePhaseSimulation, startTime ,Parameters.VIRTUAL_HOST, 0, 2*Parameters.getNumberOfCurruptedParties() )) ;
		
		
		/**
		 * Do the preprocessing step required for SPDZ
		 */
		
		onlinePhaseSimulation.schedule(new schedulePreProcessingForScMPC(onlinePhaseSimulation, startTime, Parameters.VIRTUAL_HOST, 0, Parameters.getNumberOfParties())) ;

		/**
		 * different clusters execution
		 */
		double k = (double) Parameters.getNumberOfParties()/Parameters.getClusterSize();
		int layers = (int) ( Math.log( Parameters.getNumberOfParties()) / Math.log(Parameters.getClusterSize()));
				
		int parties = Parameters.getNumberOfParties() ; 
		for (int i = layers ; i >  0 ; i--){

			System.out.println("layer : " + i);
			int thisLayersClusters = (int) ((int) parties / Parameters.getClusterSize()) ; 
			for (int j = 0 ; j < thisLayersClusters ; j++ )
			{
				System.out.println("CLuster: " + j);
				onlinePhaseSimulation.schedule(new votingCluster(onlinePhaseSimulation, startTime, Parameters.VIRTUAL_HOST, j * (Parameters.CLUSTER_SIZE), (j+1)*Parameters.CLUSTER_SIZE ) );
			}
			parties = thisLayersClusters ; 
		}		

		
		/**
		 * output
		 */

		System.out.println("Output phase starting!");
		//TODO this needs to be changed in the future! UPDATE phase is not same as SPDZ
		onlinePhaseSimulation.schedule(new OutputPhase(onlinePhaseSimulation, startTime,Parameters.VIRTUAL_HOST, 0, Parameters.getClusterSize()));

		System.out.println("Output Phase finished!");
					

		return false;
	}

}
