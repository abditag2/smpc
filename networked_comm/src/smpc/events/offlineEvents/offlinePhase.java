package smpc.events.offlineEvents;

import smpc.abstractlibrary.Event;
import smpc.Parameters;
import smpc.abstractlibrary.Simulation;
import smpc.events.BroadCast;
import smpc.events.Computation;


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

        System.out.println("Input Production(nI) started");

        for (int j = 0 ; j < Math.ceil(2 * Parameters.getNumberOfParties() / Parameters.M); j++){
            for(int k = 0 ; k < Math.ceil(Parameters.N_M) ; k++){
                simulation.schedule( new Computation(simulation, startTime, j, Parameters.ComputationType.RANDOM_GEN, 1));
                simulation.schedule(new Computation(simulation, startTime, j, Parameters.ComputationType.SHE_ENCRYPT, 1));
            }
        }

        System.out.println("Triples(nm) started");
        simulation.doAllEvents();


        for (int j = 0 ; j < Parameters.getNumberOfParties(); j++){
            for(int k = 0 ; k < Math.ceil(Parameters.N_M) ; k++){
                simulation.schedule( new BroadCast(simulation, simulation.time, j, 0, Parameters.getNumberOfParties()));
                simulation.doAllEvents();
            }
        }

        System.out.println("Triples(nm) started2");
        //Doing twice reshare



        //DO  a reshare
        simulation.setAllFinishingTimes(simulation.getLastFinishingTimeForAllNodes() + Parameters.RESHARE_TIMES);
        //Instead of the following we did the above since reshare is very expensive!
//
//        System.out.println("Triples(nm) started3");
//        for (int j = 0 ; j < Parameters.getNumberOfParties(); j++){
//            for(int k = 0 ; k < Math.ceil(Parameters.N_M) ; k++){
//                simulation.schedule(new Reshare(simulation, startTime, Parameters.VIRTUAL_HOST, 0, Parameters.getNumberOfParties()));
//                simulation.schedule(new Reshare(simulation, startTime, Parameters.VIRTUAL_HOST, 0, Parameters.getNumberOfParties()));
//            }
//        }

        simulation.doAllEvents();
        System.out.println("Triples(nm) started4");

        for (int k = 0 ; k < Parameters.getNumberOfParties(); k++){
            simulation.schedule( new Computation(simulation, simulation.time, k, Parameters.ComputationType.SHE_MULTIPLY, 1));
        }
        simulation.doAllEvents();

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

        System.out.println("CheckTriples() started");



        for(int k = 0 ; k < Math.ceil(4*Parameters.N_M/Parameters.M) ; k++) {

            simulation.schedule(new ProtEncCommitCommit(simulation, simulation.time, Parameters.VIRTUAL_HOST, 0, Parameters.getNumberOfParties()));
            simulation.doAllEvents();
            simulation.schedule(new ProtEncCommitCommit(simulation, simulation.time, Parameters.VIRTUAL_HOST, 0, Parameters.getNumberOfParties()));
            simulation.doAllEvents();

            for(int j = 0 ; j < Parameters.getNumberOfParties() ; j++){
                simulation.schedule( new Computation(simulation, simulation.time, j, Parameters.ComputationType.SHE_ADD, 2 * Parameters.getNumberOfParties()));
                simulation.schedule( new Computation(simulation, simulation.time, j, Parameters.ComputationType.SHE_MULTIPLY, 1));
            }

            simulation.setAllFinishingTimes(simulation.getLastFinishingTimeForAllNodes() + Parameters.RESHARE_TIMES);

            for(int j = 0 ; j < Parameters.getNumberOfParties() ; j++){
                simulation.schedule( new Computation(simulation, simulation.time, j, Parameters.ComputationType.SHE_MULTIPLY, 3));
            }
            simulation.doAllEvents();

        }


//        4*Parameters.N_M times Reshare
        for(int k = 0 ; k < Math.ceil(4*Parameters.N_M/Parameters.M) ; k++) {
            simulation.setAllFinishingTimes(simulation.getLastFinishingTimeForAllNodes() + Parameters.RESHARE_TIMES);
            simulation.setAllFinishingTimes(simulation.getLastFinishingTimeForAllNodes() + Parameters.RESHARE_TIMES);
            simulation.setAllFinishingTimes(simulation.getLastFinishingTimeForAllNodes() + Parameters.RESHARE_TIMES);
        }

        simulation.doAllEvents();

            /**
             *
             CheckTriples()
             For k in {1,...,nm}
             Every player broadcasts 3 values (there is some constant amount of addition/subtraction work for each player, but I guess we can ignore that)
             run MacCheck!
             */

            System.out.println("Final Broad Cast");

        int numberOfBroadCasts = Parameters.N_M * Parameters.getNumberOfParties() ;

        double before = simulation.getLastFinishingTimeForAllNodes();
        simulation.schedule(new BroadCast(simulation, simulation.time, 0, 0, Parameters.getNumberOfParties()));
        simulation.doAllEvents();
        double after = simulation.getLastFinishingTimeForAllNodes();

        System.out.println("time of last broadcasts: " + numberOfBroadCasts * (after - before) + " " + (after - before));
        simulation.setAllFinishingTimes(simulation.getLastFinishingTimeForAllNodes() + numberOfBroadCasts * (after - before));

            return false;
        }
    }
