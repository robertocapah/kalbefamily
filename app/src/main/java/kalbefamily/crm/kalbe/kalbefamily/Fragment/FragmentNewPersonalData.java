package kalbefamily.crm.kalbe.kalbefamily.Fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpStatus;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import kalbefamily.crm.kalbe.kalbefamily.BL.clsActivity;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsJenisMedia;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsMediaKontakDetail;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsMediaType;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsSendData;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsToken;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserImageProfile;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserMember;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserMemberImage;
import kalbefamily.crm.kalbe.kalbefamily.Common.mConfigData;
import kalbefamily.crm.kalbe.kalbefamily.Data.DatabaseHelper;
import kalbefamily.crm.kalbe.kalbefamily.Data.DatabaseManager;
import kalbefamily.crm.kalbe.kalbefamily.Data.VolleyResponseListener;
import kalbefamily.crm.kalbe.kalbefamily.Data.VolleyUtils;
import kalbefamily.crm.kalbe.kalbefamily.Data.clsHardCode;
import kalbefamily.crm.kalbe.kalbefamily.Data.clsHelper;
import kalbefamily.crm.kalbe.kalbefamily.HomeMenu;
import kalbefamily.crm.kalbe.kalbefamily.R;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsJenisMediaRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsMediaKontakDetailRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsMediaTypeRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsTokenRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserImageProfileRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserMemberImageRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserMemberRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.mConfigRepo;
import kalbefamily.crm.kalbe.kalbefamily.ViewPagerActivity;
import kalbefamily.crm.kalbe.kalbefamily.addons.volley.VolleyMultipartRequest;

import static android.app.Activity.RESULT_OK;
import static com.android.volley.VolleyLog.TAG;

/**
 * Created by Rian Andrivani on 7/20/2017.
 */

public class FragmentNewPersonalData extends Fragment implements AdapterView.OnItemSelectedListener{
    View v;
    TextView tvNama, tvMember, etNamaDepan, etNamaBelakang, etNamaPanggilan, etEmail, etTelpon, etAlamat, etNoKTP, tvKategori;
    RadioButton radioPria, radiowanita;
    RadioGroup radioGenderGroup;
    Button btnUpdate, btnDetail;
    private ImageView image1, image2;
    CircleImageView ivProfile;
    Context context;
    Spinner spinner;
    String imageProfile = "null";
    String noKTPSementara, access_token, txtValidationMethod;
//    String linkImage1 = "null";
//    String linkImage2 = "null";
    private Uri uriImage, selectedImage;
    DatabaseHelper helper = DatabaseManager.getInstance().getHelper();

    private static final int CAMERA_REQUEST_PROFILE = 120;
    private static final int CAMERA_REQUEST = 1888;
    private static final int CAMERA_REQUEST2 = 1889;
    private static final String IMAGE_DIRECTORY_NAME = "Image Personal";

    private static Bitmap photoImage1, photoImage2, photoProfile;
    private static ByteArrayOutputStream output = new ByteArrayOutputStream();
    private static byte[] phtProfile;
    private static byte[] phtImage1;
    private static byte[] phtImage2;
    private static Bitmap mybitmap1;
    private static Bitmap mybitmap2;
    private static Bitmap mybitmapImageProfile;
    private Bitmap bitmap;
    private Bitmap bitmap2;
    final int PIC_CROP = 2;
    final int PIC_CROP2 = 3;
    final int SELECT_FILE = 1;
    final int SELECT_FILE2 = 4;
    final int PIC_CROP_PROFILE = 5;
    final int SELECT_FILE_PROFILE = 6;

    List<clsUserMember> dataMember = null;
    List<clsUserMemberImage> dataMemberImage = null;
    List<clsUserImageProfile> dataUserImageProfile = null;
    clsUserMemberRepo repoUserMember = null;
    clsUserMemberImageRepo repoUserMemberImage = null;
    clsUserImageProfileRepo repoUserImageProfile = null;
    clsMediaKontakDetailRepo repoKontakDetail;
    clsMediaTypeRepo mediaTypeRepo;
    clsJenisMediaRepo jenisMediaRepo;
    clsTokenRepo tokenRepo;
    List<clsToken> dataToken;
    clsUserMemberImage dtImage;
    clsUserImageProfile dtImageProfile;
    boolean validate = false;
    boolean validate_2 = false;
    private String txtKontakID, txtMember;
    Bitmap thePic, thePic2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_new_personal_data,container,false);
        context = getActivity().getApplicationContext();

        ivProfile = (CircleImageView) v.findViewById(R.id.profile_image_personal);
        tvNama = (TextView) v.findViewById(R.id.tvNama);
        tvMember = (TextView) v.findViewById(R.id.tvMember);
        etNamaDepan = (TextView) v.findViewById(R.id.textViewNamaKeluarga);
        etNamaBelakang = (TextView) v.findViewById(R.id.textViewNamaBelakang);
        etNamaPanggilan = (TextView) v.findViewById(R.id.textViewNamaPanggilan);
        etEmail = (TextView) v.findViewById(R.id.textViewEmail);
        etTelpon = (TextView) v.findViewById(R.id.textVuewNoTelp);
        etAlamat = (TextView) v.findViewById(R.id.textViewAlamat);
        radioGenderGroup = (RadioGroup) v.findViewById(R.id.radioGroupGender);
        radioPria = (RadioButton) v.findViewById(R.id.radioButton1);
        radiowanita = (RadioButton) v.findViewById(R.id.radioButton2);
        etNoKTP = (TextView) v.findViewById(R.id.textViewNoKTP);
        btnUpdate = (Button) v.findViewById(R.id.btnUpdate);
        btnDetail = (Button) v.findViewById(R.id.btnDetail);
        image1 = (ImageView) v.findViewById(R.id.image1);
        image2 = (ImageView) v.findViewById(R.id.image2);
        spinner = (Spinner) v.findViewById(R.id.spinnerContact);
        tvKategori = (TextView) v.findViewById(R.id.textViewKategori);

        // receive data from activity to fragment in fragment class
        imageProfile = getArguments().getString("imageProfile");

        try {
            repoUserMember = new clsUserMemberRepo(context);
            tokenRepo = new clsTokenRepo(context);
            dataMember = (List<clsUserMember>) repoUserMember.findAll();
            dataToken = (List<clsToken>) tokenRepo.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tvNama.setText(dataMember.get(0).getTxtNama().toString());

//        String sub, sub2, sub3;
//        final String member1 = dataMember.get(0).getTxtMemberId().toString();
//        sub = member1.substring(0, member1.length() - 8);
//        sub2 = member1.substring(4, member1.length() - 4);
//        sub3 = member1.substring(8, member1.length());

        String sub, sub2, sub3 , sub4;
        final String member1 = dataMember.get(0).getTxtMemberId();
        if(member1.length() == 16){
            sub = member1.substring(0, 4);
            sub2 = member1.substring(4, 8);
            sub3 = member1.substring(8, 12);
            sub4 = member1.substring(12, 16);
            tvMember.setText(sub +" "+ sub2 +" "+ sub3+" "+sub4);
        }else if(member1.length() == 12){
            sub = member1.substring(0, 4);
            sub2 = member1.substring(4, 8);
            sub3 = member1.substring(8, 12);
            tvMember.setText(sub +" "+ sub2 +" "+ sub3);
        }

//        tvMember.setText(sub +" "+ sub2 +" "+ sub3);

        if (dataMember.get(0).getTxtNamaDepan().toString().equals("null")) {
            etNamaDepan.setText("");
        } else {
            etNamaDepan.setText(dataMember.get(0).getTxtNamaDepan().toString());
        }

        if (dataMember.get(0).getTxtNamaBelakang().toString().equals("null")) {
            etNamaBelakang.setText("");
        } else {
            etNamaBelakang.setText(dataMember.get(0).getTxtNamaBelakang().toString());
        }

        if (dataMember.get(0).getTxtNamaPanggilan().toString().equals("null")) {
            etNamaPanggilan.setText("");
        } else {
            etNamaPanggilan.setText(dataMember.get(0).getTxtNamaPanggilan());
        }

        etEmail.setText(dataMember.get(0).getTxtEmail().toString());
        etTelpon.setText(dataMember.get(0).getTxtNoTelp().toString());
        etAlamat.setText(dataMember.get(0).getTxtAlamat().toString());

        if (dataMember.get(0).txtJenisKelamin.equals("Perempuan")) {
            radioPria.setChecked(false);
            radiowanita.setChecked(true);
        } else {
            radioPria.setChecked(true);
            radiowanita.setChecked(false);
        }

        if (dataMember.get(0).getTxtNoKTP().toString().equals("null")) {
            etNoKTP.setText("");
        } else {
            etNoKTP.setText(dataMember.get(0).getTxtNoKTP().toString());
        }

        tvKategori.setText("(LINE)");

        etNamaDepan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setMessage("Masukan Nama Depan");

                // Layout Dynamic
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(25, 20, 25, 10);

                final EditText input = new EditText(context);
                input.setTextColor(Color.BLACK);
                input.setText(etNamaDepan.getText().toString());
                input.setHint("Nama Depan");
                layout.addView(input, layoutParams);

                alert.setView(layout);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Toast.makeText(context,"Success",Toast.LENGTH_LONG).show();
                        etNamaDepan.setText(input.getText().toString());
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            }
        });

        etNamaBelakang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setMessage("Masukan Nama Belakang");

                // Layout Dynamic
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(25, 20, 25, 10);

                final EditText input = new EditText(context);
                input.setTextColor(Color.BLACK);
                input.setText(etNamaBelakang.getText().toString());
                input.setHint("Nama Belakang");
                layout.addView(input, layoutParams);

                alert.setView(layout);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        etNamaBelakang.setText(input.getText().toString());
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            }
        });

        etNamaPanggilan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setMessage("Masukan Nama Pangglan");

                // Layout Dynamic
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(25, 20, 25, 10);

                final EditText input = new EditText(context);
                input.setTextColor(Color.BLACK);
                input.setText(etNamaPanggilan.getText().toString());
                input.setHint("Nama Panggilan");
                layout.addView(input, layoutParams);

                alert.setView(layout);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        etNamaPanggilan.setText(input.getText().toString());
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            }
        });

        etAlamat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setMessage("Masukan Alamat Anda");

                // Layout Dynamic
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(25, 20, 25, 10);

                final EditText input = new EditText(context);
                input.setTextColor(Color.BLACK);
                input.setText(etAlamat.getText().toString());
                input.setHint(" Alamat");
                layout.addView(input, layoutParams);

                alert.setView(layout);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        etAlamat.setText(input.getText().toString());
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            }
        });

        etNoKTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImageFromAPI();

