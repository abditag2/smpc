package smpc.Abstracts;

import java.util.Enumeration;

public class NetworkPacket {
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
}
