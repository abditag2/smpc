package smpc.abstractlibrary;

import smpc.library.ListQueue;

/**
 * Created by tanish on 5/12/15.
 */
public class Simulation {

    ListQueue eventQueue ;
    public double time;

    public Simulation(){
        eventQueue = new ListQueue() ;
        this.time = 0;
    }

    public double getLastFinishingTimeForAllNodes(){
        return eventQueue.getLastFinishingTimeForAllNodes();
    }

    public void schedule(Event e){
        eventQueue.insert(e);
    }

    public void doAllEvents() {
        //always events will execute in the order of earliest deadline
        while (eventQueue.size() > 0){
            Event e = eventQueue.removeFirst();
            e.execute() ;
        }
        return;
    }

    public void setAllFinishingTimes(double time){
        eventQueue.setAllFinishingTimes(time);
    }

}



