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
import kalbefamily.crm.kalbe.kalbefamily.Common.clsAbsenData;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsDeviceInfoData;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsDisplayPicture;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserLoginData;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserMember;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsmVersionApp;
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
    protected Dao<clsAbsenData, Integer> userAbsenDao;

    protected Dao<clsUserLoginData, Integer> userLoginDao;
    protected RuntimeExceptionDao<clsUserLoginData, Integer> userLoginRuntimeDao = null;

    protected Dao<clsDeviceInfoData, Integer> deviceInfoDao;
    protected RuntimeExceptionDao<clsDeviceInfoData, Integer> deviceInfoRuntimeDao = null;

    protected Dao<clsmVersionApp, Integer> mVersionAppsDao;
    protected RuntimeExceptionDao<clsmVersionApp, Integer> mVersionAppsRuntimeDao = null;

    protected Dao<clsUserMember, Integer> userMemberDao;
    protected RuntimeExceptionDao<clsUserMember, Integer> userMemberRuntimeDao = null;

    protected Dao<clsDisplayPicture, Integer> displayPictureDao;
    protected RuntimeExceptionDao<clsDisplayPicture, Integer> displayPictureRuntimeDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, clsUserLoginData.class);
            TableUtils.createTableIfNotExists(connectionSource, clsDeviceInfoData.class);
            TableUtils.createTableIfNotExists(connectionSource, clsmVersionApp.class);
            TableUtils.createTableIfNotExists(connectionSource, clsUserMember.class);
            TableUtils.createTableIfNotExists(connectionSource, clsDisplayPicture.class);
            TableUtils.createTableIfNotExists(connectionSource, mConfigData.class);
            TableUtils.createTableIfNotExists(connectionSource, clsAbsenData.class);
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
            TableUtils.dropTable(connectionSource, clsmVersionApp.class, true);
            TableUtils.dropTable(connectionSource, clsUserMember.class, true);
            TableUtils.dropTable(connectionSource, clsDisplayPicture.class, true);
            TableUtils.dropTable(connectionSource, mConfigData.class, true);
            TableUtils.dropTable(connectionSource, clsAbsenData.class, true);

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
            TableUtils.clearTable(connectionSource, clsmVersionApp.class);
            TableUtils.clearTable(connectionSource, clsUserMember.class);
            TableUtils.clearTable(connectionSource, clsDisplayPicture.class);
            TableUtils.clearTable(connectionSource, mConfigData.class);
            TableUtils.clearTable(connectionSource, clsAbsenData.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void refreshData(){
        Log.i(DatabaseHelper.class.getName(), "onUpgrade");
        try {
            TableUtils.clearTable(connectionSource, clsUserMember.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearDataAfterLogout(){
        try {
            TableUtils.clearTable(connectionSource, clsUserLoginData.class);
            TableUtils.clearTable(connectionSource, clsmVersionApp.class);
            TableUtils.clearTable(connectionSource, clsUserMember.class);
            TableUtils.clearTable(connectionSource, clsDisplayPicture.class);
            TableUtils.clearTable(connectionSource, clsAbsenData.class);
            // after we drop the old databases, we create the new ones
//            onCreate(db, connectionSource);
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
    public Dao<clsAbsenData,Integer> getUserAbsenDao() throws SQLException{
        if (userAbsenDao == null){
            userAbsenDao = getDao(clsAbsenData.class);
        }
        return userAbsenDao;
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

    public Dao<clsmVersionApp, Integer> getmVersionAppsDao() throws SQLException {
        if (mVersionAppsDao == null) {
            mVersionAppsDao = getDao(clsmVersionApp.class);
        }
        return mVersionAppsDao;
    }

    public Dao<clsUserMember, Integer> getUserMemberDao() throws SQLException {
        if (userMemberDao == null) {
            userMemberDao = getDao(clsUserMember.class);
        }
        return userMemberDao;
    }

    public Dao<clsDisplayPicture, Integer> getDisplayPictureDao() throws SQLException {
        if (displayPictureDao == null) {
            displayPictureDao = getDao(clsDisplayPicture.class);
        }
        return displayPictureDao;
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

    public RuntimeExceptionDao<clsmVersionApp, Integer> getmVersionAppsRuntimeDao() {
        if (mVersionAppsRuntimeDao == null) {
            mVersionAppsRuntimeDao = getRuntimeExceptionDao(clsmVersionApp.class);
        }
        return mVersionAppsRuntimeDao;
    }

    public RuntimeExceptionDao<clsUserMember, Integer> getUserMemberRuntimeDao() {
        if (userMemberRuntimeDao == null) {
            userMemberRuntimeDao = getRuntimeExceptionDao(clsUserMember.class);
        }
        return userMemberRuntimeDao;
    }

    public RuntimeExceptionDao<clsDisplayPicture, Integer> getDisplayPictureRuntimeDao() {
        if (displayPictureRuntimeDao == null) {
            displayPictureRuntimeDao = getRuntimeExceptionDao(clsDisplayPicture.class);
        }
        return displayPictureRuntimeDao;
    }

    @Override
    public void close() {
        userLoginDao = null;
        deviceInfoDao = null;
        mVersionAppsDao = null;
        userMemberDao = null;
        displayPictureDao = null;
        mConfigDao = null;
    }
}
