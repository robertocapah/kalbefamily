package kalbefamily.crm.kalbe.kalbefamily.Repo;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kalbefamily.crm.kalbe.kalbefamily.Common.clsQRCodeData;
import kalbefamily.crm.kalbe.kalbefamily.Data.DatabaseHelper;
import kalbefamily.crm.kalbe.kalbefamily.Data.DatabaseManager;

/**
 * Created by Rian Andrivani on 7/28/2017.
 */

public class clsQRCodeRepo implements crud {
    private DatabaseHelper helper;
    public clsQRCodeRepo(Context context){
        DatabaseManager.init(context);
        helper = DatabaseManager.getInstance().getHelper();
    }
    @Override
    public int create(Object item) {
        int index = -1;
        clsQRCodeData object = (clsQRCodeData) item;
        try {
            index = helper.getUserQRCodeDao().create(object);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public int createOrUpdate(Object item) {
        int index = -1;
        clsQRCodeData object = (clsQRCodeData) item;
        try {
            Dao.CreateOrUpdateStatus status = helper.getUserQRCodeDao().createOrUpdate(object);
            index = status.getNumLinesChanged();
//            index = 1;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public int update(Object item) {
        int index = -1;
        clsQRCodeData object = (clsQRCodeData) item;
        try {
            index = helper.getUserQRCodeDao().update(object);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public int delete(Object item) {
        int index = -1;
        clsQRCodeData object = (clsQRCodeData) item;
        try {
            index = helper.getUserQRCodeDao().delete(object);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public Object findById(int id) throws SQLException {
        clsQRCodeData item = null;
        try{
            item = helper.getUserQRCodeDao().queryForId(id);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public List<?> findAll() throws SQLException {
        List<clsQRCodeData> items = null;
        try{
            items = helper.getUserQRCodeDao().queryForAll();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return items;
    }

    public List<clsQRCodeData> getAllDataToSendData(Context context){
        QueryBuilder<clsQRCodeData, Integer> queryBuilder = null;
        List<clsQRCodeData> data = null;
        try {
            data = (List<clsQRCodeData>) helper.getUserQRCodeDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        clsQRCodeData dt = new clsQRCodeData();
        List<clsQRCodeData> listData = new ArrayList<>();

        if (data.size() > 0) {
            try {
                queryBuilder = helper.getUserQRCodeDao().queryBuilder();
                listData = queryBuilder.query();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return listData;
    }
}
