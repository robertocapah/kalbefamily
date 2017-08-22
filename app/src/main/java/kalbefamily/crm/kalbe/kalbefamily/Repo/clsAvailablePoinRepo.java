package kalbefamily.crm.kalbe.kalbefamily.Repo;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.dao.RawRowMapper;

import java.sql.SQLException;
import java.util.List;

import kalbefamily.crm.kalbe.kalbefamily.Common.clsAvailablePoin;
import kalbefamily.crm.kalbe.kalbefamily.Data.DatabaseHelper;
import kalbefamily.crm.kalbe.kalbefamily.Data.DatabaseManager;

/**
 * Created by Rian Andrivani on 8/16/2017.
 */

public class clsAvailablePoinRepo implements crud {
    private DatabaseHelper helper;
    public clsAvailablePoinRepo(Context context){
        DatabaseManager.init(context);
        helper = DatabaseManager.getInstance().getHelper();
    }
    @Override
    public int create(Object item) {
        int index = -1;
        clsAvailablePoin object = (clsAvailablePoin) item;
        try {
            index = helper.getAvailablePoinDao().create(object);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public int createOrUpdate(Object item) {
        int index = -1;
        clsAvailablePoin object = (clsAvailablePoin) item;
        try {
            Dao.CreateOrUpdateStatus status = helper.getAvailablePoinDao().createOrUpdate(object);
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
        clsAvailablePoin object = (clsAvailablePoin) item;
        try {
            index = helper.getAvailablePoinDao().update(object);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public int delete(Object item) {
        int index = -1;
        clsAvailablePoin object = (clsAvailablePoin) item;
        try {
            index = helper.getAvailablePoinDao().delete(object);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public Object findById(int id) throws SQLException {
        clsAvailablePoin item = null;
        try{
            item = helper.getAvailablePoinDao().queryForId(id);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public List<?> findAll() throws SQLException {
        List<clsAvailablePoin> items = null;
        try{
            items = helper.getAvailablePoinDao().queryForAll();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return items;
    }

    public void findByIdString() throws SQLException {

        try{
            long item = helper.getAvailablePoinDao().queryBuilder().groupByRaw("txtPeriodePoint").countOf();

            GenericRawResults<clsAvailablePoin> rawResults =
                    helper.getAvailablePoinDao().queryRaw(
                            "select * from clsAvailablePoin group by txtPeriodePoint",
                            new RawRowMapper<clsAvailablePoin>() {
                                public clsAvailablePoin mapRow(String[] columnNames,
                                                               String[] resultColumns) {
                                    clsAvailablePoin dt = new clsAvailablePoin();
                                    dt.txtDescription = resultColumns[0];
                                    return dt;
                                }
                            });

            List<clsAvailablePoin> askdjhad = rawResults.getResults();

            String a = "";
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public List<?> findPeriode() throws SQLException {
        List<clsAvailablePoin> items = null;
        try{
            GenericRawResults<clsAvailablePoin> rawResults =
                    helper.getAvailablePoinDao().queryRaw(
                            "select * from clsAvailablePoin group by txtPeriodePoint",
                            new RawRowMapper<clsAvailablePoin>() {
                                public clsAvailablePoin mapRow(String[] columnNames,
                                                               String[] resultColumns) {
                                    clsAvailablePoin dt = new clsAvailablePoin();
                                    dt.txtPeriodePoint = resultColumns[2];
                                    dt.txtPoint = resultColumns[3];
                                    dt.txtDescription = resultColumns[0];
                                    return dt;
                                }
                            });

            items = rawResults.getResults();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return items;
    }

    public List<?> findDataChild(String txtPeriodePoint) throws SQLException {
        List<clsAvailablePoin> items = null;
        try{
            GenericRawResults<clsAvailablePoin> rawResults =
                    helper.getAvailablePoinDao().queryRaw(
                            "select * from clsAvailablePoin where txtPeriodePoint='"+txtPeriodePoint+"'",
                            new RawRowMapper<clsAvailablePoin>() {
                                public clsAvailablePoin mapRow(String[] columnNames,
                                                               String[] resultColumns) {
                                    clsAvailablePoin dt = new clsAvailablePoin();
                                    dt.txtPoint = resultColumns[3];
                                    dt.txtDescription = resultColumns[0];
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
