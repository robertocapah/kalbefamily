package kalbefamily.crm.kalbe.kalbefamily.Common;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by Rian Andrivani on 7/25/2017.
 */
@DatabaseTable
public class clsUserMemberImage implements Serializable {
    public String getTxtGuiId() {
        return txtGuiId;
    }

    public void setTxtGuiId(String txtGuiId) {
        this.txtGuiId = txtGuiId;
    }

    public String getTxtHeaderId() {
        return txtHeaderId;
    }

    public void setTxtHeaderId(String txtHeaderId) {
        this.txtHeaderId = txtHeaderId;
    }

    public byte[] getTxtImg() {
        return txtImg;
    }

    public void setTxtImg(byte[] txtImg) {
        this.txtImg = txtImg;
    }

    public String getTxtPosition() {
        return txtPosition;
    }

    public void setTxtPosition(String txtPosition) {
        this.txtPosition = txtPosition;
    }

    @DatabaseField(id = true,columnName = "txtGuiId")
    public String txtGuiId;
    @DatabaseField(columnName = "txtHeaderId")
    public String txtHeaderId;
    @DatabaseField(columnName = "txtImg", dataType = DataType.BYTE_ARRAY)
    private byte[] txtImg;
    @DatabaseField(columnName = "txtPosition")
    public String txtPosition;

    public clsUserMemberImage(){

    }
}
