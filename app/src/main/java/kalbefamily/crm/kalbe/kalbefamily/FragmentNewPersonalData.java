package kalbefamily.crm.kalbe.kalbefamily;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import kalbefamily.crm.kalbe.kalbefamily.BL.clsActivity;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsSendData;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserImageProfile;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserMember;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserMemberImage;
import kalbefamily.crm.kalbe.kalbefamily.Data.VolleyResponseListener;
import kalbefamily.crm.kalbe.kalbefamily.Data.VolleyUtils;
import kalbefamily.crm.kalbe.kalbefamily.Data.clsHardCode;
import kalbefamily.crm.kalbe.kalbefamily.Data.clsHelper;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserImageProfileRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserMemberImageRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserMemberRepo;

import static android.R.attr.id;
import static android.app.Activity.RESULT_OK;
import static com.android.volley.VolleyLog.TAG;

/**
 * Created by Rian Andrivani on 7/20/2017.
 */

public class FragmentNewPersonalData extends Fragment {
    View v;
    TextView tvNama, tvMember, etNamaKeluarga, etNamaPanggilan, etEmail, etTelpon, etAlamat, etNoKTP;
    RadioButton radioPria, radiowanita;
    RadioGroup radioGenderGroup;
    Button btnUpdate, btnEditNamaKeluarga, btnEditNamaPanggilan, btnEditEmail, btnEditNoTelp, btnEditAlamat, btnEditNoKTP;
    private ImageView image1, image2;
    CircleImageView ivProfile;
    Context context;
    private Uri uriImage, selectedImage;

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
    clsUserMemberImage dtImage;
    clsUserImageProfile dtImageProfile;
    boolean validate = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_new_personal_data,container,false);
        context = getActivity().getApplicationContext();

        ivProfile = (CircleImageView) v.findViewById(R.id.profile_image);
        tvNama = (TextView) v.findViewById(R.id.tvNama);
        tvMember = (TextView) v.findViewById(R.id.tvMember);
        etNamaKeluarga = (TextView) v.findViewById(R.id.textViewNamaKeluarga);
        etNamaPanggilan = (TextView) v.findViewById(R.id.textViewNamaPanggilan);
        etEmail = (TextView) v.findViewById(R.id.textViewEmail);
        etTelpon = (TextView) v.findViewById(R.id.textVuewNoTelp);
        etAlamat = (TextView) v.findViewById(R.id.textViewAlamat);
        radioGenderGroup = (RadioGroup) v.findViewById(R.id.radioGroupGender);
        radioPria = (RadioButton) v.findViewById(R.id.radioButton1);
        radiowanita = (RadioButton) v.findViewById(R.id.radioButton2);
        etNoKTP = (TextView) v.findViewById(R.id.textViewNoKTP);
        btnEditNamaKeluarga = (Button) v.findViewById(R.id.btnEdit1);
        btnEditNamaPanggilan = (Button) v.findViewById(R.id.btnEdit2);
        btnEditEmail = (Button) v.findViewById(R.id.btnEdit3);
        btnEditNoTelp = (Button) v.findViewById(R.id.btnEdit4);
        btnEditAlamat = (Button) v.findViewById(R.id.btnEdit5);
        btnEditNoKTP = (Button) v.findViewById(R.id.btnEdit6);
        btnUpdate = (Button) v.findViewById(R.id.btnUpdate);
        image1 = (ImageView) v.findViewById(R.id.image1);
        image2 = (ImageView) v.findViewById(R.id.image2);

        try {
            repoUserMember = new clsUserMemberRepo(context);
            dataMember = (List<clsUserMember>) repoUserMember.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tvNama.setText(dataMember.get(0).getTxtNama().toString());

        String sub, sub2, sub3;
        final String member1 = dataMember.get(0).getTxtMemberId().toString();
        sub = member1.substring(0, member1.length() - 8);
        sub2 = member1.substring(4, member1.length() - 4);
        sub3 = member1.substring(8, member1.length());
        tvMember.setText(sub +" "+ sub2 +" "+ sub3);

        if (dataMember.get(0).getTxtNamaKeluarga().toString().equals("null")) {
            etNamaKeluarga.setText("Nama Keluarga");
        } else {
            etNamaKeluarga.setText(dataMember.get(0).getTxtNamaKeluarga().toString());
        }

        if (dataMember.get(0).getTxtNamaPanggilan().toString().equals("null")) {
            etNamaPanggilan.setText("Nama Panggilan");
        } else {
            etNamaPanggilan.setText(dataMember.get(0).getTxtNamaPanggilan().toString());
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
            etNoKTP.setText("No KTP");
        } else {
            etNoKTP.setText(dataMember.get(0).getTxtNoKTP().toString());
        }

        tvNama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Update Nama Anda");
                alert.setMessage("Nama Anda : ");

                // Layout Dynamic
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(25, 20, 25, 10);

                final EditText input = new EditText(context);
                input.setTextColor(Color.BLACK);
                input.setText(tvNama.getText().toString());
                layout.addView(input, layoutParams);

                alert.setView(layout);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        tvNama.setText(input.getText().toString());
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            }
        });

        btnEditNamaKeluarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setMessage("Masukan Nama Keluarga");

                // Layout Dynamic
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(25, 20, 25, 10);

                final EditText input = new EditText(context);
                input.setTextColor(Color.BLACK);
                input.setText(etNamaKeluarga.getText().toString());
                layout.addView(input, layoutParams);

                alert.setView(layout);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Toast.makeText(context,"Success",Toast.LENGTH_LONG).show();
                        etNamaKeluarga.setText(input.getText().toString());
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            }
        });

        btnEditNamaPanggilan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setMessage("Masukan Nama Panggilan");

                // Layout Dynamic
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(25, 20, 25, 10);

                final EditText input = new EditText(context);
                input.setTextColor(Color.BLACK);
                input.setText(etNamaPanggilan.getText().toString());
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

        btnEditEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setMessage("Masukan Email Anda");

                // Layout Dynamic
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(25, 20, 25, 10);

                final EditText input = new EditText(context);
                input.setTextColor(Color.BLACK);
                input.setText(etEmail.getText().toString());
                input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                layout.addView(input, layoutParams);

                alert.setView(layout);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        etEmail.setText(input.getText().toString());
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            }
        });

        btnEditNoTelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setMessage("Masukan No Telp Anda");

                // Layout Dynamic
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(25, 20, 25, 10);

                final EditText input = new EditText(context);
                input.setTextColor(Color.BLACK);
                input.setText(etTelpon.getText().toString());
                input.setInputType(InputType.TYPE_CLASS_PHONE);
                layout.addView(input, layoutParams);

                alert.setView(layout);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        etTelpon.setText(input.getText().toString());
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            }
        });

        btnEditAlamat.setOnClickListener(new View.OnClickListener() {
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

        btnEditNoKTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setMessage("Masukan No KTP Anda");

                // Layout Dynamic
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(25, 20, 25, 10);

                final EditText input = new EditText(context);
                input.setTextColor(Color.BLACK);
                input.setText(etNoKTP.getText().toString());
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                layout.addView(input, layoutParams);

                alert.setView(layout);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        etNoKTP.setText(input.getText().toString());
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
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

        if (dataUserImageProfile.size() > 0) {
            viewImageProfile();
        }

        if (dataMemberImage.size() > 0) {
            viewImage();
        }

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
                alertDialog.setTitle("Confirm");
                alertDialog.setMessage("Are you sure?");
                alertDialog.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clsUserMember dataUser = new clsUserMember();
                        dataUser.setTxtKontakId(dataMember.get(0).getTxtKontakId().toString());
                        dataUser.setTxtMemberId(member1);
                        dataUser.setTxtNama(tvNama.getText().toString());
                        dataUser.setTxtAlamat(etAlamat.getText().toString());
                        dataUser.setTxtNoKTP(etNoKTP.getText().toString());
                        dataUser.setTxtNamaPanggilan(etNamaPanggilan.getText().toString());
                        dataUser.setTxtNamaKeluarga(etNamaKeluarga.getText().toString());
                        dataUser.setTxtBasePoin(dataMember.get(0).getTxtBasePoin().toString());

                        if(!isValidEmail(etEmail.getText().toString())){
                            new clsActivity().showCustomToast(context.getApplicationContext(), "Email tidak valid", false);
                            validate = false;
                        } else if (!isValidMobile(etTelpon.getText().toString())) {
                            new clsActivity().showCustomToast(context.getApplicationContext(), "Telpon harus berupa angka", false);
                            validate = false;
                        } else {
                            dataUser.setTxtEmail(etEmail.getText().toString());
                            dataUser.setTxtNoTelp(etTelpon.getText().toString());

                            int selectedId = radioGenderGroup.getCheckedRadioButtonId();
                            RadioButton rbGender = (RadioButton) v.findViewById(selectedId);

                            dataUser.setTxtJenisKelamin(rbGender.getText().toString());

                            repoUserMember = new clsUserMemberRepo(context.getApplicationContext());
                            repoUserMember.createOrUpdate(dataUser);

                            savePicture1();
                            savePicture2();
                            savePictureProfile();
                            sendData();

                            new clsActivity().showCustomToast(context.getApplicationContext(), "Saved", true);
                            Intent intent = new Intent(context.getApplicationContext(), HomeMenu.class);
                            getActivity().finish();
                            startActivity(intent);
                        }
                    }
                });
                alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                alertDialog.show();
            }
        });

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
                new clsActivity().showCustomToast(getContext(), "User canceled to capture image", false);
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
                Bitmap thePic = extras.getParcelable("data");

                previewCaptureImage1(thePic);
            } else if (resultCode == 0) {
                new clsActivity().showCustomToast(getContext(), "User canceled to capture image", false);
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
                new clsActivity().showCustomToast(getContext(), "User canceled to capture image", false);
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
                Bitmap thePic = extras.getParcelable("data");

                previewCaptureImage2(thePic);
            } else if (resultCode == 0) {
                new clsActivity().showCustomToast(getContext(), "User canceled to capture image", false);
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
                new clsActivity().showCustomToast(getContext(), "User canceled to capture image", false);
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
                Bitmap thePic = extras.getParcelable("data");

                previewCaptureImageProfile(thePic);
            } else if (resultCode == 0) {
                new clsActivity().showCustomToast(getContext(), "User canceled to capture image", false);
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

            if (dtImageProfile == null){
                dtImageProfile.setTxtImg(phtProfile);
            } else {
                dtImageProfile.setTxtImg(phtProfile);
            }
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
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriImage);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    protected void captureImage2() {
        uriImage = getOutputMediaFileUri();
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriImage);
        startActivityForResult(cameraIntent, CAMERA_REQUEST2);
    }

    protected void captureImageProfile() {
        uriImage = getOutputMediaFileUri();
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriImage);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_PROFILE);
    }

    protected void savePicture1() {
        clsUserMemberImage dataImage = new clsUserMemberImage();

        if (phtImage1 != null) {
            dataImage.setTxtGuiId(new clsActivity().GenerateGuid());
            dataImage.setTxtHeaderId(dataMember.get(0).txtKontakId);
            dataImage.setTxtImg(phtImage1);
            dataImage.setTxtPosition("txtFileName1");

            repoUserMemberImage = new clsUserMemberImageRepo(context.getApplicationContext());
            repoUserMemberImage.createOrUpdate(dataImage);
        }
    }

    protected void savePicture2() {
        clsUserMemberImage dataImage = new clsUserMemberImage();

        if (phtImage2 != null) {
            dataImage.setTxtGuiId(new clsActivity().GenerateGuid());
            dataImage.setTxtHeaderId(dataMember.get(0).txtKontakId);
            dataImage.setTxtImg(phtImage2);
            dataImage.setTxtPosition("txtFileName2");

            repoUserMemberImage = new clsUserMemberImageRepo(context.getApplicationContext());
            repoUserMemberImage.createOrUpdate(dataImage);
        }
    }

    protected void savePictureProfile() {
        clsUserImageProfile dataImageProfile = new clsUserImageProfile();

        if (phtProfile != null) {
            dataImageProfile.setTxtGuiId(new clsActivity().GenerateGuid());
            dataImageProfile.setTxtKontakId(dataMember.get(0).txtKontakId);
            dataImageProfile.setTxtImg(phtProfile);

            repoUserImageProfile = new clsUserImageProfileRepo(context.getApplicationContext());
            repoUserImageProfile.createOrUpdate(dataImageProfile);
        }
    }

    private void sendData() {
        String versionName = "";
        clsSendData dtJson = new clsHelper().sendData(versionName, context.getApplicationContext());
        if (dtJson != null) {
            try {
                String strLinkAPI = new clsHardCode().linkSendData;
                final String mRequestBody = "[" + dtJson.toString() + "]";

                new VolleyUtils().makeJsonObjectRequestSendData(context.getApplicationContext(), strLinkAPI, dtJson, new VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        String error;
                    }

                    @Override
                    public void onResponse(String response, Boolean status, String strErrorMsg) {
                        String res = response;

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
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(getActivity());
                if (items[item].equals("Take Photo")) {
                    if(result)
                        captureImage1();
                } else if (items[item].equals("Choose from Library")) {
                    if(result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void selectImage2() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(getActivity());
                if (items[item].equals("Take Photo")) {
                    if(result)
                        captureImage2();
                } else if (items[item].equals("Choose from Library")) {
                    if(result)
                        galleryIntent2();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void selectImageProfile() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo Profile");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(getActivity());
                if (items[item].equals("Take Photo")) {
                    if(result)
                        captureImageProfile();
                } else if (items[item].equals("Choose from Library")) {
                    if(result)
                        galleryIntentProfile();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
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

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }
}
