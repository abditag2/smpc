package smpc.events;

import smpc.abstractlibrary.Parameters;
import smpc.library.OnlinePhaseSimulation;
import smpc.abstractlibrary.Event;

public class Multiplication extends Event {

	public Multiplication(OnlinePhaseSimulation onlinePhaseSimulation, double startTime, int hostID , int start, int end){

		this.onlinePhaseSimulation = onlinePhaseSimulation;
		this.startTime = startTime ; 
		this.hostID = hostID ;
		
		this.duration = 20.0 ;
		this.type = "Multiplication";
		
		this.start = start ; 
		this.end = end ; 

	}
	

	@Override
	public boolean execute() {
			/**
			 * Multiplication
			 */
			
			//FIrst everyone compute <a> − <f>  = ro and <b> - <g> = theta
			//Send the shares to everyone so that every one has ro and theta!
			// then every one computes this <c> − <h> − σ ·<f> − ρ · <g> − σ · ρ
			//send their shares of the above to one party (p1) and he verifies that it is zero
			//Now the resutls are verified
			//Parties open <x> − <a> to get epsilone and <y> − <b> to get δ
			//they compute  <c> + epsilone * <b> +δ * <a> + epsilone*δ


			
			//FIrst everyone compute <a> − <f>  = ro and <b> - <g> = theta
			for (int i = start; i < end; i++) 
			{
				onlinePhaseSimulation.schedule(new Computation(onlinePhaseSimulation, startTime, i, Parameters.COMPUTATION_COST1));
			}
			
			//Open the results of the previous computation! Send the shares to everyone so that every one has ro and theta!
			onlinePhaseSimulation.schedule(new Open(onlinePhaseSimulation, startTime, Parameters.VIRTUAL_HOST, start, end));
			
			// then every one computes this <c> − <h> − σ ·<f> − ρ · <g> − σ · ρ
			for (int i = start; i < end; i++) 
			{
				onlinePhaseSimulation.schedule(new Computation(onlinePhaseSimulation, startTime, i, Parameters.COMPUTATION_COST1));
			}
			
			//send their shares of the above to one party (p1) and he verifies that it is zero
			onlinePhaseSimulation.schedule(new PartiallyOpen(onlinePhaseSimulation, startTime, Parameters.VIRTUAL_HOST, 1, start, end));
			
			//Now the resutls are verified
			//Parties open <x> − <a> to get epsilone and <y> − <b> to get δ
			onlinePhaseSimulation.schedule(new Open(onlinePhaseSimulation, startTime, Parameters.VIRTUAL_HOST, start, end));
			
			//they compute  <c> + epsilone * <b> +δ * <a> + epsilone*δ
			for (int i = 0; i < Parameters.getNumberOfParties(); i++) 
			{
				onlinePhaseSimulation.schedule(new Computation(onlinePhaseSimulation, startTime, i, Parameters.COMPUTATION_COST1));
			}		
			
					
			
		return false;
	}

}
