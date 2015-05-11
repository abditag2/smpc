package Config;

/**
 * Created by tanish on 4/9/15.
 */
public final class Config {

    /*
    each epoch size
     */
    public static  int NUMBER_OF_EDGE_NODES = 10000;
    public static  int CONCURRENT_CONNECTIONS = 4;
    public static  int BANDWIDTH = 100; //data units per second
    public static  long AVG_DATA_SIZE = 1000; //data units

    public static  float COMMUNICATION_SETUP_OVERHEAD = 1; // seconds
    public static  float NETWORK_DELAY_AVERAGE = 0; // seconds
    public static  float STD_DEVIATION_NETWORK_DELAY = 0 ; //(float) 0.5; // seconds

    public static long TCP_PACKET_SIZE = 1460;
    public static long TCP_PACKET_OVERHEAD_BYTES = 40;


}
