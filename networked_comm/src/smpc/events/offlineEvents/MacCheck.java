package smpc.events.offlineEvents;

import smpc.Parameters;
import smpc.abstractlibrary.Event;
import smpc.abstractlibrary.Simulation;
import smpc.events.Computation;

/**
 * Created by tanish on 5/13/15.
 */
public class MacCheck extends Event {

    public int numberOfMultiplications;

    public MacCheck(Simulation simulation, double startTime, int hostID ,  int start, int end, int numberOfMultiplications)
    {
        this.simulation = simulation;
        this.startTime = startTime ;
        this.hostID = hostID ;

        this.duration = 0.0;
        this.type = "OfflinePhase";

        this.start = start;
        this.end = end;
        this.numberOfMultiplications = numberOfMultiplications;
    }



    @Override
    public boolean execute() {
        /**
         * Class MACCheck:
             Check(a1,...,at):
                 Every player Pi generates a random number
                 Every player Pi calls ProtCommit.Commit()
                 Every player Pi calls ProtCommit.Open()
                 Compute bitwise XOR of n integers (r1 XOR r2 XOR …. rn)
                 Every player Pi generate t random numbers
                 Every player Pi computes 2t additions and 3t multiplications
                 A single player calls ProtCommit.Commit()
                 Every player Pi calls ProtCommit.Open()
                 Compute n additions
         */

        System.out.println("MacCheck started");

        for(int i = 0 ; i < Parameters.getNumberOfParties() ; i++){
            simulation.schedule( new Computation(simulation, simulation.time, i, Parameters.ComputationType.RANDOM_GEN, 1));
            simulation.schedule(new ProtCommitCommit(simulation, simulation.time, i, 0, Parameters.getNumberOfParties()));
            simulation.doAllEvents();
            System.out.println("mcCheck 0."  + i);
        }
        System.out.println("mcCheck 0");
        for(int i = 0 ; i < Parameters.getNumberOfParties() ; i++){
            simulation.schedule(new ProtCommitOpen(simulation, simulation.time, i, 0, Parameters.getNumberOfParties()));
        }
        simulation.doAllEvents();

        for(int i = 0 ; i < Parameters.getNumberOfParties() ; i++){
            simulation.schedule( new Computation(simulation, simulation.time, i, Parameters.ComputationType.XOR, Parameters.getNumberOfParties()));
        }
        System.out.println("mcCheck 1");
        for(int i = 0 ; i < Parameters.getNumberOfParties() ; i++){
            simulation.schedule( new Computation(simulation, simulation.time, i,
                    Parameters.ComputationType.RANDOM_GEN, Parameters.getNumberOfMultiplications()));

            simulation.schedule( new Computation(simulation, simulation.time, i,
                    Parameters.ComputationType.ADDITION, 2*Parameters.getNumberOfMultiplications()));

            simulation.schedule( new Computation(simulation, simulation.time, i,
                    Parameters.ComputationType.MULITIPLICATION, 3*Parameters.getNumberOfMultiplications()));

        }

        simulation.schedule(new ProtCommitCommit(simulation, simulation.time, 0, 0, Parameters.getNumberOfParties()));

        System.out.println("mcCheck 2");
        for(int i = 0 ; i < Parameters.getNumberOfParties() ; i++){
            simulation.schedule(new ProtCommitOpen(simulation, simulation.time, i, 0, Parameters.getNumberOfParties()));
            simulation.schedule( new Computation(simulation, simulation.time, i,
                    Parameters.ComputationType.ADDITION, Parameters.getNumberOfParties()));

        }
        simulation.doAllEvents();

        return false;
    }
}
