package Util;

import Config.Config;

import javafx.util.Pair;
import java.util.Random;

/**
 * Created by tanish on 4/19/15.
 */
public class Utils {

    public static float GetNetworkDelay(int ID){
        Random r = new Random();
        return (float) r.nextGaussian() * Config.STD_DEVIATION_NETWORK_DELAY + Config.NETWORK_DELAY_AVERAGE;
    }

    public static float GetCommunicationSetupOverHead(int ID){
        return Config.COMMUNICATION_SETUP_OVERHEAD;
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

    public static int GetNumberOfActiveFlows(float currentTime, float[] startTimes, float[] endTimes){
        int countOfActiveChannels = 0;
        for(int i = 0 ; i < Config.CONCURRENT_CONNECTIONS ; i++){
            if (startTimes[i] <= currentTime && endTimes[i] > currentTime){
                countOfActiveChannels++;
            }
        }
        return countOfActiveChannels;
    }

    public static float GetRateBasedOnNumberOfActiveFlows(float bandwidth, float currentTime, float[] startTimes, float[] endTimes){
        float rate = 0 ;
        int countOfActiveChannels = GetNumberOfActiveFlows(currentTime, startTimes, endTimes);
        if (countOfActiveChannels > 0 ){
            rate = bandwidth / countOfActiveChannels;
        }
        else{
            rate = Config.BANDWIDTH;
        }
        return rate;
    }

}
