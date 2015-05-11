//package smpc.test;
//
//import static org.junit.Assert.*;
//
//import java.util.*;
//
//import javax.lang.model.util.SimpleAnnotationValueVisitor6;
//
//import org.junit.*;
//
//import smpc.CommunicationTreeSimulator.*;
//import smpc.Topology;
//import smpc.Config;
//import smpc.CommunicationTreeSimulator;
//import smpc.Abstracts.*;
//import smpc.Abstracts.NetworkPacket.PacketType;
//
//public class Tests {
//    @Test
//    public void testTopology() {
//		Config config = new Config();
//		Topology topology = new Topology(config);
//		int cluster = topology.getClusterNumber(5);
//		assertEquals(5, cluster);
//
//		ArrayList<Integer> members = topology.getClusterMembers(1);
//		ArrayList<Integer> arr = new ArrayList<Integer>(){
//			{
//				add(1);
//				add (8);
//			}
//		};
//		assertEquals(arr, members);
//
//		ArrayList<Integer> members2= topology.getClusterMembers(5);
//		ArrayList<Integer> arr2 = new ArrayList<Integer>(){
//			{
//				add(5);
//			}
//		};
//		assertEquals(arr2, members2);
//    }
//
//    @Test
//    public void testGetFailedNodes(){
//    	CommunicationTreeSimulator sim = new CommunicationTreeSimulator(new Config());
//    	sim.initialize();
//    	List<Integer> randNumbers = sim.getMRandomNumbersOutOfN(10, 1000);
//    	assert(randNumbers.size() == 10);
//    	for (Integer number:randNumbers)
//    	{
//    		assert(number < 1000);
//    	}
//    }
//
//    @Test
//    public void testFailedNodeIDs() {
//    	CommunicationTreeSimulator sim = new CommunicationTreeSimulator(new Config());
//    	ArrayList<FailedNode> failedNodes = sim.getFailedNodes(100000 ,1000, 60);
//
//    	//just manually check this outputs :)
//    }
//
//    @Test void testgetMyLayerNumber() {
//    	Config config = new Config();
//    	config.nArry = 3;
//    	config.numberOfLayersTopology  = 10;
//    	CommunicationTreeSimulator sim = new CommunicationTreeSimulator(config);
//
//    	int layer = sim.getMyLayerNumber(12, config);
//
//    	assertEquals(layer,3);
//
//
//    }
//}