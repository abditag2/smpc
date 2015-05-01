package smpc.Abstracts;

import java.util.Random;

import smpc.Config;

public class NetworkPacket implements Comparable {
	int startTime;
	int source;
	int destination;
	float newLoadData;
	float oldLoadData;
	PacketType packetType;


	public NetworkPacket(int startTime, PacketType packetType, float newLoadData, float oldLoadData, int source, int destination) {
		this.startTime = startTime;
		this.packetType = packetType;
		this.newLoadData = newLoadData;
		this.oldLoadData = oldLoadData;
		this.source = source;
		this.destination = destination;
	}
	
	
	static public int getDelayInMilliSeconds(RTTDelayDistributionType distribution, Config config){
		double RTTDelay = 0;
		Random rndGen = new Random();
		
		if(distribution == RTTDelayDistributionType.LINEAR) {
			RTTDelay = (double)config.RTTmin + rndGen.nextDouble() * (double)(config.RTTmax - config.RTTmin);
		}
		else if(distribution == RTTDelayDistributionType.GAUSSIAN) {
			RTTDelay = (double)config.avg + rndGen.nextGaussian()*config.deviation;
		}
		else if(distribution == RTTDelayDistributionType.NODELAY){
			RTTDelay = 0;
		}
		else if(distribution == RTTDelayDistributionType.CONSTANT){
			RTTDelay = config.constantDelay;
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
	    LINEAR,
	    GAUSSIAN,
		CONSTANT,
		NODELAY
	}	
	
}
