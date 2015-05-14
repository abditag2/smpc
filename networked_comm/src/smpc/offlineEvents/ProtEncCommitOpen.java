package smpc.offlineEvents;

import com.sun.xml.internal.rngom.digested.DDataPattern;
import smpc.Parameters;
import smpc.abstractlibrary.Event;
import smpc.abstractlibrary.Simulation;
import smpc.events.Computation;

/**
 * Created by tanish on 5/13/15.
 */
public class ProtEncCommitOpen extends Event {


    public ProtEncCommitOpen(Simulation simulation, double startTime, int hostID, int start, int end)
    {
        this.simulation = simulation;
        this.startTime = startTime ;
        this.hostID  = hostID;

        this.duration = 0.0 ;
        this.type = "ProtEncCommitOpen";
        this.start = start ;
        this.end = end ;
    }

    @Override
    public boolean execute() {

        /**
         ProtEncCommitOpen:
         Every player Pi calls ProtCommit.Open() c-1 times
         Every player Pi calls SHE.Encrypt n(c-1) times
         */

        for(int i = 0 ; i < Parameters.getNumberOfParties(); i++){
            for(int k =0 ; k < Parameters.N_CIPHER -1 ; k++){
                simulation.schedule(new ProtCommitOpen(simulation, simulation.time, i, 0, Parameters.getNumberOfParties()));
            }
        }

        simulation.doAllEvents();

        simulation.doAllEvents();
        for(int k = 0 ; k < Parameters.getNumberOfParties() ; k++){
            simulation.schedule( new Computation(simulation, simulation.time, k,
                    Parameters.ComputationType.SHE_ENCRYPT, Parameters.N_CIPHER * Parameters.getNumberOfParties()));
        }

        return false;
    }
}
