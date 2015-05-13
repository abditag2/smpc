package smpc.offlineEvents;

import smpc.abstractlibrary.Event;
import smpc.abstractlibrary.Simulation;

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

        //TODO
        return false;
    }
}
