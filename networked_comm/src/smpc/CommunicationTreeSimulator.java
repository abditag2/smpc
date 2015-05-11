package smpc;

import java.io.PrintStream;
import java.util.*;

import smpc.Abstracts.*;
import smpc.abstractlibrary.Parameters;
import smpc.library.OnlinePhaseSimulation;
import smpc.voting.votingSPDZ;

import java.io.PrintWriter;

public class CommunicationTreeSimulator {

	public ArrayList<Node> nodes;
	public Topology topology;
	public Config config;

	public boolean searchForMinimumRoundLength = false;
	
	
	public CommunicationTreeSimulator(Config config) {
		this.config = config;
	}
	

	public void initialize() {
		nodes = new ArrayList<Node>();
		this.topology = new Topology(this.config);

		for (int id = 0; id < config.numberOfnodes; id++) {
			int nodeCluster = topology.getClusterNumber(id);
			int myLayer = getMyLayerNumber(nodeCluster, this.config);
			int myCluster = this.topology.getClusterNumber(id);
			nodes.add(new Node(id, config, nodes, this.topology, true, false, myLayer, myCluster));

		}

		// set the subset to be corrupted
		int numberOfCorrputedNodes = config.percentOfCorruptedNodes
				* config.numberOfnodes / 100;
		List<Integer> corruptedNodes = getMRandomNumbersOutOfN(
				numberOfCorrputedNodes, config.numberOfnodes);
		for (Integer corruptedID : corruptedNodes) {
			nodes.get(corruptedID).honest = false;
		}

	}

	public int getMyLayerNumber(int nodeCluster, Config config) {
		for(int i = 0; i < config.numberOfLayersTopology; i++) {
			if( nodeCluster < Math.pow(config.nArry, i+1 )-1 ) {
				return i;
			}		
		}
		return 0;
	}


	public boolean simulate() {

		//returns true if the time was enough

		float currentTime = 0;
		float endOfCycle = currentTime + this.config.lengthOfRound;

		HashMap<Integer, FailedNode> failedNodes = new HashMap<Integer, FailedNode>();
		
		int roundNumber = 0;
		while (true) {

			//Handle failures and recoveries
			if(config.failureRate != Integer.MAX_VALUE){
				failAndRecover(failedNodes);
			}

//			System.out.println("round number:" + roundNumber);
//			System.out.println("number of failed nodes: "+ failedNodes.size());
			//for all the nodes schedule their incoming packets
			boolean didAllTheNodesHadEnoughTimeToRecieveTheirPackets = true;
			for (Node node : nodes) {
				if(!node.failed) {
					didAllTheNodesHadEnoughTimeToRecieveTheirPackets &= node.schedlueIncomingPackets(currentTime, endOfCycle, roundNumber);
				}
			}

			if(this.searchForMinimumRoundLength && !didAllTheNodesHadEnoughTimeToRecieveTheirPackets){
				//some nodes did not have enough time to deliver all their data. So, break onlinePhaseSimulation and increase the round duration time.
				return false;
			}

			// First, pick all the nodes and run the protocol function
			//From every cluster at least one node must be able to run the protocl otherwise the protocol will be considered as failed.
			boolean protocolRan = true;			
			for (int clusterID =0 ; clusterID < this.config.numberOfClusters(); clusterID++) {
				boolean protocolRanAtLeastOnce = false;
				for (int nodeID:this.topology.getClusterMembers(clusterID)) {
					Node node = this.nodes.get(nodeID);
					protocolRanAtLeastOnce |= node.protocol(currentTime, roundNumber);		
				}
				protocolRan &= protocolRanAtLeastOnce;
			}
			
			//here check for the termination Condition
			if (terminationConditionMet(roundNumber, protocolRan)) {
				//print statistics
				break;
			}
			
			currentTime = endOfCycle;
			endOfCycle = endOfCycle + this.config.lengthOfRound;
			roundNumber++;
		}
		return true;
	}

	
	private void failAndRecover(HashMap<Integer, FailedNode> failedNodes) {
		// find nodes that have failed during this cycle and set them to fail
		ArrayList<FailedNode>newFailedNodes = getFailedNodes(config.numberOfnodes,
				config.failureRate, config.lengthOfRound);

		//Update the failed nodes list
		for(FailedNode failedNode:newFailedNodes) {
			failedNodes.put(failedNode.ID, failedNode);
			nodes.get(failedNode.ID).failed = true;
		}
		
		//recover all the failed nodes that have passed the recovery time
		Iterator<Map.Entry<Integer, FailedNode>> failedNodesIterator= failedNodes.entrySet().iterator();
		while(failedNodesIterator.hasNext()) {
			Map.Entry<Integer, FailedNode> pair = failedNodesIterator.next();
			Integer failedNodeID = pair.getKey();
			FailedNode failedNode = failedNodes.get(failedNodeID);
			if (failedNode.timeLeftToRecoverMilliseconds -this.config.lengthOfRound <= 0) {
				//recover the Node
				failedNodesIterator.remove();
				this.nodes.get(failedNodeID).failed = false;
			}
			else {
				//reduce the remained time
				failedNode.timeLeftToRecoverMilliseconds = failedNode.timeLeftToRecoverMilliseconds - this.config.lengthOfRound ; 
				failedNodes.put(failedNodeID, failedNode);
			}
		}	
	}
	
