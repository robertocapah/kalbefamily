package kalbefamily.crm.kalbe.kalbefamily.Common;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by Rian Andrivani on 7/17/2017.
 */
@DatabaseTable
public class clsUserMember implements Serializable {
    public String getTxtKontakId() {
        return txtKontakId;
    }

    public void setTxtKontakId(String txtKontakId) {
        this.txtKontakId = txtKontakId;
    }

    public String getTxtMemberId() {
        return txtMemberId;
    }

    public void setTxtMemberId(String txtMemberId) {
        this.txtMemberId = txtMemberId;
    }

    public String getTxtNama() {
        return txtNama;
    }

    public void setTxtNama(String txtNama) {
        this.txtNama = txtNama;
    }

    public String getTxtAlamat() {
        return txtAlamat;
    }

    public void setTxtAlamat(String txtAlamat) {
        this.txtAlamat = txtAlamat;
    }

    public String getTxtJenisKelamin() {
        return txtJenisKelamin;
    }

    public void setTxtJenisKelamin(String txtJenisKelamin) {
        this.txtJenisKelamin = txtJenisKelamin;
    }

    public String getTxtEmail() {
        return txtEmail;
    }

    public void setTxtEmail(String txtEmail) {
        this.txtEmail = txtEmail;
    }

    public String getTxtNoTelp() {
        return txtNoTelp;
    }

    public void setTxtNoTelp(String txtNoTelp) {
        this.txtNoTelp = txtNoTelp;
    }

    public String getTxtNoKTP() {
        return txtNoKTP;
    }

    public void setTxtNoKTP(String txtNoKTP) {
        this.txtNoKTP = txtNoKTP;
    }

    @DatabaseField(id = true,columnName = "txtKontakId")
    public String txtKontakId;
    @DatabaseField(columnName = "txtMemberId")
    public String txtMemberId;
    @DatabaseField(columnName = "txtNama")
    public String txtNama;
    @DatabaseField(columnName = "txtAlamat")
    public String txtAlamat;
    @DatabaseField(columnName = "txtJenisKelamin")
    public String txtJenisKelamin;
    @DatabaseField(columnName = "txtEmail")
    public String txtEmail;
    @DatabaseField(columnName = "txtNoTelp")
    public String txtNoTelp;
    @DatabaseField(columnName = "txtNoKTP")
    public String txtNoKTP;

    public String Property_txtKontakId = "txtKontakId";
    public String Property_txtMemberId = "txtMemberId";
    public String Property_txtNama = "txtNama";
    public String Property_txtAlamat = "txtAlamat";
    public String Property_txtJenisKelamin = "txtJenisKelamin";
    public String Property_All=Property_txtKontakId +","+
            Property_txtMemberId +","+
            Property_txtNama+","+
            Property_txtAlamat+","+
            Property_txtJenisKelamin;

    public clsUserMember() {

    }
}
