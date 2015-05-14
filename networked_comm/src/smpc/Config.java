package smpc;

import smpc.communicationTree.NetworkPacket.RTTDelayDistributionType;

public class Config {

	// Modify these
	public int nArry;
	public int numberOfLayersTopology;
	public int numberOfnodes;

	// Number of clusters should be n^(layers)-1 so that it would form a
	// complete tree.
	public int numberOfClusters() {
		return (int) Math.pow(nArry, numberOfLayersTopology) - 1;
	}

	public RTTDelayDistributionType delayDistType;
	// RTT parameters needs to be defined. The following are for the linear
	// distribution in milliseconds
	public float RTTmin;
	public float RTTmax;
	
	//RTTParams for GAUSSIAN distribution
	public float avg;
	public float deviation;

	//communication over head for delay
	public float constantDelay;

	// waitingTime for the nodes in each round
	public float lengthOfRound;

	// ratio of corrupted nodes to total number of nodes
	public int percentOfCorruptedNodes;

	// set the failure rate for servers (lifetime in hours)
	//if set to Integer.MAX_VALUE it is assumed that there is no failures
	public int failureRate;
	
	//time that it takes for a node to recover in Milliseconds
	public float recoveryTime;
	
	//Size of the final cluster that performs the calculations
	public int lastClusterSize;

	//TCP packet over head
	public int tcpPacketOverHead;

	//TCP packet size
	public int tcpPacketSize;

	//Node incoming bandwidth
	public float bandWidth;

	//Node initial data size
	public float nodeInitialDataSize;

	//TCP overhead
	public float TCP_PACKET_SIZE = 1460;
	public float TCP_PACKET_OVERHEAD = 40;

	//Time to run online Phase with current number of nodes
	
	public Config() {
		/*
		 * set the default value for the parameters here and later can be chagned in the execution
		 */
		
		this.nArry = 2;

//		this.numberOfLayersTopology = 4;
//		this.numberOfnodes = 60;

		this.delayDistType = RTTDelayDistributionType.NODELAY;
		
		this.RTTmin = 10;//milliseconds
		this.RTTmax = 100;//milliseconds
		
		this.lengthOfRound = 60000; //milliseconds
		this.percentOfCorruptedNodes = 0;

		//This is the hours that a machine works on average before failing
//		this.failureRate = 8544/3; //HOurs //three times failure per year for a system
		this.failureRate = Integer.MAX_VALUE; //HOurs //three times failure per year for a system
		this.recoveryTime = 48*60*60*1000; //48 Hours
		
		this.lastClusterSize = 40;
		
		//RTTParams for GAUSSIAN distribution
		this.avg = 250;//milliseconds
		this.deviation = 150;//milliseconds

		this.bandWidth = 10;
		this.nodeInitialDataSize = 100;

		this.constantDelay = 0;
	}
}
