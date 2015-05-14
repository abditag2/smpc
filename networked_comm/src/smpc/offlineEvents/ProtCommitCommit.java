package smpc.offlineEvents;

import com.sun.xml.internal.rngom.digested.DDataPattern;
import smpc.Parameters;
import smpc.abstractlibrary.Event;
import smpc.abstractlibrary.Simulation;
import smpc.events.BroadCast;
import smpc.events.Computation;

/**
 * Created by tanish on 5/13/15.
 */
public class ProtCommitCommit extends Event{

    public ProtCommitCommit(Simulation simulation, double startTime, int hostID, int start, int end)
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
         Commit(v):
             generate a random number r
             hash v||r to get c = H(v||r) (use SHA256 hash here)
             broadcasts c, i, tv, where i is index of player (a small integer), tv is handle for commitment (for our purpose tv is just an integer)
         */

        for (int i = 0 ; i < Parameters.getNumberOfParties() ; i++){
            simulation.schedule( new Computation(simulation, simulation.time, i, Parameters.ComputationType.RANDOM_GEN, 1));
            simulation.schedule( new Computation(simulation, simulation.time, i, Parameters.ComputationType.HASH_GENERATION, 1));
            simulation.schedule( new BroadCast(simulation, simulation.time, i, 0, Parameters.getNumberOfParties()));
        }

        return false;
    }
}
