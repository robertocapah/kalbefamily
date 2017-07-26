package kalbefamily.crm.kalbe.kalbefamily.Repo;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserMemberImage;
import kalbefamily.crm.kalbe.kalbefamily.Data.DatabaseHelper;
import kalbefamily.crm.kalbe.kalbefamily.Data.DatabaseManager;

/**
 * Created by Rian Andrivani on 7/25/2017.
 */

public class clsUserMemberImageRepo implements crud {
    private DatabaseHelper helper;
    public clsUserMemberImageRepo(Context context){
        DatabaseManager.init(context);
        helper = DatabaseManager.getInstance().getHelper();
    }
    @Override
    public int create(Object item) {
        int index = -1;
        clsUserMemberImage object = (clsUserMemberImage) item;
        try {
            index = helper.getUserMemberImageDao().create(object);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public int createOrUpdate(Object item) {
        int index = -1;
        clsUserMemberImage object = (clsUserMemberImage) item;
        try {
            Dao.CreateOrUpdateStatus status = helper.getUserMemberImageDao().createOrUpdate(object);
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
        clsUserMemberImage object = (clsUserMemberImage) item;
        try {
            index = helper.getUserMemberImageDao().update(object);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public int delete(Object item) {
        int index = -1;
        clsUserMemberImage object = (clsUserMemberImage) item;
        try {
            index = helper.getUserMemberImageDao().delete(object);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public Object findById(int id) throws SQLException {
        clsUserMemberImage item = null;
        try{
            item = helper.getUserMemberImageDao().queryForId(id);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public List<?> findAll() throws SQLException {
        List<clsUserMemberImage> items = null;
        try{
            items = helper.getUserMemberImageDao().queryForAll();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return items;
    }

    public List<clsUserMemberImage> getAllDataToSendData(Context context){
        QueryBuilder<clsUserMemberImage, Integer> queryBuilder = null;
        List<clsUserMemberImage> data = null;
        try {
            data = (List<clsUserMemberImage>) helper.getUserMemberImageDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        clsUserMemberImage dt = new clsUserMemberImage();
        List<clsUserMemberImage> listData = new ArrayList<>();

        if (data.size() > 0) {
            try {
                queryBuilder = helper.getUserMemberImageDao().queryBuilder();
                listData = queryBuilder.query();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return listData;
    }
}
