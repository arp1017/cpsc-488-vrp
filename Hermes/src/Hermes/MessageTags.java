package Hermes;

import java.io.*;

import java.util.*;


/**
 * <p>Title: MessageTags.java </p>
 * <p>Description:This interface class contains the various attributes used for
 * message tags during communicating amongst the various agents (shipper,
 * carrier and masterserver). </p>
 * @author Ola Laleye, Mike McNamara, Anthony Pitluga
 * @version 2.3
 */
public interface MessageTags {
    public static String AcceptTag = "Accept";
    public static String AckTag = "Ack";
    public static String AfterIndexTag = "AfterIndex";
    public static String AgentTypeTag = "AgentType";
    public static String AuctionBreakTag = "AuctionBreak";
    public static String BestSavingsTag = "BestSavings";
    public static String BestTwoSavingsTag = "BestTwoSavings";
    public static String BroadcastTag = "Broadcast";
    public static String BroadcastOneOptTag = "BroadcastOneOpt";
    public static String BroadcastTwoOptTag = "BroadcastTwoOpt";
    public static String CarrierCountTag = "CarrierCount";
    public static String Change1_0Tag = "Change1_0";
    public static String Change1_1Tag = "Change1_1";
    public static String Change2_0Tag = "Change2_0";
    public static String Change2_1Tag = "Change2_1";
    public static String Change2_2Tag = "Change2_2";
    public static String CalcBestSavingsTag = "CalcBestSavings";
    public static String CalcBestTwoSavingsTag = "CalcBestTwoSavings";
    public static String CalculateTag = "Calculate";
    public static String CalculateExchangeTag = "CalculateExchange";
    public static String CapacityTag = "Capacity";
    public static String CarrierNumberTag = "CarrierNumber";
    public static String CodeTag = "Code";
    public static String ConfirmTag = "Confirm";
    public static String CostTag = "Cost";
    public static String CustomerTag = "Customer";
    public static String DemandTag = "Demand";
    public static String DepotNumberTag = "DepotNumber";
    public static String DestinationTag = "Destination";
    public static String DetailSolutionTag = "DetailSolution";
    public static String DistanceTag = "Distance";
    public static String EarlyTimeTag = "EarlyTime";
    public static String EnableTag = "Enable";
    public static String EndBroadcastTag = "EndBroadcast";
    public static String EndSendPointsTag = "EndPoints";
    public static String ExcessTimeTag = "ExcessTime";
    public static String ExchangeCostTag = "ExchangeCost";
    public static String ExchangeInsertCostTag = "ExchangeInsertCost";
    public static String ExchangeTag = "Exchange";
    public static String ExchangeTypeTag = "ExchangeType";
    public static String FeasibilityTag = "Feasibility";
    public static String FileNameTag = "FileName";
    public static String GetCustomerTag = "GetCustomer";
    public static String GetNumCustomersTag = "GetNumCustomers";
    public static String GetSummaryTag = "GetSummary";
    public static String GlobalOptTag = "GlobalOpt";
    public static String IndexTag = "Index";
    public static String InetAddressTag = "InternetAddress";
    public static String InsertCostTag = "InsertCost";
    public static String IPAddressTag = "IPAddress";
    public static String LateTimeTag = "LateTime";
    public static String LocalOptTag = "LocalOpt";
    public static String MasterPortTag = "MasterPort";
    public static String MaxCapacityTag = "MaxCapacity";
    public static String MaxDistanceTag = "MaxDistance";
    public static String NameTag = "Name";
    public static String NumberOfCarriersTag = "NumberOfCarriers";
    public static String NumberOfCustomersTag = "NumberOfCustomers";
    public static String NumberOfDepotsTag = "NumberOfDepots";
    public static String OverloadTag = "Overload";
    public static String OptimizationInsertCostTag = "OptimizationInsertCostTag";
    public static String PackagedMessageTag = "Package";
    public static String PointCellSavingsTag = "PointCellSavings";
    public static String PortNumberTag = "PortNumber";
    public static String RefuseTag = "Refuse";
    public static String RegisterTag = "Register";
    public static String RelayTag = "Relay";
    public static String RelayAddressTag = "RelayAddress";
    public static String RunBroadcastOneOpt = "RunBroadcastOneOpt";
    public static String RunBroadcastTwoOpt = "RunBroadcastTwoOpt";
    public static String ServiceTimeTag = "ServiceTime";
    public static String ShipmentTag = "Shipment";
    public static String ShortSolutionTag = "ShortSolution";
    public static String StartBroadcastTag = "StartBroadcast";
    public static String StartSendPointsTag = "StartPoints";
    public static String SummaryTag = "Summary";
    public static String TardinessTag = "Tardiness";
    public static String TerminateTag = "Terminate";
    public static String TotalTimeTag = "TotalTime";
    public static String TotalSavingsTag = "TotalSavings";
    public static String TruckNumberTag = "TruckNumber";
    public static String UnregisterTag = "UnRegister";
    public static String WaitTimeTag = "WaitTime";
    public static String XCoordTag = "XCoord";
    public static String YCoordTag = "YCoord";
    public static String CarrierMessage = "Carrier";
    public static String Exchange1_0Message = "Exchange1_0";
    public static String Exchange1_1Message = "Exchange1_1";
    public static String Exchange2_0Message = "Exchange2_0";
    public static String Exchange2_1Message = "Exchange2_1";
    public static String Exchange2_2Message = "Exchange2_2";
    public static String ServerMessage = "Server";
    public static String ShipperMessage = "Shipper";
}
