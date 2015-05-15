import Config.Config;
import Nodes.EdgeNode;
import Nodes.MainClusterNode;

import java.io.PrintWriter;

/**
 * Created by tanish on 4/9/15.
 * Create the topology
 * Take into account the failures
 * Variable data size
 * Variable communication overhead
 */

public class Simulator {

    public void Simulator() {

    }


    public static void main(String[] args) {

        try {
            PrintWriter writer = new PrintWriter("EXP1_direct_communication.txt", "UTF-8");

            int numberOfnodes = 10000;

            writer.println("EXP1_direct_communication" + "numberOfnodes: " + numberOfnodes);
            float delay = 50;

            for (int bandWidth = 1000; bandWidth <= 100000; bandWidth = bandWidth * 10) {
                for (int dataSize = 1; dataSize < 1000000; dataSize = dataSize * 2) {

                    Config config = new Config();

                    config.BANDWIDTH = bandWidth;
                    config.COMMUNICATION_SETUP_OVERHEAD = delay;

                    //taking into account the over head of TCP
                    config.AVG_DATA_SIZE = dataSize + (long) Math.floor(dataSize / config.TCP_PACKET_SIZE) * config.TCP_PACKET_OVERHEAD_BYTES;
                    config.NUMBER_OF_EDGE_NODES = numberOfnodes;
                    config.CONCURRENT_CONNECTIONS = 20;

                    MainClusterNode mainNode = new MainClusterNode();
                    float[] results = mainNode.runSimulation();

                    writer.println(delay + " " + bandWidth + " " + dataSize + " " + results[0]);
                }
            }

            writer.close();
        } catch (Exception e) {
            System.out.println("Error Happened");
        }


    }
}
