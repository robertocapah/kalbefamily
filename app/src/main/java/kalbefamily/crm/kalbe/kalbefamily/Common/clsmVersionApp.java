package kalbefamily.crm.kalbe.kalbefamily.Common;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by Rian Andrivani on 7/6/2017.
 */
@DatabaseTable
public class clsmVersionApp implements Serializable {
    @DatabaseField(id = true,columnName = "txtGUI")
    public String txtGUI;
    @DatabaseField(columnName = "bitActive")
    public String bitActive;
    @DatabaseField(columnName = "txtNameApp")
    public String txtNameApp;
    @DatabaseField(columnName = "txtVersion")
    public String txtVersion;
    @DatabaseField(columnName = "txtFile")
    public String txtFile;

    public void mVersionApp(){

    }

    public String getBitActive() {
        return bitActive;
    }

    public void setBitActive(String bitActive) {
        this.bitActive = bitActive;
    }

    public String getTxtGUI() {
        return txtGUI;
    }

    public void setTxtGUI(String txtGUI) {
        this.txtGUI = txtGUI;
    }

    public String getTxtNameApp() {
        return txtNameApp;
    }

    public void setTxtNameApp(String txtNameApp) {
        this.txtNameApp = txtNameApp;
    }

    public String getTxtVersion() {
        return txtVersion;
    }

    public void setTxtVersion(String txtVersion) {
        this.txtVersion = txtVersion;
    }

    public String getTxtFile() {
        return txtFile;
    }

    public void setTxtFile(String txtFile) {
        this.txtFile = txtFile;
    }
}
