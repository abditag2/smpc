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

        for (int hostID = 0 ; hostID < Parameters.getNumberOfParties(); hostID++){
            simulation.schedule( new Computation(simulation, simulation.time, hostID, Parameters.ComputationType.SHE_DECRYPT));
            simulation.schedule( new Computation(simulation, simulation.time, hostID, Parameters.ComputationType.SHE_DECRYPT));
            simulation.schedule( new BroadCast(simulation, simulation.time, hostID, 0, Parameters.getNumberOfParties()));
        }
        return false;
    }
}
