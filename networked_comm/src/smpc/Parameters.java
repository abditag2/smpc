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

	static public int NUMBER_OF_PARTIES = 5 ;

	/***
	 * Computation Costs
	 */

	static public int ADDITION_TIME  = 1;
	static public int MULITIPLICATION_TIME  = 1;
	static public int XOR_TIME  = 1;
	static public int RANDOM_GEN_TIME  = 1;
	static public int COMMIT_COMPUTATION_COST  = 1;
	static public int SEND_TIME  = 1;
	static public int RECEIVE_TIME = 2;
	static public int SHE_ENCRYPT_TIME = 1;
	static public int SHE_DECRYPT_TIME = 1;
	static public int AES_ENCRYPT_TIME = 1;
	static public int HASH_GENERATION_TIME = 1;

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
	static public int N_I = 10;
	static public int M = 10;
	static public int N_M = 10;
	static public int N_CIPHER = 10;
	//TODO verify what these numbers are
	// nI input tuples,
	// nm = number of multiplication

	static public double getDuration(ComputationType computationType){

		int duration = 0;
		switch (computationType)
		{
			case ADDITION:
				duration = ADDITION_TIME;
				break;
			case MULITIPLICATION:
				duration = MULITIPLICATION_TIME;
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
			case SHE_DECRYPT:
				duration = SHE_DECRYPT_TIME;
				break;
			case AES_ENCRYPT:
				duration = AES_ENCRYPT_TIME;
				break;
			case HASH_GENERATION:
				duration = HASH_GENERATION_TIME;
				break;
			case XOR:
				duration = XOR_TIME;
				break;
		}

		return (double) duration;

	}

	public enum ComputationType{
		ADDITION,
		XOR,
		MULITIPLICATION,
		RANDOM_GEN,
		COMMIT_COMPUTATION,
		SHE_ENCRYPT,
		SHE_DECRYPT,
		SHE_MULTIPLY,
		SHE_ADD,
		SEND,
		RECIEVE,
		AES_ENCRYPT,
		HASH_GENERATION

	}

	static public double getNetworkDelay(){
		return NETWORK_DELAY ;
	}
	
	static public int getNumberOfParties(){
		return NUMBER_OF_PARTIES; 
	}

	//TODO update this!
	static public int getNumberOfMultiplications(){
		return N_M;
	}

	static public int getNumberOfCurruptedParties()
	{
		return (int) (getNumberOfParties()/16);
	}
}
