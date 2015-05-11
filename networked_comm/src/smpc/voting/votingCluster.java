package smpc.voting;

import smpc.abstractlibrary.Event;
import smpc.abstractlibrary.Parameters;
import smpc.events.Computation;
import smpc.events.InputPhase;
import smpc.library.OnlinePhaseSimulation;

public class votingCluster extends Event{

	public  votingCluster(OnlinePhaseSimulation onlinePhaseSimulation, double startTime, int hostID ,  int start, int end)
	{
		this.onlinePhaseSimulation = onlinePhaseSimulation;
		this.startTime = startTime ; 
		this.hostID = hostID ;
		
		this.duration = 20.0 ;
		this.type = "votingCluster";
		
		this.start = start ; 
		this.end = end ; 

	}
	
	@Override
	public boolean execute() {
			
		onlinePhaseSimulation.schedule(new InputPhase(onlinePhaseSimulation, startTime, Parameters.VIRTUAL_HOST, start, end) );
		System.out.println("input Phase finished!");
		for (int i = start ; i < end; i++ ) 
		{
			for(int j = start ; j < end ; j++) {
				onlinePhaseSimulation.schedule(new Computation(onlinePhaseSimulation, startTime, i, Parameters.ADDITION_TIME)) ;
				onlinePhaseSimulation.schedule(new Computation(onlinePhaseSimulation, startTime, i, Parameters.ADDITION_TIME)) ;
			}
		}
		
		System.out.println("computation is done!");
		return false;
	}

}
