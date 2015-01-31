package smpc.Abstracts;

import smpc.Config;

import java.util.LinkedList;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;


public class Node {
	public int ID;
	public boolean honest;
	public boolean failed;
	
	public LinkedList<NetworkPacket> packetsReceived;
	public TreeSet<NetworkPacket> packetQToBeRecieved;
	
	
	private Config config;
		
	public Node(int ID, Config config, boolean honest, boolean failed) {
		this.ID = ID;
		this.honest = honest;
		this.failed = failed;
		
		packetQToBeRecieved = new TreeSet<NetworkPacket>();
		packetsReceived = new LinkedList<NetworkPacket>();
		
		this.config = config;
	}
	
	public void protocol(){
		/*
		 * This runs in every round. must send all the messages 
		 * and also take care of all the time related issues.
		 */
		
		
		
		
	}

	public void recievePacket(NetworkPacket networkPacket) 
	{
		packetQToBeRecieved.add(networkPacket);
	}
	

	public void schedlueIncomingPackets(int currentTime, int endOfCycle) {
		
		/*
		 *the goal of this function is to schedule the packets until the end of cycle and 
		 */
		while(!packetQToBeRecieved.isEmpty()) {
			NetworkPacket packet = packetQToBeRecieved.first();
			if (packet.startTime < endOfCycle) {
				packetsReceived.add(packet);
				packetQToBeRecieved.remove(packet);
			}
			else {
				break;
			}
				
		}
   	}
	
}
