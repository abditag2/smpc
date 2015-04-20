package Config;

/**
 * Created by tanish on 4/9/15.
 */
public final class Config {

    /*
    each epoch size
     */
    public static final int NUMBER_OF_EDGE_NODES = 10000;
    public static final int CONCURRENT_CONNECTIONS = 2;
    public static final int BANDWIDTH = 100; //data units per second
    public static final int AVG_DATA_SIZE = 1000; //data units

    public static final float COMMUNICATION_SETUP_OVERHEAD = 10; // seconds
    public static final float NETWORK_DELAY_AVERAGE = 0; // seconds
    public static final float STD_DEVIATION_NETWORK_DELAY = 0 ; //(float) 0.5; // seconds

}
