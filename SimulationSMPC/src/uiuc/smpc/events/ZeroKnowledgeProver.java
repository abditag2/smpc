package uiuc.smpc.events;


import uiuc.smpc.abstractlibrary.Event;
import uiuc.smpc.abstractlibrary.Parameters;
import uiuc.smpc.library.Simulation;

public class ZeroKnowledgeProver extends Event
{

	int start, end ; 
	
	public ZeroKnowledgeProver(Simulation simulation, double startTime, int hostID , int start, int end)
	{
		this.simulation = simulation ;
		this.startTime = startTime ; 
		this.hostID = hostID ;
		
		this.duration = 20.0 ;
		this.type = "ZeroKnowledge";
		
		this.start = start ; 
		this.end = end ; 

	}



	@Override
	public boolean execute(){
		
		for (int i = start; i < end ; i ++ ) 
		{
			simulation.schedule(new Computation(simulation, startTime, i, Parameters.ZERO_KNOWLEDGE_COST)) ; 
			
			
			for (int j = start; j < end; j++) {
				
				simulation.schedule(new Send(simulation, startTime, i, j));
			}
		}
		
		return true ; 
	}



}
