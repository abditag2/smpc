package uiuc.smpc.problemEvents;

import uiuc.smpc.abstractlibrary.Event;
import uiuc.smpc.abstractlibrary.Parameters;
import uiuc.smpc.events.Computation;
import uiuc.smpc.events.InputPhase;
import uiuc.smpc.events.OutputPhase;
import uiuc.smpc.events.Send;
import uiuc.smpc.library.Simulation;

public class ClusterCoinToss extends Event{


	public  ClusterCoinToss(Simulation simulation, double startTime, int hostID ,  int start, int end)
	{
		this.simulation = simulation ;
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
				simulation.schedule(new Computation(simulation, startTime, i, Parameters.RANDOM_COIN_GENERATION_LOCAL)) ; 
			}
			
			//share the secrets 
			simulation.schedule(new InputPhase(simulation, startTime, Parameters.VIRTUAL_HOST , start, end)) ;
			
			
			for (int i = start ; i < end; i++ ) 
			{
			//each node needs to do 2*corrupted parties times addition
				for(int j = start ; j < end ; j++)
					simulation.schedule(new Computation(simulation, startTime, i, Parameters.ADDITION_TIME)) ; 
			}
			
			//reveal the output
			simulation.schedule(new OutputPhase(simulation, startTime, Parameters.VIRTUAL_HOST, start, end ) );

			for (int i = 0; i < Parameters.getNumberOfParties(); i++) 
			{
				for (int j = 0; j < Parameters.CLUSTER_SIZE; j++) {
					//each nodes asks from cluster size of members about the final output of the coin Tossing
					simulation.schedule(new Send(simulation, startTime, i, j));
					simulation.schedule(new Send(simulation, startTime, j, i));
				}	
			}
			
		return false;
	}

}
