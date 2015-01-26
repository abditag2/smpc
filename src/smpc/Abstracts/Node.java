package smpc.Abstracts;

import java.util.LinkedList;
import smpc.Config;

public class Node {
	
	
	int initialContentSize;
	int ID;
	
	public LinkedList<NetworkPacket> packetsReceived;
	public LinkedList<NetworkPacket> packetQToBeRecieved;
	
	private Config config;
	
	public Node(int initialContentSize, int ID, Config config) {
		this.initialContentSize = initialContentSize;
		this.ID = ID;
		
		packetQToBeRecieved = new LinkedList<NetworkPacket>();
		packetsReceived = new LinkedList<NetworkPacket>();
		
		this.config = config;
	}
	
	public void protocol(){
		//Here set all the packets that need to be sent out during the next protocol round
	}

	
	public void recievePacket(NetworkPacket networkPacket) 
	{
		packetQToBeRecieved.add(networkPacket);
	}
	

	public void schedlueIncomingPackets(int currentTime, int endOfCycle) {
		
		/*
		 *the goal of this function is to schedule the packets until the end of cycle and
		 *set the communication flows that are completely recieved. 
		 */
		
		//initialize the now variable with the cycle start time
		float now = currentTime;

		while(true) {

			float[] results= findMinStartEndtimesAfterCurrentTime(
					now, 
					packetQToBeRecieved);
			
			float nextTime = Math.min(results[0], endOfCycle);
			float countOngoingFlows = results[1];
			float newRateForEachOngoingFlow = this.config.nodeIncomingBandWidth/countOngoingFlows;
			float transmittedPacketsInSubCycle = (float) (nextTime - now)/newRateForEachOngoingFlow;
			
			for (NetworkPacket packet:packetQToBeRecieved) {				
				if (now >= packet.startTime){
					packet.numberOfRemainingPackets = packet.numberOfRemainingPackets - transmittedPacketsInSubCycle;
					if(packet.numberOfRemainingPackets <= 0)
					{
						packetQToBeRecieved.remove(packet);
						packetsReceived.add(packet);
					}
				}
			}
			now = Math.min(nextTime, endOfCycle);
			if (now > endOfCycle)
				break;
		}
   	}
	
	public float[] findMinStartEndtimesAfterCurrentTime(
			float now, 
			LinkedList<NetworkPacket> packetQToBeRecieved)
	{
		float minTime = Float.MAX_VALUE;
		float minPacket = Float.MAX_VALUE;
		
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
		
		float rate = this.config.nodeIncomingBandWidth;
		float minTimeForOngoingFlows = minPacket/rate + 1; //+1 is for the division error
		float nextTime = (float) Math.min(minTime, minTimeForOngoingFlows);
		return new float[] {nextTime, countOngoingFlows};
	}
}
