package smpc.offlineEvents;

import smpc.Parameters;
import smpc.abstractlibrary.Event;
import smpc.abstractlibrary.Simulation;
import smpc.events.BroadCast;
import smpc.events.Computation;

/**
 * Created by tanish on 5/12/15.
 */
public class ProtEncCommitCommit extends Event{

    public ProtEncCommitCommit(Simulation simulation, double startTime, int hostID, int start, int end)
    {
        this.simulation = simulation;
        this.startTime = startTime ;
        this.hostID  = hostID;

        this.duration = 0.0 ;
        this.type = "Share";
        this.start = start ;
        this.end = end ;
    }

    @Override
    public boolean execute() {


        /**
         Class ProtEncCommitCommit:
         Commit:
         Each player Pi samples a uniform integer ei from a set {1,2,...,c} and calls ProtCommitCommit.Commit(ei)
         For j = 1,...,c
             Every player Pi samples a random seed si,j and calls ProtCommitCommit.Commit(si,j)
             Every player Pi encrypts a message mi,j using AES with key si,j
             Every player Pi call SHE.Encrypt to compute cti,j = SHE.Encrypt(mi,j)
             Every player Pi broadcasts cti,j
         Every player Pi calls ProtCommitCommit.Open()
         All players do n+1 modular additions
         */

        simulation.schedule(new ProtCommitCommit(simulation, simulation.time, hostID, 0, Parameters.getNumberOfParties()));
        simulation.doAllEvents();
        for (int i = 0 ; i < Parameters.N_CIPHER ; i++){
            simulation.schedule(new ProtCommitCommit(simulation, simulation.time, hostID, 0, Parameters.getNumberOfParties()));
        }

        for (int i = 0 ; i < Parameters.N_CIPHER ; i++){
            simulation.schedule( new Computation(simulation, simulation.time, hostID, Parameters.ComputationType.AES_ENCRYPT));
            simulation.schedule( new Computation(simulation, simulation.time, hostID, Parameters.ComputationType.SHE_ENCRYPT));
            simulation.schedule( new BroadCast(simulation, simulation.time, hostID, 0, Parameters.getNumberOfParties()));
        }

        simulation.doAllEvents();

        for(int i = 0 ; i < Parameters.getNumberOfParties() ; i ++){
            simulation.schedule(new ProtCommitOpen(simulation, simulation.time, hostID, 0, Parameters.getNumberOfParties()));
        }
        simulation.doAllEvents();

        for (int i = 0 ; i < Parameters.getNumberOfParties() ; i++){
            simulation.schedule( new Computation(simulation, simulation.time, hostID, Parameters.ComputationType.ADDITION));
        }



        return false;
    }
}
