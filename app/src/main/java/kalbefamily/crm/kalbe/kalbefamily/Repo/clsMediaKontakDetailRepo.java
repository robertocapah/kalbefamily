package kalbefamily.crm.kalbe.kalbefamily.Repo;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.dao.RawRowMapper;

import java.sql.SQLException;
import java.util.List;

import kalbefamily.crm.kalbe.kalbefamily.Common.clsMediaKontakDetail;
import kalbefamily.crm.kalbe.kalbefamily.Data.DatabaseHelper;
import kalbefamily.crm.kalbe.kalbefamily.Data.DatabaseManager;

/**
 * Created by Rian Andrivani on 8/29/2017.
 */

public class clsMediaKontakDetailRepo implements crud {
    private DatabaseHelper helper;
    public clsMediaKontakDetailRepo(Context context){
        DatabaseManager.init(context);
        helper = DatabaseManager.getInstance().getHelper();
    }
    @Override
    public int create(Object item) {
        int index = -1;
        clsMediaKontakDetail object = (clsMediaKontakDetail) item;
        try {
            index = helper.getUserMediaKontakDetailDao().create(object);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public int createOrUpdate(Object item) {
        int index = -1;
        clsMediaKontakDetail object = (clsMediaKontakDetail) item;
        try {
            Dao.CreateOrUpdateStatus status = helper.getUserMediaKontakDetailDao().createOrUpdate(object);
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
        clsMediaKontakDetail object = (clsMediaKontakDetail) item;
        try {
            index = helper.getUserMediaKontakDetailDao().update(object);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public int delete(Object item) {
        int index = -1;
        clsMediaKontakDetail object = (clsMediaKontakDetail) item;
        try {
            index = helper.getUserMediaKontakDetailDao().delete(object);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public Object findById(int id) throws SQLException {
        clsMediaKontakDetail item = null;
        try{
            item = helper.getUserMediaKontakDetailDao().queryForId(id);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public List<?> findAll() throws SQLException {
        List<clsMediaKontakDetail> items = null;
        try{
            items = helper.getUserMediaKontakDetailDao().queryForAll();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return items;
    }

    public List<?> findbyTelpon() throws SQLException {
        List<clsMediaKontakDetail> items = null;
        try{
            GenericRawResults<clsMediaKontakDetail> rawResults =
                    helper.getUserMediaKontakDetailDao().queryRaw(
                            "select * from clsMediaKontakDetail where txtDeskripsi = 'Telepon'",
                            new RawRowMapper<clsMediaKontakDetail>() {
                                public clsMediaKontakDetail mapRow(String[] columnNames,
                                                               String[] resultColumns) {
                                    clsMediaKontakDetail dt = new clsMediaKontakDetail();
                                    dt.txtDetailMedia = resultColumns[3];

                                    return dt;
                                }
                            });

            items = rawResults.getResults();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return items;
    }

    public List<?> findbySms() throws SQLException {
        List<clsMediaKontakDetail> items = null;
        try{
            GenericRawResults<clsMediaKontakDetail> rawResults =
                    helper.getUserMediaKontakDetailDao().queryRaw(
                            "select * from clsMediaKontakDetail where txtDeskripsi = 'SMS'",
                            new RawRowMapper<clsMediaKontakDetail>() {
                                public clsMediaKontakDetail mapRow(String[] columnNames,
                                                                   String[] resultColumns) {
                                    clsMediaKontakDetail dt = new clsMediaKontakDetail();
                                    dt.txtDetailMedia = resultColumns[3];

                                    return dt;
                                }
                            });

            items = rawResults.getResults();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return items;
    }
}
