package kalbefamily.crm.kalbe.kalbefamily.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

//import kalbefamily.crm.kalbe.kalbefamily.Common.Mobile_mConfigData;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsDeviceInfoData;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserLoginData;
import kalbefamily.crm.kalbe.kalbefamily.Common.mConfigData;

/**
 * Created by Rian Andrivani on 6/22/2017.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static clsHardCode _path = new clsHardCode();
    // name of the database file for your application -- change to something appropriate for your app
    private static final String DATABASE_NAME = _path.dbName;
    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 1;

    // the DAO object we use to access the SimpleData table
    protected Dao<mConfigData, Integer> mConfigDao;

    protected Dao<clsUserLoginData, Integer> userLoginDao;
    protected RuntimeExceptionDao<clsUserLoginData, Integer> userLoginRuntimeDao = null;

    protected Dao<clsDeviceInfoData, Integer> deviceInfoDao;
    protected RuntimeExceptionDao<clsDeviceInfoData, Integer> deviceInfoRuntimeDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, clsUserLoginData.class);
            TableUtils.createTableIfNotExists(connectionSource, clsDeviceInfoData.class);
            TableUtils.createTableIfNotExists(connectionSource, mConfigData.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, clsUserLoginData.class, true);
            TableUtils.dropTable(connectionSource, clsDeviceInfoData.class, true);
            TableUtils.dropTable(connectionSource, mConfigData.class, true);

            // after we drop the old databases, we create the new ones
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    public void clearAllDataInDatabase(){
        Log.i(DatabaseHelper.class.getName(), "onUpgrade");
        try {
            TableUtils.clearTable(connectionSource, clsUserLoginData.class);
            TableUtils.clearTable(connectionSource, clsDeviceInfoData.class);
            TableUtils.clearTable(connectionSource, mConfigData.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Dao<mConfigData, Integer> getmConfigDao() throws SQLException {
        if (mConfigDao == null) {
            mConfigDao = getDao(mConfigData.class);
        }
        return mConfigDao;
    }

    public Dao<clsUserLoginData, Integer> getUserLoginDao() throws SQLException {
        if (userLoginDao == null) {
            userLoginDao = getDao(clsUserLoginData.class);
        }
        return userLoginDao;
    }

    public Dao<clsDeviceInfoData, Integer> getDeviceInfoDao() throws SQLException {
        if (deviceInfoDao == null) {
            deviceInfoDao = getDao(clsDeviceInfoData.class);
        }
        return deviceInfoDao;
    }

    public RuntimeExceptionDao<clsUserLoginData, Integer> getUserLoginRuntimeDao() {
        if (userLoginRuntimeDao == null) {
            userLoginRuntimeDao = getRuntimeExceptionDao(clsUserLoginData.class);
        }
        return userLoginRuntimeDao;
    }

    public RuntimeExceptionDao<clsDeviceInfoData, Integer> getDeviceInfoRuntimeDao() {
        if (deviceInfoRuntimeDao == null) {
            deviceInfoRuntimeDao = getRuntimeExceptionDao(clsDeviceInfoData.class);
        }
        return deviceInfoRuntimeDao;
    }

    @Override
    public void close() {
        userLoginDao = null;
        deviceInfoDao = null;
        mConfigDao = null;
    }
}
