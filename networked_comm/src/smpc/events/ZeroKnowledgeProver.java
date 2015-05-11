package smpc.events;


import smpc.abstractlibrary.Parameters;
import smpc.abstractlibrary.Event;
import smpc.library.OnlinePhaseSimulation;

public class ZeroKnowledgeProver extends Event
{

	int start, end ; 
	
	public ZeroKnowledgeProver(OnlinePhaseSimulation onlinePhaseSimulation, double startTime, int hostID , int start, int end)
	{
		this.onlinePhaseSimulation = onlinePhaseSimulation;
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
			onlinePhaseSimulation.schedule(new Computation(onlinePhaseSimulation, startTime, i, Parameters.ZERO_KNOWLEDGE_COST)) ;
			
			
			for (int j = start; j < end; j++) {
				
				onlinePhaseSimulation.schedule(new Send(onlinePhaseSimulation, startTime, i, j));
			}
		}
		
		return true ; 
	}



}
