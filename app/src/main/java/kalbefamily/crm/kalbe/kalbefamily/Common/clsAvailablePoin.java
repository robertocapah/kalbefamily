package kalbefamily.crm.kalbe.kalbefamily.Common;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by Rian Andrivani on 8/16/2017.
 */
@DatabaseTable
public class clsAvailablePoin implements Serializable {
    public String getTxtGuiId() {
        return txtGuiId;
    }

    public void setTxtGuiId(String txtGuiId) {
        this.txtGuiId = txtGuiId;
    }

    public String getTxtPoint() {
        return txtPoint;
    }

    public void setTxtPoint(String txtPoint) {
        this.txtPoint = txtPoint;
    }

    public String getTxtPeriodePoint() {
        return txtPeriodePoint;
    }

    public void setTxtPeriodePoint(String txtPeriodePoint) {
        this.txtPeriodePoint = txtPeriodePoint;
    }

    public String getTxtDescription() {
        return txtDescription;
    }

    public void setTxtDescription(String txtDescription) {
        this.txtDescription = txtDescription;
    }

    @DatabaseField(id = true,columnName = "txtGuiId")
    public String txtGuiId;
    @DatabaseField(columnName = "txtPoint")
    public String txtPoint;
    @DatabaseField(columnName = "txtPeriodePoint")
    public String txtPeriodePoint;
    @DatabaseField(columnName = "txtDescription")
    public String txtDescription;

    public String Property_txtGuiId = "txtGuiId";
    public String Property_txtPoint = "txtPoint";
    public String Property_txtPeriodePoint = "txtPeriodePoint";
    public String Property_txtDescription = "txtDescription";
    public String Property_All=Property_txtGuiId +","+
            Property_txtPoint +","+
            Property_txtPeriodePoint +","+
            Property_txtDescription;

    public clsAvailablePoin(){}
}
