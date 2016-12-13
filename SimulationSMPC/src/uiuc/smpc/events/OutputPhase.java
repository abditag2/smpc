package uiuc.smpc.events;

import uiuc.smpc.abstractlibrary.Event;
import uiuc.smpc.abstractlibrary.Parameters;
import uiuc.smpc.library.Simulation;

public class OutputPhase extends Event{

	public OutputPhase(Simulation simulation, double startTime, int hostID , int start, int end){

		this.simulation = simulation ;
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
		
		simulation.schedule(new Open(simulation, startTime, Parameters.VIRTUAL_HOST, start, end)) ; 

		//Compute Sigma(e_j * a_j)
		for (int i = start; i < end; i++) {
			simulation.schedule(new Computation(simulation, startTime, i, Parameters.COMPUTATION_COST1));
		}
		simulation.doAllEvents() ; 
		System.out.println("commiting  started!");
		
		//Each party commits to its final y_i
		for (int i = start; i < end; i++) 
		{
			simulation.schedule(new Commit(simulation, startTime, i, start, end));
		}
		simulation.doAllEvents() ; 
		System.out.println("commiting  finished!");
		
		//open [[alpha]] 
		simulation.schedule(new Open(simulation, startTime, Parameters.VIRTUAL_HOST, start, end)) ; 

		//Fcom opens Y_i s so that every one can check the MAC
		for (int i = start; i < end; i++) 
		{
			simulation.schedule(new OpenCommit(simulation, startTime, i, start, end) );
		}
				

		//Players check Alpha*(a + Sigma(e_j*sigma_j)) = sigma Y_i
		for (int i = start; i < end; i++){
			
			simulation.schedule(new Computation(simulation, startTime, i, Parameters.COMPUTATION_COST1));
		}
		
		//Fcom opens Y_i s so that every one can check the MAC
		for (int i = start; i < end; i++) 
		{
			simulation.schedule(new OpenCommit(simulation, startTime, i, start, end));
		}
		
		
		//each party computes alpha*(y+sigma) = Sigma(y(y))
		for (int i = start; i < end; i++)
		{
			simulation.schedule(new Computation(simulation, startTime, i, Parameters.COMPUTATION_COST1));
		}
		
		System.out.println("Output Phase finished!");

		return false ; 
	}

}
