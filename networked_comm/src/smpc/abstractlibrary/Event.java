package smpc.abstractlibrary;

import smpc.library.OnlinePhaseSimulation;

public abstract class Event implements Comparable{

	//This value needs to be defined with every new event type
	
    protected double startTime;
    protected OnlinePhaseSimulation onlinePhaseSimulation;

    protected int hostID ; 
    public Double duration = null;
    
    public int start, end ; 
    
    public String type = null; 
     
	public int compareTo(Object o) {
		
		Event e = (Event) o ;
		//ascending order
		return (int) (this.getFinishingTime() - e.getFinishingTime());
 
		//descending order
		//return compareQuantity - this.quantity;
 
	}
     
    public double getFinishingTime(){
        double finishing_time = startTime + duration;
    	this.onlinePhaseSimulation.time = Math.max(this.onlinePhaseSimulation.time, finishing_time );
        return finishing_time;
    }
    
    public double getStartTime(){
    	return startTime;
    }
    
    public int getHostID(){
    	return hostID ; 
    }
    
    
    public void setStartTime(double startTime){
    	this.startTime = startTime;
    }
    
	abstract public boolean execute();	

}


