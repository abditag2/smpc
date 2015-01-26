package smpc;

public class Config {

	//Modify these
	public int nArry;
	public int numberOfLayersTopology;
	public int numberOfnodes;
	
	//Number of clusters should be n^(layers)-1 so that it would form a complete tree.
	public int numberOfClusters() {
		return (int) Math.pow(nArry,numberOfLayersTopology)-1 ;
	}	

	//RTT parameters needs to be defined. The following are for the linear distribution
	public int RTTmin;
	public int RTTmax;
	
	//this parameter is to determine the nodes incoming bandwidth
	public int nodeIncomingBandWidth ;
	
	//waitingTime for the nodes in each round
	public int cycleLength;
	
	public Config() {
		this.nArry = 2;
		this.numberOfLayersTopology = 3;
		this.numberOfnodes = 10;
		

		//RTT parameters needs to be defined. The following are for the linear distribution
		this.RTTmin = 10;
		this.RTTmax = 20;
		
		//this parameter is to determine the nodes incoming bandwidth
		this.nodeIncomingBandWidth = 100;
		
		//waitingTime for the nodes in each round
		this.cycleLength = 100;
		
	}
}
