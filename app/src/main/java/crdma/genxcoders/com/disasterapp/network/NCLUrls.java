package crdma.genxcoders.com.disasterapp.network;


import crdma.genxcoders.com.disasterapp.utils.AppConstants;

/**
 * Created by admin on 10/21/2016.
 */

public class NCLUrls implements AppConstants {
    //Production Environment
//    static String BASE_URL = "http://ec2-18-221-160-33.us-east-2.compute.amazonaws.com:8080/projectx/api/";
    public static String BASE_URL = "https://genxcoders.in/demo/crdma/json/";

    public static String HELPLINE_CONTACTS_URL= BASE_URL+"helplineJson.php";
    public static String LOGIN_URL= "https://www.genxcoders.in/demo/crdma/rest/loginAPI.php";


}
