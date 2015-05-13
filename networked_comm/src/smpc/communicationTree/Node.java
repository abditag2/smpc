package smpc.communicationTree;

import smpc.Config;
import smpc.Topology;
import smpc.communicationTree.NetworkPacket.PacketType;

import java.util.LinkedList;
import java.util.ArrayList;


public class Node {
    public int ID;
    public int myLayer;
    public int myCluster;

    public boolean honest;
    public boolean failed;

    public Topology toplogy;
    public LinkedList<NetworkPacket> packetsReceived;
    public LinkedList<NetworkPacket> packetQToBeRecieved;
    public float sizeOfReceivedData;
    float dataRecievedInThisCycleForThisNode = 0;

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

        this.packetQToBeRecieved = new LinkedList<NetworkPacket>();
        this.packetsReceived = new LinkedList<NetworkPacket>();

        this.config = config;

        this.sizeOfReceivedData = config.nodeInitialDataSize;
    }

    public boolean protocol(float currentTime, int roundNumber) {
        /*
		 * This runs in every round. must send all the messages 
		 * and also take care of all the time related issues.
		 */

        if (this.failed || !this.honest) {
            //if node has failed or is not honest I assume that it does not send the packet
            return true;
        } else {

            int myClustersID = this.toplogy.getClusterNumber(this.ID);

            if (myLayer == 0) {
                //TOP layer do not need to send packets to anywhere! so thay are ok :)
            } else if (roundNumber == 0 && myLayer == this.config.numberOfLayersTopology - 1) {
                //if I am a leaf node in the topology
                int myClustersParent = this.toplogy.getParent(myClustersID);
                ArrayList<Integer> parentsClusterMembers = this.toplogy.getClusterMembers(myClustersParent);
                ArrayList<NetworkPacket> packetsToBeSentOut = new ArrayList<NetworkPacket>();

                //send packets that needs to be sent
//				System.out.println("current time: " + currentTime + " round number: "+ roundNumber + " clusterID: " + myClustersID);
                for (Integer parentClusterMember : parentsClusterMembers) {
//					System.out.println("from " + this.ID + " in: " + myClustersID+ " to " + parentClusterMember + " in c: " + this.toplogy.getParent(myClustersID));
                    int delay = NetworkPacket.getDelayInMilliSeconds(this.config.delayDistType, this.config);
//					System.out.println(delay + " dest: " + parentClusterMember);
                    float overHead = (float) Math.ceil(config.nodeInitialDataSize / config.TCP_PACKET_SIZE) * config.TCP_PACKET_OVERHEAD;

                    this.nodes.get(parentClusterMember).recievePacket(new NetworkPacket(currentTime + delay, PacketType.INPUTPACKET, config.nodeInitialDataSize, overHead,
                            0, this.ID, parentClusterMember));
                }
                //after the data that needs to be sent is sent, it is set to 0
                dataRecievedInThisCycleForThisNode = 0;

            } else if (roundNumber != 0 && myLayer != this.config.numberOfLayersTopology - 1) {

                //TODO: check if all the packets from the nodes in the previous cluster are the same

                //Next, send one packet to each of the parent cluster members
                int myClustersParent = this.toplogy.getParent(myClustersID);
                ArrayList<Integer> parentsClusterMembers = this.toplogy.getClusterMembers(myClustersParent);
                ArrayList<NetworkPacket> packetsToBeSentOut = new ArrayList<NetworkPacket>();

                if (myLayer == (this.config.numberOfLayersTopology - roundNumber - 1)) {
//					System.out.println("cID: " + this.myCluster + " current time: " + currentTime + " round number: "+ roundNumber);
                    //if there is a packet in the recieved q then send out the new packets!
                    if (packetsReceived.isEmpty()) {
                        //TODO we may need to add something here for the case there is no packets
//						System.exit(0);
                        return false;
                    }

                    //send packets that needs to be sent
                    for (Integer parentClusterMember : parentsClusterMembers) {
//						System.out.println("sending out- src: "+ this.ID + " dest: " + parentClusterMember + " cID: " + this.myCluster);

//						System.out.println("from " + this.ID + " in: " + myClustersID+ " to " + parentClusterMember + " in c: " + this.toplogy.getParent(myClustersID));
                        int delay = NetworkPacket.getDelayInMilliSeconds(this.config.delayDistType, this.config);

                        float overhead = (float) Math.ceil(dataRecievedInThisCycleForThisNode / config.TCP_PACKET_SIZE) * config.TCP_PACKET_OVERHEAD +
                                (float) Math.ceil(config.nodeInitialDataSize / config.TCP_PACKET_SIZE) * config.TCP_PACKET_OVERHEAD;

                        this.nodes.get(parentClusterMember).recievePacket(new NetworkPacket(currentTime + delay, PacketType.INPUTPACKET,
                                config.nodeInitialDataSize, dataRecievedInThisCycleForThisNode, overhead, this.ID, parentClusterMember));
                    }
                }

                //remove the packets in the recieved queue
                dataRecievedInThisCycleForThisNode = 0;
                this.packetsReceived.clear();
            }
        }

        return true;
    }


    public void recievePacket(NetworkPacket networkPacket) {
        this.packetQToBeRecieved.add(networkPacket);
    }


    public boolean schedlueIncomingPackets(float currentTime, float endOfCycle, long roundNumber) {
		
		/*
		 *the goal of this function is to schedule the packets until the end of cycle and 
		 */
        //TODO add the effect of size of the packets that are being recieved

        int countOfNumberOfNodesFromWhichDataWasRecieved = 0;

        float sumOfNewLoadFromLowerCluster = 0;
        float sumOfOldLoadFromLowerCluster = 0;
        float sumOfTotalDataRecieved = 0;

        float startTimeOfPackets = currentTime - config.lengthOfRound;

        while (!this.packetQToBeRecieved.isEmpty()) {

            NetworkPacket packet = this.packetQToBeRecieved.getFirst(); //first();
//			System.out.println("node: "+ this.ID + " time: " + packet.startTime + " endOfCycle: " + currentTime);
            if (packet.startTime < currentTime) {
                //find smallest start time
                startTimeOfPackets = Math.max(packet.startTime, startTimeOfPackets);
                //sum packets
                sumOfNewLoadFromLowerCluster += packet.newLoadData;
                sumOfOldLoadFromLowerCluster = Math.max(packet.oldLoadData, sumOfOldLoadFromLowerCluster);

                sumOfTotalDataRecieved += packet.newLoadData + packet.oldLoadData + packet.overHead;
                packetsReceived.add(packet);
                this.packetQToBeRecieved.remove(packet);
                countOfNumberOfNodesFromWhichDataWasRecieved++;
            }

        }

        //this is the amount of data that goes out
        dataRecievedInThisCycleForThisNode = sumOfNewLoadFromLowerCluster + config.nArry * sumOfOldLoadFromLowerCluster;

        //this is the actual data that comes in throufh links
        float requiredTime = sumOfTotalDataRecieved / config.bandWidth;


//		if(dataRecievedInThisCycleForThisNode != 0)
//			System.out.println("layer: " + this.myLayer + " cluster: " +this.myCluster +" d: " + sumOfTotalDataRecieved + " round : " + roundNumber + " c: " + countOfNumberOfNodesFromWhichDataWasRecieved);

        //this is the round length - start of packets
        float timeForPackets = currentTime - startTimeOfPackets;
        if (timeForPackets < 0)
            timeForPackets = 0;
        if (startTimeOfPackets != 0 && startTimeOfPackets != -500000) {
            int h = 9;
        }
        if (requiredTime <= timeForPackets)
            return true;
        else
            return false;
    }

}
