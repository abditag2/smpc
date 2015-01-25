package smpc;

public class Config {

	//Modify these
	public static final int nArry = 2;
	public static final int numberOfLayersTopology = 3;
	public static final int numberOfnodes = 10;
	
	//Number of clusters should be n^(layers)-1 so that it would form a complete tree.
	public int numberOfClusters() {
		return (int) Math.pow(nArry,numberOfLayersTopology)-1 ;
	}	

	//RTT parameters needs to be defined. The following are for the linear distribution
	public static final int RTTmin = 10;
	public static final int RTTmax = 20;

}
