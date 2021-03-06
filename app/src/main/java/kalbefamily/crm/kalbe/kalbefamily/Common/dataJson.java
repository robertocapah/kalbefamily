package kalbefamily.crm.kalbe.kalbefamily.Common;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import kalbefamily.crm.kalbe.kalbefamily.BL.clsActivity;

/**
 * Created by Rian Andrivani on 7/26/2017.
 */

public class dataJson {
    public List<clsUserMember> ListDataUserMember;
    public List<clsUserMemberImage> ListDataUserMemberImage;
    public List<clsUserImageProfile> ListDataUserImageProfile;
    public List<clsQRCodeData> LisClsQRCodeData;
    public List<clsMediaKontakDetail> ListMediaKontakDetail;
    public List<clsImageStruk> ListImageStruk;

    public synchronized List<clsUserMember> getListDataUserMember() {
        return ListDataUserMember;
    }

    public synchronized void setListDataUserMember(List<clsUserMember> listDataUserMember) {
        ListDataUserMember = listDataUserMember;
    }

    public synchronized List<clsUserMemberImage> getListDataUserMemberImage() {
        return ListDataUserMemberImage;
    }

    public synchronized void setListDataUserMemberImage(List<clsUserMemberImage> listDataUserMemberImage) {
        ListDataUserMemberImage = listDataUserMemberImage;
    }

    public synchronized List<clsUserImageProfile> getListDataUserImageProfile() {
        return ListDataUserImageProfile;
    }

    public synchronized void setListDataUserImageProfile(List<clsUserImageProfile> listDataUserImageProfile) {
        ListDataUserImageProfile = listDataUserImageProfile;
    }

    public synchronized List<clsQRCodeData> getLisClsQRCodeData() {
        return LisClsQRCodeData;
    }

    public synchronized void setLisClsQRCodeData(List<clsQRCodeData> lisClsQRCodeData) {
        LisClsQRCodeData = lisClsQRCodeData;
    }

    public synchronized List<clsMediaKontakDetail> getListMediaKontakDetail() {
        return ListMediaKontakDetail;
    }

    public synchronized void setListMediaKontakDetail(List<clsMediaKontakDetail> listMediaKontakDetail) {
        ListMediaKontakDetail = listMediaKontakDetail;
    }

    public List<clsImageStruk> getListImageStruk() {
        return ListImageStruk;
    }

    public void setListImageStruk(List<clsImageStruk> listImageStruk) {
        ListImageStruk = listImageStruk;
    }

    public JSONObject txtJSON() throws JSONException {
        JSONObject resJson = new JSONObject();
        Collection<JSONObject> itemsListJquey = new ArrayList<JSONObject>();
        if (this.getListDataUserMember() != null) {
            clsUserMember dtUserMember = new clsUserMember();
            itemsListJquey = new ArrayList<JSONObject>();
            for (clsUserMember data : this.getListDataUserMember()){
                JSONObject item1 = new JSONObject();
                resJson.put(dtUserMember.Property_txtKontakId, String.valueOf(data.getTxtKontakId()));
                resJson.put(dtUserMember.Property_txtNamaDepan, String.valueOf(data.getTxtNamaDepan()));
                resJson.put(dtUserMember.Property_txtAlamat, String.valueOf(data.getTxtAlamat()));
                resJson.put(dtUserMember.Property_txtJenisKelamin, String.valueOf(data.getTxtJenisKelamin()));
                resJson.put(dtUserMember.Property_txtNoTelp, String.valueOf(data.getTxtNoTelp()));
                resJson.put(dtUserMember.Property_txtNoKTP, String.valueOf(data.getTxtNoKTP()));
                resJson.put(dtUserMember.Property_txtNamaBelakang, String.valueOf(data.getTxtNamaBelakang()));
                resJson.put(dtUserMember.Property_txtNamaPanggilan, String.valueOf(data.getTxtNamaPanggilan()));
                itemsListJquey.add(resJson);
            }
//            resJson.put(dtUserMember.Property_ListOfUserMember, new JSONArray(itemsListJquey));
        }

        if (this.getListDataUserMemberImage() != null) {
            clsUserMemberImage dtUserMemberImage = new clsUserMemberImage();
            itemsListJquey = new ArrayList<JSONObject>();
            for (clsUserMemberImage data : this.getListDataUserMemberImage()){
                JSONObject item1 = new JSONObject();
                resJson.put(dtUserMemberImage.Property_txtHeaderId, String.valueOf(data.getTxtHeaderId()));
                resJson.put(dtUserMemberImage.Property_txtImg, String.valueOf(data.getTxtImg()));
                resJson.put(dtUserMemberImage.Property_txtPosition, String.valueOf(data.getTxtPosition()));
                itemsListJquey.add(resJson);
            }
//            resJson.put(dtUserMemberImage.Property_ListOfMemberImage, new JSONArray(itemsListJquey));
        }

        if (this.getListDataUserImageProfile() != null) {
            clsUserImageProfile dtUserImageProfile = new clsUserImageProfile();
            itemsListJquey = new ArrayList<JSONObject>();
            for (clsUserImageProfile data : this.getListDataUserImageProfile()) {
                resJson.put(dtUserImageProfile.Property_txtHeaderId, String.valueOf(data.getTxtKontakId()));
                resJson.put(dtUserImageProfile.Property_txtImg, String.valueOf(data.getTxtImg()));
                itemsListJquey.add(resJson);
            }
        }

        return resJson;
    }

