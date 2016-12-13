package uiuc.smpc.voting;

import uiuc.smpc.abstractlibrary.Event;
import uiuc.smpc.abstractlibrary.Parameters;
import uiuc.smpc.events.Computation;
import uiuc.smpc.events.InputPhase;
import uiuc.smpc.events.ZeroKnowledgeProver;
import uiuc.smpc.library.Simulation;

public class votingCluster extends Event{

	public  votingCluster(Simulation simulation, double startTime, int hostID ,  int start, int end)
	{
		this.simulation = simulation ;
		this.startTime = startTime ; 
		this.hostID = hostID ;
		
		this.duration = 20.0 ;
		this.type = "votingCluster";
		
		this.start = start ; 
		this.end = end ; 

	}
	
	@Override
	public boolean execute() {
			
		simulation.schedule(new InputPhase(simulation, startTime, Parameters.VIRTUAL_HOST, start, end) );
		System.out.println("input Phase finished!");
		for (int i = start ; i < end; i++ ) 
		{
			for(int j = start ; j < end ; j++) {
				simulation.schedule(new Computation(simulation, startTime, i, Parameters.ADDITION_TIME)) ;
				simulation.schedule(new Computation(simulation, startTime, i, Parameters.ADDITION_TIME)) ;
			}
		}
		
		System.out.println("computation is done!");
		return false;
	}

}
