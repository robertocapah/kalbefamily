package com.oktaviani.dewi.mylibrary.authenticator;

/**
 * Created by Dewi Oktaviani on 1/22/2018.
 */

public class AccountGeneral {
    /**
     * Account type id
     */
    public static final String ACCOUNT_TYPE = "com.kalbe";

    /**
     * Account name
     */
    public static final String ACCOUNT_NAME = "Kalbe";

    /**
     * Auth token types
     */
    public static final String AUTHTOKEN_TYPE_READ_ONLY = "Read only";
    public static final String AUTHTOKEN_TYPE_READ_ONLY_LABEL = "Read only access to an Kalbe account";

    public static final String AUTHTOKEN_TYPE_FULL_ACCESS = "Full access";
    public static final String AUTHTOKEN_TYPE_FULL_ACCESS_LABEL = "Full access to an Kalbe account";

    //untuk login dll
    public final static String ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public final static String ARG_AUTH_TYPE = "AUTH_TYPE";
    public final static String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
    public final static String ARG_IS_ADDING_NEW_ACCOUNT = "IS_ADDING_ACCOUNT";
    public final static String ARG_IS_REFRESH_INVALID_TOKEN = "IS_REFRESH_INVALID_TOKEN";

    public static final String ARG_ARRAY_ACCOUNT_NAME = "ARRAY_ACCOUNT_NAME";
    public static final String ARG_ARRAY_ACCOUNT_AVAILABLE = "ARRAY_ACCOUNT_AVAILABLE";
    public static final String ARG_ARRAY_DATA_TOKEN = "ARRAY_DATA_TOKEN";
    public static final String ARG_ARRAY_DATA_ACCESS_TOKEN = "ARRAY_DATA_ACCESS_TOKEN";

    public final static String PARAM_USER_PASS = "USER_PASS";
    public final static String PARAM_LINK_API = "LINK_API";
    public final static String PARAM_REFRESH_TOKEN = "REFRESH_TOKEN ";
    public final static String PARAM_ACCESS_TOKEN = "ACCESS_TOKEN ";
    public final static String PARAM_ISSUED_TOKEN = "ISSUED_TOKEN ";

}