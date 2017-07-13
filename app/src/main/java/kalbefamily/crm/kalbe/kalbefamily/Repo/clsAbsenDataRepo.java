package kalbefamily.crm.kalbe.kalbefamily.Repo;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import kalbefamily.crm.kalbe.kalbefamily.Common.clsAbsenData;
import kalbefamily.crm.kalbe.kalbefamily.Data.DatabaseHelper;
import kalbefamily.crm.kalbe.kalbefamily.Data.DatabaseManager;

/**
 * Created by Rian Andrivani on 7/11/2017.
 */

public class clsAbsenDataRepo implements crud {
    DatabaseHelper helper;

    public clsAbsenDataRepo(Context context) {
        DatabaseManager.init(context);
        helper = DatabaseManager.getInstance().getHelper();
    }

    @Override
    public int create(Object item) throws SQLException {
        int index = -1;
        clsAbsenData object = (clsAbsenData) item;
        try {
            index = helper.getUserAbsenDao().create(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public int createOrUpdate(Object item) throws SQLException {
        int index = -1;
        clsAbsenData object = (clsAbsenData) item;
        try {
            Dao.CreateOrUpdateStatus status = helper.getUserAbsenDao().createOrUpdate(object);
            index = status.getNumLinesChanged();
//            index = 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public int update(Object item) throws SQLException {
        int index = -1;
        clsAbsenData object = (clsAbsenData) item;
        try {
            index = helper.getUserAbsenDao().update(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public int delete(Object item) throws SQLException {
        int index = -1;
        clsAbsenData object = (clsAbsenData) item;
        try {
            index = helper.getUserAbsenDao().delete(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public Object findById(int id) throws SQLException {
        clsAbsenData item = null;
        try {
            item = helper.getUserAbsenDao().queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public List<?> findAll() throws SQLException {
        List<clsAbsenData> items = null;
        try {
            items = helper.getUserAbsenDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public clsAbsenData getDataCheckinActive(Context context) {
        clsAbsenDataRepo repo = new clsAbsenDataRepo(context);
        clsAbsenData dataAbsen = null;
        List<clsAbsenData> listData = null;

//            QueryBuilder<clsAbsenData, Integer> queryBuilder = helper.getUserAbsenDao().queryBuilder();
//            queryBuilder.where().eq(dataAbsen.Property_dtCheckout, null).or().eq(dataAbsen.Property_dtCheckout, "");
//            listData = queryBuilder.query();
        try {
            listData = helper.getUserAbsenDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (listData != null && listData.size() > 0) {
            for (clsAbsenData data : listData) {
                if (data.getIntSubmit().equals("1")&& (data.getDtCheckout() == null || data.getDtCheckout() == "")) {
                    dataAbsen = data;
                }
            }
        }


//        }
        return dataAbsen;
    }

    public int getContactsCount(Context context) throws SQLException {
        List<clsAbsenData> items = null;
        int num = 0;
        try {
            items = helper.getUserAbsenDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        num = items.size();
        // return count
        return num;
    }
}
