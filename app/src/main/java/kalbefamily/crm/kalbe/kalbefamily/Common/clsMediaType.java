package kalbefamily.crm.kalbe.kalbefamily.Common;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by Rian Andrivani on 9/20/2017.
 */
@DatabaseTable
public class clsMediaType implements Serializable {
    public String getTxtGuiId() {
        return txtGuiId;
    }

    public void setTxtGuiId(String txtGuiId) {
        this.txtGuiId = txtGuiId;
    }

    public String getTxtGrupMasterID() {
        return txtGrupMasterID;
    }

    public void setTxtGrupMasterID(String txtGrupMasterID) {
        this.txtGrupMasterID = txtGrupMasterID;
    }

    public String getTxtNamaMasterData() {
        return txtNamaMasterData;
    }

    public void setTxtNamaMasterData(String txtNamaMasterData) {
        this.txtNamaMasterData = txtNamaMasterData;
    }

    public String getTxtKeterangan() {
        return txtKeterangan;
    }

    public void setTxtKeterangan(String txtKeterangan) {
        this.txtKeterangan = txtKeterangan;
    }

    @DatabaseField(id = true,columnName = "txtGuiId")
    public String txtGuiId;
    @DatabaseField(columnName = "txtGrupMasterID")
    public String txtGrupMasterID;
    @DatabaseField(columnName = "txtNamaMasterData")
    public String txtNamaMasterData;
    @DatabaseField(columnName = "txtKeterangan")
    public String txtKeterangan;

    public clsMediaType(){}
}
