package Zeus;


/**
 *
 * <p>Title: Zeus - A Unified Object Oriented Model for Routing and Scheduling Problems</p>
 * <p>Description: This class is used to hold all the constants used throughout the Zeus
 *    system. This class is a static class and does not require instantiation to access the
 *    variable locations.The MaxCombination constant can be invoked using  Zeus.MaxCombinations. </p>
 * <p>Copyright: Copyright (c) 2001-2003</p>
 * <p>Company: </p>
 * @version 1.0
 * @author Sam R. Thangiah
 */
public class ZeusConstants {
    //Constant values
    public final static int MaxCombinations = 12; //total number of combination days
    public final static int MaxHorizon = 7; //total horizon, week is a 7 day horizon

    //constants used in the Tabu search
    final static int FIRST_FIRST = 0;

    //constants used in the Tabu search
    final static int FIRST_BEST = 1;

    //constants used in the Tabu search
    final static int BEST_BEST = 2;
    final static int EXCHANGE_01 = 0;
    final static int EXCHANGE_02 = 1;
    final static int EXCHANGE_10 = 2;
    final static int EXCHANGE_11 = 3;
    final static int EXCHANGE_12 = 4;
    final static int EXCHANGE_20 = 5;
    final static int EXCHANGE_21 = 6;
    final static int EXCHANGE_22 = 7;

    //Constant to initiate Tabu search

    /*final static boolean USE_TABU = true,
   DO_NOT_USE_TABU = false;*/

    //Do not change these values to use or not use Tabu. In the problem file,
    //e.g. MDVRP, set the Tabu_or_Not_To_Tabu.
    final static boolean USE_TABU = true;

    //Constant to initiate Tabu search

    /*final static boolean USE_TABU = true,
   DO_NOT_USE_TABU = false;*/

    //Do not change these values to use or not use Tabu. In the problem file,
    //e.g. MDVRP, set the Tabu_or_Not_To_Tabu.
    final static boolean DO_NOT_USE_TABU = false;

    /**
 * Exchanges used for moving customers between routes
 * @param exchangeType Type of exchange being done.
 * @return Returns the exchange to be done in String format ("01", "02)
 */
    public static String getExchangeName(int exchangeType) {
        switch (exchangeType) {
        case EXCHANGE_01:
            return "01";

        case EXCHANGE_02:
            return "02";

        case EXCHANGE_10:
            return "10";

        case EXCHANGE_11:
            return "11";

        case EXCHANGE_12:
            return "12";

        case EXCHANGE_20:
            return "20";

        case EXCHANGE_21:
            return "21";

        case EXCHANGE_22:
            return "22";
        }

        return null;
    }
}