	private boolean terminationConditionMet(int roundNumber, boolean protocolRan) {
		/*
		 * condition1: At all the times there must be at least one honest party in each cluster.
		 */
		for(int i = 0 ; i< this.config.numberOfClusters(); i++) {
			ArrayList<Integer> clusterMembers = this.topology.getClusterMembers(i);
			boolean hasHonestLiveNode = false;
			for(Integer memberID:clusterMembers) {
				Node member = this.nodes.get(memberID);

				if(member.failed == false && member.honest == true) {
					//if there is at least one honest member that has not failed
					hasHonestLiveNode = true;
					break;
				}
			}
			
			if (!hasHonestLiveNode) {		
				System.out.println("Termination condition 1");
				return true;
			}
		}

		/*
		 * condition 2
		 * if protocol could not run in any of the previous rounds then we cannot converge
		 */
		
		if (protocolRan == false) {
			System.out.println("Terminatin condition 2");
			return true;
		}
		

		/*
		 * Successful Termination
		 * if the nodes in the last level clusters have gathered their inputs
		 */
		if (roundNumber == this.config.numberOfLayersTopology) {
			System.out.println("Succussful termination.");
			return true;
		}
		
		return false;
	}

	public ArrayList<FailedNode> getFailedNodes(int totalNumberOfNodes,
		int failureRate, float timeLengthMilliSeconds) {
		ArrayList<FailedNode> results = new ArrayList<>();
		//this is the average of the numbers that should fail in this round
		double failedNumberAvg= (double)totalNumberOfNodes/(double)failureRate * ((double)timeLengthMilliSeconds/(double)60000) ;
		
		//here we calculate the exact number of nodes that must fail with a gausian distribution
		//following the previous mean and deviation of 1 
		Random rndGen = new Random();
		double nodeIDToFailDouble = rndGen.nextGaussian()*10 + failedNumberAvg;
		
		
		for(int i = 0 ; i < nodeIDToFailDouble ; i++) {
			int nextRndID = rndGen.nextInt(totalNumberOfNodes);
			results.add(new FailedNode(this.config.recoveryTime, nextRndID));
		}
		return results;
	}

