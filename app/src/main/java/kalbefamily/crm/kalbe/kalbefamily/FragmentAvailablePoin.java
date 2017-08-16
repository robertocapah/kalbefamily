package kalbefamily.crm.kalbe.kalbefamily;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import kalbefamily.crm.kalbe.kalbefamily.BL.clsActivity;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsAvailablePoin;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserMember;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserMemberImage;
import kalbefamily.crm.kalbe.kalbefamily.Data.VolleyResponseListener;
import kalbefamily.crm.kalbe.kalbefamily.Data.VolleyUtils;
import kalbefamily.crm.kalbe.kalbefamily.Data.clsHardCode;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsAvailablePoinRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserMemberImageRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserMemberRepo;

/**
 * Created by Rian Andrivani on 8/16/2017.
 */

public class FragmentAvailablePoin extends Fragment {
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

    public void init() {
        TableLayout stk = (TableLayout) v.findViewById(R.id.table_main);
        TableRow tbrow0 = new TableRow(context.getApplicationContext());
        TextView tv0 = new TextView(context.getApplicationContext());
        tv0.setText(" No Urut ");
        tv0.setTextColor(Color.WHITE);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(context.getApplicationContext());
        tv1.setText(" Description ");
        tv1.setTextColor(Color.WHITE);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(context.getApplicationContext());
        tv2.setText(" Point ");
        tv2.setTextColor(Color.WHITE);
        tbrow0.addView(tv2);
        TextView tv3 = new TextView(context.getApplicationContext());
        tv3.setText(" Periode Point ");
        tv3.setTextColor(Color.WHITE);
        tbrow0.addView(tv3);
        stk.addView(tbrow0);
        for (int i = 0; i < 25; i++) {
            TableRow tbrow = new TableRow(context.getApplicationContext());
            TextView t1v = new TextView(context.getApplicationContext());
            t1v.setText("" + i);
            t1v.setTextColor(Color.WHITE);
            t1v.setGravity(Gravity.CENTER);
            tbrow.addView(t1v);
            TextView t2v = new TextView(context.getApplicationContext());
            t2v.setText("Product " + i);
            t2v.setTextColor(Color.WHITE);
            t2v.setGravity(Gravity.CENTER);
            tbrow.addView(t2v);
            TextView t3v = new TextView(context.getApplicationContext());
            t3v.setText("Rs." + i);
            t3v.setTextColor(Color.WHITE);
            t3v.setGravity(Gravity.CENTER);
            tbrow.addView(t3v);
            TextView t4v = new TextView(context.getApplicationContext());
            t4v.setText("" + i * 15 / 32 * 10);
            t4v.setTextColor(Color.WHITE);
            t4v.setGravity(Gravity.CENTER);
            tbrow.addView(t4v);
            stk.addView(tbrow);
        }

    }
}
