package smpc.Abstracts;

import java.util.LinkedList;

public class Node {
	
	
	int initialContentSize;
	int ID;
	
	public LinkedList<NetworkPacket> packetsReceived;
	public LinkedList<NetworkPacket> packetQToBeRecieved;
	
	public Node(int initialContentSize, int ID) {
		this.initialContentSize = initialContentSize;
		this.ID = ID;
		
		packetQToBeRecieved = new LinkedList<NetworkPacket>();
		packetsReceived = new LinkedList<NetworkPacket>();
	}
	
	public void recievePacket(NetworkPacket networkPacket) 
	{
		packetQToBeRecieved.add(networkPacket);
	}
	
	public void protocol(){
		//Here set all the packets that need to be sent out during the next protocol round
	}

	public void schedlueIncomingPackets(int currentTime, int endOfCycle) {
		
		/*
		 *the goal of this function is to schedule the packets until the end of cycle and
		 *set the communication flows that are completely recieved. 
		 */

		
		
	
		
	}
	
	public int findMinStartEndtimesAfterCurrentTime(
			int currentTime, 
			LinkedList<NetworkPacket> packetQToBeRecieved)
	{
		
		return min;
	}
}
