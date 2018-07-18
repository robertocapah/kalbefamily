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

import kalbefamily.crm.kalbe.kalbefamily.Common.clsAbsenData;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsAvailablePoin;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsDeviceInfoData;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsDisplayPicture;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsImageStruk;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsJenisMedia;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsMediaKontakDetail;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsMediaType;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsQRCodeData;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsToken;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserImageProfile;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserLoginData;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserMember;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserMemberImage;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsmVersionApp;
import kalbefamily.crm.kalbe.kalbefamily.Common.mConfigData;

//import kalbefamily.crm.kalbe.kalbefamily.Common.Mobile_mConfigData;

/**
 * Created by Rian Andrivani on 6/22/2017.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static clsHardCode _path = new clsHardCode();
    // name of the database file for your application -- change to something appropriate for your app
    private static final String DATABASE_NAME = _path.dbName;
    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 2;

    // the DAO object we use to access the SimpleData table
    protected Dao<mConfigData, Integer> mConfigDao;
    protected Dao<clsAbsenData, Integer> userAbsenDao;

    protected Dao<clsToken, Integer> tokenDao;
    protected RuntimeExceptionDao<clsToken, Integer> tokenRuntimeDao = null;

    protected Dao<clsUserLoginData, Integer> userLoginDao;
    protected RuntimeExceptionDao<clsUserLoginData, Integer> userLoginRuntimeDao = null;

    protected Dao<clsDeviceInfoData, Integer> deviceInfoDao;
    protected RuntimeExceptionDao<clsDeviceInfoData, Integer> deviceInfoRuntimeDao = null;

    protected Dao<clsmVersionApp, Integer> mVersionAppsDao;
    protected RuntimeExceptionDao<clsmVersionApp, Integer> mVersionAppsRuntimeDao = null;

    protected Dao<clsUserMember, Integer> userMemberDao;
    protected RuntimeExceptionDao<clsUserMember, Integer> userMemberRuntimeDao = null;

    protected Dao<clsUserMemberImage, Integer> userMemberImageDao;
    protected RuntimeExceptionDao<clsUserMemberImage, Integer> userMemberImageRuntimeDao = null;

    protected Dao<clsUserImageProfile, Integer> userImageProfileDao;
    protected RuntimeExceptionDao<clsUserImageProfile, Integer> userImageProfileIntegerRuntimeDao;

    protected Dao<clsImageStruk, Integer> imageStrukDao;
    protected RuntimeExceptionDao<clsImageStruk, Integer> imageStrukRuntimeDao = null;

    protected Dao<clsMediaKontakDetail, Integer> userMediaKontakDetailDao;
    protected RuntimeExceptionDao<clsMediaKontakDetail, Integer> userMediaKontakDetailRuntimeDao;

    protected Dao<clsQRCodeData, Integer> userQRCodeDao;
    protected RuntimeExceptionDao<clsQRCodeData, Integer> userQRCodeRuntimeDao = null;

    protected Dao<clsAvailablePoin, Integer> availablePoinDao;
    protected RuntimeExceptionDao<clsAvailablePoin, Integer> availablePoinRuntimeDao;

    protected Dao<clsMediaType, Integer> mediaTypeDao;
    protected RuntimeExceptionDao<clsMediaType, Integer> mediaTypeRuntimeDao;

    protected Dao<clsJenisMedia, Integer> jenisMediaDao;
    protected RuntimeExceptionDao<clsJenisMedia, Integer> jenisMediaRuntimeDao;

    protected Dao<clsDisplayPicture, Integer> displayPictureDao;
    protected RuntimeExceptionDao<clsDisplayPicture, Integer> displayPictureRuntimeDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, clsToken.class);
            TableUtils.createTableIfNotExists(connectionSource, clsUserLoginData.class);
            TableUtils.createTableIfNotExists(connectionSource, clsDeviceInfoData.class);
            TableUtils.createTableIfNotExists(connectionSource, clsmVersionApp.class);
            TableUtils.createTableIfNotExists(connectionSource, clsUserMember.class);
            TableUtils.createTableIfNotExists(connectionSource, clsUserMemberImage.class);
            TableUtils.createTableIfNotExists(connectionSource, clsMediaKontakDetail.class);
            TableUtils.createTableIfNotExists(connectionSource, clsUserImageProfile.class);
            TableUtils.createTableIfNotExists(connectionSource, clsImageStruk.class);
            TableUtils.createTableIfNotExists(connectionSource, clsQRCodeData.class);
            TableUtils.createTableIfNotExists(connectionSource, clsAvailablePoin.class);
            TableUtils.createTableIfNotExists(connectionSource, clsMediaType.class);
            TableUtils.createTableIfNotExists(connectionSource, clsJenisMedia.class);
            TableUtils.createTableIfNotExists(connectionSource, clsDisplayPicture.class);
            TableUtils.createTableIfNotExists(connectionSource, mConfigData.class);
            TableUtils.createTableIfNotExists(connectionSource, clsAbsenData.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        Dao<clsUserLoginData, Integer> dao = null;
        try {
            dao = getUserLoginDao();
            Log.i(DatabaseHelper.class.getName(), "onUpgrade");
//            TableUtils.dropTable(connectionSource, clsUserLoginData.class, true);
//            TableUtils.dropTable(connectionSource, clsDeviceInfoData.class, true);
//            TableUtils.dropTable(connectionSource, clsmVersionApp.class, true);
//            TableUtils.dropTable(connectionSource, clsUserMember.class, true);
//            TableUtils.dropTable(connectionSource, clsUserMemberImage.class, true);
//            TableUtils.dropTable(connectionSource, clsMediaKontakDetail.class, true);
//            TableUtils.dropTable(connectionSource, clsUserImageProfile.class, true);
//            TableUtils.dropTable(connectionSource, clsQRCodeData.class, true);
//            TableUtils.dropTable(connectionSource, clsAvailablePoin.class, true);
//            TableUtils.dropTable(connectionSource, clsMediaType.class, true);
//            TableUtils.dropTable(connectionSource, clsJenisMedia.class, true);
//            TableUtils.dropTable(connectionSource, clsDisplayPicture.class, true);
//            TableUtils.dropTable(connectionSource, mConfigData.class, true);
//            TableUtils.dropTable(connectionSource, clsAbsenData.class, true);
            TableUtils.dropTable(connectionSource, clsImageStruk.class, true);

            // after we drop the old databases, we create the new ones
            //onCreate(db, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
//            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
//            throw new RuntimeException(e);
        }
    }

    public void clearAllDataInDatabase(){
        Log.i(DatabaseHelper.class.getName(), "onUpgrade");
        try {
            TableUtils.clearTable(connectionSource, clsToken.class);
            TableUtils.clearTable(connectionSource, clsUserLoginData.class);
            TableUtils.clearTable(connectionSource, clsDeviceInfoData.class);
            TableUtils.clearTable(connectionSource, clsmVersionApp.class);
            TableUtils.clearTable(connectionSource, clsUserMember.class);
            TableUtils.clearTable(connectionSource, clsUserMemberImage.class);
            TableUtils.clearTable(connectionSource, clsMediaKontakDetail.class);
            TableUtils.clearTable(connectionSource, clsUserImageProfile.class);
            TableUtils.clearTable(connectionSource, clsImageStruk.class);
            TableUtils.clearTable(connectionSource, clsQRCodeData.class);
            TableUtils.clearTable(connectionSource, clsAvailablePoin.class);
            TableUtils.clearTable(connectionSource, clsMediaType.class);
            TableUtils.clearTable(connectionSource, clsJenisMedia.class);
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

    public void refreshData2(){
        Log.i(DatabaseHelper.class.getName(), "onUpgrade");
        try {
            TableUtils.clearTable(connectionSource, clsAvailablePoin.class);
            TableUtils.clearTable(connectionSource, clsMediaKontakDetail.class);
            TableUtils.clearTable(connectionSource, clsUserMemberImage.class);
            TableUtils.clearTable(connectionSource, clsImageStruk.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearDataAfterLogout(){
        try {
            TableUtils.clearTable(connectionSource, clsUserLoginData.class);
            TableUtils.clearTable(connectionSource, clsmVersionApp.class);
            TableUtils.clearTable(connectionSource, clsUserMember.class);
            TableUtils.clearTable(connectionSource, clsUserMemberImage.class);
            TableUtils.clearTable(connectionSource, clsMediaKontakDetail.class);
            TableUtils.clearTable(connectionSource, clsUserImageProfile.class);
            TableUtils.clearTable(connectionSource, clsImageStruk.class);
            TableUtils.clearTable(connectionSource, clsQRCodeData.class);
            TableUtils.clearTable(connectionSource, clsAvailablePoin.class);
            TableUtils.clearTable(connectionSource, clsMediaType.class);
            TableUtils.clearTable(connectionSource, clsJenisMedia.class);
            TableUtils.clearTable(connectionSource, clsDisplayPicture.class);
            TableUtils.clearTable(connectionSource, clsAbsenData.class);
            // after we drop the old databases, we create the new ones
//            onCreate(db, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void clear(){
        Log.i(DatabaseHelper.class.getName(), "onUpgrade");
        try {
            TableUtils.clearTable(connectionSource, clsUserImageProfile.class);
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

    public Dao<clsToken, Integer> getTokenDao() throws SQLException {
        if (tokenDao == null) {
            tokenDao = getDao(clsToken.class);
        }
        return tokenDao;
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

    public Dao<clsUserMemberImage, Integer> getUserMemberImageDao() throws SQLException {
        if (userMemberImageDao == null) {
            userMemberImageDao = getDao(clsUserMemberImage.class);
        }
        return userMemberImageDao;
    }

    public Dao<clsMediaKontakDetail, Integer> getUserMediaKontakDetailDao() throws SQLException {
        if (userMediaKontakDetailDao == null) {
            userMediaKontakDetailDao = getDao(clsMediaKontakDetail.class);
        }
        return userMediaKontakDetailDao;
    }

    public Dao<clsUserImageProfile, Integer> getUserImageProfileDao() throws SQLException {
        if (userImageProfileDao == null) {
            userImageProfileDao = getDao(clsUserImageProfile.class);
        }
        return userImageProfileDao;
    }

    public Dao<clsImageStruk, Integer> getImageStrukDao() throws SQLException {
        if (imageStrukDao == null) {
            imageStrukDao = getDao(clsImageStruk.class);
        }
        return imageStrukDao;
    }

    public Dao<clsQRCodeData, Integer> getUserQRCodeDao() throws SQLException {
        if (userQRCodeDao == null) {
            userQRCodeDao = getDao(clsQRCodeData.class);
        }
        return userQRCodeDao;
    }

    public Dao<clsAvailablePoin, Integer> getAvailablePoinDao() throws SQLException {
        if (availablePoinDao == null) {
            availablePoinDao = getDao(clsAvailablePoin.class);
        }
        return availablePoinDao;
    }

    public Dao<clsMediaType, Integer> getMediaTypeDao() throws SQLException {
        if (mediaTypeDao == null) {
            mediaTypeDao = getDao(clsMediaType.class);
        }
        return mediaTypeDao;
    }

    public Dao<clsJenisMedia, Integer> getJenisMediaDao() throws SQLException {
        if (jenisMediaDao == null) {
            jenisMediaDao = getDao(clsJenisMedia.class);
        }
        return jenisMediaDao;
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

    public RuntimeExceptionDao<clsUserMemberImage, Integer> getUserMemberImageRuntimeDao() {
        if (userMemberImageRuntimeDao == null) {
            userMemberImageRuntimeDao = getRuntimeExceptionDao(clsUserMemberImage.class);
        }
        return userMemberImageRuntimeDao;
    }

    public RuntimeExceptionDao<clsMediaKontakDetail, Integer> getUserMediaKontakDetailRuntimeDao() {
        if (userMediaKontakDetailRuntimeDao == null) {
            userMediaKontakDetailRuntimeDao = getRuntimeExceptionDao(clsMediaKontakDetail.class);
        }
        return userMediaKontakDetailRuntimeDao;
    }

    public RuntimeExceptionDao<clsUserImageProfile, Integer> getUserImageProfileIntegerRuntimeDao() {
        if (userImageProfileIntegerRuntimeDao == null) {
            userImageProfileIntegerRuntimeDao = getRuntimeExceptionDao(clsUserImageProfile.class);
        }
        return userImageProfileIntegerRuntimeDao;
    }

    public RuntimeExceptionDao<clsQRCodeData, Integer> getUserQRCodeRuntimeDao() {
        if (userQRCodeRuntimeDao == null) {
            userQRCodeRuntimeDao = getRuntimeExceptionDao(clsQRCodeData.class);
        }
        return userQRCodeRuntimeDao;
    }

    public RuntimeExceptionDao<clsAvailablePoin, Integer> getAvailablePoinRuntimeDao() {
        if (availablePoinRuntimeDao == null) {
            availablePoinRuntimeDao = getRuntimeExceptionDao(clsAvailablePoin.class);
        }
        return availablePoinRuntimeDao;
    }

    public RuntimeExceptionDao<clsMediaType, Integer> getMediaTypeRuntimeDao() {
        if (mediaTypeRuntimeDao == null) {
            mediaTypeRuntimeDao = getRuntimeExceptionDao(clsMediaType.class);
        }
        return mediaTypeRuntimeDao;
    }

    public RuntimeExceptionDao<clsJenisMedia, Integer> getJenisMediaRuntimeDao() {
        if (jenisMediaRuntimeDao == null) {
            jenisMediaRuntimeDao = getRuntimeExceptionDao(clsJenisMedia.class);
        }
        return jenisMediaRuntimeDao;
    }

    public RuntimeExceptionDao<clsDisplayPicture, Integer> getDisplayPictureRuntimeDao() {
        if (displayPictureRuntimeDao == null) {
            displayPictureRuntimeDao = getRuntimeExceptionDao(clsDisplayPicture.class);
        }
        return displayPictureRuntimeDao;
    }

    @Override
    public void close() {
        tokenDao = null;
        userLoginDao = null;
        deviceInfoDao = null;
        mVersionAppsDao = null;
        userMemberDao = null;
        userMemberImageDao = null;
        userMediaKontakDetailDao = null;
        userImageProfileDao = null;
        imageStrukDao = null;
        userQRCodeDao = null;
        availablePoinDao = null;
        mediaTypeDao = null;
        jenisMediaDao = null;
        displayPictureDao = null;
        mConfigDao = null;
    }
}
