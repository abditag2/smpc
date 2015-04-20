package Nodes;


import java.util.ArrayList;

import Config.Config;
import Util.Utils;
import javafx.util.Pair;

/**
 * Created by tanish on 4/19/15.
 */

public class MainClusterNode {
    float currentTime;

    float[] startTimes;
    float[] endTimes;
    float[] remainingData;

    ArrayList<EdgeNode> edgeNodesList;

    public MainClusterNode(){

        currentTime = 0;

        edgeNodesList = new ArrayList<EdgeNode>();
        for (int i = 0; i < Config.NUMBER_OF_EDGE_NODES ; i++){
            EdgeNode node  = new EdgeNode(Utils.GetDataSizeForNode(i));
            edgeNodesList.add(node);
        }

        startTimes = new float[Config.CONCURRENT_CONNECTIONS] ;
        for (int i = 0 ; i < Config.CONCURRENT_CONNECTIONS ; i++) {
            startTimes[i] = Float.MAX_VALUE;
        }

        endTimes = new float[Config.CONCURRENT_CONNECTIONS];
        for (int i = 0 ; i < Config.CONCURRENT_CONNECTIONS ; i++){
            endTimes[i] = Float.MAX_VALUE;
        }

        remainingData = new float[Config.CONCURRENT_CONNECTIONS];
        for (float dataSize: remainingData)
            dataSize = 0;
    }

    public float[] runSimulation(){

        float lastEventTime = 0 ;

        //before starting set the channels
        for(int i = 0 ; i < Config.CONCURRENT_CONNECTIONS ; i++){
            EdgeNode node = edgeNodesList.remove(0);
            remainingData[i] = node.dataSize;
            startTimes[i] = Utils.GetCommunicationSetupOverHead(i) + Utils.GetNetworkDelay(i) + currentTime + (float)0.0001;
        }

        int left1 = 0 ;
        int left2 = 0;
        int count = 0 ;
        while( (int) Utils.Min(startTimes).getKey() != -1){

            left2 = left1;
            left1 = edgeNodesList.size();
            if(left2 == left1) count++;
            else count = 0;

            if (count > 100)
                System.out.println("Stuck on Node: " + left1);

            Pair startTimesMinPairinfo = Utils.minValueLargerThanCurrent(startTimes, currentTime);
            float startTimesMin = (float) startTimesMinPairinfo.getValue();
            int startTimesMinID = (int) startTimesMinPairinfo.getKey();

            Pair endTimesMaxPairInfo = Utils.Min(endTimes);
            float endTimesMin = (float) endTimesMaxPairInfo.getValue();
            int endTimesMinID = (int) endTimesMaxPairInfo.getKey();

            //store the time of the last event before updating the gloabltime
            lastEventTime = currentTime;

            if(startTimesMin < endTimesMin){
                //it is a start  event
                currentTime = startTimesMin;
                updateRemainingData(lastEventTime);
                updateEndtimes(currentTime);
            }
            else if(startTimesMin >= endTimesMin){
                //it is a end  event
                //update remaining data before removing finished flows
                currentTime = endTimesMin;
                updateRemainingData(lastEventTime);

                //this is the end of a flow, remove the flows that end
                if(!edgeNodesList.isEmpty()){
                    EdgeNode node = edgeNodesList.remove(0);
                    remainingData[endTimesMinID] = node.dataSize;
                    startTimes[endTimesMinID] = Utils.GetCommunicationSetupOverHead(endTimesMinID) + Utils.GetNetworkDelay(endTimesMinID) + currentTime;
                    endTimes[endTimesMinID] = Float.MAX_VALUE;
                }
                else{
                    remainingData[endTimesMinID] = Float.MAX_VALUE;
                    startTimes[endTimesMinID] = Float.MAX_VALUE;
                    endTimes[endTimesMinID] = Float.MAX_VALUE;
                }

                //update end times after finished flows are over and the new values are updated
                updateEndtimes(currentTime);

            }
        }

        float[] resultArray = new float[2];
        resultArray[0] = currentTime; // time of finishing the flows
        return resultArray;
    }


    public void updateEndtimes(float currentTime){
        //update all the end times for the only active flows based on the number of active flows
        float rate = Utils.GetRateBasedOnNumberOfActiveFlows(Config.BANDWIDTH, currentTime, startTimes, endTimes);
        for (int i = 0 ; i < Config.CONCURRENT_CONNECTIONS ; i ++){
            if(startTimes[i] <= currentTime ){
                endTimes[i] = remainingData[i]/rate + currentTime;
            }
        }
    }

    public void updateRemainingData(float lastEventTime){
        //calculate the rate and update the remaining of datasize for each channel
        float rate = Utils.GetRateBasedOnNumberOfActiveFlows(Config.BANDWIDTH, lastEventTime, startTimes, endTimes);

        //update all the remaining data based on how much time has passed since last event and what the rate is
        for (int i = 0 ; i < Config.CONCURRENT_CONNECTIONS ; i ++){
            if (startTimes[i] < currentTime && endTimes[i] >= currentTime){
                remainingData[i] = remainingData[i] - rate * (currentTime - lastEventTime);
            }
        }
    }


}