	public List<Integer> getMRandomNumbersOutOfN(int m, int n) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < n; i++) {
			list.add(new Integer(i));
		}
		Collections.shuffle(list);
		List<Integer> results = list.subList(0, m);
		return results;
	}


	public static void main(String[] args) {
		PrintStream out = null;

		/*
		Here we run online phase once to measure how long it takes to execute it for one cluser and then later add that to the execution time
		 */
		Parameters.count = 0 ;

		System.out.println("OnlinePhaseSimulation Starting");
		OnlinePhaseSimulation onlinePhaseSimulation = new OnlinePhaseSimulation();

		double timeToSchedule = 0 ;


		onlinePhaseSimulation.schedule(new votingSPDZ(onlinePhaseSimulation, timeToSchedule, Parameters.VIRTUAL_HOST, 0, Parameters.getNumberOfParties()));
		//onlinePhaseSimulation.schedule(new votingSMPC(onlinePhaseSimulation, timeToSchedule, Parameters.VIRTUAL_HOST, 0, Parameters.getNumberOfParties()));


		onlinePhaseSimulation.doAllEvents() ;

		System.out.println("OnlinePhaseSimulation Ends");
		System.out.println("Recieves are " + Parameters.count);
		System.out.println("Execution Time: " + onlinePhaseSimulation.time);

		float online_phase_execution_time = (float) onlinePhaseSimulation.time;

		/*
		Start the communication network onlinePhaseSimulation with the parameter measured above
		 */

		boolean searchForMinimumRoundLength  = true ;

		// search for a minimum round duration time that all the packets can be delivered
		if (searchForMinimumRoundLength == false){
			Config config = new Config();
			config.lengthOfRound = 30000;
			config.online_phase_execution_time = online_phase_execution_time;
			CommunicationTreeSimulator sim = new CommunicationTreeSimulator(config);
			sim.initialize();
			sim.simulate();
		}

		else if (searchForMinimumRoundLength == true){
			// do experiment on failre and etc
			try{
				PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
//				float bandWidth = 100;
				//bw is byte per 10 ms
				for(int bandWidth = 1 ; bandWidth < 2000000 ; bandWidth = bandWidth * 2){
//				{int bandWidth = 1 ;
					for(float dataSize = 1 ; dataSize < 1000000000 ; dataSize = dataSize *  4){
//					{float dataSize = 1024;
						//delay is per 10 ms
						for(float delay = 1 ; delay < 20 ; delay = delay + 2){
//						{long delay = 7;
							float currentLengthBeforeChange = 0;
							float acceptableTimeError = 100;
							boolean timeEnough = false;
							float LENGTH_RANGE = (float)100* (float)1000000000;
							float lastLength = 0;
							float minLength = 0;
							float maxLength = LENGTH_RANGE;
							float currentLength = (maxLength + minLength)/2;

							while(true)
							{
								System.out.println("length: " + currentLength);

								//set onlinePhaseSimulation configuration
								Config config = new Config();

								//settings that do not change
								config.delayDistType = NetworkPacket.RTTDelayDistributionType.CONSTANT;
								config.failureRate = Integer.MAX_VALUE;
								config.numberOfnodes = 10000;
								config.numberOfLayersTopology = 9;

								//settings that change
								config.nodeInitialDataSize = dataSize;
								config.bandWidth = bandWidth;
								config.constantDelay = delay;

								config.lengthOfRound = currentLength ;

								CommunicationTreeSimulator sim = new CommunicationTreeSimulator(config);
								sim.searchForMinimumRoundLength = true;
								sim.initialize();
								timeEnough = sim.simulate();

								//adjust the time for next cycles
								lastLength = currentLengthBeforeChange;
								currentLengthBeforeChange = currentLength;

								if (!timeEnough)	{
									minLength = currentLength;
									currentLength = (currentLength + maxLength)/2 ;

								}
								else if (timeEnough){
									if(Math.abs(currentLengthBeforeChange - lastLength)< acceptableTimeError){
										writer.println(bandWidth + " " + delay + " " + dataSize + " " + currentLength);
										writer.flush();
										break;
									}else{
										maxLength = currentLength;
										currentLength = (currentLength + minLength) / 2;
									}
								}

								if(currentLength == currentLengthBeforeChange){
									currentLength++;
								}
								if(currentLength >= LENGTH_RANGE){
									writer.println(bandWidth + " " + delay + " " + dataSize + " " + LENGTH_RANGE);
									writer.flush();
									break;
								}
							}
						}
					}
				}

				writer.close();
			}
			catch (Exception e){
				System.out.println("error occured");
			}


		}
	}
	
	public class FailedNode{
		float timeLeftToRecoverMilliseconds;
		int ID;
		public FailedNode(float roundsLeft, int ID) {
			this.ID = ID;
			this.timeLeftToRecoverMilliseconds = roundsLeft;
		}
	}

}
