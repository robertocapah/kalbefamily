package kalbefamily.crm.kalbe.kalbefamily;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.owater.library.CircleTextView;

import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import kalbefamily.crm.kalbe.kalbefamily.BL.clsActivity;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserImageProfile;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserMember;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserMemberImage;
import kalbefamily.crm.kalbe.kalbefamily.Data.VolleyResponseListener;
import kalbefamily.crm.kalbe.kalbefamily.Data.VolleyUtils;
import kalbefamily.crm.kalbe.kalbefamily.Data.clsHardCode;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserImageProfileRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserMemberImageRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserMemberRepo;

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
    clsUserMemberRepo repoUserMember = null;
    clsUserMemberImageRepo imageRepo = null;
    clsUserImageProfileRepo repoUserImageProfile = null;
    private String txtMember;
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
//        ctvStatus = (CircleTextView) v.findViewById(R.id.status);
//        clsUserLoginData data = new clsUserLoginRepo(context).getDataLogin(context);
//        clsAbsenData dataAbsen = new clsAbsenDataRepo(context).getDataCheckinActive(context);

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

        if (dataUserImageProfile.size() > 0) {
            viewImageProfile();
        }

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                new clsActivity().zoomImage(mybitmap2, getActivity());
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserMember();
            }
        });
//        try {
//            new clsHelper().copydb(context.getApplicationContext());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        if (data != null){
//            tvUsername.setText(data.getTxtName().toString());
//            tvEmail.setText(data.getTxtEmail().toString());
//        }
//        if (dataAbsen!=null){
//            ctvStatus.setText("Checkin");
//        }else{
//            ctvStatus.setText("Checkout");
//        }

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

        new VolleyUtils().makeJsonObjectRequest(getActivity(), strLinkAPI, mRequestBody, "Updating Data, Please wait !", new VolleyResponseListener() {
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
                                String txtNamaKeluarga = jsonobject.getString("TxtNamaKeluarga");
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
                                dataUser.setTxtNamaKeluarga(txtNamaKeluarga);
                                dataUser.setTxtNamaPanggilan(txtNamaPanggilan);
                                dataUser.setTxtBasePoin(intBasePoin);
                                dataUser.setTxtTglAwal(txtTglAwal);
                                dataUser.setTxtTglBerlaku(txtTglBerlaku);

                                repoUserMember = new clsUserMemberRepo(context.getApplicationContext());
                                repoUserMember.createOrUpdate(dataUser);
//
//                                int h = 0;
//                                h = repoUserMember.createOrUpdate(dataUser);
//                                if(h > -1) {
//                                    Log.d("Data info", "Data Member berhasil di update");
////                                    status = true;
//                                }

                                String listtkontakImage = jsonobject.getString("ListtkontakImage");
                                if (listtkontakImage != "null") {
                                    JSONArray jsonDataUserMemberImage = jsonobject.getJSONArray("ListtkontakImage");
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

                                        byte[] logoImage = getLogoImage(url);

                                        if (logoImage != null) {
                                            dataImage.setTxtImg(logoImage);

                                            imageRepo = new clsUserMemberImageRepo(context.getApplicationContext());

                                            int k = 0;
                                            k = imageRepo.createOrUpdate(dataImage);
                                            if(k > -1) {
                                                Log.d("Data info", "Image " +txtType+ " Berhasil di update");
                                                Log.d("Data info", "Data Member Image berhasil di update");
//                                    status = true;
                                            }
                                        }
                                    }
                                }

                            }
                            new clsActivity().showCustomToast(context.getApplicationContext(), "Update Data, Success", true);
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
                        tvBasePoint.setText("( Base Point : 0 )");
                    } else {
                        tvBasePoint.setText("( Base Point : " +dataMember.get(0).getTxtBasePoin()+ " )");
                    }
                }
//                if(!status){
//                    new clsMainActivity().showCustomToast(getApplicationContext(), strErrorMsg, false);
//                }
            }
        });
    }

    private byte[] getLogoImage(String url) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            URL imageUrl = new URL(url);
            URLConnection ucon = imageUrl.openConnection();
            String contentType = ucon.getHeaderField("Content-Type");
            boolean image = contentType.startsWith("image/");

            if (image) {
                InputStream is = ucon.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);

                ByteArrayBuffer baf = new ByteArrayBuffer(500);
                int current = 0;
                while ((current = bis.read()) != -1) {
                    baf.append((byte) current);
                }

                return baf.toByteArray();
            } else {
                return null;
            }
        } catch (Exception e) {
            Log.d("ImageManager", "Error: " + e.toString());
        }
        return null;
    }

    private void viewImageProfile() {
        try {
            repoUserImageProfile = new clsUserImageProfileRepo(context);
            dataUserImageProfile = (List<clsUserImageProfile>) repoUserImageProfile.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        File folder = new File(Environment.getExternalStorageDirectory().toString() + "/data/data/KalbeFamily/tempdata/Foto_Profil");
        folder.mkdir();

        for (clsUserImageProfile imgDt : dataUserImageProfile){
            final byte[] imgFile = imgDt.getTxtImg();
            if (imgFile != null) {
                mybitmapImageProfile = BitmapFactory.decodeByteArray(imgFile, 0, imgFile.length);
                Bitmap bitmap = Bitmap.createScaledBitmap(mybitmapImageProfile, 150, 150, true);
                ivProfile.setImageBitmap(bitmap);
            }
        }
    }

}
