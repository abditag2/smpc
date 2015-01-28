package smpc;

import java.util.*;

import smpc.Abstracts.*;

public class simulator {

	public ArrayList<Node> nodes;
	public Config config;

	public void initialize(Config config) {
		nodes = new ArrayList<Node>();
		this.config = config;

		for (int id = 0; id < config.numberOfnodes; id++) {
			nodes.add(new Node(id, config, true, false));
		}

		// set the subset to be corrupted
		int numberOfCorrputedNodes = config.percentOfCorruptedNodes
				* config.numberOfnodes / 100;
		ArrayList<Integer> corruptedNodes = getMRandomNumbersOutOfN(
				numberOfCorrputedNodes, config.numberOfLayersTopology);
		for (Integer corruptedID : corruptedNodes) {
			nodes.get(corruptedID).honest = false;
		}

	}

	public void simulate() {

		int currentTime = 0;
		int endOfCycle = currentTime + this.config.lengthOfRound;

		while (true) {
			// Then, take all the nodes and run the packet recieve function on
			// all of them

			// find nodes that have failed during this cycle and set them to fail
			ArrayList<Integer> failedNodeIDs = getFailedNodes(config.numberOfnodes,
					config.failureRate, config.lengthOfRound);			
			for(Integer failedNodeId:failedNodeIDs) {
				nodes.get(failedNodeId).failed = true;
			}

			
			for (Node node : nodes) {
				node.schedlueIncomingPackets(currentTime, endOfCycle);
			}

			// First, pick all the nodes and run the protocol function
			for (Node node : nodes) {
				node.protocol();
			}

			currentTime = endOfCycle;
			endOfCycle = endOfCycle + this.config.lengthOfRound;
			// TODO: insert the condition for the ending and also set the
			// counters
		}
	}

	private ArrayList<Integer> getFailedNodes(int totalNumberOfNodes,
		int failureRate, int timeLengthMinute) {
		ArrayList<Integer> results = new ArrayList<>();
		//this is the average of the numbers that should fail in this round
		double failedNumberAvg= (double)failureRate/(double)totalNumberOfNodes * ((double)timeLengthMinute/(float)60) ;		
		
		//here we calculate the exact number of nodes that must fail with a gausian distribution
		//following the previous mean and deviation of 1 
		Random rndGen = new Random();
		double nodeIDToFailDouble = rndGen.nextGaussian() + failedNumberAvg;
		
		
		for(int i = 0 ; i < nodeIDToFailDouble ; i++) {
			results.add(rndGen.nextInt(totalNumberOfNodes));
		}
		return results;
	}

	private ArrayList<Integer> getMRandomNumbersOutOfN(int m, int n) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < n; i++) {
			list.add(new Integer(i));
		}
		Collections.shuffle(list);
		ArrayList<Integer> results = new ArrayList<Integer>();
		for (int i = 0; i < m; i++) {
			results.add(i);
		}
		return results;
	}

	public static void main(String[] args) {

	}
}
