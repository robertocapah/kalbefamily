package kalbefamily.crm.kalbe.kalbefamily.Common;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by Rian Andrivani on 8/16/2017.
 */
@DatabaseTable
public class clsUserImageProfile implements Serializable {
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

    @DatabaseField(id = true,columnName = "txtGuiId")
    public String txtGuiId;
    @DatabaseField(columnName = "txtKontakId")
    public String txtKontakId;
    @DatabaseField(columnName = "txtImg", dataType = DataType.BYTE_ARRAY)
    private byte[] txtImg;

    public String Property_txtGuiId = "txtGuiId";
    public String Property_txtKontakId = "txtKontakID";
    public String Property_txtImg = "txtImageName";
    public String Property_All=Property_txtGuiId +","+
            Property_txtKontakId +","+
            Property_txtImg;

    public clsUserImageProfile(){}
}
