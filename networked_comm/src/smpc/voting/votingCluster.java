package smpc.voting;

import smpc.abstractlibrary.Event;
import smpc.Parameters;
import smpc.abstractlibrary.Simulation;
import smpc.events.Computation;

public class votingCluster extends Event{

	public  votingCluster(Simulation simulation, double startTime, int hostID ,  int start, int end)
	{
		this.simulation = simulation;
		this.startTime = startTime ; 
		this.hostID = hostID ;
		
		this.duration = 20.0 ;
		this.type = "votingCluster";
		
		this.start = start ; 
		this.end = end ; 

	}
	
	@Override
	public boolean execute() {


		//TODO Make sure voting is right
		for (int i = start ; i < end; i++ ) 
		{
			for(int j = start ; j < end ; j++) {
				simulation.schedule(new Computation(simulation, startTime, i, Parameters.ComputationType.ADDITION)) ;
				simulation.schedule(new Computation(simulation, startTime, i, Parameters.ComputationType.ADDITION)) ;
			}
		}
		
		System.out.println("computation is done!");
		return false;
	}

}
