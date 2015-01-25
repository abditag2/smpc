package smpc;

import java.util.*;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import smpc.Config;

public class Topology {
	//Define the nodes topology here
	
	HashMap<Integer, Integer> clusterParents;
	Config config; 
	
	public Topology(Config config){
		this.config = config ; 
		this.clusterParents = setClusterParents(this.config.numberOfClusters(), this.config.nArry);

	}

	private HashMap<Integer, Integer>  setClusterParents(int numberOfClusters, int nArry) {
		HashMap<Integer, Integer> clusterParents = new HashMap<Integer, Integer>();
		
		clusterParents.put(0, null);
		Queue<Integer> q = new LinkedList<Integer>();
		q.add(0);
		int count = 1;
		
		while(!q.isEmpty()){
			int last = q.remove();
			
			for (int k = 0 ; k < nArry; k++){
				
				if (count<this.config.numberOfClusters()){
					clusterParents.put(count,last);
					q.add(count);	
					count++ ; 
				}
			}
		}	
		
		return clusterParents;
	}
	
	
	public int getParent(int clusterID){
		return clusterParents.get(clusterID);
	}
	
	public int getClusterNumber(int nodeId){
		return nodeId%this.config.numberOfClusters();
	}
	
	public ArrayList<Integer> getClusterMembers(int clusterID){
		ArrayList<Integer> clusterMembers = new ArrayList<Integer>();
		for(int i = 0 ; clusterID + i < this.config.numberOfnodes; i= i + this.config.numberOfClusters())
		{
			clusterMembers.add(clusterID + i);			
		}
		return clusterMembers;
	}
}

