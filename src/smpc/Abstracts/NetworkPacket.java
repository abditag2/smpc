package smpc.Abstracts;

import java.util.Enumeration;
import java.util.Random;

import smpc.Config;

public class NetworkPacket implements Comparable {
	int startTime;
	float numberOfRemainingPackets;	
	PacketType packetType;


	public NetworkPacket(int startTime, PacketType packetType) {
		this.startTime = startTime;
		this.packetType = packetType;
	}
	
	
	static public int getDelayInMilliSeconds(RTTDelayDistributionType distribution, Config config){
		double RTTDelay = 0;
		
		if(distribution == RTTDelayDistributionType.LINEAR) {
			Random rndGen = new Random();
			RTTDelay = (double)config.RTTmin + rndGen.nextDouble() * (double)(config.RTTmax - config.RTTmin);
		}
		return (int) RTTDelay;
	}

	@Override
	public int compareTo(Object o) {
		NetworkPacket anotherPacket = (NetworkPacket) o ;
		return anotherPacket.startTime - this.startTime;
	}

	public enum PacketType{
	    INPUTPACKET, 
	    FORWAREDPACKET,
	    COMBINEDPACKET
	}	
	
	public enum RTTDelayDistributionType{
	    LINEAR
	}	
	
}
