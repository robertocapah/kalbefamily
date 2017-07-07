package kalbefamily.crm.kalbe.kalbefamily.Repo;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import kalbefamily.crm.kalbe.kalbefamily.Common.clsmVersionApp;
import kalbefamily.crm.kalbe.kalbefamily.Data.DatabaseHelper;
import kalbefamily.crm.kalbe.kalbefamily.Data.DatabaseManager;

/**
 * Created by Rian Andrivani on 7/6/2017.
 */

public class clsmVersionAppRepo implements crud  {
    private DatabaseHelper helper;
    public clsmVersionAppRepo(Context context){
        DatabaseManager.init(context);
        helper = DatabaseManager.getInstance().getHelper();
    }

    @Override
    public int create(Object item) throws SQLException {
        int index = -1;
        clsmVersionApp object = (clsmVersionApp) item;
        try {
            index = helper.getmVersionAppsDao().create(object);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public int createOrUpdate(Object item) throws SQLException {
        int index = -1;
        clsmVersionApp object = (clsmVersionApp) item;
        try {
            Dao.CreateOrUpdateStatus status = helper.getmVersionAppsDao().createOrUpdate(object);
            index = status.getNumLinesChanged();
//            index = 1;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public int update(Object item) throws SQLException {
        int index = -1;
        clsmVersionApp object = (clsmVersionApp) item;
        try {
            index = helper.getmVersionAppsDao().update(object);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public int delete(Object item) throws SQLException {
        int index = -1;
        clsmVersionApp object = (clsmVersionApp) item;
        try {
            index = helper.getmVersionAppsDao().delete(object);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public Object findById(int id) throws SQLException {
        clsmVersionApp item = null;
        try{
            item = helper.getmVersionAppsDao().queryForId(id);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public List<?> findAll() throws SQLException {
        List<clsmVersionApp> items = null;
        try{
            items = helper.getmVersionAppsDao().queryForAll();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return items;
    }
}
