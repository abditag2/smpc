package smpc.events.offlineEvents;

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


        //TODO make sure n is number of parties
        /**
         * Class ProtReshare(c, enc)
             Call EncCommit()
             Every player Pi calls SHE.Add() n times (this would first need
                to call SHE.Encrypt() and then add the resulting ciphertexts, the cost of encryption is not part of the protocol)
             Call DistDec
             If enc = NewCiphertext
                 Call SHE.Encryption()
                Call SHE.Add() n times
         *
         */
        System.out.println("Reshare 0");
        for(int i = 0 ; i < Parameters.getNumberOfParties() ; i++){
            simulation.schedule(new ProtEncCommitCommit(simulation, startTime, i, 0, Parameters.getNumberOfParties()));
            simulation.doAllEvents();
            System.out.println("Reshare 0." + i);
        }
        System.out.println("Reshare 1");

        for(int i = 0 ; i < Parameters.getNumberOfParties() ; i++){
            simulation.schedule( new Computation(simulation, simulation.time, i,
                    Parameters.ComputationType.SHE_ADD, Parameters.getNumberOfParties()));
        }
        simulation.doAllEvents();
        System.out.println("Reshare 2");

        simulation.schedule(new DistDec(simulation, simulation.time, Parameters.VIRTUAL_HOST, 0, Parameters.getNumberOfParties()));
        simulation.doAllEvents();
        System.out.println("Reshare 3");

        if(Parameters.NEW_CIPHER_TEXT == true){
            for(int i = 0 ; i < Parameters.getNumberOfParties() ; i++){
                simulation.schedule( new Computation(simulation, simulation.time, i,
                        Parameters.ComputationType.SHE_ADD, Parameters.getNumberOfParties()));
                simulation.schedule( new Computation(simulation, simulation.time, i, Parameters.ComputationType.SHE_ENCRYPT, 1));
            }
        }
        simulation.doAllEvents();
        System.out.println("Reshare 4");

        return false;
    }
}
