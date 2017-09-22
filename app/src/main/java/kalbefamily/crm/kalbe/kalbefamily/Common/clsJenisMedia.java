package kalbefamily.crm.kalbe.kalbefamily.Common;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by Rian Andrivani on 9/22/2017.
 */
@DatabaseTable
public class clsJenisMedia implements Serializable {
    public String getTxtGuiId() {
        return txtGuiId;
    }

    public void setTxtGuiId(String txtGuiId) {
        this.txtGuiId = txtGuiId;
    }

    public String getTxtNamaMasterData() {
        return txtNamaMasterData;
    }

    public void setTxtNamaMasterData(String txtNamaMasterData) {
        this.txtNamaMasterData = txtNamaMasterData;
    }

    public String getTxtGrupMasterID() {
        return txtGrupMasterID;
    }

    public void setTxtGrupMasterID(String txtGrupMasterID) {
        this.txtGrupMasterID = txtGrupMasterID;
    }

    public String getTxtNamaGrupMaster() {
        return txtNamaGrupMaster;
    }

    public void setTxtNamaGrupMaster(String txtNamaGrupMaster) {
        this.txtNamaGrupMaster = txtNamaGrupMaster;
    }

    @DatabaseField(id = true,columnName = "txtGuiId")
    public String txtGuiId;
    @DatabaseField(columnName = "txtNamaMasterData")
    public String txtNamaMasterData;
    @DatabaseField(columnName = "txtGrupMasterID")
    public String txtGrupMasterID;
    @DatabaseField(columnName = "txtNamaGrupMaster")
    public String txtNamaGrupMaster;

    public clsJenisMedia() {}
}
