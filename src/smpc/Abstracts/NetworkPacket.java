package smpc.Abstracts;

import java.util.Enumeration;

public class NetworkPacket {
	int startTime;
	int numberOfRemainingPackets;
	
	PacketType packetType;

	public enum PacketType{
	    SUNDAY, MONDAY, TUESDAY, WEDNESDAY,
	    THURSDAY, FRIDAY, SATURDAY 
	}	
	
	public NetworkPacket(int startTime, int numberOfRemainingPackets, PacketType packetType) {
		this.startTime = startTime;
		this.numberOfRemainingPackets = numberOfRemainingPackets;
		this.packetType = packetType;
	}
}
