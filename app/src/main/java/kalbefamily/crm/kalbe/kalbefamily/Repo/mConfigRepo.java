package kalbefamily.crm.kalbe.kalbefamily.Repo;

import android.content.Context;

import java.sql.SQLException;
import java.util.List;

import kalbefamily.crm.kalbe.kalbefamily.Common.mConfigData;
import kalbefamily.crm.kalbe.kalbefamily.Data.DatabaseHelper;
import kalbefamily.crm.kalbe.kalbefamily.Data.DatabaseManager;

/**
 * Created by Rian Andrivani on 7/3/2017.
 */

public class mConfigRepo {
    DatabaseHelper helper;
//    public String API = "http://10.171.10.28:8036/KF/";
//    public String APIToken = "http://10.171.10.28:8036/";
    public String API = "http://appgw.kalbenutritionals.com/api/kalbefamily/KF/";
    public String APIToken = "http://appgw.kalbenutritionals.com/api/kalbefamily/";
//    public String API = "http://Apikf.kalbenutritionals.web.id/KF/";
//    public String APIToken = "http://Apikf.kalbenutritionals.web.id/";

    public mConfigRepo(Context context) {
        DatabaseManager.init(context);
        helper = DatabaseManager.getInstance().getHelper();
    }

    public Object findById(int id) throws SQLException {
        mConfigData item = null;
        try {
            item = helper.getmConfigDao().queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return item;
    }

    public List<?> findAll() throws SQLException {
        List<mConfigData> items = null;
        try{
            items = helper.getmConfigDao().queryForAll();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return items;
    }

    public void InsertDefaultmConfig() throws SQLException {
        mConfigData data1 = new mConfigData();
        data1.setIntId(1);
        data1.setTxtName("android:versionCode");
        data1.setTxtValue("5");
        data1.setTxtDefaultValue("5");
        data1.setIntEditAdmin("1");
        helper.getmConfigDao().createOrUpdate(data1);
//        mConfigData data2 = new mConfigData();
//        data2.setIntId(2);
//        data2.setTxtName("API_LOGIN");
//        data2.setTxtValue("http://http://10.171.11.70/WebApi2/KF/GetDetailKontak");
//        data2.setTxtDefaultValue("http://10.171.11.70/WebApi2/KF/GetDetailKontak");
//        data2.setIntEditAdmin("1");
//        helper.getmConfigDao().createOrUpdate(data2);
        mConfigData data3 = new mConfigData();
        data3.setIntId(3);
        data3.setTxtName("API_VERSION");
        data3.setTxtValue(API);
        data3.setTxtDefaultValue(API);
        data3.setIntEditAdmin("1");
        helper.getmConfigDao().createOrUpdate(data3);
        mConfigData data4 = new mConfigData();
        data4.setIntId(4);
        data4.setTxtName("Domain Kalbe");
        data4.setTxtValue("ONEKALBE.LOCAL");
        data4.setTxtDefaultValue("ONEKALBE.LOCAL");
        data4.setIntEditAdmin("1");
        helper.getmConfigDao().createOrUpdate(data4);
        mConfigData data5 = new mConfigData();
        data5.setIntId(5);
        data5.setTxtName("Application Name");
        data5.setTxtValue("Kalbe Family");
        data5.setTxtDefaultValue("5p9N66fDmyUlPBzuZacOlhNFxyRZp8GspXhlV8CSvgI=");
        data5.setIntEditAdmin("1");
        helper.getmConfigDao().createOrUpdate(data5);
        mConfigData data6 = new mConfigData();
        data6.setIntId(6);
        data6.setTxtName("UserName");
        data6.setTxtValue("rian.andrivani");
        data6.setTxtDefaultValue("rian.andrivani");
        data6.setIntEditAdmin("1");
        helper.getmConfigDao().createOrUpdate(data6);
        mConfigData data7 = new mConfigData();
        data7.setIntId(7);
        data7.setTxtName("Text Footer");
        data7.setTxtValue("Copyright &copy; KN IT 2017");
        data7.setTxtDefaultValue("Copyright &copy; KN IT 2017");
        data7.setIntEditAdmin("1");
        helper.getmConfigDao().createOrUpdate(data7);
    }

    public List<mConfigData> findByName(String name) throws SQLException {
        List<mConfigData> items = null;
        try {
            items = helper.getmConfigDao().queryForEq("txtName", name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }
}
