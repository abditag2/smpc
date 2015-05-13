package smpc.offline;

import smpc.abstractlibrary.Event;
import smpc.Parameters;
import smpc.abstractlibrary.Simulation;
import smpc.events.BroadCast;
import smpc.events.Computation;
import smpc.offlineEvents.ProtEncCommitCommit;
import smpc.offlineEvents.Reshare;


public class offlinePhase extends Event {

    public offlinePhase(Simulation simulation, double startTime, int hostID ,  int start, int end)
    {
        this.simulation = simulation;
        this.startTime = startTime ;
        this.hostID = hostID ;

        this.duration = 0.0;
        this.type = "OfflinePhase";

        this.start = start;
        this.end = end;

    }

    @Override
    public boolean execute(){

        /**
         For j in {1,...,n} and k in {1,...,ceil(2.nI/m)}
             Player j Generate a random r
             Player j Call SHE.Encrypt
             Player j does a broadcast
             Call Reshare(~,NoNewCiphertext) two times
             All players compute SHE.Multiply
         */

        for (int hostID = 0 ; hostID < Parameters.getNumberOfParties(); hostID++){
            for(int k = 0 ; k < Math.ceil(Parameters.NUMBER_OF_TRIPLETES_TOBE_GENERATED) ; k++){
                simulation.schedule( new Computation(simulation, startTime, hostID, Parameters.ComputationType.RANDOM_GEN));
                simulation.schedule( new Computation(simulation, startTime, hostID, Parameters.ComputationType.SHE_ENCRYPT));
            }
        }


        simulation.doAllEvents();


        for (int hostIDToBe = 0 ; hostIDToBe < Parameters.getNumberOfParties(); hostIDToBe++){
            for(int k = 0 ; k < Math.ceil(Parameters.NUMBER_OF_TRIPLETES_TOBE_GENERATED) ; k++){
                simulation.schedule( new BroadCast(simulation, simulation.time, hostIDToBe, 0, Parameters.getNumberOfParties()));
            }
        }

        simulation.doAllEvents();


        simulation.schedule(new Reshare(simulation, startTime, hostID, 0, Parameters.getNumberOfParties()));
        simulation.schedule(new Reshare(simulation, startTime, hostID, 0, Parameters.getNumberOfParties()));

        for (int hostID = 0 ; hostID < Parameters.getNumberOfParties(); hostID++){
            simulation.schedule( new Computation(simulation, simulation.time, hostID, Parameters.ComputationType.SHE_MULTIPLY));
        }

        simulation.doAllEvents();

        //TODO figure out what is nm and m

        /**
         * Triples(nm)
         For k in {1,..., ceil(4.nm/m)}
             Run ProtEncCommitCommit 2 times

         All players call SHE.Add() 2n times
         All parties call SHE.Multiply()
         Call Reshare(~,NewCiphertext)
         All parties call SHE.Multiply three times
         Call Reshare(~,NoNewCiphertext) three times
         */

        for(int k = 0 ; k < Parameters.getNumberOfParties() ; k++){
            simulation.schedule(new ProtEncCommitCommit(simulation, simulation.time, hostID, 0, Parameters.getNumberOfParties()));
        }
        simulation.doAllEvents();

        for(int k = 0 ; k < Parameters.getNumberOfParties() ; k++){
            simulation.schedule( new Computation(simulation, simulation.time, hostID, Parameters.ComputationType.SHE_ADD));
            simulation.schedule( new Computation(simulation, simulation.time, hostID, Parameters.ComputationType.SHE_MULTIPLY));
        }

        simulation.schedule(new Reshare(simulation, startTime, hostID, 0, Parameters.getNumberOfParties()));

        for(int k = 0 ; k < Parameters.getNumberOfParties() ; k++){
            simulation.schedule( new Computation(simulation, simulation.time, hostID, Parameters.ComputationType.SHE_MULTIPLY));
            simulation.schedule( new Computation(simulation, simulation.time, hostID, Parameters.ComputationType.SHE_MULTIPLY));
            simulation.schedule( new Computation(simulation, simulation.time, hostID, Parameters.ComputationType.SHE_MULTIPLY));
        }
        simulation.doAllEvents();


        simulation.schedule(new Reshare(simulation, startTime, hostID, 0, Parameters.getNumberOfParties()));
        simulation.schedule(new Reshare(simulation, startTime, hostID, 0, Parameters.getNumberOfParties()));
        simulation.schedule(new Reshare(simulation, startTime, hostID, 0, Parameters.getNumberOfParties()));
        simulation.doAllEvents();

        /**
         *
         CheckTriples()
         For k in {1,...,nm}
         Every player broadcasts 3 values (there is some constant amount of addition/subtraction work for each player, but I guess we can ignore that)
         */

        for(int k = 0 ; k < Parameters.getNumberOfParties() ; k++){
            simulation.schedule( new BroadCast(simulation, simulation.time, k, 0, Parameters.getNumberOfParties()));
        }

        return false;
    }

}
