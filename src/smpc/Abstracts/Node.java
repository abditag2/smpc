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
	public int myCluster;
	
	public boolean honest;
	public boolean failed;
	
	public Topology toplogy;
	public LinkedList<NetworkPacket> packetsReceived;
	public TreeSet<NetworkPacket> packetQToBeRecieved;
	
	ArrayList<Node> nodes;
	
	private Config config;
		
	public Node(int ID, Config config, ArrayList<Node> nodes, Topology topology, boolean honest, boolean failed, int myLayer, int myCluster) {
		this.ID = ID;
		this.honest = honest;
		this.failed = failed;
		this.toplogy = topology;
		this.nodes = nodes;
		this.myLayer = myLayer;
		this.myCluster = myCluster;
		
		packetQToBeRecieved = new TreeSet<NetworkPacket>();
		packetsReceived = new LinkedList<NetworkPacket>();
		
		this.config = config;
	}
	
	public boolean protocol(int currentTime, int roundNumber){
		/*
		 * This runs in every round. must send all the messages 
		 * and also take care of all the time related issues.
		 */
		
		if(this.failed || !this.honest) {
			//if node has failed or is not honest I assume that it does not send the packet
			return true;
		}
		else {
			
			int myClustersID = this.toplogy.getClusterNumber(this.ID);
			
			if(myLayer == 0) {
				//TOP layer do not need to send packets to anywhere! so thay are ok :)	
			}
			else if (roundNumber == 0 && myLayer == this.config.numberOfLayersTopology-1) {
				//if I am a leaf node in the topology
				int myClustersParent = this.toplogy.getParent(myClustersID);		
				ArrayList<Integer> parentsClusterMembers = this.toplogy.getClusterMembers(myClustersParent);
				ArrayList<NetworkPacket> packetsToBeSentOut = new ArrayList<NetworkPacket>();
				
				//send packets that needs to be sent
//				System.out.println("current time: " + currentTime + " round number: "+ roundNumber + " clusterID: " + myClustersID);
				for (Integer parentClusterMember:parentsClusterMembers) {
					int delay = NetworkPacket.getDelayInMilliSeconds(this.config.delayDistType, this.config);
//					System.out.println(delay + " dest: " + parentClusterMember);
					this.nodes.get(parentClusterMember).recievePacket(new NetworkPacket(currentTime + delay, PacketType.INPUTPACKET));
				}
				
			}
			else if(roundNumber!= 0 && myLayer != this.config.numberOfLayersTopology-1 ){
				
				//TODO: check if all the packets from the nodes in the previous cluster are the same

				//Next, send one packet to each of the parent cluster members
				int myClustersParent = this.toplogy.getParent(myClustersID);		
				ArrayList<Integer> parentsClusterMembers = this.toplogy.getClusterMembers(myClustersParent);
				ArrayList<NetworkPacket> packetsToBeSentOut = new ArrayList<NetworkPacket>();
				
				if(myLayer == (this.config.numberOfLayersTopology - roundNumber -1)) {
//					System.out.println("cID: " + this.myCluster + " current time: " + currentTime + " round number: "+ roundNumber);
					//if there is a packet in the recieved q then send out the new packets!
					if(packetsReceived.isEmpty()) {
//						System.out.println("this node did not reciece anypackets:" + this.ID + " cID: " + myClustersID);
						System.exit(0);
						return false;
					}
					else {
//						System.out.println("this node packets:" + this.ID + " cID: " + myClustersID);
					}
					//send packets that needs to be sent
					for (Integer parentClusterMember:parentsClusterMembers) {
//						System.out.println("sending out- src: "+ this.ID + " dest: " + parentClusterMember + " cID: " + this.myCluster);
						int delay = NetworkPacket.getDelayInMilliSeconds(this.config.delayDistType, this.config);
						this.nodes.get(parentClusterMember).recievePacket(new NetworkPacket(currentTime + delay, PacketType.INPUTPACKET));
					}
				}			
								
				//remove the packets in the recieved queue
				this.packetsReceived.clear();
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
//			System.out.println("node: "+ this.ID + " time: " + packet.startTime + " endOfCycle: " + currentTime);
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
