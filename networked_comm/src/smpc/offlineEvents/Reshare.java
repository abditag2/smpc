package smpc.offlineEvents;

import smpc.Parameters;
import smpc.abstractlibrary.Event;
import smpc.abstractlibrary.Simulation;
import smpc.events.Computation;

/**
 * Created by tanish on 5/12/15.
 */
public class Reshare extends Event {


    public Reshare(Simulation simulation,  double startTime , int hostID, int start, int end)
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
         * Class ProtReshare(c, enc)
         Call EncCommit()
         Every player Pi calls SHE.Add() n times (this would first need to call SHE.Encrypt() and then add the resulting ciphertexts, the cost of encryption is not part of the protocol)
         Call DistDec
         If enc = NewCiphertext
         Call SHE.Encryption()
         Call SHE.Add() n times
         *
         */

        simulation.schedule(new ProtEncCommit(simulation, startTime, hostID, 0, Parameters.getNumberOfParties()));
        for (int i = 0; i < Parameters.getNumberOfParties() ; i++)
        {
            simulation.schedule( new Computation(simulation, simulation.time, hostID, Parameters.ComputationType.SHE_ADD));
        }

        simulation.schedule( new DistDec(simulation, simulation.time, hostID, 0, Parameters.getNumberOfParties()));

        if(Parameters.NEW_CIPHER_TEXT == true){
            simulation.schedule( new Computation(simulation, simulation.time, hostID, Parameters.ComputationType.SHE_ADD));
            simulation.schedule( new Computation(simulation, simulation.time, hostID, Parameters.ComputationType.SHE_ENCRYPT));
        }
        return false;
    }
}
