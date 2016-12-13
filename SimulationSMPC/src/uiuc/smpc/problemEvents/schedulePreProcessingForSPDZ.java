package uiuc.smpc.problemEvents;

import uiuc.smpc.abstractlibrary.Event;
import uiuc.smpc.abstractlibrary.Parameters;
import uiuc.smpc.events.Computation;
import uiuc.smpc.events.ZeroKnowledgeProver;
import uiuc.smpc.library.Simulation;

public class schedulePreProcessingForSPDZ extends Event{

	public  schedulePreProcessingForSPDZ(Simulation simulation, double startTime, int hostID ,  int start, int end)
	{
		this.simulation = simulation ;
		this.startTime = startTime ; 
		this.hostID = hostID ;
		
		this.duration = 20.0 ;
		this.type = "schedulePreProcessingForSPDZ";
		
		this.start = start ; 
		this.end = end ; 

	}
	
	@Override
	public boolean execute() {
			
			//call Fkeygendec
			for (int i = start;i < end ; i++)
			{
				simulation.schedule(new Computation(simulation, startTime, i, Parameters.FKEYGENDEC)) ; 
				//TODO ZeroKNowledge prrove needs to be completed!
				simulation.schedule(new Computation(simulation, startTime, i, Parameters.GENERATE_MAC_KEY)) ;
				simulation.schedule(new Computation(simulation, startTime, i, Parameters.GENERATE_ALPHA_i)) ;
				simulation.schedule(new Computation(simulation, startTime, i, Parameters.ENC_DIAG_ENC)) ;
			}
			
			simulation.schedule(new ZeroKnowledgeProver(simulation, startTime, Parameters.VIRTUAL_HOST, start, end) ); 
			
			for (int i = start;i < end ; i++)
			{
				simulation.schedule(new Computation(simulation, startTime, i, Parameters.BOX_OPERATION));
			}
		return false;
	}

}
