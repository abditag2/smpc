package smpc.Abstracts;

import java.util.LinkedList;

import smpc.Config;

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
		
		int now = currentTime;
		int nextstopTime = findMinStartEndtimesAfterCurrentTime(
				now, 
				packetQToBeRecieved);
		
		for (NetworkPacket packet:packetQToBeRecieved) {
			
			
			
			if (now >= packet.startTime){
				packet.numberOfRemainingPackets - 
			}
		}
   	}
	
	public int[] findMinStartEndtimesAfterCurrentTime(
			int now, 
			LinkedList<NetworkPacket> packetQToBeRecieved)
	{
		int minTime = Integer.MAX_VALUE;
		int minPacket = Integer.MAX_VALUE;
		
		int countOngoingFlows = 0 ; 
		for (NetworkPacket packet:packetQToBeRecieved) {
			//find the minimum number of packets remaining for the nodes in between the flow
			if (now >= packet.startTime) {
				countOngoingFlows++;
				if (minPacket < packet.numberOfRemainingPackets)
					minPacket = packet.numberOfRemainingPackets;
			}
			
			else if (now <= packet.startTime) {
				if (minTime > packet.startTime)
					minTime = packet.startTime;
			}			
		}
		
		int rate = Config.nodeIncomingBandWidth;
		int minTimeForOngoingFlows = minPacket/rate + 1; //+1 is for the division error
		int nextTime = (int) Math.min(minTime, minTimeForOngoingFlows);
		return new int[] {nextTime, countOngoingFlows};
	}
}
