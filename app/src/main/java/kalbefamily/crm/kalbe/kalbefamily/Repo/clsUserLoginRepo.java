package kalbefamily.crm.kalbe.kalbefamily.Repo;

import android.content.Context;

import java.sql.SQLException;
import java.util.List;

import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserLoginData;
import kalbefamily.crm.kalbe.kalbefamily.Data.DatabaseHelper;
import kalbefamily.crm.kalbe.kalbefamily.Data.DatabaseManager;

/**
 * Created by Rian Andrivani on 7/4/2017.
 */

public class clsUserLoginRepo implements crud {
    private DatabaseHelper helper;
    public clsUserLoginRepo(Context context){
        DatabaseManager.init(context);
        helper = DatabaseManager.getInstance().getHelper();
    }
    @Override
    public int create(Object item) {
        int index = -1;
        clsUserLoginData object = (clsUserLoginData) item;
        try {
            index = helper.getUserLoginDao().create(object);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public int createOrUpdate(Object item) {
        return 0;
    }

    @Override
    public int update(Object item) {
        int index = -1;
        clsUserLoginData object = (clsUserLoginData) item;
        try {
            index = helper.getUserLoginDao().update(object);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public int delete(Object item) {
        int index = -1;
        clsUserLoginData object = (clsUserLoginData) item;
        try {
            index = helper.getUserLoginDao().delete(object);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public Object findById(int id) {
        clsUserLoginData item = null;
        try{
            item = helper.getUserLoginDao().queryForId(id);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public List<?> findAll() {
        List<clsUserLoginData> items = null;
        try{
            items = helper.getUserLoginDao().queryForAll();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return items;
    }
}
