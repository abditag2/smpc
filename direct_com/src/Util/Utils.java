package Util;

import Config.Config;

import javafx.util.Pair;

/**
 * Created by tanish on 4/19/15.
 */
public class Utils {

    public static float GetNetworkDelay(int ID){
        return 0;
    }

    public static float GetCommunicationSetupOverHead(int ID){
        return 0;
    }

    public static int GetDataSizeForNode(int nodeID){
        return Config.AVG_DATA_SIZE;
    }

    public static Pair minValueLargerThanCurrent(float[] array, float currentValue){
        float rv = Float.MAX_VALUE;
        int id = -1 ;

        for(int i = 0 ; i < array.length ; i++){
            float val = array[i];
            if(val < rv && val > currentValue){
                rv = val;
                id = i;
            }
        }

        Pair pairRV = new Pair(id, rv);
        return pairRV;
    }


    public static Pair Min(float[] array){
        float rv = Float.MAX_VALUE;
        int id = -1 ;

        for(int i = 0 ; i < array.length ; i++){
            float val = array[i];
            if(val < rv){
                rv = val;
                id = i;
            }
        }

        Pair pairRV = new Pair(id, rv);
        return pairRV;
    }

    public static int GetNumberOfActiveFlows(float currentTime, float[] startTimes){
        int countOfActiveChannels = 0;
        for(int i = 0 ; i < Config.CONCURRENT_CONNECTIONS ; i++){
            if (startTimes[i] <= currentTime){
                countOfActiveChannels++;
            }
        }
        return countOfActiveChannels;
    }

    public static float GetRateBasedOnNumberOfActiveFlows(float bandwidth, float currentTime, float[] startTimes){
        float rate = 0 ;
        int countOfActiveChannels = GetNumberOfActiveFlows(currentTime, startTimes);
        if (countOfActiveChannels > 0 ){
            rate = bandwidth / countOfActiveChannels;
        }
        else{
            rate = 0;
        }
        return rate;
    }

}