//                try {
//                    repoUserMemberImage = new clsUserMemberImageRepo(context);
//                    dataMemberImage = (List<clsUserMemberImage>) repoUserMemberImage.findAll();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//                if (dataMemberImage.size() == 0) {
//                    getImageFromAPI();
//                } else {
//                    final ProgressDialog dialog2 = new ProgressDialog(getActivity(), ProgressDialog.STYLE_SPINNER);
//                    dialog2.setIndeterminate(true);
//                    dialog2.setMessage("Mohon Tunggu...");
//                    dialog2.show();
//
//                    new android.os.Handler().postDelayed(
//                            new Runnable() {
//                                public void run() {
//                                    // On complete call either onLoginSuccess or onLoginFailed
//                                    showDialog();
//                                    // onLoginFailed();
//                                    dialog2.dismiss();
//                                }
//                            }, 2500);
//                }
            }
        });

        try {
            repoUserMemberImage = new clsUserMemberImageRepo(context);
            dataMemberImage = (List<clsUserMemberImage>) repoUserMemberImage.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            repoUserImageProfile = new clsUserImageProfileRepo(context);
            dataUserImageProfile = (List<clsUserImageProfile>) repoUserImageProfile.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (!imageProfile.equals("null")) {
            viewImageProfile();
        }
//        if (dataUserImageProfile.size() > 0) {
//            viewImageProfile();
//        }

//        if (dataMemberImage.size() > 0) {
//            viewImage();
//        }

        phtImage1 = null;
        phtImage2 = null;
        phtProfile = null;

        if (photoImage1 != null){
            image1.setImageBitmap(photoImage1);
            photoImage1.compress(Bitmap.CompressFormat.PNG, 100, output);
            phtImage1 = output.toByteArray();
        }

        if (photoImage2 != null){
            image2.setImageBitmap(photoImage2);
            photoImage2.compress(Bitmap.CompressFormat.PNG, 100, output);
            phtImage2 = output.toByteArray();
        }

        if (photoProfile != null) {
            ivProfile.setImageBitmap(photoProfile);
            photoProfile.compress(Bitmap.CompressFormat.PNG, 100, output);
            phtImage2 = output.toByteArray();
        }

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                captureImage1();
                selectImage1();
            }
        });

        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                captureImage2();
                selectImage2();
            }
        });

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImageProfile();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("Konfirmasi");
                alertDialog.setMessage("Apakah Anda yakin?");
                alertDialog.setPositiveButton("SIMPAN", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clsUserMember dataUser = new clsUserMember();
                        dataUser.setTxtKontakId(dataMember.get(0).getTxtKontakId().toString());
                        dataUser.setTxtMemberId(member1);
                        dataUser.setTxtNama(tvNama.getText().toString());
                        dataUser.setTxtNamaDepan(etNamaDepan.getText().toString());
                        dataUser.setTxtAlamat(etAlamat.getText().toString());
                        dataUser.setTxtNoKTP(etNoKTP.getText().toString());
                        dataUser.setTxtNamaPanggilan(etNamaPanggilan.getText().toString());
                        dataUser.setTxtNamaBelakang(etNamaBelakang.getText().toString());
                        dataUser.setTxtBasePoin(dataMember.get(0).getTxtBasePoin().toString());

                        dataUser.setTxtEmail(etEmail.getText().toString());
                        dataUser.setTxtNoTelp(etTelpon.getText().toString());

                        int selectedId = radioGenderGroup.getCheckedRadioButtonId();
                        RadioButton rbGender = (RadioButton) v.findViewById(selectedId);

                        dataUser.setTxtJenisKelamin(rbGender.getText().toString());

                        repoUserMember = new clsUserMemberRepo(context.getApplicationContext());
                        repoUserMember.createOrUpdate(dataUser);

                        savePictureProfile();
                        sendData();

                    }
                });
                alertDialog.setNegativeButton("BATAL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                alertDialog.show();
            }
        });

        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clsUserMember dataUser = new clsUserMember();
                dataUser.setTxtKontakId(dataMember.get(0).getTxtKontakId().toString());
                dataUser.setTxtMemberId(member1);
                dataUser.setTxtNama(tvNama.getText().toString());
                dataUser.setTxtNamaDepan(etNamaDepan.getText().toString());
                dataUser.setTxtAlamat(etAlamat.getText().toString());
                dataUser.setTxtNoKTP(etNoKTP.getText().toString());
                dataUser.setTxtNamaPanggilan(etNamaPanggilan.getText().toString());
                dataUser.setTxtNamaBelakang(etNamaBelakang.getText().toString());
                dataUser.setTxtBasePoin(dataMember.get(0).getTxtBasePoin().toString());

                dataUser.setTxtEmail(etEmail.getText().toString());
                dataUser.setTxtNoTelp(etTelpon.getText().toString());

                int selectedId = radioGenderGroup.getCheckedRadioButtonId();
                RadioButton rbGender = (RadioButton) v.findViewById(selectedId);

                dataUser.setTxtJenisKelamin(rbGender.getText().toString());

                repoUserMember = new clsUserMemberRepo(context.getApplicationContext());
                repoUserMember.createOrUpdate(dataUser);

                savePictureProfile();

                // update username and profile image @navigation menu
                TextView tvUsername = (TextView) getActivity().findViewById(R.id.username);
                CircleImageView photoMenu = (CircleImageView) getActivity().findViewById(R.id.profile_image);

                tvUsername.setText(etNamaDepan.getText().toString());
                try {
                    repoUserImageProfile = new clsUserImageProfileRepo(context);
                    dataUserImageProfile = (List<clsUserImageProfile>) repoUserImageProfile.findAll();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                for (clsUserImageProfile imgDt : dataUserImageProfile){
                    final byte[] imgFile = imgDt.getTxtImg();
                    if (imgFile != null) {
                        mybitmapImageProfile = BitmapFactory.decodeByteArray(imgFile, 0, imgFile.length);
                        Bitmap bitmapProfile = Bitmap.createScaledBitmap(mybitmapImageProfile, 150, 150, true);
                        photoMenu.setImageBitmap(bitmapProfile);
                    }
                }

                sendDataForwardMediaKomunikasi();
            }
        });

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("No Telp");
        categories.add("No HP");
        categories.add("WhatsApp");
        categories.add("Line");
        categories.add("BBM");
        categories.add("Facebook");
        categories.add("Twitter");

        // Creating adapter for spinnerTelp
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context.getApplicationContext(), android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinnerTelp
        spinner.setAdapter(dataAdapter);

        return v;
    }

    private void viewImage() {
        try {
            repoUserMemberImage = new clsUserMemberImageRepo(context);
            dataMemberImage = (List<clsUserMemberImage>) repoUserMemberImage.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        File folder = new File(Environment.getExternalStorageDirectory().toString() + "/data/data/KalbeFamily/tempdata/FotoKTP");
        folder.mkdir();

        for (clsUserMemberImage imgDt : dataMemberImage){
            final byte[] imgFile = imgDt.getTxtImg();
            if (imgFile != null) {
                if (imgDt.getTxtPosition().equals("txtFileName1")) {
                    mybitmap1 = BitmapFactory.decodeByteArray(imgFile, 0, imgFile.length);
                    Bitmap bitmap = Bitmap.createScaledBitmap(mybitmap1, 150, 150, true);
                    image1.setImageBitmap(bitmap);

//                    File file = null;
//                    try {
//                        file = File.createTempFile("image-", ".jpg", new File(Environment.getExternalStorageDirectory().toString() + "/data/data/KalbeFamily/tempdata/FotoKTP"));
//                        FileOutputStream out = new FileOutputStream(file);
//                        out.write(imgFile);
//                        out.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                }
            }

            final byte[] imgFile2 = imgDt.getTxtImg();
            if (imgFile2 != null) {
                if (imgDt.getTxtPosition().equals("txtFileName2")) {
                    mybitmap2 = BitmapFactory.decodeByteArray(imgFile2, 0, imgFile2.length);
                    Bitmap bitmap = Bitmap.createScaledBitmap(mybitmap2, 150, 150, true);
                    image2.setImageBitmap(bitmap);

//                    File file = null;
//                    try {
//                        file = File.createTempFile("image-", ".jpg", new File(Environment.getExternalStorageDirectory().toString() + "/data/data/KalbeFamily/tempdata/FotoKTP"));
//                        FileOutputStream out = new FileOutputStream(file);
//                        out.write(imgFile2);
//                        out.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                }
            }
        }
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
//
//        for (clsUserImageProfile imgDt : dataUserImageProfile){
//            final byte[] imgFile = imgDt.getTxtImg();
//            if (imgFile != null) {
//                mybitmapImageProfile = BitmapFactory.decodeByteArray(imgFile, 0, imgFile.length);
//                Bitmap bitmap = Bitmap.createScaledBitmap(mybitmapImageProfile, 150, 150, true);
//                ivProfile.setImageBitmap(bitmap);
//            }
//        }
        final ProgressDialog dialog2 = new ProgressDialog(getActivity(), ProgressDialog.STYLE_SPINNER);
        dialog2.setIndeterminate(true);
        dialog2.setMessage("Refresh Data...");
        dialog2.setCancelable(false);
        dialog2.show();

        Picasso.with(getContext()).load(imageProfile)
                .placeholder(R.drawable.profile)
                .error(R.drawable.profile)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .fit()
                .into(ivProfile, new Callback() {
                    @Override
                    public void onSuccess() {
                        dialog2.dismiss();
                    }

                    @Override
                    public void onError() {
                        new clsActivity().showToast(context.getApplicationContext(), "Error Loading Image", false);
                        dialog2.dismiss();
                    }
                });
    }

    // put image from camera
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST) {
            if (resultCode == -1) {
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    String uri = uriImage.getPath().toString();

                    bitmap = BitmapFactory.decodeFile(uri, bitmapOptions);

                    // crop image
                    performCrop();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (resultCode == 0) {
                new clsActivity().showCustomToast(getContext(), "User batal mengambil gambar", false);
            }  else {
                try {
                    photoImage1 = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), data.getData());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == PIC_CROP) {
            if (resultCode == -1) {
                //get the returned data
                Bundle extras = data.getExtras();
                //get the cropped bitmap
                Uri uri = data.getData();
                if (uri != null){
                    thePic = decodeUriAsBitmap(uri);
                }
                if  (extras != null){
                    Bitmap tempBitm = extras.getParcelable("data");
                    if (tempBitm != null){
                        thePic = tempBitm;
                    }
                }
                validate = true;
//                previewCaptureImage1(thePic);
                dialogPopupImage(thePic);
            } else if (resultCode == 0) {
                new clsActivity().showCustomToast(getContext(), "User batal mengambil gambar", false);
            }
        }

        else if (requestCode == CAMERA_REQUEST2) {
            if (resultCode == -1) {
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    String uri = uriImage.getPath().toString();

                    bitmap = BitmapFactory.decodeFile(uri, bitmapOptions);

                    performCrop2();

//                    previewCaptureImage2(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (resultCode == 0) {
                new clsActivity().showCustomToast(getContext(), "User batal mengambil gambar", false);
            }  else {
                try {
                    photoImage2 = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), data.getData());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else if (requestCode == PIC_CROP2) {
            if (resultCode == -1) {
                //get the returned data
                Bundle extras = data.getExtras();
                //get the cropped bitmap
                Uri uri = data.getData();
                if (uri != null){
                    thePic2 = decodeUriAsBitmap(uri);
                }
                if  (extras != null){
                    Bitmap tempBitm = extras.getParcelable("data");
                    if (tempBitm != null){
                        thePic2 = tempBitm;
                    }
                }
                validate_2 = true;

//                previewCaptureImage2(thePic2);
                dialogPopupImage(thePic2);
            } else if (resultCode == 0) {
                new clsActivity().showCustomToast(getContext(), "User batal mengambil gambar", false);
            }
        }

        else if (requestCode == CAMERA_REQUEST_PROFILE) {
            if (resultCode == -1) {
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    String uri = uriImage.getPath().toString();

                    bitmap = BitmapFactory.decodeFile(uri, bitmapOptions);

                    performCropProfile();

//                    previewCaptureImage2(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (resultCode == 0) {
                new clsActivity().showCustomToast(getContext(), "User batal mengambil gambar", false);
            }  else {
                try {
                    photoProfile = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), data.getData());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else if (requestCode == PIC_CROP_PROFILE) {
            if (resultCode == -1) {
                //get the returned data
                Bundle extras = data.getExtras();
                //get the cropped bitmap
                Bitmap thePic = null;
                Uri uri = data.getData();
                if (uri != null){
                    thePic = decodeUriAsBitmap(uri);
                }
                if  (extras != null){
                    Bitmap tempBitm = extras.getParcelable("data");
                    if (tempBitm != null){
                        thePic = tempBitm;
                    }
                }

                previewCaptureImageProfile(thePic);
            } else if (resultCode == 0) {
                new clsActivity().showCustomToast(getContext(), "User batal mengambil gambar", false);
            }
        }

        else if (requestCode == SELECT_FILE) {
            if(resultCode == RESULT_OK){
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    selectedImage = data.getData();
                    String uri = selectedImage.getPath().toString();
                    bitmap = BitmapFactory.decodeFile(uri, bitmapOptions);

                    performCropGallery();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == SELECT_FILE2) {
            if(resultCode == RESULT_OK){
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    selectedImage = data.getData();
                    String uri = selectedImage.getPath().toString();
                    bitmap = BitmapFactory.decodeFile(uri, bitmapOptions);

                    performCropGallery2();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == SELECT_FILE_PROFILE) {
            if(resultCode == RESULT_OK){
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    selectedImage = data.getData();
                    String uri = selectedImage.getPath().toString();
                    bitmap = BitmapFactory.decodeFile(uri, bitmapOptions);

                    performCropGalleryProfile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // preview image 1
    private void previewCaptureImage1(Bitmap photo){
        try {
            Bitmap bitmap = new clsActivity().resizeImageForBlob(photo);
            image1.setVisibility(View.VISIBLE);
            output = null;
            try {
                output = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 0, output);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (output != null){
                        output.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Bitmap photo_view = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
            phtImage1 = output.toByteArray();
            image1.setImageBitmap(photo_view);

            if (dtImage == null){
                dtImage.setTxtImg(phtImage1);
            } else {
                dtImage.setTxtImg(phtImage1);
            }
            repoUserMemberImage = new clsUserMemberImageRepo(context.getApplicationContext());

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    // preview image 2
    private void previewCaptureImage2(Bitmap photo){
        try {
            Bitmap bitmap = new clsActivity().resizeImageForBlob(photo);
            image2.setVisibility(View.VISIBLE);
            output = null;
            try {
                output = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 0, output);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (output != null){
                        output.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Bitmap photo_view = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
            phtImage2 = output.toByteArray();
            image2.setImageBitmap(photo_view);

            if (dtImage == null){
                dtImage.setTxtImg(phtImage2);
            } else {
                dtImage.setTxtImg(phtImage2);
            }
            repoUserMemberImage = new clsUserMemberImageRepo(context.getApplicationContext());

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    // preview image profile
    private void previewCaptureImageProfile(Bitmap photo){
        try {
            Bitmap bitmap = new clsActivity().resizeImageForBlob(photo);
            ivProfile.setVisibility(View.VISIBLE);
            output = null;
            try {
                output = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 0, output);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (output != null){
                        output.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Bitmap photo_view = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
            phtProfile = output.toByteArray();
            ivProfile.setImageBitmap(photo_view);

//            if (dtImageProfile == null){
//                dtImageProfile.setTxtImg(phtProfile);
//            } else {
//                dtImageProfile.setTxtImg(phtProfile);
//            }
            repoUserImageProfile = new clsUserImageProfileRepo(context.getApplicationContext());

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }

    private File getOutputMediaFile() {
        // External sdcard location

        File mediaStorageDir = new File(new clsHardCode().txtFolderData + File.separator);
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Failed create " + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "tmp_act"  + ".png");
        return mediaFile;
    }

    protected void captureImage1() {
        uriImage = getOutputMediaFileUri();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriImage);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    protected void captureImage2() {
        uriImage = getOutputMediaFileUri();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriImage);
        startActivityForResult(cameraIntent, CAMERA_REQUEST2);
    }

    protected void captureImageProfile() {
        uriImage = getOutputMediaFileUri();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriImage);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_PROFILE);
    }

    protected void savePicture1() {
        try {
            repoUserMemberImage = new clsUserMemberImageRepo(context);
            dataMemberImage = (List<clsUserMemberImage>) repoUserMemberImage.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (phtImage2 == null){
            helper.refreshData2();
        }

        if (dataMemberImage.size() == 0) {
            if (phtImage1 != null) {
                clsUserMemberImage dataImage = new clsUserMemberImage();

                dataImage.setTxtGuiId(new clsActivity().GenerateGuid());
                dataImage.setTxtHeaderId(dataMember.get(0).txtKontakId);
                dataImage.setTxtImg(phtImage1);
                dataImage.setTxtPosition("txtFileName1");

                repoUserMemberImage = new clsUserMemberImageRepo(context.getApplicationContext());
                repoUserMemberImage.createOrUpdate(dataImage);
            }
        } else {
            for (clsUserMemberImage imgDt : dataMemberImage) {
                clsUserMemberImage dataImage = new clsUserMemberImage();

                if (phtImage1 != null) {
                    if (imgDt.getTxtPosition().equals("txtFileName1")) {
                        dataImage.setTxtGuiId(imgDt.getTxtGuiId().toString());
                        dataImage.setTxtHeaderId(dataMember.get(0).txtKontakId);
                        dataImage.setTxtImg(phtImage1);
                        dataImage.setTxtPosition("txtFileName1");

                        repoUserMemberImage = new clsUserMemberImageRepo(context.getApplicationContext());
                        repoUserMemberImage.createOrUpdate(dataImage);
                    } else {
                        dataImage.setTxtGuiId(new clsActivity().GenerateGuid());
                        dataImage.setTxtHeaderId(dataMember.get(0).txtKontakId);
                        dataImage.setTxtImg(phtImage1);
                        dataImage.setTxtPosition("txtFileName1");

                        repoUserMemberImage = new clsUserMemberImageRepo(context.getApplicationContext());
                        repoUserMemberImage.createOrUpdate(dataImage);
                    }
                } else if (phtImage1 == null){
                    helper.refreshData2();
                }
            }
        }
    }

    protected void savePicture2() {
        try {
            repoUserMemberImage = new clsUserMemberImageRepo(context);
            dataMemberImage = (List<clsUserMemberImage>) repoUserMemberImage.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (dataMemberImage.size() == 0) {
            if (phtImage2 != null) {
                clsUserMemberImage dataImage = new clsUserMemberImage();

                dataImage.setTxtGuiId(new clsActivity().GenerateGuid());
                dataImage.setTxtHeaderId(dataMember.get(0).txtKontakId);
                dataImage.setTxtImg(phtImage2);
                dataImage.setTxtPosition("txtFileName2");

                repoUserMemberImage = new clsUserMemberImageRepo(context.getApplicationContext());
                repoUserMemberImage.createOrUpdate(dataImage);
            }
        } else {
            for (clsUserMemberImage imgDt : dataMemberImage) {
                clsUserMemberImage dataImage = new clsUserMemberImage();

                if (phtImage2 != null) {
                    if (imgDt.getTxtPosition().equals("txtFileName2")) {
                        dataImage.setTxtGuiId(imgDt.getTxtGuiId().toString());
                        dataImage.setTxtHeaderId(dataMember.get(0).txtKontakId);
                        dataImage.setTxtImg(phtImage2);
                        dataImage.setTxtPosition("txtFileName2");

                        repoUserMemberImage = new clsUserMemberImageRepo(context.getApplicationContext());
                        repoUserMemberImage.createOrUpdate(dataImage);
                    } else {
                        dataImage.setTxtGuiId(new clsActivity().GenerateGuid());
                        dataImage.setTxtHeaderId(dataMember.get(0).txtKontakId);
                        dataImage.setTxtImg(phtImage2);
                        dataImage.setTxtPosition("txtFileName2");

                        repoUserMemberImage = new clsUserMemberImageRepo(context.getApplicationContext());
                        repoUserMemberImage.createOrUpdate(dataImage);
                    }
                }
            }
        }

    }

    protected void savePictureProfile() {
        try {
            repoUserImageProfile = new clsUserImageProfileRepo(context);
            dataUserImageProfile = (List<clsUserImageProfile>) repoUserImageProfile.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (dataUserImageProfile.size() == 0) {
            if (phtProfile != null) {
                clsUserImageProfile dataImageProfile = new clsUserImageProfile();

                dataImageProfile.setTxtGuiId(new clsActivity().GenerateGuid());
                dataImageProfile.setTxtKontakId(dataMember.get(0).txtKontakId);
                dataImageProfile.setTxtImg(phtProfile);

                repoUserImageProfile = new clsUserImageProfileRepo(context.getApplicationContext());
                repoUserImageProfile.createOrUpdate(dataImageProfile);
            }
        } else {
            for (clsUserImageProfile imgProfile : dataUserImageProfile) {
                if (phtProfile != null) {
                    clsUserImageProfile dataImageProfile = new clsUserImageProfile();
                    dataImageProfile.setTxtGuiId(imgProfile.getTxtGuiId().toString());
                    dataImageProfile.setTxtKontakId(dataMember.get(0).txtKontakId);
                    dataImageProfile.setTxtImg(phtProfile);

                    repoUserImageProfile = new clsUserImageProfileRepo(context.getApplicationContext());
                    repoUserImageProfile.createOrUpdate(dataImageProfile);
                }
            }
        }
    }

    private void sendData() {
        String versionName = "";
        clsSendData dtJson = new clsHelper().sendData(versionName, context.getApplicationContext());
        if (dtJson != null) {
            try {
                String strLinkAPI = new clsHardCode().linkSendData;
                final String mRequestBody = "[" + dtJson.toString() + "]";

                volleyRequestSendData(strLinkAPI, dtJson, new VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        String error;
                    }

                    @Override
                    public void onResponse(String response, Boolean status, String strErrorMsg) {
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);
                            JSONObject jsn = jsonObject1.getJSONObject("validJson");
                            String warn = jsn.getString("TxtMessage");
                            String result = jsn.getString("IntResult");
                            String res = response;

                            if (result.equals("1")) {
                                new clsActivity().showToast(context.getApplicationContext(), "Saved", true);
                                Intent intent = new Intent(context.getApplicationContext(), HomeMenu.class);
                                getActivity().finish();
                                startActivity(intent);
                            } else {
                                new clsActivity().showToast(context.getApplicationContext(), warn, false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.i(TAG, "Ski data from server - " + response);
                    }
                });
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void sendDataForwardMediaKomunikasi() {
        String versionName = "";
        clsSendData dtJson = new clsHelper().sendData(versionName, context.getApplicationContext());
        if (dtJson != null) {
            try {
                String strLinkAPI = new clsHardCode().linkSendData;
                final String mRequestBody = "[" + dtJson.toString() + "]";

                volleyRequestSendData(strLinkAPI, dtJson, new VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        String error;
                    }

                    @Override
                    public void onResponse(String response, Boolean status, String strErrorMsg) {
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);
                            JSONObject jsn = jsonObject1.getJSONObject("validJson");
                            String warn = jsn.getString("TxtMessage");
                            String result = jsn.getString("IntResult");
                            String res = response;

                            if (result.equals("1")) {
//                                new clsActivity().showCustomToast(context.getApplicationContext(), "Saved", true);
                                Log.d("Personal Data Status", "Saved");
                                mediaType();
                                jenisMedia();
                                kontakDetail();
                            } else {
//                                new clsActivity().showCustomToast(context.getApplicationContext(), warn, false);
                                popup();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.i(TAG, "Ski data from server - " + response);
                        clsUserMember userMemberData = new clsUserMember();
                    }
                });
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void sendDataImageKTP() {
        String versionName = "";
        clsSendData dtJson = new clsHelper().sendData(versionName, context.getApplicationContext());
        if (dtJson != null) {
            try {
                String strLinkAPI = new clsHardCode().linkSendData;
                final String mRequestBody = "[" + dtJson.toString() + "]";

                volleyRequestSendDataKTP(strLinkAPI, dtJson, new VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        String error;
                    }

                    @Override
                    public void onResponse(String response, Boolean status, String strErrorMsg) {
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);
                            JSONObject jsn = jsonObject1.getJSONObject("validJson");
                            String warn = jsn.getString("TxtMessage");
                            String result = jsn.getString("IntResult");
                            String res = response;

                            if (result.equals("1")) {
//                                new clsActivity().showCustomToast(context.getApplicationContext(), "Saved", true);
                                Log.d("Image KTP Status", "Saved");
                            } else {
//                                new clsActivity().showCustomToast(context.getApplicationContext(), warn, false);
                                popup();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.i(TAG, "Ski data from server - " + response);
                    }
                });
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    // crop image after user take image from camera or gallery
    private void performCrop(){
        try {
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(uriImage, "image/*");
//            cropIntent.setDataAndType(selectedImage, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 0);
            cropIntent.putExtra("aspectY", 0);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            cropIntent.putExtra("scale", true);
            cropIntent.putExtra("scaleUpIfNeeded", true);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void performCropGallery(){
        try {
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(selectedImage, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 0);
            cropIntent.putExtra("aspectY", 0);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void performCrop2(){
        try {
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(uriImage, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 0);
            cropIntent.putExtra("aspectY", 0);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP2);
        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void performCropGallery2(){
        try {
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(selectedImage, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 0);
            cropIntent.putExtra("aspectY", 0);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP2);
        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void performCropProfile(){
        try {
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(uriImage, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 0);
            cropIntent.putExtra("aspectY", 0);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP_PROFILE);
        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void performCropGalleryProfile(){
        try {
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(selectedImage, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 0);
            cropIntent.putExtra("aspectY", 0);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP_PROFILE);
        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    // select take image from camera or gallery
    private void selectImage1() {
        final CharSequence[] items = { "Ambil Foto", "Pilih dari Galeri",
                "Batal" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Tambah Foto!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(getActivity());
                if (items[item].equals("Ambil Foto")) {
                    if(result)
                        captureImage1();
                } else if (items[item].equals("Pilih dari Galeri")) {
                    if(result)
                        galleryIntent();
                } else if (items[item].equals("Batal")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void selectImage2() {
        final CharSequence[] items = { "Ambil Foto", "Pilih dari Galeri",
                "Batal" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Tambah Foto!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(getActivity());
                if (items[item].equals("Ambil Foto")) {
                    if(result)
                        captureImage2();
                } else if (items[item].equals("Pilih dari Galeri")) {
                    if(result)
                        galleryIntent2();
                } else if (items[item].equals("Batal")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void selectImageProfile() {
        final CharSequence[] items = { "Ambil Foto", "Pilih dari Galeri",
                "Batal" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Tambah Foto Profile");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(getActivity());
                if (items[item].equals("Ambil Foto")) {
                    if(result)
                        captureImageProfile();
                } else if (items[item].equals("Pilih dari Galeri")) {
                    if(result)
                        galleryIntentProfile();
                } else if (items[item].equals("Batal")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private Bitmap decodeUriAsBitmap(Uri uri){
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String kontak = adapterView.getItemAtPosition(i).toString();

        if (kontak =="Line") {
            tvKategori.setText("(LINE)");
        } else if (kontak == "BBM") {
            tvKategori.setText("(BBM)");
        } else if (kontak == "No Telp") {
            tvKategori.setText("(No Telp)");
        } else if (kontak == "No HP") {
            tvKategori.setText("(No HP)");
        } else if (kontak == "WhatsApp") {
            tvKategori.setText("(WhatsApp)");
        } else if (kontak == "Facebook") {
            tvKategori.setText("(Facebook)");
        } else {
            tvKategori.setText("(Twitter)");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public static class Utility {
        public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public static boolean checkPermission(final Context context)
        {
            int currentAPIVersion = Build.VERSION.SDK_INT;
            if(currentAPIVersion>= Build.VERSION_CODES.M)
            {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                        alertBuilder.setCancelable(true);
                        alertBuilder.setTitle("Permission necessary");
                        alertBuilder.setMessage("External storage permission is necessary");
                        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                            }
                        });
                        AlertDialog alert = alertBuilder.create();
                        alert.show();
                    } else {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        }
    }

    // choose image from gallery
    private void galleryIntent() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.setType("image/*");
//        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);

        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , SELECT_FILE);//one can be replaced with any action code
    }

    private void galleryIntent2() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , SELECT_FILE2);//one can be replaced with any action code
    }

    private void galleryIntentProfile() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , SELECT_FILE_PROFILE);//one can be replaced with any action code
    }

    public void kontakDetail() {
        final ProgressDialog Dialog = new ProgressDialog(getActivity());
        txtValidationMethod = "kontakDetail";
        clsUserMemberRepo repo = new clsUserMemberRepo(context.getApplicationContext());
        DatabaseHelper helper = DatabaseManager.getInstance().getHelper();
        helper.refreshData2();
        try {
            dataMember = (List<clsUserMember>) repo.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        txtKontakID = dataMember.get(0).getTxtKontakId();
        String strLinkAPI = new clsHardCode().linkGetDataKontakDetail;
//        String nameRole = selectedRole;
        JSONObject resJson = new JSONObject();

        try {
            resJson.put("txtKontakID", txtKontakID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String mRequestBody = "[" + resJson.toString() + "]";

        volleyJsonObjectRequest(strLinkAPI, mRequestBody, "Sinkronisasi Data...", new VolleyResponseListener() {
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
                                String lttxtMediaID = jsonobject.getString("LttxtMediaID");
                                String txtDeskripsi = jsonobject.getString("TxtDeskripsi");
                                String txtPrioritasKontak = jsonobject.getString("IntPrioritasKontak");
                                String txtDetailMedia = jsonobject.getString("TxtDetailMedia");
                                String txtKeterangan = jsonobject.getString("TxtKeterangan");
                                String lttxtStatusAktif = jsonobject.getString("LttxtStatusAktif");
                                String txtKategoriMedia = jsonobject.getString("TxtKategoriMedia");
                                String txtExtension = jsonobject.getString("TxtExtension");
                                txtDeskripsi = txtDeskripsi.trim();

                                clsMediaKontakDetail dataKontak = new clsMediaKontakDetail();
                                dataKontak.setTxtGuiId(new clsActivity().GenerateGuid());
                                dataKontak.setTxtKontakId(dataMember.get(0).txtKontakId);
                                dataKontak.setLttxtMediaID(lttxtMediaID);
                                dataKontak.setTxtDeskripsi(txtDeskripsi);
                                dataKontak.setTxtPrioritasKontak(txtPrioritasKontak);
                                dataKontak.setTxtDetailMedia(txtDetailMedia);
                                dataKontak.setTxtKeterangan(txtKeterangan);
                                dataKontak.setLttxtStatusAktif(lttxtStatusAktif);
                                dataKontak.setTxtKategoriMedia(txtKategoriMedia);
                                dataKontak.setTxtExtension(txtExtension);

                                repoKontakDetail = new clsMediaKontakDetailRepo(context.getApplicationContext());
                                repoKontakDetail.createOrUpdate(dataKontak);
                            }
                            Log.d("Data info", "Data Kontak Detail berhasil di update");
                            Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
                            toolbar.setTitle("Media Komunikasi");

                            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                            FragmentNewDetailPersonal fragmentDetailPersonalData = new FragmentNewDetailPersonal();
                            FragmentTransaction fragmentTransactionPersonalData = getActivity().getSupportFragmentManager().beginTransaction();
                            fragmentTransactionPersonalData.replace(R.id.frame, fragmentDetailPersonalData);
                            fragmentTransactionPersonalData.commit();

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

    public void mediaType() {
        final ProgressDialog Dialog = new ProgressDialog(getActivity());
        txtValidationMethod = "MediaType";
        try {
            repoUserMember = new clsUserMemberRepo(context);
            dataMember = (List<clsUserMember>) repoUserMember.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        txtKontakID = dataMember.get(0).getTxtKontakId();
        String strLinkAPI = new clsHardCode().linkGetDataMediaType;
//        String nameRole = selectedRole;
        JSONObject resJson = new JSONObject();

        try {
            resJson.put("txtKontakID", txtKontakID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String mRequestBody = "[" + resJson.toString() + "]";

        volleyJsonObjectRequest(strLinkAPI, mRequestBody, "Sinkronisasi Data...", new VolleyResponseListener() {
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
                                String txtMasterID = jsonobject.getString("TxtMasterID");
                                String txtGrupMasterID = jsonobject.getString("TxtGrupMasterID");
                                String txtNamaMasterData = jsonobject.getString("TxtNamaMasterData");
                                String txtKeterangan = jsonobject.getString("TxtKeterangan");
                                txtMasterID = txtMasterID.trim();
                                txtGrupMasterID = txtGrupMasterID.trim();
                                txtNamaMasterData = txtNamaMasterData.trim();
                                txtKeterangan = txtKeterangan.trim();

                                clsMediaType dataMedia = new clsMediaType();
                                dataMedia.setTxtGuiId(txtMasterID);
                                dataMedia.setTxtGrupMasterID(txtGrupMasterID);
                                dataMedia.setTxtNamaMasterData(txtNamaMasterData);
                                dataMedia.setTxtKeterangan(txtKeterangan);

                                mediaTypeRepo = new clsMediaTypeRepo(context);
                                mediaTypeRepo.createOrUpdate(dataMedia);
                            }
                            Log.d("Data info", "Get Data media type success");

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

    public void jenisMedia() {
        final ProgressDialog Dialog = new ProgressDialog(getActivity());
        txtValidationMethod = "jenisMedia";
        try {
            repoUserMember = new clsUserMemberRepo(context);
            dataMember = (List<clsUserMember>) repoUserMember.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        txtKontakID = dataMember.get(0).getTxtKontakId();
        String strLinkAPI = new clsHardCode().linkGetDataJenisMedia;
//        String nameRole = selectedRole;
        JSONObject resJson = new JSONObject();

        try {
            resJson.put("txtKontakID", txtKontakID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String mRequestBody = "[" + resJson.toString() + "]";

        volleyJsonObjectRequest(strLinkAPI, mRequestBody, "Sinkronisasi Data...", new VolleyResponseListener() {
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
                                String txtMasterID = jsonobject.getString("TxtMasterID");
                                String txtGrupMasterID = jsonobject.getString("TxtGrupMasterID");
                                String txtNamaMasterData = jsonobject.getString("TxtNamaMasterData");
                                String txtNamaGrupMaster = jsonobject.getString("TxtNamaGrupMaster");
                                txtMasterID = txtMasterID.trim();
                                txtGrupMasterID = txtGrupMasterID.trim();
                                txtNamaMasterData = txtNamaMasterData.trim();
                                txtNamaGrupMaster = txtNamaGrupMaster.trim();

                                clsJenisMedia dataJenisMedia = new clsJenisMedia();
                                dataJenisMedia.setTxtGuiId(txtMasterID);
                                dataJenisMedia.setTxtNamaMasterData(txtNamaMasterData);
                                dataJenisMedia.setTxtGrupMasterID(txtGrupMasterID);
                                dataJenisMedia.setTxtNamaGrupMaster(txtNamaGrupMaster);

                                jenisMediaRepo = new clsJenisMediaRepo(context);
                                jenisMediaRepo.createOrUpdate(dataJenisMedia);
                            }
                            Log.d("Data info", "Get Data jenis media success");

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

    private void getImageFromAPI() {
        final ProgressDialog Dialog = new ProgressDialog(getActivity());
        txtValidationMethod = "getImageFromAPI";
        clsUserMemberRepo repo = new clsUserMemberRepo(context.getApplicationContext());
        helper.refreshData2();
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

        volleyJsonObjectRequest(strLinkAPI, mRequestBody, "Refresh Data...", new VolleyResponseListener() {
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

                                        if (!url.equals("null")) {
//                                            String substring = url.substring(Math.max(url.length() - 15, 0));
//
//                                            if (substring.equals("file_image1.jpg")) {
//                                                linkImage1 = url;
//                                            } else if (substring.equals("file_image2.jpg")){
//                                                linkImage2 = url;
//                                            }

                                            byte[] logoImage = getLogoImage(url);

                                            dataImage.setTxtImg(logoImage);

                                            repoUserMemberImage = new clsUserMemberImageRepo(context.getApplicationContext());

                                            int k = 0;
                                            k = repoUserMemberImage.createOrUpdate(dataImage);
                                            if(k > -1) {
                                                Log.d("Data info", "Image " +txtType+ " Berhasil di update");
                                                Log.d("Data info", "Data Member Image berhasil di update");
                                            }
                                        }
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
                    showDialog();
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

    Dialog dialog;
    private void showDialog() {
        // custom dialog
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setCancelable(true);

        // set the custom dialog components - text, image and button
        FloatingActionButton ktp1 = (FloatingActionButton) dialog.findViewById(R.id.editImageKtp1);
        FloatingActionButton ktp2 = (FloatingActionButton) dialog.findViewById(R.id.editImageKtp2);
        CircleImageView imageKTP1 = (CircleImageView) dialog.findViewById(R.id.image_ktp1);
        CircleImageView imageKTP2 = (CircleImageView) dialog.findViewById(R.id.image_ktp2);
        ImageButton close = (ImageButton) dialog.findViewById(R.id.btnClose);
        Button simpan = (Button) dialog.findViewById(R.id.btnBuy);
        final EditText etKTPDialog = (EditText) dialog.findViewById(R.id.etKTPDialog);

        etKTPDialog.setText(etNoKTP.getText().toString());
        int maxLength = 16;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        etKTPDialog.setFilters(FilterArray);
        etKTPDialog.setInputType(InputType.TYPE_CLASS_NUMBER);

        // Close Button
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //TODO Close button action
            }
        });

        // save Button
        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etNoKTP.setText(etKTPDialog.getText().toString());

                clsUserMember dataUser = new clsUserMember();
                dataUser.setTxtKontakId(dataMember.get(0).getTxtKontakId().toString());
                dataUser.setTxtMemberId(dataMember.get(0).getTxtMemberId().toString());
                dataUser.setTxtNama(tvNama.getText().toString());
                dataUser.setTxtNamaDepan(etNamaDepan.getText().toString());
                dataUser.setTxtAlamat(etAlamat.getText().toString());
                dataUser.setTxtNoKTP(etKTPDialog.getText().toString());
                dataUser.setTxtNamaPanggilan(etNamaPanggilan.getText().toString());
                dataUser.setTxtNamaBelakang(etNamaBelakang.getText().toString());
                dataUser.setTxtBasePoin(dataMember.get(0).getTxtBasePoin().toString());
                dataUser.setTxtEmail(etEmail.getText().toString());
                dataUser.setTxtNoTelp(etTelpon.getText().toString());
                dataUser.setTxtJenisKelamin(dataMember.get(0).txtJenisKelamin);

                repoUserMember = new clsUserMemberRepo(context.getApplicationContext());
                repoUserMember.createOrUpdate(dataUser);

                savePicture1();
                savePicture2();
                sendDataImageKTP();

                dialog.dismiss();
                //TODO Buy button action
            }
        });

        ktp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noKTPSementara = etKTPDialog.getText().toString();
                selectImage1();
                dialog.dismiss();
            }
        });

        ktp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noKTPSementara = etKTPDialog.getText().toString();
                selectImage2();
                dialog.dismiss();
            }
        });

        try {
            repoUserMemberImage = new clsUserMemberImageRepo(context);
            dataMemberImage = (List<clsUserMemberImage>) repoUserMemberImage.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (dataMemberImage.size() > 0) {
            for (clsUserMemberImage imgDt : dataMemberImage) {
                final byte[] imgFile = imgDt.getTxtImg();
                if (imgFile != null) {
                    if (imgDt.getTxtPosition().equals("txtFileName1")) {
                        mybitmap1 = BitmapFactory.decodeByteArray(imgFile, 0, imgFile.length);
                        Bitmap bitmap = Bitmap.createScaledBitmap(mybitmap1, 150, 150, true);
                        imageKTP1.setImageBitmap(bitmap);
                    }
                }

                final byte[] imgFile2 = imgDt.getTxtImg();
                if (imgFile2 != null) {
                    if (imgDt.getTxtPosition().equals("txtFileName2")) {
                        mybitmap2 = BitmapFactory.decodeByteArray(imgFile2, 0, imgFile2.length);
                        Bitmap bitmap = Bitmap.createScaledBitmap(mybitmap2, 150, 150, true);
                        imageKTP2.setImageBitmap(bitmap);
                    }
                }
            }
        }

        if (validate == true) {
            Bitmap photo = thePic;
            try {
                bitmap = new clsActivity().resizeImageForBlob(photo);
                imageKTP1.setVisibility(View.VISIBLE);
                output = null;
                try {
                    output = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0, output);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (output != null){
                            output.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Bitmap photo_view = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
                phtImage1 = output.toByteArray();
                imageKTP1.setImageBitmap(photo_view);

//                if (dtImage == null){
//                    dtImage.setTxtImg(phtImage1);
//                } else {
//                    dtImage.setTxtImg(phtImage1);
//                }
                repoUserMemberImage = new clsUserMemberImageRepo(context.getApplicationContext());

            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        } if (validate_2 == true) {
            Bitmap photo = thePic2;
            try {
                bitmap2 = new clsActivity().resizeImageForBlob(photo);
                imageKTP2.setVisibility(View.VISIBLE);
                output = null;
                try {
                    output = new ByteArrayOutputStream();
                    bitmap2.compress(Bitmap.CompressFormat.PNG, 0, output);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (output != null){
                            output.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Bitmap photo_view = Bitmap.createScaledBitmap(bitmap2, 150, 150, true);
                phtImage2 = output.toByteArray();
                imageKTP2.setImageBitmap(photo_view);

//                if (dtImage == null){
//                    dtImage.setTxtImg(phtImage2);
//                } else {
//                    dtImage.setTxtImg(phtImage2);
//                }
                repoUserMemberImage = new clsUserMemberImageRepo(context.getApplicationContext());

            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        imageKTP1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bitmap != null) {
//                    new clsActivity().zoomImage(bitmap, getActivity());
//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                    byte[] byteArray = stream.toByteArray();

                    File file = new File(Environment.getExternalStorageDirectory() + File.separator + "GambarKTP1" + ".png");
                    file.delete();
                    FileOutputStream fOut = null;
                    try {
                        fOut = new FileOutputStream(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
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
                    intent.putExtra("imageKTP1", "GambarKTP1");
                    startActivity(intent);
                } else {
                    if (dataMemberImage.size() > 0 && mybitmap1 != null){
//                        new clsActivity().zoomImage(mybitmap1, getActivity());
                        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "GambarKTP1" + ".png");
                        file.delete();
                        FileOutputStream fOut = null;
                        try {
                            fOut = new FileOutputStream(file);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        mybitmap1.compress(Bitmap.CompressFormat.PNG, 85, fOut);
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
                        intent.putExtra("imageKTP1", "GambarKTP1");
                        startActivity(intent);
                    }
                }
            }
        });

        imageKTP2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bitmap2 != null) {
//                    new clsActivity().zoomImage(bitmap2, getActivity());
                    File file = new File(Environment.getExternalStorageDirectory() + File.separator + "GambarKTP2" + ".png");
                    file.delete();
                    FileOutputStream fOut = null;
                    try {
                        fOut = new FileOutputStream(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    bitmap2.compress(Bitmap.CompressFormat.PNG, 85, fOut);
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
                    intent.putExtra("imageKTP2", "GambarKTP2");
                    startActivity(intent);
                } else {
                    if (dataMemberImage.size() > 0 && mybitmap2 != null) {
//                        new clsActivity().zoomImage(mybitmap2, getActivity());
                        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "GambarKTP2" + ".png");
                        file.delete();
                        FileOutputStream fOut = null;
                        try {
                            fOut = new FileOutputStream(file);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        mybitmap2.compress(Bitmap.CompressFormat.PNG, 85, fOut);
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
                        intent.putExtra("imageKTP2", "GambarKTP2");
                        startActivity(intent);
                    }
                }
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();
    }

    private void dialogPopupImage(Bitmap photo) {
        // custom dialog
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setCancelable(false);

        // set the custom dialog components - text, image and button
        FloatingActionButton ktp1 = (FloatingActionButton) dialog.findViewById(R.id.editImageKtp1);
        FloatingActionButton ktp2 = (FloatingActionButton) dialog.findViewById(R.id.editImageKtp2);
        CircleImageView imageKTP1 = (CircleImageView) dialog.findViewById(R.id.image_ktp1);
        CircleImageView imageKTP2 = (CircleImageView) dialog.findViewById(R.id.image_ktp2);
        ImageButton close = (ImageButton) dialog.findViewById(R.id.btnClose);
        Button simpan = (Button) dialog.findViewById(R.id.btnBuy);
        final EditText etKTPDialog = (EditText) dialog.findViewById(R.id.etKTPDialog);

        etKTPDialog.setText(noKTPSementara);
//         max lenght
        int maxLength = 16;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        etKTPDialog.setFilters(FilterArray);
        etKTPDialog.setInputType(InputType.TYPE_CLASS_NUMBER);

        // Close Button
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //TODO Close button action
            }
        });

        // Save Button
        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etNoKTP.setText(etKTPDialog.getText().toString());

                clsUserMember dataUser = new clsUserMember();
                dataUser.setTxtKontakId(dataMember.get(0).getTxtKontakId().toString());
                dataUser.setTxtMemberId(dataMember.get(0).getTxtMemberId().toString());
                dataUser.setTxtNama(tvNama.getText().toString());
                dataUser.setTxtNamaDepan(etNamaDepan.getText().toString());
                dataUser.setTxtAlamat(etAlamat.getText().toString());
                dataUser.setTxtNoKTP(etKTPDialog.getText().toString());
                dataUser.setTxtNamaPanggilan(etNamaPanggilan.getText().toString());
                dataUser.setTxtNamaBelakang(etNamaBelakang.getText().toString());
                dataUser.setTxtBasePoin(dataMember.get(0).getTxtBasePoin().toString());
                dataUser.setTxtEmail(etEmail.getText().toString());
                dataUser.setTxtNoTelp(etTelpon.getText().toString());
                dataUser.setTxtJenisKelamin(dataMember.get(0).txtJenisKelamin);

                repoUserMember = new clsUserMemberRepo(context.getApplicationContext());
                repoUserMember.createOrUpdate(dataUser);

                savePicture1();
                savePicture2();
                sendDataImageKTP();

                dialog.dismiss();
                //TODO Buy button action
            }
        });

        ktp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage1();
                dialog.dismiss();
            }
        });

        ktp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage2();
                dialog.dismiss();
            }
        });

        if (dataMemberImage.size() > 0) {
            try {
                repoUserMemberImage = new clsUserMemberImageRepo(context);
                dataMemberImage = (List<clsUserMemberImage>) repoUserMemberImage.findAll();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            File folder = new File(Environment.getExternalStorageDirectory().toString() + "/data/data/KalbeFamily/tempdata/FotoKTP");
            folder.mkdir();

            for (clsUserMemberImage imgDt : dataMemberImage) {
                final byte[] imgFile = imgDt.getTxtImg();
                if (imgFile != null) {
                    if (imgDt.getTxtPosition().equals("txtFileName1")) {
                        mybitmap1 = BitmapFactory.decodeByteArray(imgFile, 0, imgFile.length);
                        Bitmap bitmap = Bitmap.createScaledBitmap(mybitmap1, 150, 150, true);
                        imageKTP1.setImageBitmap(bitmap);

//                    File file = null;
//                    try {
//                        file = File.createTempFile("image-", ".jpg", new File(Environment.getExternalStorageDirectory().toString() + "/data/data/KalbeFamily/tempdata/FotoKTP"));
//                        FileOutputStream out = new FileOutputStream(file);
//                        out.write(imgFile);
//                        out.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    }
                }

                final byte[] imgFile2 = imgDt.getTxtImg();
                if (imgFile2 != null) {
                    if (imgDt.getTxtPosition().equals("txtFileName2")) {
                        mybitmap2 = BitmapFactory.decodeByteArray(imgFile2, 0, imgFile2.length);
                        Bitmap bitmap = Bitmap.createScaledBitmap(mybitmap2, 150, 150, true);
                        imageKTP2.setImageBitmap(bitmap);

//                    File file = null;
//                    try {
//                        file = File.createTempFile("image-", ".jpg", new File(Environment.getExternalStorageDirectory().toString() + "/data/data/KalbeFamily/tempdata/FotoKTP"));
//                        FileOutputStream out = new FileOutputStream(file);
//                        out.write(imgFile2);
//                        out.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    }
                }
            }
        }

        if (validate == true) {
            Bitmap photo_ktp1 = thePic;
            try {
                bitmap = new clsActivity().resizeImageForBlob(photo_ktp1);
                imageKTP1.setVisibility(View.VISIBLE);
                output = null;
                try {
                    output = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0, output);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (output != null){
                            output.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Bitmap photo_view = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
                phtImage1 = output.toByteArray();
                imageKTP1.setImageBitmap(photo_view);

//                if (dtImage == null){
//                    dtImage.setTxtImg(phtImage1);
//                } else {
//                    dtImage.setTxtImg(phtImage1);
//                }
                repoUserMemberImage = new clsUserMemberImageRepo(context.getApplicationContext());

            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        } if (validate_2 == true) {
            Bitmap photo_ktp2 = thePic2;
            try {
                bitmap2 = new clsActivity().resizeImageForBlob(photo_ktp2);
                imageKTP2.setVisibility(View.VISIBLE);
                output = null;
                try {
                    output = new ByteArrayOutputStream();
                    bitmap2.compress(Bitmap.CompressFormat.PNG, 0, output);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (output != null){
                            output.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Bitmap photo_view = Bitmap.createScaledBitmap(bitmap2, 150, 150, true);
                phtImage2 = output.toByteArray();
                imageKTP2.setImageBitmap(photo_view);

//                if (dtImage == null){
//                    dtImage.setTxtImg(phtImage2);
//                } else {
//                    dtImage.setTxtImg(phtImage2);
//                }
                repoUserMemberImage = new clsUserMemberImageRepo(context.getApplicationContext());

            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        imageKTP1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bitmap != null) {
//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                    byte[] byteArray = stream.toByteArray();

                    File file = new File(Environment.getExternalStorageDirectory() + File.separator + "GambarKTP1" + ".png");
                    file.delete();
                    FileOutputStream fOut = null;
                    try {
                        fOut = new FileOutputStream(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
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
                    intent.putExtra("imageKTP1", "GambarKTP1");
                    startActivity(intent);
//                    new clsActivity().zoomImage(bitmap, getActivity());
                } else {
                    if (dataMemberImage.size() > 0 && mybitmap1 != null){
//                        new clsActivity().zoomImage(mybitmap1, getActivity());
                        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "GambarKTP1" + ".png");
                        file.delete();
                        FileOutputStream fOut = null;
                        try {
                            fOut = new FileOutputStream(file);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        mybitmap1.compress(Bitmap.CompressFormat.PNG, 85, fOut);
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
                        intent.putExtra("imageKTP1", "GambarKTP1");
                        startActivity(intent);
                    }
                }
            }
        });

        imageKTP2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bitmap2 != null) {
//                    new clsActivity().zoomImage(bitmap2, getActivity());
                    File file = new File(Environment.getExternalStorageDirectory() + File.separator + "GambarKTP2" + ".png");
                    file.delete();
                    FileOutputStream fOut = null;
                    try {
                        fOut = new FileOutputStream(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    bitmap2.compress(Bitmap.CompressFormat.PNG, 85, fOut);
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
                    intent.putExtra("imageKTP2", "GambarKTP2");
                    startActivity(intent);
                } else {
                    if (dataMemberImage.size() > 0 && mybitmap2 != null) {
//                        new clsActivity().zoomImage(mybitmap2, getActivity());
                        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "GambarKTP2" + ".png");
                        file.delete();
                        FileOutputStream fOut = null;
                        try {
                            fOut = new FileOutputStream(file);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        mybitmap2.compress(Bitmap.CompressFormat.PNG, 85, fOut);
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
                        intent.putExtra("imageKTP2", "GambarKTP2");
                        startActivity(intent);
                    }
                }
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();
    }

    public void volleyJsonObjectRequest(String strLinkAPI, final String mRequestBody, String progressBarType, final VolleyResponseListener listener) {
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
                    //new clsActivity().showCustomToast(getApplicationContext(), "401, Authorization has been denied for this request", false);

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
                                        if (txtValidationMethod.equals("kontakDetail")) {
                                            kontakDetail();
                                        } else if (txtValidationMethod.equals("MediaType")){
                                            mediaType();
                                        } else if (txtValidationMethod.equals("jenisMedia")){
                                            jenisMedia();
                                        } else if (txtValidationMethod.equals("getImageFromAPI")){
                                            getImageFromAPI();
                                        } else {
                                            Toast.makeText(context, "Silahkan coba lagi", Toast.LENGTH_SHORT).show();
                                        }
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
                        dialog.dismiss();
                        sweetAlertDialog.dismiss();
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

    private void volleyRequestSendDataKTP(String strLinkAPI, final clsSendData mRequestBody, final VolleyResponseListener listener) {
        String client = "";
        RequestQueue queue = Volley.newRequestQueue(context);
        ProgressDialog Dialog = new ProgressDialog(getActivity());

        Dialog = ProgressDialog.show(getActivity(), "", "Mohon Tunggu...", true);
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
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, strLinkAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Boolean status = false;
                String errorMessage = null;
                listener.onResponse(response.toString(), status, errorMessage);
                finalDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String strLinkRequestToken = new clsHardCode().linkToken;
                final String refresh_token = dataToken.get(0).txtRefreshToken;
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {

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
                                        sendDataImageKTP();
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
                final SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
                dialog.setTitleText("Oops...");
                dialog.setContentText("Mohon check kembali koneksi internet anda");
                dialog.setCancelable(false);
                dialog.show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                try {
                    final String mRequestBody2 = "[" +  mRequestBody.getDtdataJson().txtJSON().toString() + "]";
                    params.put("txtParam", mRequestBody2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView
                if (mRequestBody.getFileUpload().get("txtFileName1") != null){
                    params.put("file_image1.jpg", new DataPart("file_image1.jpg", mRequestBody.getFileUpload().get("txtFileName1"), "image/jpeg"));
                }
                if (mRequestBody.getFileUpload().get("txtFileName2") != null){
                    params.put("file_image2.jpg", new DataPart("file_image2.jpg", mRequestBody.getFileUpload().get("txtFileName2"), "image/jpeg"));
                }

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
        multipartRequest.setRetryPolicy(new
                DefaultRetryPolicy(500000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(multipartRequest);
    }

    private void volleyRequestSendData(String strLinkAPI, final clsSendData mRequestBody, final VolleyResponseListener listener) {
        String client = "";
        RequestQueue queue = Volley.newRequestQueue(context);
        ProgressDialog Dialog = new ProgressDialog(getActivity());

        Dialog = ProgressDialog.show(getActivity(), "", "Mohon Tunggu...", true);
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
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, strLinkAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Boolean status = false;
                String errorMessage = null;
                listener.onResponse(response.toString(), status, errorMessage);
                finalDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String strLinkRequestToken = new clsHardCode().linkToken;
                final String refresh_token = dataToken.get(0).txtRefreshToken;
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {

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
                                        sendDataImageKTP();
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
                final SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
                dialog.setTitleText("Oops...");
                dialog.setContentText("Mohon check kembali koneksi internet anda");
                dialog.setCancelable(false);
                dialog.show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                try {
                    final String mRequestBody2 = "[" +  mRequestBody.getDtdataJson().txtJSON().toString() + "]";
                    params.put("txtParam", mRequestBody2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView
                if (mRequestBody.getFileUploadProfile().get("profile_picture") != null){
                    params.put("profile_picture.jpg", new DataPart("profile_picture.jpg", mRequestBody.getFileUploadProfile().get("profile_picture"), "image/jpeg"));
                }

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
        multipartRequest.setRetryPolicy(new
                DefaultRetryPolicy(500000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(multipartRequest);
    }

    private void popup() {
        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Oops...")
                .setContentText("Data Gagal disimpan, silahkan coba lagi...")
                .show();
    }
}
