package uiuc.smpc.abstractlibrary;

public class Parameters {
	
	
	static public long count ;
	
	static public int VIRTUAL_HOST = -1 ;
	
	
	/**`
	 * Cluster info
	 */
	
	static public int NUMBER_OF_PARTIES = 50 ; 
	static public int CLUSTER_SIZE = 20 ;
	

	/***
	 * Computation Costs
	 */
	static public int COMPUTATION_COST1  = 2;
	static public int ADDITION_TIME  = 1;
	static public int RANDOM_COIN_GENERATION_LOCAL = 1;
	static public int FKEYGENDEC = 1;
	static public int GENERATE_MAC_KEY = 1;
	static public int GENERATE_ALPHA_i = 1;
	static public int ENC_DIAG_ENC = 1;
	static public int BOX_OPERATION = 1 ; 
	static public int ZERO_KNOWLEDGE_COST  = 1;	
	static public int COMMIT_COMPUTATION_COST  = 1; 
	
	
	
	/**
	 * Network Delay
	 */	
	static public int NETWORK_DELAY = 5 ; 

	static public int getClusterSize(){
		return  CLUSTER_SIZE ; 
	}

	
	static public double getNetworkDelay(){
		return NETWORK_DELAY ; 
		//TODO we must change this or randomize this
	}
	
	static public int getNumberOfParties(){
		return NUMBER_OF_PARTIES; 
	}
	
	static public int getNumberOfCurruptedParties()
	{
		return (int) (getNumberOfParties()/16);
	}

}
