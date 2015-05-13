package smpc;

public class Parameters {

	static public long count ;
	
	static public int VIRTUAL_HOST = -1 ;

	/**
	offline phase parameters
	 */
	static public int NUMBER_OF_TRIPLETES_TOBE_GENERATED = 1;

	/**
	* Cluster info
	*/
	static public int ADDITION_TIME  = 1;

	/***
	 * Computation Costs
	 */

	static public int NUMBER_OF_PARTIES = 5 ;
	static public int RANDOM_GEN_TIME  = 1;
	static public int COMMIT_COMPUTATION_COST  = 1;
	static public int SEND_TIME  = 1;
	static public int RECEIVE_TIME = 2;
	static public int SHE_ENCRYPT_TIME = 1;

	static public int COMPUTATION_COST1  = 2;
	static public int RANDOM_COIN_GENERATION_LOCAL = 1;
	static public int FKEYGENDEC = 1;
	static public int GENERATE_MAC_KEY = 1;
	static public int GENERATE_ALPHA_i = 1;
	static public int ENC_DIAG_ENC = 1;
	static public int BOX_OPERATION = 1 ; 
	static public int ZERO_KNOWLEDGE_COST  = 1;	

	/**
	 * Network Delay
	 */	
	static public int NETWORK_DELAY = 5 ;

	/**
	 * Offline phase paramteres
	 */

	static public boolean NEW_CIPHER_TEXT = true;

	static public double getDuration(ComputationType computationType){

		int duration = 0;
		switch (computationType)
		{
			case ADDITION:
				duration = ADDITION_TIME;
				break;
			case RANDOM_GEN:
				duration = RANDOM_GEN_TIME;
				break;
			case COMMIT_COMPUTATION:
				duration = COMMIT_COMPUTATION_COST;
				break;
			case SEND:
				duration = SEND_TIME;
				break;
			case RECIEVE:
				duration = RECEIVE_TIME;
				break;
			case SHE_ENCRYPT:
				duration = SHE_ENCRYPT_TIME;
				break;
		}

		return (double) duration;

	}

	public enum ComputationType{
		ADDITION,
		RANDOM_GEN,
		COMMIT_COMPUTATION,
		SHE_ENCRYPT,
		SHE_MULTIPLY,
		SHE_ADD,
		SEND,
		RECIEVE
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
