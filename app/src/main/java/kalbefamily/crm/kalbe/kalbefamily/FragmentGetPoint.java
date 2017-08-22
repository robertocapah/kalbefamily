package kalbefamily.crm.kalbe.kalbefamily;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.List;

import kalbefamily.crm.kalbe.kalbefamily.BL.clsActivity;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsAvailablePoin;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserMember;
import kalbefamily.crm.kalbe.kalbefamily.Data.DatabaseHelper;
import kalbefamily.crm.kalbe.kalbefamily.Data.DatabaseManager;
import kalbefamily.crm.kalbe.kalbefamily.Data.VolleyResponseListener;
import kalbefamily.crm.kalbe.kalbefamily.Data.VolleyUtils;
import kalbefamily.crm.kalbe.kalbefamily.Data.clsHardCode;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsAvailablePoinRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserMemberRepo;

/**
 * Created by Rian Andrivani on 8/22/2017.
 */

public class FragmentGetPoint extends Fragment {
    View v;
    Context context;
    List<clsUserMember> dataMember = null;
    clsAvailablePoinRepo repoAvailablePoin;
    private String txtKontakID;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_available_poin, container, false);
        context = getActivity().getApplicationContext();
        availablePoin();
        return v;
    }

    public void availablePoin() {
        final ProgressDialog Dialog = new ProgressDialog(getActivity());
        RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());
        clsUserMemberRepo repo = new clsUserMemberRepo(context.getApplicationContext());
        DatabaseHelper helper = DatabaseManager.getInstance().getHelper();
        helper.refreshData2();
        try {
            dataMember = (List<clsUserMember>) repo.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        txtKontakID = dataMember.get(0).getTxtKontakId();
        String strLinkAPI = new clsHardCode().linkAvailablePoin;
//        String nameRole = selectedRole;
        JSONObject resJson = new JSONObject();

        try {
            resJson.put("txtKontakID", "1110-8885986");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String mRequestBody = "[" + resJson.toString() + "]";

        new VolleyUtils().makeJsonObjectRequest(getActivity(), strLinkAPI, mRequestBody, "Getting Data, Please wait !", new VolleyResponseListener() {
            @Override
            public void onError(String response) {
                new clsActivity().showCustomToast(context.getApplicationContext(), response, false);
            }

            @Override
            public void onResponse(String response, Boolean status, String strErrorMsg) {
                if (response != null) {
                    try {
                        JSONObject jsonObject1 = new JSONObject(response);
                        JSONObject jsn = jsonObject1.getJSONObject("validJson");
                        String warn = jsn.getString("TxtMessage");
                        String result = jsn.getString("IntResult");

                        if (result.equals("1")) {
                            JSONArray jsonDataUserMember = jsn.getJSONArray("ListOfObjectData");
                            for(int i=0; i < jsonDataUserMember.length(); i++) {
                                JSONObject jsonobject = jsonDataUserMember.getJSONObject(i);
                                String txtDescription = jsonobject.getString("TxtDescription");
                                String txtPoint = jsonobject.getString("TxtPoint");
                                String txtPeriodePoint = jsonobject.getString("TxtPeriodePoint");

                                clsAvailablePoin dataPoin = new clsAvailablePoin();
                                dataPoin.setTxtGuiId(new clsActivity().GenerateGuid());
                                dataPoin.setTxtPoint(txtPoint);
                                dataPoin.setTxtPeriodePoint(txtPeriodePoint);
                                dataPoin.setTxtDescription(txtDescription);

                                repoAvailablePoin = new clsAvailablePoinRepo(context.getApplicationContext());
                                repoAvailablePoin.createOrUpdate(dataPoin);
                            }
                            FragmentAvailablePoin fragmentAvailablePoin = new FragmentAvailablePoin();
                            FragmentTransaction fragmentTransactionAvailablePoint = getActivity().getSupportFragmentManager().beginTransaction();
                            fragmentTransactionAvailablePoint.replace(R.id.frame, fragmentAvailablePoin);
                            fragmentTransactionAvailablePoint.commit();
                            new clsActivity().showCustomToast(context.getApplicationContext(), "Get Data, Success", true);
                        } else {
                            new clsActivity().showCustomToast(context.getApplicationContext(), warn, false);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
//                if(!status){
//                    new clsMainActivity().showCustomToast(getApplicationContext(), strErrorMsg, false);
//                }
            }
        });
    }
}
