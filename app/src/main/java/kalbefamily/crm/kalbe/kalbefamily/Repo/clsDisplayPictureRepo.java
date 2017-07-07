package kalbefamily.crm.kalbe.kalbefamily.Repo;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import kalbefamily.crm.kalbe.kalbefamily.Common.clsDisplayPicture;
import kalbefamily.crm.kalbe.kalbefamily.Data.DatabaseHelper;
import kalbefamily.crm.kalbe.kalbefamily.Data.DatabaseManager;

/**
 * Created by Rian Andrivani on 7/7/2017.
 */

public class clsDisplayPictureRepo implements crud {
    DatabaseHelper helper;
    public clsDisplayPictureRepo(Context context){
        DatabaseManager.init(context);
        helper = DatabaseManager.getInstance().getHelper();
    }
    @Override
    public int create(Object item) throws SQLException {
        int index = -1;
        clsDisplayPicture object = (clsDisplayPicture) item;
        try {
            index = helper.getDisplayPictureDao().create(object);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public int createOrUpdate(Object item) throws SQLException {
        int index = -1;
        clsDisplayPicture object = (clsDisplayPicture) item;
        try {
            Dao.CreateOrUpdateStatus status = helper.getDisplayPictureDao().createOrUpdate(object);
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
        clsDisplayPicture object = (clsDisplayPicture) item;
        try {
            index = helper.getDisplayPictureDao().create(object);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public int delete(Object item) throws SQLException {
        int index = -1;
        clsDisplayPicture object = (clsDisplayPicture) item;
        try {
            index = helper.getDisplayPictureDao().create(object);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public Object findById(int id) throws SQLException {
        clsDisplayPicture item = null;
        try{
            item = helper.getDisplayPictureDao().queryForId(id);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public List<?> findAll() throws SQLException {
        List<clsDisplayPicture> items = null;
        try{
            items = helper.getDisplayPictureDao().queryForAll();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return items;
    }
}
