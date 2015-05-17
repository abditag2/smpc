package smpc;

import java.io.PrintStream;
import java.util.*;

import smpc.communicationTree.*;
import smpc.events.offlineEvents.Reshare;
import smpc.library.OnlinePhaseSimulation;
import smpc.library.OfflinePhaseSimulation;
import smpc.events.offlineEvents.offlinePhase;
import smpc.events.OnlineEvents.OnlinePhaseForVotingSPDZ;

import java.io.PrintWriter;

public class CommunicationTreeSimulator {

    public ArrayList<Node> nodes;
    public Topology topology;
    public Config config;

    public boolean searchForMinimumRoundLength = true;


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
        for (int i = 0; i < config.numberOfLayersTopology; i++) {
            if (nodeCluster < Math.pow(config.nArry, i + 1) - 1) {
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
            if (config.failureRate != Integer.MAX_VALUE) {
                failAndRecover(failedNodes);
            }

//			System.out.println("round number:" + roundNumber);
//			System.out.println("number of failed nodes: "+ failedNodes.size());
            //for all the nodes schedule their incoming packets
            boolean didAllTheNodesHadEnoughTimeToRecieveTheirPackets = true;
            for (Node node : nodes) {
                if (!node.failed) {
                    didAllTheNodesHadEnoughTimeToRecieveTheirPackets &= node.schedlueIncomingPackets(currentTime, endOfCycle, roundNumber);
                }
            }

            if (this.searchForMinimumRoundLength && !didAllTheNodesHadEnoughTimeToRecieveTheirPackets) {
                //some nodes did not have enough time to deliver all their data. So, break simulation and increase the round duration time.
                return false;
            }

            // First, pick all the nodes and run the protocol function
            //From every cluster at least one node must be able to run the protocl otherwise the protocol will be considered as failed.
            boolean protocolRan = true;
            for (int clusterID = 0; clusterID < this.config.numberOfClusters(); clusterID++) {
                boolean protocolRanAtLeastOnce = false;
                for (int nodeID : this.topology.getClusterMembers(clusterID)) {
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
        ArrayList<FailedNode> newFailedNodes = getFailedNodes(config.numberOfnodes,
                config.failureRate, config.lengthOfRound);

        //Update the failed nodes list
        for (FailedNode failedNode : newFailedNodes) {
            failedNodes.put(failedNode.ID, failedNode);
            nodes.get(failedNode.ID).failed = true;
        }

        //recover all the failed nodes that have passed the recovery time
        Iterator<Map.Entry<Integer, FailedNode>> failedNodesIterator = failedNodes.entrySet().iterator();
        while (failedNodesIterator.hasNext()) {
            Map.Entry<Integer, FailedNode> pair = failedNodesIterator.next();
            Integer failedNodeID = pair.getKey();
            FailedNode failedNode = failedNodes.get(failedNodeID);
            if (failedNode.timeLeftToRecoverMilliseconds - this.config.lengthOfRound <= 0) {
                //recover the Node
                failedNodesIterator.remove();
                this.nodes.get(failedNodeID).failed = false;
            } else {
                //reduce the remained time
                failedNode.timeLeftToRecoverMilliseconds = failedNode.timeLeftToRecoverMilliseconds - this.config.lengthOfRound;
                failedNodes.put(failedNodeID, failedNode);
            }
        }
    }

    private boolean terminationConditionMet(int roundNumber, boolean protocolRan) {
        /*
         * condition1: At all the times there must be at least one honest party in each cluster.
		 */
        for (int i = 0; i < this.config.numberOfClusters(); i++) {
            ArrayList<Integer> clusterMembers = this.topology.getClusterMembers(i);
            boolean hasHonestLiveNode = false;
            for (Integer memberID : clusterMembers) {
                Node member = this.nodes.get(memberID);

                if (member.failed == false && member.honest == true) {
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
        double failedNumberAvg = (double) totalNumberOfNodes / (double) failureRate * ((double) timeLengthMilliSeconds / (double) 60000);

        //here we calculate the exact number of nodes that must fail with a gausian distribution
        //following the previous mean and deviation of 1
        Random rndGen = new Random();
        double nodeIDToFailDouble = rndGen.nextGaussian() * 10 + failedNumberAvg;


        for (int i = 0; i < nodeIDToFailDouble; i++) {
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


    static private float runOnlinePhaseSim() {
        Parameters.count = 0;

        System.out.println("OnlinePhaseSimulation Starting");
        OnlinePhaseSimulation onlinePhaseSimulation = new OnlinePhaseSimulation();

        double timeToSchedule = 0;


        onlinePhaseSimulation.schedule(new OnlinePhaseForVotingSPDZ(onlinePhaseSimulation, timeToSchedule, Parameters.VIRTUAL_HOST, 0, Parameters.getNumberOfParties()));
        //simulation.schedule(new votingSMPC(simulation, timeToSchedule, Parameters.VIRTUAL_HOST, 0, Parameters.getNumberOfParties()));


        onlinePhaseSimulation.doAllEvents();

        Config config = new Config();

        Parameters.NUMBER_OF_PARTIES = config.numberOfnodes;

        System.out.println("OnlinePhaseSimulation Ends");
        System.out.println("OnlinePhaseSimulation Recieves are " + Parameters.count);
        System.out.println("OnlinePhaseSimulation Execution Time: " + onlinePhaseSimulation.getLastFinishingTimeForAllNodes());

        return (float) onlinePhaseSimulation.getLastFinishingTimeForAllNodes();
    }

    static private float simulateOfflinePhase() {
        System.out.println("offlinePhaseSimulation Starting");
        OfflinePhaseSimulation offlinePhaseSimulation = new OfflinePhaseSimulation();

        double timeToSchedule = 0;

        offlinePhaseSimulation.schedule(new offlinePhase(offlinePhaseSimulation, timeToSchedule, Parameters.VIRTUAL_HOST, 0, Parameters.getNumberOfParties()));
        //simulation.schedule(new votingSMPC(simulation, timeToSchedule, Parameters.VIRTUAL_HOST, 0, Parameters.getNumberOfParties()));

        offlinePhaseSimulation.doAllEvents();

        Config config = new Config();

        Parameters.NUMBER_OF_PARTIES = config.numberOfnodes;

        System.out.println("offlinePhaseSimulation Ends");
        System.out.println("offlinePhaseSimulation Recieves are " + Parameters.count);
        System.out.println("offlinePhaseSimulation Execution Time: " + offlinePhaseSimulation.getLastFinishingTimeForAllNodes());

        return (float) offlinePhaseSimulation.getLastFinishingTimeForAllNodes();

    }

    static private void searchForExecutionTime(PrintWriter writer, float delay, float bandWidth, float dataSize,
                                               int numberOfLayersTopology, int numberOfnodes) {


        //      1 <  data / bw  < 1000
        float currentLengthBeforeChange = 0;
        float acceptableTimeError = 100;
        boolean timeEnough = false;
        float LENGTH_RANGE = (float) 100 * (float) 1000000000;
        float lastLength = 0;
        float minLength = 0;
        float maxLength = LENGTH_RANGE;
        float currentLength = (maxLength + minLength) / 2;

        while (true) {
            System.out.println("length: " + currentLength);

            //set simulation configuration
            Config config = new Config();

            //settings that do not change
            config.delayDistType = NetworkPacket.RTTDelayDistributionType.CONSTANT;
            config.failureRate = Integer.MAX_VALUE;
            config.numberOfnodes = numberOfnodes;
            config.numberOfLayersTopology = numberOfLayersTopology;

            //settings that change
            config.nodeInitialDataSize = dataSize;
            config.bandWidth = bandWidth;
            config.delayDistType = NetworkPacket.RTTDelayDistributionType.CONSTANT;
            config.constantDelay = delay;

            config.lengthOfRound = currentLength;

            CommunicationTreeSimulator sim = new CommunicationTreeSimulator(config);
            sim.searchForMinimumRoundLength = true;
            sim.initialize();
            timeEnough = sim.simulate();

            //adjust the time for next cycles
            lastLength = currentLengthBeforeChange;
            currentLengthBeforeChange = currentLength;

            if (!timeEnough) {
                minLength = currentLength;
                currentLength = (currentLength + maxLength) / 2;

            } else if (timeEnough) {
                if (Math.abs(currentLengthBeforeChange - lastLength) < acceptableTimeError) {
                    writer.println(delay + " " + bandWidth + " " + numberOfLayersTopology + " " + dataSize + " " + config.numberOfnodes + " " + currentLength);
                    writer.flush();
                    break;
                } else {
                    maxLength = currentLength;
                    currentLength = (currentLength + minLength) / 2;
                }
            }

            if (currentLength == currentLengthBeforeChange) {
                currentLength++;
            }
            if (currentLength >= LENGTH_RANGE) {
                writer.println(delay + " " + bandWidth + " " + numberOfLayersTopology + " " + dataSize + " " + config.numberOfnodes + " " + LENGTH_RANGE + " overflow");
                writer.flush();
                break;
            }
        }
    }


    public static void main(String[] args) {
        PrintStream out = null;


        boolean offlinePhase = false;
        boolean onlinePhase = false;

        boolean commTreeNetwork = true;
        boolean searchForMinimumRoundLength = true;

        int expNumber = 6;

        if (expNumber == 1) {

            try {
                PrintWriter writer = new PrintWriter("EXP1_network.txt", "UTF-8");
                int numberOfLayersTopology = 8;
                int numberOfnodes = 10000;

                writer.println("EXP1_comm_network" + "numberOfnodes: " + numberOfnodes + " Layers:  " + numberOfLayersTopology);

                float delay = 50;

                for (int bandWidth = 1000; bandWidth <= 100000; bandWidth = bandWidth * 10) {
                    for (int dataSize = 1; dataSize < 1000000; dataSize = dataSize * 2) {
                        {
                            searchForExecutionTime(writer, delay, bandWidth, dataSize, numberOfLayersTopology, numberOfnodes);
                        }
                    }
                }

                writer.close();
            } catch (Exception e) {
                System.out.println("error occured");
            }


            try {
                PrintWriter writer = new PrintWriter("EXP1_online_cluster_40.txt", "UTF-8");

                Parameters.NUMBER_OF_PARTIES = 40; //this is only for one cluster
                Parameters.N_M = 10000;
                Parameters.N_A = 10000;


                int numberOfLayersTopology = 8;
                Parameters.RECEIVE_TIME = 1;

                writer.println("EXP1 " + "numberOfnodes: " + Parameters.NUMBER_OF_PARTIES + " Layers:  " + numberOfLayersTopology);
                float perClusterOnlineExecutionTime = runOnlinePhaseSim();
                writer.println("Execution time: " + perClusterOnlineExecutionTime);


                writer.close();
            } catch (Exception e) {
                System.out.println("error occured");
            }
        } else if (expNumber == 2) {

            /**
             *
             duration of offline Phase for different numbers of Layers
             with number of nodes 10000 nodes
             number of layers: 10  9      8     7    6
             cluster members:  5  10     40   80  150
             */


            /**
             *
             * communication network simulation
             */
            float delay = 50;
            float dataSize = 1000; //kb
            int bandWidth = 10000000;

            try {
                PrintWriter writer = new PrintWriter("EXP2_network.txt", "UTF-8");

                int[] numberOfnodesArray = {1000, 5000, 10000};

                for (int numberOfnodes : numberOfnodesArray) {
                    for (int numberOfLayersTopology = 6; numberOfLayersTopology <= 10; numberOfLayersTopology++) {
                        searchForExecutionTime(writer, delay, bandWidth, dataSize, numberOfLayersTopology, numberOfnodes);
                    }
                }

                writer.close();
            } catch (Exception e) {
                System.out.println("error occurred");
            }

            /**
             * offline phase cluster level simulation
             */

            try {
                PrintWriter writer = new PrintWriter("EXP2_offline_phase.txt", "UTF-8");
                writer.println("numberOfnodes Parameters.NUMBER_OF_PARTIES numberOfLayersTopology executionTime");
                Parameters.count = 0;

                int[] numberOfnodesArray = {1000, 5000, 10000};

                for (int numberOfnodes : numberOfnodesArray) {
                    for (int numberOfLayersTopology = 10; numberOfLayersTopology >= 8; numberOfLayersTopology--) {

                        Parameters.N_M = 10000;

                        Parameters.NUMBER_OF_PARTIES = (int) Math.ceil(numberOfnodes / (Math.pow(2, numberOfLayersTopology) - 1));

                        OfflinePhaseSimulation offlinePhaseSimulationToMeasureReshare = new OfflinePhaseSimulation();
                        double time_before = offlinePhaseSimulationToMeasureReshare.getLastFinishingTimeForAllNodes();
                        offlinePhaseSimulationToMeasureReshare.schedule(new Reshare(offlinePhaseSimulationToMeasureReshare, 0, Parameters.VIRTUAL_HOST, 0, Parameters.getNumberOfParties()));
                        offlinePhaseSimulationToMeasureReshare.doAllEvents();
                        double time_after = offlinePhaseSimulationToMeasureReshare.getLastFinishingTimeForAllNodes();

                        Parameters.RESHARE_TIMES = (time_after - time_before) * (Parameters.getNumberOfParties() * Math.ceil(Parameters.N_M) * 2);

                        /**
                         *start the experiment
                         */

                        float executionTime = simulateOfflinePhase();

                        writer.println(numberOfnodes + " " + Parameters.NUMBER_OF_PARTIES + " " + numberOfLayersTopology + " " + executionTime + " " + Parameters.RESHARE_TIMES);
                    }
                }

                writer.close();
            } catch (Exception e) {
                System.out.println("error occurred");
            }

        } else if (expNumber == 3) {

            int[] numberOfNodesArray = {1024, 2048, 4096, 8192, 16384, 32768, 65536, 131072, 262144, 524288, 1048576};
            int[] numberOfLayersTopologyArray = {8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18};

            float delay = 50;
            float dataSize = 1000;
            int bandWidth = 10000000;

            try {
                PrintWriter writer = new PrintWriter("EXP3_network_128.txt", "UTF-8");
                for (int i = 0; i < numberOfNodesArray.length; i++) {
                    int numberOfnodes = numberOfNodesArray[i];
                    int numberOfLayersTopology = numberOfLayersTopologyArray[i];
                    searchForExecutionTime(writer, delay, bandWidth, dataSize, numberOfLayersTopology, numberOfnodes);
                }
                writer.close();
            } catch (Exception e) {
                System.out.println("error occurred");
            }


            /**
             * OFFLINE CLUSTER PHASE
             */

            try {
                PrintWriter writer = new PrintWriter("EXP3_offline_phase_128.txt", "UTF-8");

                Parameters.count = 0;
                writer.println("Parameters.NUMBER_OF_PARTIES numberOfLayersTopology executionTime");

                Parameters.N_M = 10000;
                Parameters.NUMBER_OF_PARTIES = 6; //(int) Math.ceil(numberOfnodes/(Math.pow(2,numberOfLayersTopology) - 1));

                OfflinePhaseSimulation offlinePhaseSimulationToMeasureReshare = new OfflinePhaseSimulation();
                double time_before = offlinePhaseSimulationToMeasureReshare.getLastFinishingTimeForAllNodes();
                offlinePhaseSimulationToMeasureReshare.schedule(new Reshare(offlinePhaseSimulationToMeasureReshare, 0, Parameters.VIRTUAL_HOST, 0, Parameters.getNumberOfParties()));
                offlinePhaseSimulationToMeasureReshare.doAllEvents();
                double time_after = offlinePhaseSimulationToMeasureReshare.getLastFinishingTimeForAllNodes();

                Parameters.RESHARE_TIMES = (time_after - time_before) * (Parameters.getNumberOfParties() * Math.ceil(Parameters.N_M) * 2);
                System.out.println("time for a reshare: " + Parameters.RESHARE_TIMES + " numberOfLayersTopology: " + Parameters.NUMBER_OF_PARTIES);

                /**
                 *start the experiment
                 */

                float executionTime = simulateOfflinePhase();

                writer.println("Parameters.NUMBER_OF_PARTIES:" + 8 + " executionTime: " + executionTime);
                writer.close();
            } catch (Exception e) {
                System.out.println("error occurred");
            }

//
//
//            try {
//                PrintWriter writer = new PrintWriter("EXP3_offline_phase_original_SPDZ.txt", "UTF-8");
//
//                Parameters.count = 0;
//                writer.println("Parameters.NUMBER_OF_PARTIES numberOfLayersTopology executionTime");
//
//                Parameters.N_M = 10000;
//                Parameters.NUMBER_OF_PARTIES = 1024 ; //(int) Math.ceil(numberOfnodes/(Math.pow(2,numberOfLayersTopology) - 1));
//
//                OfflinePhaseSimulation offlinePhaseSimulationToMeasureReshare = new OfflinePhaseSimulation();
//                double time_before = offlinePhaseSimulationToMeasureReshare.getLastFinishingTimeForAllNodes();
//                offlinePhaseSimulationToMeasureReshare.schedule(new Reshare(offlinePhaseSimulationToMeasureReshare, 0, Parameters.VIRTUAL_HOST, 0, Parameters.getNumberOfParties()));
//                offlinePhaseSimulationToMeasureReshare.doAllEvents();
//                double time_after = offlinePhaseSimulationToMeasureReshare.getLastFinishingTimeForAllNodes();
//
//                Parameters.RESHARE_TIMES = (time_after - time_before) * (Parameters.getNumberOfParties() * Math.ceil(Parameters.N_M) *2);
//                System.out.println("time for a reshare: " + Parameters.RESHARE_TIMES + " numberOfLayersTopology: " + Parameters.NUMBER_OF_PARTIES);
//
//                /**
//                 *start the experiment
//                 */
//
//                float executionTime = simulateOfflinePhase();
//
//                writer.println("Parameters.NUMBER_OF_PARTIES:" + 8 + " executionTime: " + executionTime);
//                writer.close();
//            } catch (Exception e) {
//                System.out.println("error occurred");
//            }

        } else if (expNumber == 4) {


            float DELAY_COM = 50;
            float BW = 10000000;

            try {
                PrintWriter writer = new PrintWriter("EXP4.txt", "UTF-8");


                for (float N_SMALL = 10; N_SMALL < 100; N_SMALL += 10) {
                    for (float N_LARGE = 10; N_LARGE < 100; N_LARGE += 10) {
                        for (float DATA = 4; DATA < 40000; DATA *= 10) {

                            //from small to large;
                            float time_STOL = DELAY_COM + (DATA) / BW * N_LARGE;

                            //from large to small
                            float time_LTOS = DELAY_COM + DATA / BW * N_LARGE;

                            //inside small
                            float time_SS = DELAY_COM + DATA / BW * N_SMALL;

                            //inside large
                            float time_LL = DELAY_COM + DATA / BW * N_LARGE;

                            writer.println();
                        }

                    }

                }

            } catch (Exception e) {
                System.out.println("error occurred");
            }
        } else if (expNumber == 5) {
            /**
             * Measuer the time for running the online final cluster with various numbers!
             */

            try {
                PrintWriter writer = new PrintWriter("EXP5_online_cluster_size.txt", "UTF-8");

                writer.println("numberOfParties perClusterOnlineExecutionTime");
                for (int numberOfParties = 5; numberOfParties <= 30; numberOfParties = numberOfParties + 3) {

                    Parameters.NUMBER_OF_PARTIES = numberOfParties; //this is only for one cluster
                    Parameters.N_M = 10000;
                    Parameters.N_A = 10000;

                    Parameters.RECEIVE_TIME = 1;

                    float perClusterOnlineExecutionTime = runOnlinePhaseSim();
                    writer.println(numberOfParties + " " + perClusterOnlineExecutionTime);
                    writer.flush();
                }

                writer.close();
            } catch (Exception e) {
                System.out.println("error occured");
            }


        }
        else if (expNumber == 6) {


            try {
                PrintWriter writer = new PrintWriter("EXP5_online_cluster_size.txt", "UTF-8");

                writer.println("numberOfParties perClusterOnlineExecutionTime");
//                for (int numberOfParties = 5; numberOfParties <= 30; numberOfParties = numberOfParties + 3) {

                int numberOfParties = 40;

                    Parameters.NUMBER_OF_PARTIES = numberOfParties; //this is only for one cluster
                    Parameters.N_M = 10000;
                    Parameters.N_A = 10000;

                    Parameters.RECEIVE_TIME = 1;

                    float perClusterOnlineExecutionTime = runOnlinePhaseSim();
                    writer.println(numberOfParties + " " + perClusterOnlineExecutionTime);
                System.out.println(numberOfParties + " " + perClusterOnlineExecutionTime);
                    writer.flush();
//                }

                writer.close();
            } catch (Exception e) {
                System.out.println("error occured");
            }


        }

//
//
//        if (offlinePhase == true) {
//
//            simulateOfflinePhase();
//
//        } else if (onlinePhase == true) {
//			/*
//			Here we run online phase once to measure how long it takes to execute it for one cluser and then later add that to the execution time
//			 */
//
//            float perClusterOnlineExecutionTime = runOnlinePhaseSim();
//
//        } else if (commTreeNetwork == true) {
//
//			/*
//			Start the communication network simulation with the parameter measured above
//			 */
//            // search for a minimum round duration time that all the packets can be delivered
//            if (searchForMinimumRoundLength == false) {
//                Config config = new Config();
//                config.lengthOfRound = 30000;
//                CommunicationTreeSimulator sim = new CommunicationTreeSimulator(config);
//                sim.initialize();
//                sim.simulate();
//            } else if (searchForMinimumRoundLength == true) {
//
//                try {
//                    PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
//
//                    for (float delay = 1; delay < 200; delay += 50) {
//                        for (int bandWidth = 1; bandWidth < 100000; bandWidth = bandWidth * 2) {
////          				for(float dataSize = 1 ; dataSize < 1000000000 ; dataSize = dataSize *  4){
//                            {float dataSize = 1000;
//
//                                int numberOfLayersTopology = 9;
//                                int numberOfnodes = 1000;
//                                writer.println("");
//                                searchForExecutionTime(writer, delay, bandWidth, dataSize, numberOfLayersTopology, numberOfnodes);
//
//                            }
//                        }
//                    }
//
//                    writer.close();
//                } catch (Exception e) {
//                    System.out.println("error occured");
//                }
//            }
//        }

    }


    public class FailedNode {
        float timeLeftToRecoverMilliseconds;
        int ID;

        public FailedNode(float roundsLeft, int ID) {
            this.ID = ID;
            this.timeLeftToRecoverMilliseconds = roundsLeft;
        }
    }


}

