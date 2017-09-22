package kalbefamily.crm.kalbe.kalbefamily.Repo;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.dao.RawRowMapper;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kalbefamily.crm.kalbe.kalbefamily.Common.clsJenisMedia;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsMediaKontakDetail;
import kalbefamily.crm.kalbe.kalbefamily.Data.DatabaseHelper;
import kalbefamily.crm.kalbe.kalbefamily.Data.DatabaseManager;

/**
 * Created by Rian Andrivani on 8/29/2017.
 */

public class clsMediaKontakDetailRepo implements crud {
    private DatabaseHelper helper;

    public clsMediaKontakDetailRepo(Context context) {
        DatabaseManager.init(context);
        helper = DatabaseManager.getInstance().getHelper();
    }

    @Override
    public int create(Object item) {
        int index = -1;
        clsMediaKontakDetail object = (clsMediaKontakDetail) item;
        try {
            index = helper.getUserMediaKontakDetailDao().create(object);
        } catch (SQLException e) {
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
        } catch (SQLException e) {
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
            List<clsMediaKontakDetail> dt = helper.getUserMediaKontakDetailDao().queryForAll();
            String asd = "";
        } catch (SQLException e) {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public Object findById(int id) throws SQLException {
        clsMediaKontakDetail item = null;
        try {
            item = helper.getUserMediaKontakDetailDao().queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return item;
    }

    public List<clsMediaKontakDetail> findByIdString(String id) throws SQLException {
        List<clsMediaKontakDetail> item = null;
        try {
            item = helper.getUserMediaKontakDetailDao().queryBuilder().where().eq("txtGuiId", id).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public List<?> findAll() throws SQLException {
        List<clsMediaKontakDetail> items = null;
        try {
            items = helper.getUserMediaKontakDetailDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public List<?> findWhere(String txtDeskripsi) throws SQLException {
        List<clsMediaKontakDetail> items = null;
        try {
            items = helper.getUserMediaKontakDetailDao().queryBuilder().where().eq("txtDeskripsi", txtDeskripsi).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public List<?> findDataByParent() throws SQLException {
        List<clsMediaKontakDetail> items = null;
        try {
            GenericRawResults<clsMediaKontakDetail> rawResults =
                    helper.getAvailablePoinDao().queryRaw(
                            "select * from clsMediaKontakDetail group by txtDeskripsi",
                            new RawRowMapper<clsMediaKontakDetail>() {
                                public clsMediaKontakDetail mapRow(String[] columnNames,
                                                                   String[] resultColumns) {
                                    clsMediaKontakDetail dt = new clsMediaKontakDetail();
                                    dt.txtGuiId = resultColumns[5];
                                    dt.txtDetailMedia = resultColumns[3];
                                    dt.txtPrioritasKontak = resultColumns[9];
                                    dt.txtKeterangan = resultColumns[7];
                                    dt.lttxtStatusAktif = resultColumns[1];
                                    dt.txtDeskripsi = resultColumns[2];
                                    dt.lttxtMediaID = resultColumns[0];
                                    dt.txtExtension = resultColumns[4];
                                    dt.txtKategoriMedia = resultColumns[6];
                                    return dt;
                                }
                            });

            items = rawResults.getResults();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public List<?> findDataChild(String txtDeskripsi) throws SQLException {
        List<clsMediaKontakDetail> items = null;
        try {
            GenericRawResults<clsMediaKontakDetail> rawResults =
                    helper.getAvailablePoinDao().queryRaw(
                            "select * from clsMediaKontakDetail where txtDeskripsi='" + txtDeskripsi + "'",
                            new RawRowMapper<clsMediaKontakDetail>() {
                                public clsMediaKontakDetail mapRow(String[] columnNames,
                                                                   String[] resultColumns) {
                                    clsMediaKontakDetail dt = new clsMediaKontakDetail();
                                    dt.txtGuiId = resultColumns[5];
                                    dt.txtDetailMedia = resultColumns[3];
                                    dt.txtPrioritasKontak = resultColumns[9];
                                    dt.txtKeterangan = resultColumns[7];
                                    dt.lttxtStatusAktif = resultColumns[1];
                                    dt.txtDeskripsi = resultColumns[2];
                                    dt.lttxtMediaID = resultColumns[0];
                                    dt.txtExtension = resultColumns[4];
                                    dt.txtKategoriMedia = resultColumns[6];
                                    dt.txtNamaMasterData = resultColumns[3];
                                    return dt;
                                }
                            });

            items = rawResults.getResults();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public List<?> findDataChildJoin(String txtDeskripsi) throws SQLException {
        List<clsMediaKontakDetail> items = null;
        try {
            GenericRawResults<clsMediaKontakDetail> rawResults =
                    helper.getAvailablePoinDao().queryRaw(
                            "select a.lttxtMediaID, a.lttxtStatusAktif, a.txtDeskripsi, a.txtDetailMedia, a.txtExtension, a.txtGuiId," +
                                    "a.txtKategoriMedia, a.txtKeterangan, a.txtKontakId, a.txtPrioritasKontak, b.txtNamaMasterData" +
                                    " from clsMediaKontakDetail a left join clsJenisMedia b on a.txtKategoriMedia = b.txtGuiId where a.txtDeskripsi='" + txtDeskripsi + "'",
                            new RawRowMapper<clsMediaKontakDetail>() {
                                public clsMediaKontakDetail mapRow(String[] columnNames,
                                                                   String[] resultColumns) {
                                    clsMediaKontakDetail dt = new clsMediaKontakDetail();
                                    dt.txtGuiId = resultColumns[5];
                                    dt.txtDetailMedia = resultColumns[3];
                                    dt.txtPrioritasKontak = resultColumns[9];
                                    dt.txtKeterangan = resultColumns[7];
                                    dt.lttxtStatusAktif = resultColumns[1];
                                    dt.txtDeskripsi = resultColumns[2];
                                    dt.lttxtMediaID = resultColumns[0];
                                    dt.txtExtension = resultColumns[4];
                                    dt.txtKategoriMedia = resultColumns[6];
                                    dt.txtNamaMasterData = resultColumns[10];
                                    return dt;
                                }
                            });

            items = rawResults.getResults();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public List<clsMediaKontakDetail> getAllDataToSendData(Context context){
        QueryBuilder<clsMediaKontakDetail, Integer> queryBuilder = null;
        List<clsMediaKontakDetail> data = null;
        try {
            data = (List<clsMediaKontakDetail>) helper.getUserMediaKontakDetailDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        clsMediaKontakDetail dt = new clsMediaKontakDetail();
        List<clsMediaKontakDetail> listData = new ArrayList<>();

        if (data.size() > 0) {
            try {
                queryBuilder = helper.getUserMediaKontakDetailDao().queryBuilder();
                listData = queryBuilder.query();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return listData;
    }

    public List<?> findbyTelpon() throws SQLException {
        List<clsMediaKontakDetail> items = null;
        try {
            GenericRawResults<clsMediaKontakDetail> rawResults =
                    helper.getUserMediaKontakDetailDao().queryRaw(
                            "select * from clsMediaKontakDetail where txtDeskripsi = 'Telepon'",
                            new RawRowMapper<clsMediaKontakDetail>() {
                                public clsMediaKontakDetail mapRow(String[] columnNames,
                                                                   String[] resultColumns) {
                                    clsMediaKontakDetail dt = new clsMediaKontakDetail();
                                    dt.txtGuiId = resultColumns[4];
                                    dt.txtDetailMedia = resultColumns[3];
                                    dt.txtPrioritasKontak = resultColumns[8];
                                    dt.txtKeterangan = resultColumns[6];
                                    dt.lttxtStatusAktif = resultColumns[1];

                                    return dt;
                                }
                            });

            items = rawResults.getResults();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public List<?> findbySms() throws SQLException {
        List<clsMediaKontakDetail> items = null;
        try {
            GenericRawResults<clsMediaKontakDetail> rawResults =
                    helper.getUserMediaKontakDetailDao().queryRaw(
                            "select * from clsMediaKontakDetail where txtDeskripsi = 'SMS'",
                            new RawRowMapper<clsMediaKontakDetail>() {
                                public clsMediaKontakDetail mapRow(String[] columnNames,
                                                                   String[] resultColumns) {
                                    clsMediaKontakDetail dt = new clsMediaKontakDetail();
                                    dt.txtGuiId = resultColumns[4];
                                    dt.txtDetailMedia = resultColumns[3];
                                    dt.txtPrioritasKontak = resultColumns[8];
                                    dt.txtKeterangan = resultColumns[6];
                                    dt.lttxtStatusAktif = resultColumns[1];

                                    return dt;
                                }
                            });

            items = rawResults.getResults();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public List<?> findbyBBM() throws SQLException {
        List<clsMediaKontakDetail> items = null;
        try {
            GenericRawResults<clsMediaKontakDetail> rawResults =
                    helper.getUserMediaKontakDetailDao().queryRaw(
                            "select * from clsMediaKontakDetail where txtDeskripsi = 'BLACKBERRY'",
                            new RawRowMapper<clsMediaKontakDetail>() {
                                public clsMediaKontakDetail mapRow(String[] columnNames,
                                                                   String[] resultColumns) {
                                    clsMediaKontakDetail dt = new clsMediaKontakDetail();
                                    dt.txtGuiId = resultColumns[4];
                                    dt.txtDetailMedia = resultColumns[3];
                                    dt.txtPrioritasKontak = resultColumns[8];
                                    dt.txtKeterangan = resultColumns[6];
                                    dt.lttxtStatusAktif = resultColumns[1];

                                    return dt;
                                }
                            });

            items = rawResults.getResults();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public List<?> findbyLine() throws SQLException {
        List<clsMediaKontakDetail> items = null;
        try {
            GenericRawResults<clsMediaKontakDetail> rawResults =
                    helper.getUserMediaKontakDetailDao().queryRaw(
                            "select * from clsMediaKontakDetail where txtDeskripsi = 'LINE'",
                            new RawRowMapper<clsMediaKontakDetail>() {
                                public clsMediaKontakDetail mapRow(String[] columnNames,
                                                                   String[] resultColumns) {
                                    clsMediaKontakDetail dt = new clsMediaKontakDetail();
                                    dt.txtGuiId = resultColumns[4];
                                    dt.txtDetailMedia = resultColumns[3];
                                    dt.txtPrioritasKontak = resultColumns[8];
                                    dt.txtKeterangan = resultColumns[6];
                                    dt.lttxtStatusAktif = resultColumns[1];

                                    return dt;
                                }
                            });

            items = rawResults.getResults();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public List<?> findbyWA() throws SQLException {
        List<clsMediaKontakDetail> items = null;
        try {
            GenericRawResults<clsMediaKontakDetail> rawResults =
                    helper.getUserMediaKontakDetailDao().queryRaw(
                            "select * from clsMediaKontakDetail where txtDeskripsi = 'WHATSAPP'",
                            new RawRowMapper<clsMediaKontakDetail>() {
                                public clsMediaKontakDetail mapRow(String[] columnNames,
                                                                   String[] resultColumns) {
                                    clsMediaKontakDetail dt = new clsMediaKontakDetail();
                                    dt.txtGuiId = resultColumns[4];
                                    dt.txtDetailMedia = resultColumns[3];
                                    dt.txtPrioritasKontak = resultColumns[8];
                                    dt.txtKeterangan = resultColumns[6];
                                    dt.lttxtStatusAktif = resultColumns[1];

                                    return dt;
                                }
                            });

            items = rawResults.getResults();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public List<?> findbyEmail() throws SQLException {
        List<clsMediaKontakDetail> items = null;
        try {
            GenericRawResults<clsMediaKontakDetail> rawResults =
                    helper.getUserMediaKontakDetailDao().queryRaw(
                            "select * from clsMediaKontakDetail where txtDeskripsi = 'Email'",
                            new RawRowMapper<clsMediaKontakDetail>() {
                                public clsMediaKontakDetail mapRow(String[] columnNames,
                                                                   String[] resultColumns) {
                                    clsMediaKontakDetail dt = new clsMediaKontakDetail();
                                    dt.txtGuiId = resultColumns[4];
                                    dt.txtDetailMedia = resultColumns[3];
                                    dt.txtPrioritasKontak = resultColumns[8];
                                    dt.txtKeterangan = resultColumns[6];
                                    dt.lttxtStatusAktif = resultColumns[1];

                                    return dt;
                                }
                            });

            items = rawResults.getResults();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public List<?> findbyTwitter() throws SQLException {
        List<clsMediaKontakDetail> items = null;
        try {
            GenericRawResults<clsMediaKontakDetail> rawResults =
                    helper.getUserMediaKontakDetailDao().queryRaw(
                            "select * from clsMediaKontakDetail where txtDeskripsi = 'TWITTER'",
                            new RawRowMapper<clsMediaKontakDetail>() {
                                public clsMediaKontakDetail mapRow(String[] columnNames,
                                                                   String[] resultColumns) {
                                    clsMediaKontakDetail dt = new clsMediaKontakDetail();
                                    dt.txtGuiId = resultColumns[4];
                                    dt.txtDetailMedia = resultColumns[3];
                                    dt.txtPrioritasKontak = resultColumns[8];
                                    dt.txtKeterangan = resultColumns[6];
                                    dt.lttxtStatusAktif = resultColumns[1];

                                    return dt;
                                }
                            });

            items = rawResults.getResults();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public List<?> findbyFacebook() throws SQLException {
        List<clsMediaKontakDetail> items = null;
        try {
            GenericRawResults<clsMediaKontakDetail> rawResults =
                    helper.getUserMediaKontakDetailDao().queryRaw(
                            "select * from clsMediaKontakDetail where txtDeskripsi = 'FACEBOOK'",
                            new RawRowMapper<clsMediaKontakDetail>() {
                                public clsMediaKontakDetail mapRow(String[] columnNames,
                                                                   String[] resultColumns) {
                                    clsMediaKontakDetail dt = new clsMediaKontakDetail();
                                    dt.txtGuiId = resultColumns[4];
                                    dt.txtDetailMedia = resultColumns[3];
                                    dt.txtPrioritasKontak = resultColumns[8];
                                    dt.txtKeterangan = resultColumns[6];
                                    dt.lttxtStatusAktif = resultColumns[1];

                                    return dt;
                                }
                            });

            items = rawResults.getResults();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public List<?> findbyInstagram() throws SQLException {
        List<clsMediaKontakDetail> items = null;
        try {
            GenericRawResults<clsMediaKontakDetail> rawResults =
                    helper.getUserMediaKontakDetailDao().queryRaw(
                            "select * from clsMediaKontakDetail where txtDeskripsi = 'INSTAGRAM'",
                            new RawRowMapper<clsMediaKontakDetail>() {
                                public clsMediaKontakDetail mapRow(String[] columnNames,
                                                                   String[] resultColumns) {
                                    clsMediaKontakDetail dt = new clsMediaKontakDetail();
                                    dt.txtGuiId = resultColumns[4];
                                    dt.txtDetailMedia = resultColumns[3];
                                    dt.txtPrioritasKontak = resultColumns[8];
                                    dt.txtKeterangan = resultColumns[6];
                                    dt.lttxtStatusAktif = resultColumns[1];

                                    return dt;
                                }
                            });

            items = rawResults.getResults();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public List<?> findbyFax() throws SQLException {
        List<clsMediaKontakDetail> items = null;
        try {
            GenericRawResults<clsMediaKontakDetail> rawResults =
                    helper.getUserMediaKontakDetailDao().queryRaw(
                            "select * from clsMediaKontakDetail where txtDeskripsi = 'Fax'",
                            new RawRowMapper<clsMediaKontakDetail>() {
                                public clsMediaKontakDetail mapRow(String[] columnNames,
                                                                   String[] resultColumns) {
                                    clsMediaKontakDetail dt = new clsMediaKontakDetail();
                                    dt.txtGuiId = resultColumns[4];
                                    dt.txtDetailMedia = resultColumns[3];
                                    dt.txtPrioritasKontak = resultColumns[8];
                                    dt.txtKeterangan = resultColumns[6];
                                    dt.lttxtStatusAktif = resultColumns[1];

                                    return dt;
                                }
                            });

            items = rawResults.getResults();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public List<?> findbyPath() throws SQLException {
        List<clsMediaKontakDetail> items = null;
        try {
            GenericRawResults<clsMediaKontakDetail> rawResults =
                    helper.getUserMediaKontakDetailDao().queryRaw(
                            "select * from clsMediaKontakDetail where txtDeskripsi = 'PATH'",
                            new RawRowMapper<clsMediaKontakDetail>() {
                                public clsMediaKontakDetail mapRow(String[] columnNames,
                                                                   String[] resultColumns) {
                                    clsMediaKontakDetail dt = new clsMediaKontakDetail();
                                    dt.txtGuiId = resultColumns[4];
                                    dt.txtDetailMedia = resultColumns[3];
                                    dt.txtPrioritasKontak = resultColumns[8];
                                    dt.txtKeterangan = resultColumns[6];
                                    dt.lttxtStatusAktif = resultColumns[1];

                                    return dt;
                                }
                            });

            items = rawResults.getResults();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public List<?> findbyMMS() throws SQLException {
        List<clsMediaKontakDetail> items = null;
        try {
            GenericRawResults<clsMediaKontakDetail> rawResults =
                    helper.getUserMediaKontakDetailDao().queryRaw(
                            "select * from clsMediaKontakDetail where txtDeskripsi = 'MMS'",
                            new RawRowMapper<clsMediaKontakDetail>() {
                                public clsMediaKontakDetail mapRow(String[] columnNames,
                                                                   String[] resultColumns) {
                                    clsMediaKontakDetail dt = new clsMediaKontakDetail();
                                    dt.txtGuiId = resultColumns[4];
                                    dt.txtDetailMedia = resultColumns[3];
                                    dt.txtPrioritasKontak = resultColumns[8];
                                    dt.txtKeterangan = resultColumns[6];
                                    dt.lttxtStatusAktif = resultColumns[1];

                                    return dt;
                                }
                            });

            items = rawResults.getResults();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }
}
