package kalbefamily.crm.kalbe.kalbefamily.Common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Rian Andrivani on 7/26/2017.
 */

public class dataJson {
    public List<clsUserMember> ListDataUserMember;
    public List<clsUserMemberImage> ListDataUserMemberImage;

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

    public JSONObject txtJSON() throws JSONException {
        JSONObject resJson = new JSONObject();
        Collection<JSONObject> itemsListJquey = new ArrayList<JSONObject>();
        if (this.getListDataUserMember() != null) {
            clsUserMember dtUserMember = new clsUserMember();
            itemsListJquey = new ArrayList<JSONObject>();
            for (clsUserMember data : this.getListDataUserMember()){
                JSONObject item1 = new JSONObject();
                item1.put(dtUserMember.Property_txtKontakId, String.valueOf(data.getTxtKontakId()));
                item1.put(dtUserMember.Property_txtNama, String.valueOf(data.getTxtNama()));
                item1.put(dtUserMember.Property_txtAlamat, String.valueOf(data.getTxtAlamat()));
                item1.put(dtUserMember.Property_txtJenisKelamin, String.valueOf(data.getTxtJenisKelamin()));
                item1.put(dtUserMember.Property_txtNoTelp, String.valueOf(data.getTxtNoTelp()));
                item1.put(dtUserMember.Property_txtNoKTP, String.valueOf(data.getTxtNoKTP()));
                item1.put(dtUserMember.Property_txtNamaKeluarga, String.valueOf(data.getTxtNamaKeluarga()));
                item1.put(dtUserMember.Property_txtNamaPanggilan, String.valueOf(data.getTxtNamaPanggilan()));
                itemsListJquey.add(item1);
            }
            resJson.put(dtUserMember.Property_ListOfUserMember, new JSONArray(itemsListJquey));
        }

        if (this.getListDataUserMemberImage() != null) {
            clsUserMemberImage dtUserMemberImage = new clsUserMemberImage();
            itemsListJquey = new ArrayList<JSONObject>();
            for (clsUserMemberImage data : this.getListDataUserMemberImage()){
                JSONObject item1 = new JSONObject();
                item1.put(dtUserMemberImage.Property_txtHeaderId, String.valueOf(data.getTxtHeaderId()));
                item1.put(dtUserMemberImage.Property_txtImg, String.valueOf(data.getTxtImg()));
                item1.put(dtUserMemberImage.Property_txtPosition, String.valueOf(data.getTxtPosition()));
                itemsListJquey.add(item1);
            }
            resJson.put(dtUserMemberImage.Property_ListOfMemberImage, new JSONArray(itemsListJquey));
        }

        return resJson;
    }

    public dataJson() {
        super();
        // TODO Auto-generated constructor stub
    }
}
