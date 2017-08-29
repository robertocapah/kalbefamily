package kalbefamily.crm.kalbe.kalbefamily.Common;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by Rian Andrivani on 8/29/2017.
 */
@DatabaseTable
public class clsMediaKontakDetail implements Serializable {
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

    public String getLttxtMediaID() {
        return lttxtMediaID;
    }

    public void setLttxtMediaID(String lttxtMediaID) {
        this.lttxtMediaID = lttxtMediaID;
    }

    public String getTxtDeskripsi() {
        return txtDeskripsi;
    }

    public void setTxtDeskripsi(String txtDeskripsi) {
        this.txtDeskripsi = txtDeskripsi;
    }

    public String getTxtPrioritasKontak() {
        return txtPrioritasKontak;
    }

    public void setTxtPrioritasKontak(String txtPrioritasKontak) {
        this.txtPrioritasKontak = txtPrioritasKontak;
    }

    public String getTxtDetailMedia() {
        return txtDetailMedia;
    }

    public void setTxtDetailMedia(String txtDetailMedia) {
        this.txtDetailMedia = txtDetailMedia;
    }

    public String getTxtKeterangan() {
        return txtKeterangan;
    }

    public void setTxtKeterangan(String txtKeterangan) {
        this.txtKeterangan = txtKeterangan;
    }

    public String getLttxtStatusAktif() {
        return lttxtStatusAktif;
    }

    public void setLttxtStatusAktif(String lttxtStatusAktif) {
        this.lttxtStatusAktif = lttxtStatusAktif;
    }

    public String getTxtKategoriMedia() {
        return txtKategoriMedia;
    }

    public void setTxtKategoriMedia(String txtKategoriMedia) {
        this.txtKategoriMedia = txtKategoriMedia;
    }

    @DatabaseField(id = true,columnName = "txtGuiId")
    public String txtGuiId;
    @DatabaseField(columnName = "txtKontakId")
    public String txtKontakId;
    @DatabaseField(columnName = "lttxtMediaID")
    public String lttxtMediaID;
    @DatabaseField(columnName = "txtDeskripsi")
    public String txtDeskripsi;
    @DatabaseField(columnName = "txtPrioritasKontak")
    public String txtPrioritasKontak;
    @DatabaseField(columnName = "txtDetailMedia")
    public String txtDetailMedia;
    @DatabaseField(columnName = "txtKeterangan")
    public String txtKeterangan;
    @DatabaseField(columnName = "lttxtStatusAktif")
    public String lttxtStatusAktif;
    @DatabaseField(columnName = "txtKategoriMedia")
    public String txtKategoriMedia;

    public String Property_txtGuiId = "txtGuiId";
    public String Property_txtKontakId = "txtKontakId";
    public String Property_lttxtMediaID = "lttxtMediaID";
    public String Property_txtDeskripsi = "txtDeskripsi";
    public String Property_txtPrioritasKontak = "txtPrioritasKontak";
    public String Property_txtDetailMedia = "txtDetailMedia";
    public String Property_txtKeterangan = "txtKeterangan";
    public String Property_lttxtStatusAktif = "lttxtStatusAktif";
    public String Property_txtKategoriMedia = "txtKategoriMedia";
    public String Property_All=Property_txtGuiId +","+
            Property_txtKontakId +","+
            Property_lttxtMediaID+","+
            Property_txtDeskripsi+","+
            Property_txtPrioritasKontak+","+
            Property_txtDetailMedia+","+
            Property_txtKeterangan+","+
            Property_txtKeterangan+","+
            Property_lttxtStatusAktif +","+
            Property_txtKategoriMedia;
}
