package smpc;

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

	// RTT parameters needs to be defined. The following are for the linear
	// distribution
	public int RTTmin;
	public int RTTmax;

	// this parameter is to determine the nodes incoming bandwidth
	public int nodeIncomingBandWidth;

	// waitingTime for the nodes in each round
	public int lengthOfRound;

	// ratio of corrupted nodes to total number of nodes
	public int percentOfCorruptedNodes;

	// set the failure rate for servers (lifetime in hours)
	public int failureRate;

	public Config() {
		/*
		 * set the default value for the parameters here and later can be chagned in the execution
		 */
		
		this.nArry = 2;
		this.numberOfLayersTopology = 3;
		this.numberOfnodes = 10;
		this.RTTmin = 10;
		this.RTTmax = 20;
		this.nodeIncomingBandWidth = 100;
		this.lengthOfRound = 100;
		this.percentOfCorruptedNodes = 20;
		this.failureRate = 10000;
	}
}
