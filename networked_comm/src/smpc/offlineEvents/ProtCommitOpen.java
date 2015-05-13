package smpc.offlineEvents;

import smpc.Parameters;
import smpc.abstractlibrary.Event;
import smpc.abstractlibrary.Simulation;
import smpc.events.BroadCast;
import smpc.events.Computation;

/**
 * Created by tanish on 5/13/15.
 */
public class ProtCommitOpen extends Event {


    public ProtCommitOpen(Simulation simulation, double startTime, int hostID, int start, int end)
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
         *
         Open(tv):
         player Pi broadcasts (v||r, i, tv)
         all players compute H(v||r) and check whether c = H(v||r)
         players accept if and only if above check passes

         */

        simulation.schedule( new BroadCast(simulation, simulation.time, hostID, 0, Parameters.getNumberOfParties()));

        for(int i = 0 ; i < Parameters.getNumberOfParties() ; i ++ ){
            simulation.schedule( new Computation(simulation, simulation.time, hostID, Parameters.ComputationType.HASH_GENERATION));
            simulation.schedule( new Computation(simulation, simulation.time, hostID, Parameters.ComputationType.HASH_GENERATION));
        }


        return false;
    }
}
