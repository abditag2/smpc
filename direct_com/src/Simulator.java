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

    public void Simulator(){

    }


    public static void main(String[] args){

        try {
            PrintWriter writer = new PrintWriter("output.txt", "UTF-8");

//            int bandwidth = 100;
            for(int bandWidth = 1 ; bandWidth < 2000000 ; bandWidth = bandWidth * 2) {
                for (long dataSize = 1; dataSize < 1000000000; dataSize = dataSize * 4) {
                    //delay is per 10 ms

                    for (long delay = 1; delay < 20; delay = delay + 2) {

                        System.out.println("Simulation started");
                        Config config = new Config();

                        config.BANDWIDTH = 100;
                        config.COMMUNICATION_SETUP_OVERHEAD = delay;
                        //taking into account the over head of TCP
                        config.AVG_DATA_SIZE = dataSize + (long)Math.floor(dataSize / config.TCP_PACKET_SIZE) *  config.TCP_PACKET_OVERHEAD_BYTES;
                        config.NUMBER_OF_EDGE_NODES = 10000;
                        config.CONCURRENT_CONNECTIONS = 10;

                        MainClusterNode mainNode = new MainClusterNode();
                        float[] results = mainNode.runSimulation();

                        System.out.println("Results:");
                        System.out.println("\ttime: " + results[0]);
                        writer.println(bandWidth + " " + delay + " " + dataSize + " " + results[0]);
                    }
                }
            }
            writer.close();
        }
        catch (Exception e){
            System.out.println("Error Happened");
        }



    }
}
