package smpc.Abstracts;

import java.util.Enumeration;

public class NetworkPacket implements Comparable {
	int startTime;
	int totalPacketContent;
	float numberOfRemainingPackets;
	
	PacketType packetType;

	public enum PacketType{
	    PHASE1, PHASE2
	}	
	
	public NetworkPacket(int startTime, int totalPacketContent, PacketType packetType) {
		this.startTime = startTime;
		this.numberOfRemainingPackets = totalPacketContent;
		this.totalPacketContent = totalPacketContent;
		this.packetType = packetType;
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		NetworkPacket anotherPacket = (NetworkPacket) o ;
		return anotherPacket.startTime - this.startTime;
	}
}
