package uiuc.smpc.voting;

import uiuc.smpc.abstractlibrary.Event;
import uiuc.smpc.abstractlibrary.Parameters;
import uiuc.smpc.events.Computation;
import uiuc.smpc.events.InputPhase;
import uiuc.smpc.events.OutputPhase;
import uiuc.smpc.events.ZeroKnowledgeProver;
import uiuc.smpc.library.Simulation;
import uiuc.smpc.problemEvents.ClusterCoinToss;
import uiuc.smpc.problemEvents.schedulePreProcessingForScMPC;

public class votingSMPC extends Event{

	public  votingSMPC(Simulation simulation, double startTime, int hostID ,  int start, int end)
	{
		this.simulation = simulation ;
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
		
		simulation.schedule(new ClusterCoinToss(simulation, startTime ,Parameters.VIRTUAL_HOST, 0, 2*Parameters.getNumberOfCurruptedParties() )) ;
		
		
		/**
		 * Do the preprocessing step required for SPDZ
		 */
		
		simulation.schedule(new schedulePreProcessingForScMPC(simulation, startTime, Parameters.VIRTUAL_HOST, 0, Parameters.getNumberOfParties())) ;

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
				simulation.schedule(new votingCluster(simulation, startTime, Parameters.VIRTUAL_HOST, j * (Parameters.CLUSTER_SIZE), (j+1)*Parameters.CLUSTER_SIZE ) );
			}
			parties = thisLayersClusters ; 
		}		

		
		/**
		 * output
		 */

		System.out.println("Output phase starting!");
		//TODO this needs to be changed in the future! UPDATE phase is not same as SPDZ
		simulation.schedule(new OutputPhase(simulation, startTime,Parameters.VIRTUAL_HOST, 0, Parameters.getClusterSize()));

		System.out.println("Output Phase finished!");
					

		return false;
	}

}
