package kalbefamily.crm.kalbe.kalbefamily.Common;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by Rian Andrivani on 7/28/2017.
 */
@DatabaseTable
public class clsQRCodeData implements Serializable {
    public String getIntQRCodeID() {
        return IntQRCodeID;
    }

    public void setIntQRCodeID(String intQRCodeID) {
        IntQRCodeID = intQRCodeID;
    }

    public String getTxtKontakID() {
        return txtKontakID;
    }

    public void setTxtKontakID(String txtKontakID) {
        this.txtKontakID = txtKontakID;
    }

    @DatabaseField(id = true,columnName = "IntQRCodeID")
    public String IntQRCodeID;
    @DatabaseField(columnName = "txtKontakID")
    public String txtKontakID;

    public String Property_IntQRCodeID = "IntQRCodeID";
    public String Property_txtKontakID = "txtKontakID";
    public String Property_All=Property_IntQRCodeID +","+
            Property_txtKontakID;

    public clsQRCodeData(){}
}
