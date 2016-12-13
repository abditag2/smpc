package uiuc.smpc.library;

import java.util.HashMap;

import uiuc.smpc.abstractlibrary.Event;
import uiuc.smpc.abstractlibrary.Parameters;

public class Simulation {
	
	ListQueue eventQueue ; 
	
	public Simulation(){
		eventQueue = new ListQueue() ;
		
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
		
		
		
		
	}

}
