package smpc.problemEvents;

import smpc.abstractlibrary.Parameters;
import smpc.abstractlibrary.Event;
import smpc.events.Computation;
import smpc.events.InputPhase;
import smpc.events.OutputPhase;
import smpc.events.Send;
import smpc.library.OnlinePhaseSimulation;

public class ClusterCoinToss extends Event {


	public  ClusterCoinToss(OnlinePhaseSimulation onlinePhaseSimulation, double startTime, int hostID ,  int start, int end)
	{
		this.onlinePhaseSimulation = onlinePhaseSimulation;
		this.startTime = startTime ; 
		this.hostID = hostID ;
		
		this.duration = 20.0 ;
		this.type = "ClusterCoinToss";
		
		this.start = start ; 
		this.end = end ; 

	}
	
	@Override
	public boolean execute() {
			//generate the random number in each node
			for (int i = start ; i < end; i++ ) 
			{
				onlinePhaseSimulation.schedule(new Computation(onlinePhaseSimulation, startTime, i, Parameters.RANDOM_COIN_GENERATION_LOCAL)) ;
			}
			
			//share the secrets 
			onlinePhaseSimulation.schedule(new InputPhase(onlinePhaseSimulation, startTime, Parameters.VIRTUAL_HOST , start, end)) ;
			
			
			for (int i = start ; i < end; i++ ) 
			{
			//each node needs to do 2*corrupted parties times addition
				for(int j = start ; j < end ; j++)
					onlinePhaseSimulation.schedule(new Computation(onlinePhaseSimulation, startTime, i, Parameters.ADDITION_TIME)) ;
			}
			
			//reveal the output
			onlinePhaseSimulation.schedule(new OutputPhase(onlinePhaseSimulation, startTime, Parameters.VIRTUAL_HOST, start, end ) );

			for (int i = 0; i < Parameters.getNumberOfParties(); i++) 
			{
				for (int j = 0; j < Parameters.CLUSTER_SIZE; j++) {
					//each nodes asks from cluster size of members about the final output of the coin Tossing
					onlinePhaseSimulation.schedule(new Send(onlinePhaseSimulation, startTime, i, j));
					onlinePhaseSimulation.schedule(new Send(onlinePhaseSimulation, startTime, j, i));
				}	
			}
			
		return false;
	}

}
