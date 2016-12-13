import uiuc.smpc.abstractlibrary.Parameters;
import uiuc.smpc.events.BroadCast;
import uiuc.smpc.events.Commit;
import uiuc.smpc.events.Computation;
import uiuc.smpc.events.InputPhase;
import uiuc.smpc.events.Open;
import uiuc.smpc.events.OpenCommit;
import uiuc.smpc.events.OutputPhase;
import uiuc.smpc.events.PartiallyOpen;
import uiuc.smpc.events.Send;
import uiuc.smpc.events.ZeroKnowledgeProver;
import uiuc.smpc.library.Simulation;
import uiuc.smpc.problemEvents.ClusterCoinToss;
import uiuc.smpc.problemEvents.schedulePreProcessingForSPDZ;
import uiuc.smpc.problemEvents.schedulePreProcessingForScMPC;
import uiuc.smpc.voting.votingCluster;
import uiuc.smpc.voting.votingSMPC;
import uiuc.smpc.voting.votingSPDZ;

public class Run {
	
	public static void main (String args[]){

		Parameters.count = 0 ;
		
		System.out.println("Simulation Starting");
		Simulation simulation = new Simulation();	

		double timeToSchedule = 0 ;
		
		
		simulation.schedule(new votingSPDZ(simulation, timeToSchedule, Parameters.VIRTUAL_HOST, 0, Parameters.getNumberOfParties()));
		//simulation.schedule(new votingSMPC(simulation, timeToSchedule, Parameters.VIRTUAL_HOST, 0, Parameters.getNumberOfParties()));
	

		simulation.doAllEvents() ; 
		System.out.println("Simulation Ends");
		System.out.println("Recieves are " + Parameters.count);
	}


}
