package smpc.problemEvents;

import smpc.abstractlibrary.Parameters;
import smpc.abstractlibrary.Event;
import smpc.events.Computation;
import smpc.events.ZeroKnowledgeProver;
import smpc.library.OnlinePhaseSimulation;

public class schedulePreProcessingForSPDZ extends Event {

	public  schedulePreProcessingForSPDZ(OnlinePhaseSimulation onlinePhaseSimulation, double startTime, int hostID ,  int start, int end)
	{
		this.onlinePhaseSimulation = onlinePhaseSimulation;
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
				onlinePhaseSimulation.schedule(new Computation(onlinePhaseSimulation, startTime, i, Parameters.FKEYGENDEC)) ;
				//TODO ZeroKNowledge prrove needs to be completed!
				onlinePhaseSimulation.schedule(new Computation(onlinePhaseSimulation, startTime, i, Parameters.GENERATE_MAC_KEY)) ;
				onlinePhaseSimulation.schedule(new Computation(onlinePhaseSimulation, startTime, i, Parameters.GENERATE_ALPHA_i)) ;
				onlinePhaseSimulation.schedule(new Computation(onlinePhaseSimulation, startTime, i, Parameters.ENC_DIAG_ENC)) ;
			}
			
			onlinePhaseSimulation.schedule(new ZeroKnowledgeProver(onlinePhaseSimulation, startTime, Parameters.VIRTUAL_HOST, start, end) );
			
			for (int i = start;i < end ; i++)
			{
				onlinePhaseSimulation.schedule(new Computation(onlinePhaseSimulation, startTime, i, Parameters.BOX_OPERATION));
			}
		return false;
	}

}