    public JSONObject txtJSONInputStruk() throws JSONException {
        JSONObject resJson = new JSONObject();
        Collection<JSONObject> itemsListJquey = new ArrayList<JSONObject>();
        if (this.getListImageStruk() != null) {
            clsImageStruk dtImageStruk = new clsImageStruk();
            itemsListJquey = new ArrayList<JSONObject>();
            for (clsImageStruk data : this.getListImageStruk()) {
                resJson.put(dtImageStruk.Property_txtKontakID, String.valueOf(data.getTxtKontakId()));
                resJson.put(dtImageStruk.Property_txtImg, String.valueOf(data.getTxtImg()));
                resJson.put(dtImageStruk.Property_txtGuiId, String.valueOf(data.getTxtGuiId()));
                itemsListJquey.add(resJson);
            }
        }

        return resJson;
    }

    public JSONObject txtJSONmediaKontak() throws JSONException {
        JSONObject resJson = new JSONObject();
        Collection<JSONObject> itemsListJquey = new ArrayList<JSONObject>();
        if (this.getListMediaKontakDetail() != null) {
            clsMediaKontakDetail dtMediaKontakDetail = new clsMediaKontakDetail();
            itemsListJquey = new ArrayList<JSONObject>();
            for (clsMediaKontakDetail data : this.getListMediaKontakDetail()) {
                resJson.put(dtMediaKontakDetail.Property_txtKontakId, String.valueOf(data.getTxtKontakId()));
                resJson.put(dtMediaKontakDetail.Property_lttxtMediaID, String.valueOf(data.getLttxtMediaID()));
                resJson.put(dtMediaKontakDetail.Property_txtPrioritasKontak, String.valueOf(data.getTxtPrioritasKontak()));
                resJson.put(dtMediaKontakDetail.Property_txtDetailMedia, String.valueOf(data.getTxtDetailMedia()));
                resJson.put(dtMediaKontakDetail.Property_txtKeterangan, String.valueOf(data.getTxtKeterangan()));
                resJson.put(dtMediaKontakDetail.Property_lttxtStatusAktif, String.valueOf(data.getLttxtStatusAktif()));
                resJson.put(dtMediaKontakDetail.Property_txtPrioritasKontak, String.valueOf(data.getTxtPrioritasKontak()));
                resJson.put(dtMediaKontakDetail.Property_txtKategoriMedia, String.valueOf(data.getTxtKategoriMedia()));
                resJson.put(dtMediaKontakDetail.Property_txtExtension, String.valueOf(data.getTxtExtension()));
                itemsListJquey.add(resJson);
            }
        }

        return resJson;
    }

    public JSONObject txtJSONqrCode() throws JSONException {
        JSONObject resJson = new JSONObject();
        Collection<JSONObject> itemsListJquey = new ArrayList<JSONObject>();
        if (this.getLisClsQRCodeData() != null) {
            clsQRCodeData dtQRCodeData = new clsQRCodeData();
            itemsListJquey = new ArrayList<JSONObject>();
            for (clsQRCodeData data : this.getLisClsQRCodeData()) {
                resJson.put(dtQRCodeData.Property_IntQRCodeID, String.valueOf(data.getIntQRCodeID()));
                resJson.put(dtQRCodeData.Property_txtKontakID, String.valueOf(data.getTxtKontakID()));
                resJson.put(dtQRCodeData.Property_txtMemberID, String.valueOf(data.getTxtMemberID()));
                itemsListJquey.add(resJson);
            }
        }

        return resJson;
    }

    public dataJson() {
        super();
        // TODO Auto-generated constructor stub
    }
}
