package smpc;

import smpc.Abstracts.NetworkPacket.RTTDelayDistributionType;

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
	public int RTTmin;
	public int RTTmax;
	
	//RTTParams for GAUSSIAN distribution
	public int avg;
	public int deviation;

	// waitingTime for the nodes in each round
	public int lengthOfRound;

	// ratio of corrupted nodes to total number of nodes
	public int percentOfCorruptedNodes;

	// set the failure rate for servers (lifetime in hours)
	public int failureRate;
	
	//time that it takes for a node to recover in Milliseconds
	public int recoveryTime;	
	
	//Size of the final cluster that performs the calculations
	public int lastClusterSize;
	
	public Config() {
		/*
		 * set the default value for the parameters here and later can be chagned in the execution
		 */
		
		this.nArry = 2;

		this.numberOfLayersTopology = 10;
		this.numberOfnodes = 100000;

		this.delayDistType = RTTDelayDistributionType.GAUSSIAN;
		
		this.RTTmin = 200;//milliseconds
		this.RTTmax = 400;//milliseconds
		
		this.lengthOfRound = 60000; //milliseconds
		this.percentOfCorruptedNodes = 90;

		//This is the hours that a machine works on average before failing
		this.failureRate = 8544/3; //HOurs //three times failure per year for a system
		this.recoveryTime = 48*60*60*1000; //48 Hours
		
		this.lastClusterSize = 40;
		
		//RTTParams for GAUSSIAN distribution
		this.avg = 250;//milliseconds
		this.deviation = 150;//milliseconds
	}
}
