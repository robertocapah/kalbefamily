package kalbefamily.crm.kalbe.kalbefamily.Repo;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.dao.RawRowMapper;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kalbefamily.crm.kalbe.kalbefamily.Common.clsImageStruk;
import kalbefamily.crm.kalbe.kalbefamily.Data.DatabaseHelper;
import kalbefamily.crm.kalbe.kalbefamily.Data.DatabaseManager;

/**
 * Created by User on 2/14/2018.
 */

public class clsImageStrukRepo implements crud {
    private DatabaseHelper helper;
    public clsImageStrukRepo(Context context){
        DatabaseManager.init(context);
        helper = DatabaseManager.getInstance().getHelper();
    }
    @Override
    public int create(Object item) {
        int index = -1;
        clsImageStruk object = (clsImageStruk) item;
        try {
            index = helper.getImageStrukDao().create(object);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public int createOrUpdate(Object item) {
        int index = -1;
        clsImageStruk object = (clsImageStruk) item;
        try {
            Dao.CreateOrUpdateStatus status = helper.getImageStrukDao().createOrUpdate(object);
            index = status.getNumLinesChanged();
//            index = 1;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public int update(Object item) throws SQLException {
        return 0;
    }

    @Override
    public int delete(Object item) {
        int index = -1;
        clsImageStruk object = (clsImageStruk) item;
        try {
            index = helper.getImageStrukDao().delete(object);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public Object findById(int id) throws SQLException {
        clsImageStruk item = null;
        try{
            item = helper.getImageStrukDao().queryForId(id);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public List<?> findAll() throws SQLException {
        List<clsImageStruk> items = null;
        try{
            items = helper.getImageStrukDao().queryForAll();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return items;
    }

    public List<clsImageStruk> findByIdString(String id) throws SQLException {
        List<clsImageStruk> item = null;
        try {
            item = helper.getImageStrukDao().queryBuilder().where().eq("txtGuiId", id).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return item;
    }
    public List<clsImageStruk> findByAllSortByDate(String id) throws SQLException {
        List<clsImageStruk> item = null;

        try {
//            item = helper.getImageStrukDao().queryBuilder().where().eq("txtGuiId", id).query();
            item = helper.getImageStrukDao().queryBuilder().orderBy("dtDate",false).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return item;
    }

    public List<clsImageStruk> getAllDataToSendData(Context context) {
        QueryBuilder<clsImageStruk, Integer> queryBuilder = null;
        List<clsImageStruk> data = null;
        try {
            data = (List<clsImageStruk>) helper.getImageStrukDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        clsImageStruk dt = new clsImageStruk();
        List<clsImageStruk> listData = new ArrayList<>();

        if (data.size() > 0) {
            try {
                queryBuilder = helper.getImageStrukDao().queryBuilder();
                listData = queryBuilder.query();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return listData;
    }

    public List<?> findDataChild(String txtGuiId) throws SQLException {
        List<clsImageStruk> items = null;
        try{
            GenericRawResults<clsImageStruk> rawResults =
                    helper.getImageStrukDao().queryRaw(
                            "select * from clsImageStruk where txtGuiId='"+txtGuiId+"'",
                            new RawRowMapper<clsImageStruk>() {
                                public clsImageStruk mapRow(String[] columnNames,
                                                               String[] resultColumns) {
                                    clsImageStruk dt = new clsImageStruk();
                                    dt.txtGuiId = resultColumns[0];
                                    dt.txtPath = resultColumns[3];
                                    dt.txtValidate = resultColumns[4];
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
