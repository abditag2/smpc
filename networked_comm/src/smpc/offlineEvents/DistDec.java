package smpc.offlineEvents;

import smpc.Parameters;
import smpc.abstractlibrary.Event;
import smpc.abstractlibrary.Simulation;
import smpc.events.BroadCast;
import smpc.events.Computation;

/**
 * Created by tanish on 5/13/15.
 */
public class DistDec extends Event {

    public DistDec(Simulation simulation,  double startTime , int hostID, int start, int end)
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
         DistDec(c)
         Every player Pi calls SHE.Decrypt 2 times
         Every player Pi broadcasts a value
         */

        for (int i = 0 ; i < Parameters.getNumberOfParties(); i++){
            simulation.schedule( new Computation(simulation, simulation.time, i, Parameters.ComputationType.SHE_DECRYPT, 2));
            simulation.schedule( new BroadCast(simulation, simulation.time, i, 0, Parameters.getNumberOfParties()));
        }
        return false;
    }
}
