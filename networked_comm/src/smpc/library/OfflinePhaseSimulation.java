package smpc.library;

import smpc.abstractlibrary.Simulation;

public class OfflinePhaseSimulation extends Simulation{

    ListQueue eventQueue ;
    public double time;

    public OfflinePhaseSimulation(){
        eventQueue = new ListQueue() ;
        this.time = 0;

    }

}
