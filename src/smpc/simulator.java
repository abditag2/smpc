package smpc;


import java.util.*;

import smpc.Abstracts.*;

public class simulator {

	public ArrayList<Node> nodes;
	public void initialize() {
		nodes = new ArrayList<Node>();
	}
	
	public void simulate() {
		
		while (true) {
			//First, pick all the nodes and run the protocol function
			for(Node node : nodes) {
				node.protocol();
			}
			//Then, take all the nodes and run the 	packet recieve function on all of them
			for(Node node:nodes) {
				node.schedlueIncomingPackets();
			}			
		}
	}
}
