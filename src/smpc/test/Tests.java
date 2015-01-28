package smpc.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import org.junit.*;

import smpc.Topology;
import smpc.Config;
import smpc.Abstracts.*;
import smpc.Abstracts.NetworkPacket.PacketType;

public class Tests {
    @Test
    public void testTopology() {
		Config config = new Config();
		Topology topology = new Topology(config);
		int cluster = topology.getClusterNumber(5);
		assertEquals(5, cluster);
		
		ArrayList<Integer> members = topology.getClusterMembers(1);
		ArrayList<Integer> arr = new ArrayList<Integer>(){
			{
				add(1);
				add (8);
			}
		};
		assertEquals(arr, members);
		
		ArrayList<Integer> members2= topology.getClusterMembers(5);
		ArrayList<Integer> arr2 = new ArrayList<Integer>(){
			{
				add(5);
			}
		};
		assertEquals(arr2, members2);
    }
    
    @Test
    public void testNodePacketScheduling(){
    	Node node = new Node(1000, 1);
    	
    	NetworkPacket packet1 = new NetworkPacket(0, 100, PacketType.PHASE1);
    	NetworkPacket packet2 = new NetworkPacket(10, 100, PacketType.PHASE1);
    	node.recievePacket(networkPacket);
    	
    }
}