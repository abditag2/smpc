package smpc;


import java.util.*;

import smpc.Abstracts.*;

public class simulator {

	public ArrayList<Node> nodes;
	public Config config;
	
	public void initialize(Config config) {
		nodes = new ArrayList<Node>();
		this.config = config ; 
	}
	
	public void simulate() {
		
		int currentTime = 0;
		int endOfCycle = currentTime + this.config.cycleLength;
		
		while (true) {
			//First, pick all the nodes and run the protocol function
			for(Node node : nodes) {
				node.protocol();
			}
			//Then, take all the nodes and run the 	packet recieve function on all of them
			for(Node node:nodes) {
				node.schedlueIncomingPackets(currentTime, endOfCycle);
			}			
			
			currentTime = endOfCycle;
			endOfCycle = endOfCycle + this.config.cycleLength;
			
			//TODO: insert the condition for the ending and also set the counters
		}
	}
	
	public static void main (String[] args) {
		
	}
}
