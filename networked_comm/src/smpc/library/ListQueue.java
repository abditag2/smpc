package smpc.library;

import smpc.Parameters;
import smpc.abstractlibrary.Event;
import smpc.communicationTree.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class ListQueue {
	
	public LinkedList<Event> q;
	public ArrayList<NodeInfo> nodeInfo ; 

	public ListQueue (){
		
		//initialize the nodeInof Array
		
		nodeInfo = new ArrayList<NodeInfo>() ; 
		for(int  i = 0 ; i < Parameters.getNumberOfParties() ; i++){
			nodeInfo.add(i, new NodeInfo(i));
		}
		
		q = new LinkedList<Event>() ;
		
	}
	
	
	
	public void insert(Event event) {

		//virtual event not on any node (with ID < 0)
		if (event.getHostID() < 0 ){
			event.execute();
			return ; 
		}

		//real Event on a node!
		if ( nodeInfo.get(event.getHostID()).getFinishTime() < event.getStartTime() ){
			nodeInfo.get(event.getHostID()).setFinishTime(event.getFinishingTime());
		}
		else {
			event.setStartTime(nodeInfo.get(event.getHostID()).getFinishTime());
			nodeInfo.get(event.getHostID()).setFinishTime(event.getFinishingTime());
		}
		
//		System.out.println("schedule type: " + event.type  +" st: " + event.getStartTime()  + " ft: " + event.getFinishingTime() + " hostID: " + event.getHostID());
		
		q.add(event);

		//TODO improve the sorting with less complexity
		Collections.sort(q);
		
	}
	
	
	public Event removeFirst(){
		return q.remove();
	}

	public int size(){
		return q.size();
	}

	public double getLastFinishingTimeForAllNodes(){
		double time = Double.MIN_VALUE;

		for(NodeInfo node: nodeInfo){
			time = Math.max(node.getFinishTime(), time);
		}

		return time;
	}

	public void setAllFinishingTimes(double time){

		for(NodeInfo node: nodeInfo){
			node.finishTimes = time ;
		}
	}

}
