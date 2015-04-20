import Config.Config;
import Nodes.MainClusterNode;

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

        System.out.println("Simulation started");
        Config config = new Config();
        MainClusterNode mainNode = new MainClusterNode();
        float[] results = mainNode.runSimulation();

        System.out.println("Results:");
        System.out.println("\ttime: " + results[0]);


    }
}
