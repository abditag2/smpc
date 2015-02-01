package smpc.Abstracts;

import smpc.Config;
import smpc.Topology;
import smpc.Abstracts.NetworkPacket.PacketType;
import smpc.Abstracts.NetworkPacket.RTTDelayDistributionType;

import java.util.LinkedList;
import java.util.TreeSet;
import java.util.ArrayList;


public class Node {
	public int ID;
	public int myLayer;
	
	public boolean honest;
	public boolean failed;
	
	public Topology toplogy;
	public LinkedList<NetworkPacket> packetsReceived;
	public TreeSet<NetworkPacket> packetQToBeRecieved;
	
	ArrayList<Node> nodes;
	
	private Config config;
		
	public Node(int ID, Config config, ArrayList<Node> nodes, Topology topology, boolean honest, boolean failed, int myLayer) {
		this.ID = ID;
		this.honest = honest;
		this.failed = failed;
		this.toplogy = topology;
		this.nodes = nodes;
		this.myLayer = myLayer;
		
		packetQToBeRecieved = new TreeSet<NetworkPacket>();
		packetsReceived = new LinkedList<NetworkPacket>();
		
		this.config = config;
	}
	
	public boolean protocol(int currentTime, int roundNumber){
		/*
		 * This runs in every round. must send all the messages 
		 * and also take care of all the time related issues.
		 */
		int myClustersID = this.toplogy.getClusterNumber(this.ID);
		
		if(myLayer == 0) {
			// add input packets and create shares just takes some time!						
			//Create shares and then bundle them all	
		}
		else if (myLayer == this.config.numberOfLayersTopology) {
			int myClustersParent = this.toplogy.getParent(myClustersID);		
			ArrayList<Integer> parentsClusterMembers = this.toplogy.getClusterMembers(myClustersParent);
			ArrayList<NetworkPacket> packetsToBeSentOut = new ArrayList<NetworkPacket>();

			//send packets that needs to be sent
			for (Integer parentClusterMember:parentsClusterMembers) {
				int delay = NetworkPacket.getDelayInMilliSeconds(RTTDelayDistributionType.LINEAR, this.config);
				this.nodes.get(parentClusterMember).recievePacket(new NetworkPacket(currentTime + delay, PacketType.INPUTPACKET));
			}

		}
		else if(myLayer != this.config.numberOfLayersTopology ) {

			int myClustersParent = this.toplogy.getParent(myClustersID);		
			ArrayList<Integer> parentsClusterMembers = this.toplogy.getClusterMembers(myClustersParent);
			ArrayList<NetworkPacket> packetsToBeSentOut = new ArrayList<NetworkPacket>();

			if(myLayer == (this.config.numberOfLayersTopology - roundNumber)) {				
				//if there is a packet in the recieved q then send out the new packets!
				if(packetsReceived.isEmpty()) {
					System.out.println(this.ID);
					return false;
				}
			}			

			//send packets that needs to be sent
			for (Integer parentClusterMember:parentsClusterMembers) {
				int delay = NetworkPacket.getDelayInMilliSeconds(RTTDelayDistributionType.LINEAR, this.config);
				this.nodes.get(parentClusterMember).recievePacket(new NetworkPacket(currentTime + delay, PacketType.INPUTPACKET));
			}
		}
	
		return true;
	}

	public void recievePacket(NetworkPacket networkPacket) 
	{
		this.packetQToBeRecieved.add(networkPacket);
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
