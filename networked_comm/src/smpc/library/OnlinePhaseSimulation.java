package smpc.library;

import smpc.abstractlibrary.Parameters;
import smpc.abstractlibrary.Event;

public class OnlinePhaseSimulation {
	
	ListQueue eventQueue ;
	public double time;
	
	public OnlinePhaseSimulation(){
		eventQueue = new ListQueue() ;
		this.time = 0;
		
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

}
