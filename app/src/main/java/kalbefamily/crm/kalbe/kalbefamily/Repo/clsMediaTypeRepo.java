package kalbefamily.crm.kalbe.kalbefamily.Repo;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import kalbefamily.crm.kalbe.kalbefamily.Common.clsMediaType;
import kalbefamily.crm.kalbe.kalbefamily.Data.DatabaseHelper;
import kalbefamily.crm.kalbe.kalbefamily.Data.DatabaseManager;

/**
 * Created by Rian Andrivani on 9/20/2017.
 */

public class clsMediaTypeRepo implements crud {
    private DatabaseHelper helper;
    public clsMediaTypeRepo(Context context){
        DatabaseManager.init(context);
        helper = DatabaseManager.getInstance().getHelper();
    }
    @Override
    public int create(Object item) {
        int index = -1;
        clsMediaType object = (clsMediaType) item;
        try {
            index = helper.getMediaTypeDao().create(object);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public int createOrUpdate(Object item) {
        int index = -1;
        clsMediaType object = (clsMediaType) item;
        try {
            Dao.CreateOrUpdateStatus status = helper.getMediaTypeDao().createOrUpdate(object);
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
        clsMediaType object = (clsMediaType) item;
        try {
            index = helper.getMediaTypeDao().update(object);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public int delete(Object item) {
        int index = -1;
        clsMediaType object = (clsMediaType) item;
        try {
            index = helper.getMediaTypeDao().delete(object);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public Object findById(int id) throws SQLException {
        clsMediaType item = null;
        try{
            item = helper.getMediaTypeDao().queryForId(id);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public List<?> findAll() throws SQLException {
        List<clsMediaType> items = null;
        try{
            items = helper.getMediaTypeDao().queryForAll();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return items;
    }
}
