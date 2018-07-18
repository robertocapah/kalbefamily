package kalbefamily.crm.kalbe.kalbefamily.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.owater.library.CircleTextView;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import kalbefamily.crm.kalbe.kalbefamily.BL.clsActivity;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsToken;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserImageProfile;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserMember;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserMemberImage;
import kalbefamily.crm.kalbe.kalbefamily.Common.mConfigData;
import kalbefamily.crm.kalbe.kalbefamily.Data.VolleyResponseListener;
import kalbefamily.crm.kalbe.kalbefamily.Data.VolleyUtils;
import kalbefamily.crm.kalbe.kalbefamily.Data.clsHardCode;
import kalbefamily.crm.kalbe.kalbefamily.R;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsTokenRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserImageProfileRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserMemberImageRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserMemberRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.mConfigRepo;
import kalbefamily.crm.kalbe.kalbefamily.ViewPagerActivity;

/**
 * Created by Rian Andrivani on 7/19/2017.
 */

public class FragmentInfoContact extends Fragment {
    View v;
    TextView tvUsername, tvPhone, tvEmail, tvAddress, tvBasePoint;
    CircleImageView ivProfile;
    FloatingActionButton refreshButton;
    Context context;
    CircleTextView ctvStatus;
    List<clsUserMember> dataMember = null;
    List<clsUserImageProfile> dataUserImageProfile = null;
    List<clsToken> dataToken;
    clsTokenRepo tokenRepo;
    clsUserMemberRepo repoUserMember = null;
    clsUserMemberImageRepo imageRepo = null;
    clsUserImageProfileRepo repoUserImageProfile = null;
    private String txtMember, access_token;
    private String linkImageProfile = "null";
    private static Bitmap mybitmapImageProfile;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_kontak_detail,container,false);
        context = getActivity().getApplicationContext();
        ivProfile = (CircleImageView) v.findViewById(R.id.profile_image);
        tvUsername = (TextView) v.findViewById(R.id.tvUsername);
        tvPhone = (TextView) v.findViewById(R.id.tvNumber1);
        tvEmail = (TextView) v.findViewById(R.id.tvNumber3);
        tvAddress = (TextView) v.findViewById(R.id.tvNumber5);
        tvBasePoint = (TextView) v.findViewById(R.id.tvBasePoint);
        refreshButton = (FloatingActionButton) v.findViewById(R.id.fab);

        try {
            tokenRepo = new clsTokenRepo(context);
            dataToken = (List<clsToken>) tokenRepo.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        UserMember();

        try {
            repoUserMember = new clsUserMemberRepo(context);
            dataMember = (List<clsUserMember>) repoUserMember.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tvUsername.setText(dataMember.get(0).getTxtNama().toString());
        tvPhone.setText(dataMember.get(0).getTxtNoTelp().toString());
        tvEmail.setText(dataMember.get(0).getTxtEmail().toString());
        tvAddress.setText(dataMember.get(0).getTxtAlamat().toString());
        if (dataMember.get(0).getTxtBasePoin().equals("null")) {
            tvBasePoint.setText("( Base Point : 0 )");
        } else {
            tvBasePoint.setText("( Base Point : " +dataMember.get(0).getTxtBasePoin()+ " )");
        }

        try {
            repoUserImageProfile = new clsUserImageProfileRepo(context);
            dataUserImageProfile = (List<clsUserImageProfile>) repoUserImageProfile.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

//        if (dataUserImageProfile.size() > 0) {
//            viewImageProfile();
//        }

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!linkImageProfile.equals("null")) {
                    File file = new File(Environment.getExternalStorageDirectory() + File.separator + "Gambar" + ".png");
                    file.delete();
                    FileOutputStream fOut = null;
                    try {
                        fOut = new FileOutputStream(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    mybitmapImageProfile = ((BitmapDrawable)ivProfile.getDrawable()).getBitmap();
                    mybitmapImageProfile.compress(Bitmap.CompressFormat.PNG, 85, fOut);
                    try {
                        fOut.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        fOut.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent(getActivity(), ViewPagerActivity.class);
                    intent.putExtra("gambar profile", "Gambar");
                    startActivity(intent);
                }

//                if (dataUserImageProfile.size() > 0) {
////                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
////                    mybitmapImageProfile.compress(Bitmap.CompressFormat.PNG, 100, stream);
////                    byte[] byteArray = stream.toByteArray();
//
//
//                }
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserMemberRefresh();
            }
        });

        return v;
    }

    public void UserMember() {
        final ProgressDialog Dialog = new ProgressDialog(getActivity());
        RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());
        clsUserMemberRepo repo = new clsUserMemberRepo(context.getApplicationContext());
        try {
            dataMember = (List<clsUserMember>) repo.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        txtMember = dataMember.get(0).getTxtMemberId();
        String strLinkAPI = new clsHardCode().linkGetDetailCustomer;
//        String nameRole = selectedRole;
        JSONObject resJson = new JSONObject();

        try {
            resJson.put("txtMemberId", txtMember);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String mRequestBody = "[" + resJson.toString() + "]";

        volleyUserMember(strLinkAPI, mRequestBody, "Refresh Data...", new VolleyResponseListener() {
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
                                String txtKontakId = jsonobject.getString("TxtKontakId");
                                String memberID = jsonobject.getString("TxtMemberId");
                                String txtNama = jsonobject.getString("TxtNama");
                                String txtAlamat = jsonobject.getString("TxtAlamat");
                                String txtJenisKelamin = jsonobject.getString("TxtJenisKelamin");
                                String txtEmail = jsonobject.getString("TxtEmail");
                                String txtTelp = jsonobject.getString("TxtTelp");
                                String txtNoKTP = jsonobject.getString("TxtNoKTP");
                                String txtNamaDepan = jsonobject.getString("TxtNamaDepan");
                                String txtNamaBelakang = jsonobject.getString("TxtNamaKeluarga");
                                String txtNamaPanggilan = jsonobject.getString("TxtNamaPanggilan");
                                String intBasePoin = jsonobject.getString("IntBasePoin");
                                String txtTglAwal = jsonobject.getString("DtTglAwal");
                                String txtTglBerlaku = jsonobject.getString("TxttglBerlaku");

                                clsUserMember dataUser = new clsUserMember();
                                dataUser.setTxtKontakId(txtKontakId);
                                dataUser.setTxtMemberId(memberID);
                                dataUser.setTxtNama(txtNama);
                                dataUser.setTxtAlamat(txtAlamat);
                                dataUser.setTxtJenisKelamin(txtJenisKelamin);
                                dataUser.setTxtEmail(txtEmail);
                                dataUser.setTxtNoTelp(txtTelp);
                                dataUser.setTxtNoKTP(txtNoKTP);
                                dataUser.setTxtNamaDepan(txtNamaDepan);
                                dataUser.setTxtNamaBelakang(txtNamaBelakang);
                                dataUser.setTxtNamaPanggilan(txtNamaPanggilan);
                                dataUser.setTxtBasePoin(intBasePoin);
                                dataUser.setTxtTglAwal(txtTglAwal);
                                dataUser.setTxtTglBerlaku(txtTglBerlaku);

                                repoUserMember = new clsUserMemberRepo(context.getApplicationContext());
                                repoUserMember.createOrUpdate(dataUser);
                                Log.d("Data info", "Data Member berhasil di update");
//
//                                int h = 0;
//                                h = repoUserMember.createOrUpdate(dataUser);
//                                if(h > -1) {
//                                    Log.d("Data info", "Data Member berhasil di update");
////                                    status = true;
//                                }

                                String listtkontakImage = jsonobject.getString("ListtkontakImageProfile");
                                if (listtkontakImage != "null") {
                                    JSONArray jsonDataUserMemberImage = jsonobject.getJSONArray("ListtkontakImageProfile");
                                    for(int j=0; j < jsonDataUserMemberImage.length(); j++) {
                                        JSONObject jsonobjectImage = jsonDataUserMemberImage.getJSONObject(j);
                                        String txtGuiID = jsonobjectImage.getString("TxtDataID");
                                        String txtKontakIDImage = jsonobjectImage.getString("TxtKontakID");
                                        String txtImageName = jsonobjectImage.getString("TxtImageName");
                                        String txtType = jsonobjectImage.getString("TxtType");

                                        clsUserMemberImage dataImage = new clsUserMemberImage();
                                        dataImage.setTxtGuiId(txtGuiID);
                                        dataImage.setTxtHeaderId(txtKontakIDImage);
                                        dataImage.setTxtPosition(txtType);

                                        String url = String.valueOf(jsonobjectImage.get("TxtPath"));
                                        if (!url.equals("null")) {
                                            linkImageProfile = url;
                                        }
                                        if (!linkImageProfile.equals("null")) {
                                            viewImageProfile();
                                        }

//                                        byte[] logoImage = getLogoImage(url);
//
//                                        if (logoImage != null) {
//                                            dataImage.setTxtImg(logoImage);
//
//                                            imageRepo = new clsUserMemberImageRepo(context.getApplicationContext());
//
//                                            int k = 0;
//                                            k = imageRepo.createOrUpdate(dataImage);
//                                            if(k > -1) {
//                                                Log.d("Data info", "Image " +txtType+ " Berhasil di update");
//                                                Log.d("Data info", "Data Member Image berhasil di update");
////                                    status = true;
//                                            }
//                                        }
                                    }
                                }
                            }
//                            new clsActivity().showCustomToast(context.getApplicationContext(), "Update Data, Success", true);
                        } else {
                            new clsActivity().showCustomToast(context.getApplicationContext(), warn, false);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    clsUserMemberRepo repo = new clsUserMemberRepo(context.getApplicationContext());
                    try {
                        dataMember = (List<clsUserMember>) repo.findAll();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    tvUsername.setText(dataMember.get(0).getTxtNama().toString());
                    tvPhone.setText(dataMember.get(0).getTxtNoTelp().toString());
                    tvEmail.setText(dataMember.get(0).getTxtEmail().toString());
                    tvAddress.setText(dataMember.get(0).getTxtAlamat().toString());
                    if (dataMember.get(0).getTxtBasePoin().equals("null")) {
                        tvBasePoint.setText("( Poin Kalbe Family : 0 )");
                    } else {
                        tvBasePoint.setText("( Poin Kalbe Family : " +dataMember.get(0).getTxtBasePoin()+ " )");
                    }

                    try {
                        repoUserImageProfile = new clsUserImageProfileRepo(context);
                        dataUserImageProfile = (List<clsUserImageProfile>) repoUserImageProfile.findAll();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
//                    if (dataUserImageProfile.size() > 0) {
//                        viewImageProfile();
//                    }
//                    if (!linkImageProfile.equals("null")) {
//                        viewImageProfile();
//                    }
                }
//                if(!status){
//                    new clsMainActivity().showCustomToast(getApplicationContext(), strErrorMsg, false);
//                }
            }
        });
    }

    public void UserMemberRefresh() {
        final ProgressDialog Dialog = new ProgressDialog(getActivity());
        RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());
        clsUserMemberRepo repo = new clsUserMemberRepo(context.getApplicationContext());
        try {
            dataMember = (List<clsUserMember>) repo.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        txtMember = dataMember.get(0).getTxtMemberId();
        String strLinkAPI = new clsHardCode().linkGetDetailCustomer;
//        String nameRole = selectedRole;
        JSONObject resJson = new JSONObject();

        try {
            resJson.put("txtMemberId", txtMember);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String mRequestBody = "[" + resJson.toString() + "]";

        volleyUserMember(strLinkAPI, mRequestBody, "Refresh Data...", new VolleyResponseListener() {
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
                                String txtKontakId = jsonobject.getString("TxtKontakId");
                                String memberID = jsonobject.getString("TxtMemberId");
                                String txtNama = jsonobject.getString("TxtNama");
                                String txtAlamat = jsonobject.getString("TxtAlamat");
                                String txtJenisKelamin = jsonobject.getString("TxtJenisKelamin");
                                String txtEmail = jsonobject.getString("TxtEmail");
                                String txtTelp = jsonobject.getString("TxtTelp");
                                String txtNoKTP = jsonobject.getString("TxtNoKTP");
                                String txtNamaDepan = jsonobject.getString("TxtNamaDepan");
                                String txtNamaBelakang = jsonobject.getString("TxtNamaKeluarga");
                                String txtNamaPanggilan = jsonobject.getString("TxtNamaPanggilan");
                                String intBasePoin = jsonobject.getString("IntBasePoin");
                                String txtTglAwal = jsonobject.getString("DtTglAwal");
                                String txtTglBerlaku = jsonobject.getString("TxttglBerlaku");

                                clsUserMember dataUser = new clsUserMember();
                                dataUser.setTxtKontakId(txtKontakId);
                                dataUser.setTxtMemberId(memberID);
                                dataUser.setTxtNama(txtNama);
                                dataUser.setTxtAlamat(txtAlamat);
                                dataUser.setTxtJenisKelamin(txtJenisKelamin);
                                dataUser.setTxtEmail(txtEmail);
                                dataUser.setTxtNoTelp(txtTelp);
                                dataUser.setTxtNoKTP(txtNoKTP);
                                dataUser.setTxtNamaDepan(txtNamaDepan);
                                dataUser.setTxtNamaBelakang(txtNamaBelakang);
                                dataUser.setTxtNamaPanggilan(txtNamaPanggilan);
                                dataUser.setTxtBasePoin(intBasePoin);
                                dataUser.setTxtTglAwal(txtTglAwal);
                                dataUser.setTxtTglBerlaku(txtTglBerlaku);

                                repoUserMember = new clsUserMemberRepo(context.getApplicationContext());
                                repoUserMember.createOrUpdate(dataUser);
                                Log.d("Data info", "Data Member berhasil di update");
//
                            }
//                            new clsActivity().showCustomToast(context.getApplicationContext(), "Update Data, Success", true);
                        } else {
                            new clsActivity().showCustomToast(context.getApplicationContext(), warn, false);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    clsUserMemberRepo repo = new clsUserMemberRepo(context.getApplicationContext());
                    try {
                        dataMember = (List<clsUserMember>) repo.findAll();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    tvUsername.setText(dataMember.get(0).getTxtNama().toString());
                    tvPhone.setText(dataMember.get(0).getTxtNoTelp().toString());
                    tvEmail.setText(dataMember.get(0).getTxtEmail().toString());
                    tvAddress.setText(dataMember.get(0).getTxtAlamat().toString());
                    if (dataMember.get(0).getTxtBasePoin().equals("null")) {
                        tvBasePoint.setText("( Poin Kalbe Family : 0 )");
                    } else {
                        tvBasePoint.setText("( Poin Kalbe Family : " +dataMember.get(0).getTxtBasePoin()+ " )");
                    }

                    try {
                        repoUserImageProfile = new clsUserImageProfileRepo(context);
                        dataUserImageProfile = (List<clsUserImageProfile>) repoUserImageProfile.findAll();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
//                    if (dataUserImageProfile.size() > 0) {
//                        viewImageProfile();
//                    }
                }
//                if(!status){
//                    new clsMainActivity().showCustomToast(getApplicationContext(), strErrorMsg, false);
//                }
            }
        });
    }

    private void volleyUserMember(String strLinkAPI, final String mRequestBody, String progressBarType, final VolleyResponseListener listener) {
        String client = "";
        RequestQueue queue = Volley.newRequestQueue(context);
        ProgressDialog Dialog = new ProgressDialog(getActivity());
        Dialog = ProgressDialog.show(getActivity(), "", progressBarType, false);

        final ProgressDialog finalDialog = Dialog;
        final ProgressDialog finalDialog1 = Dialog;

        mConfigRepo configRepo = new mConfigRepo(context);
        try {
            mConfigData configDataClient = (mConfigData) configRepo.findById(5);
            client = configDataClient.getTxtDefaultValue().toString();
            dataToken = (List<clsToken>) tokenRepo.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        final String finalClient = client;
        StringRequest req = new StringRequest(Request.Method.POST, strLinkAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Boolean status = false;
                String errorMessage = null;
                listener.onResponse(response, status, errorMessage);
                finalDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String strLinkRequestToken = new clsHardCode().linkToken;
                final String refresh_token = dataToken.get(0).txtRefreshToken;
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                    // HTTP Status Code: 401 Unauthorized
//                    new clsActivity().showCustomToast(context, "401, Authorization has been denied for this request", false);
                    new VolleyUtils().requestTokenWithRefresh(getActivity(), strLinkRequestToken, refresh_token, finalClient, new VolleyResponseListener() {
                        @Override
                        public void onError(String message) {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(String response, Boolean status, String strErrorMsg) {
                            if (response != null) {
                                try {
                                    String accessToken = "";
                                    String newRefreshToken = "";
                                    String refreshToken = "";
                                    JSONObject jsonObject = new JSONObject(response);
                                    accessToken = jsonObject.getString("access_token");
                                    refreshToken = jsonObject.getString("refresh_token");
                                    String dtIssued = jsonObject.getString(".issued");

                                    clsToken data = new clsToken();
                                    data.setIntId("1");
                                    data.setDtIssuedToken(dtIssued);
                                    data.setTxtUserToken(accessToken);
                                    data.setTxtRefreshToken(refreshToken);

                                    tokenRepo.createOrUpdate(data);
                                    //Toast.makeText(getApplicationContext(), "Success get new Access Token", Toast.LENGTH_SHORT).show();
                                    newRefreshToken = refreshToken;
                                    if (refreshToken == newRefreshToken && !newRefreshToken.equals("")) {
                                        UserMember();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(context, strErrorMsg, Toast.LENGTH_SHORT).show();
                            }
                            finalDialog1.dismiss();
                        }
                    });
                } else if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR ){
                    new clsActivity().showCustomToast(context, "Error 500, Server Error", false);
                    finalDialog1.dismiss();
                } else {
                    popup();
                    finalDialog1.dismiss();
                }
            }
            public void popup() {
                final SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
                dialog.setTitleText("Oops...");
                dialog.setContentText("Mohon check kembali koneksi internet anda");
                dialog.setCancelable(false);
                dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
//                        activity.startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK)); // turn on internet with wifi
//                        activity.startActivity(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS)); // turn on internet with mobile data
                        dialog.dismiss();
                        sweetAlertDialog.dismiss();

                        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
                        pDialog.setTitleText("Refresh Data ?");
                        pDialog.setContentText("");
                        pDialog.setConfirmText("Refresh");
                        pDialog.setCancelText("Keluar Aplikasi");
                        pDialog.showCancelButton(true);
                        pDialog.setCancelable(false);
                        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                UserMember();
                                sweetAlertDialog.dismiss();
                            }
                        });
                        pDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                pDialog.cancel();
                                getActivity().finish();
                            }
                        });
                        pDialog.show();
                    }
                });
                dialog.show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("txtParam", mRequestBody);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                try {
                    tokenRepo = new clsTokenRepo(context);
                    dataToken = (List<clsToken>) tokenRepo.findAll();
                    access_token = dataToken.get(0).getTxtUserToken();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + access_token);

                return headers;
            }
        };
        req.setRetryPolicy(new
                DefaultRetryPolicy(60000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        queue.add(req);
    }

    private void viewImageProfile() {
//        try {
//            repoUserImageProfile = new clsUserImageProfileRepo(context);
//            dataUserImageProfile = (List<clsUserImageProfile>) repoUserImageProfile.findAll();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        File folder = new File(Environment.getExternalStorageDirectory().toString() + "/data/data/KalbeFamily/tempdata/Foto_Profil");
//        folder.mkdir();

//        for (clsUserImageProfile imgDt : dataUserImageProfile){
//            final byte[] imgFile = imgDt.getTxtImg();
//            if (imgFile != null) {
//                mybitmapImageProfile = BitmapFactory.decodeByteArray(imgFile, 0, imgFile.length);
//                Bitmap bitmap = Bitmap.createScaledBitmap(mybitmapImageProfile, 150, 150, true);
//                ivProfile.setImageBitmap(bitmap);
//            }
//        }

        // view image from web API with picasso
        Picasso.with(getContext()).load(linkImageProfile)
                .placeholder(R.drawable.loading2)
                .error(R.drawable.profile)
//                .networkPolicy(NetworkPolicy.NO_CACHE)
//                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .fit()
                .into(ivProfile);
    }

}
