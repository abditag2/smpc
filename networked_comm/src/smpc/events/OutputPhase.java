package smpc.events;

import smpc.abstractlibrary.Parameters;
import smpc.abstractlibrary.Event;
import smpc.library.OnlinePhaseSimulation;

public class OutputPhase extends Event {

	public OutputPhase(OnlinePhaseSimulation onlinePhaseSimulation, double startTime, int hostID , int start, int end){

		this.onlinePhaseSimulation = onlinePhaseSimulation;
		this.startTime = startTime ; 
		this.hostID = hostID ;
		
		this.duration = 20.0 ;
		this.type = "InputPhase";
		
		this.start = start ; 
		this.end = end ; 

	}

	
	@Override
	public boolean execute() 
	{
		
		System.out.println("Output Phase started!");
		//open [[e]]
		
		onlinePhaseSimulation.schedule(new Open(onlinePhaseSimulation, startTime, Parameters.VIRTUAL_HOST, start, end)) ;

		//Compute Sigma(e_j * a_j)
		for (int i = start; i < end; i++) {
			onlinePhaseSimulation.schedule(new Computation(onlinePhaseSimulation, startTime, i, Parameters.COMPUTATION_COST1));
		}
		onlinePhaseSimulation.doAllEvents() ;
		System.out.println("commiting  started!");
		
		//Each party commits to its final y_i
		for (int i = start; i < end; i++) 
		{
			onlinePhaseSimulation.schedule(new Commit(onlinePhaseSimulation, startTime, i, start, end));
		}
		onlinePhaseSimulation.doAllEvents() ;
		System.out.println("commiting  finished!");
		
		//open [[alpha]] 
		onlinePhaseSimulation.schedule(new Open(onlinePhaseSimulation, startTime, Parameters.VIRTUAL_HOST, start, end)) ;

		//Fcom opens Y_i s so that every one can check the MAC
		for (int i = start; i < end; i++) 
		{
			onlinePhaseSimulation.schedule(new OpenCommit(onlinePhaseSimulation, startTime, i, start, end) );
		}
				

		//Players check Alpha*(a + Sigma(e_j*sigma_j)) = sigma Y_i
		for (int i = start; i < end; i++){
			
			onlinePhaseSimulation.schedule(new Computation(onlinePhaseSimulation, startTime, i, Parameters.COMPUTATION_COST1));
		}
		
		//Fcom opens Y_i s so that every one can check the MAC
		for (int i = start; i < end; i++) 
		{
			onlinePhaseSimulation.schedule(new OpenCommit(onlinePhaseSimulation, startTime, i, start, end));
		}
		
		
		//each party computes alpha*(y+sigma) = Sigma(y(y))
		for (int i = start; i < end; i++)
		{
			onlinePhaseSimulation.schedule(new Computation(onlinePhaseSimulation, startTime, i, Parameters.COMPUTATION_COST1));
		}
		
		System.out.println("Output Phase finished!");

		return false ; 
	}

}
