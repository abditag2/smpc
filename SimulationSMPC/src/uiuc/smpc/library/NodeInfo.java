package uiuc.smpc.library;

import java.util.ArrayList;
import java.util.HashMap;

public class NodeInfo {
	
	public double finishTimes ; 
	public int nodeID ;
	public HashMap<String, Integer> eventCountMap;
	
	public NodeInfo(int nodeID)
	{
		
		this.finishTimes = 0 ; 
		this.nodeID = nodeID ; 
		this.eventCountMap = new HashMap<String, Integer>() ;
	}

	
	

	
	
	public double getFinishTime(){
		return finishTimes; 
	}

	public void setFinishTime(double finishTime){
		this.finishTimes = finishTime;
		
	}

	
}
