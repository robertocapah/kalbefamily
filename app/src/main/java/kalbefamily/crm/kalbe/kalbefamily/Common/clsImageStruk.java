package kalbefamily.crm.kalbe.kalbefamily.Common;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by User on 2/14/2018.
 */
@DatabaseTable
public class clsImageStruk implements Serializable {
    public String getTxtGuiId() {
        return txtGuiId;
    }

    public void setTxtGuiId(String txtGuiId) {
        this.txtGuiId = txtGuiId;
    }

    public String getTxtKontakId() {
        return txtKontakId;
    }

    public void setTxtKontakId(String txtKontakId) {
        this.txtKontakId = txtKontakId;
    }

    public byte[] getTxtImg() {
        return txtImg;
    }

    public void setTxtImg(byte[] txtImg) {
        this.txtImg = txtImg;
    }

    public String getTxtPath() {
        return txtPath;
    }

    public void setTxtPath(String txtPath) {
        this.txtPath = txtPath;
    }

    public String getTxtValidate() {
        return txtValidate;
    }

    public void setTxtValidate(String txtValidate) {
        this.txtValidate = txtValidate;
    }

    public String getTxtDate() {
        return txtDate;
    }

    public void setTxtDate(String txtDate) {
        this.txtDate = txtDate;
    }

    public String getTxtActiveOcr() {
        return txtActiveOcr;
    }

    public void setTxtActiveOcr(String txtActiveOcr) {
        this.txtActiveOcr = txtActiveOcr;
    }

    public String getTxtFileEdit() {
        return txtFileEdit;
    }

    public void setTxtFileEdit(String txtFileEdit) {
        this.txtFileEdit = txtFileEdit;
    }

    public String getTxtReason() {
        return txtReason;
    }

    public void setTxtReason(String txtReason) {
        this.txtReason = txtReason;
    }

    public Date getDtDate() {
        return dtDate;
    }

    public void setDtDate(Date dtDate) {
        this.dtDate = dtDate;
    }

    @DatabaseField(id = true,columnName = "txtGuiId")
    public String txtGuiId;
    @DatabaseField(columnName = "txtKontakId")
    public String txtKontakId;
    @DatabaseField(columnName = "txtImg", dataType = DataType.BYTE_ARRAY)
    private byte[] txtImg;
    @DatabaseField(columnName = "txtPath")
    public String txtPath;
    @DatabaseField(columnName = "txtValidate")
    public String txtValidate;
    @DatabaseField(columnName = "txtDate")
    public String txtDate;
    @DatabaseField(columnName = "dtDate", dataType = DataType.DATE_STRING, format = "yyyy-MM-dd HH:mm:ss")
    public Date dtDate;
    @DatabaseField(columnName = "txtActiveOcr")
    public String txtActiveOcr;
    @DatabaseField(columnName = "txtFileEdit")
    public String txtFileEdit;
    @DatabaseField(columnName = "txtReason")
    public String txtReason;

    public String Property_txtGuiId = "txtGuiId";
    public String Property_txtKontakID = "txtKontakID";
    public String Property_txtImg = "txtImageStruk";
    public String Property_All=Property_txtGuiId +","+
            Property_txtKontakID +","+
            Property_txtImg;
}
