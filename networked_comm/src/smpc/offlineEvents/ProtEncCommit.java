package smpc.offlineEvents;

import smpc.abstractlibrary.Event;
import smpc.abstractlibrary.Simulation;

/**
 * Created by tanish on 5/12/15.
 */
public class ProtEncCommit extends Event{

    public ProtEncCommit(Simulation simulation,  double startTime , int hostID, int start, int end)
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
        //TODO fill these events
        return false;
    }
}
