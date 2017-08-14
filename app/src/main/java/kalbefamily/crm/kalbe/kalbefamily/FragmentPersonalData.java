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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import kalbefamily.crm.kalbe.kalbefamily.BL.clsActivity;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsSendData;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserMember;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserMemberImage;
import kalbefamily.crm.kalbe.kalbefamily.Data.VolleyResponseListener;
import kalbefamily.crm.kalbe.kalbefamily.Data.VolleyUtils;
import kalbefamily.crm.kalbe.kalbefamily.Data.clsHardCode;
import kalbefamily.crm.kalbe.kalbefamily.Data.clsHelper;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserMemberImageRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserMemberRepo;

import static android.app.Activity.RESULT_OK;
import static com.android.volley.VolleyLog.TAG;

/**
 * Created by Rian Andrivani on 7/20/2017.
 */

public class FragmentPersonalData extends Fragment {
    View v;
    EditText etNama, etEmail, etAlamat, etTelpon, etNoKTP, etNamaPanggilan, etNamaKeluarga, etBasePoin;
    TextView etKontakId, etMemberId;
    RadioButton radioPria, radiowanita;
    RadioGroup radioGenderGroup;
    Button btnUpdate;
    private ImageView image1, image2;
    Context context;
    private Uri uriImage, selectedImage;

    private static final int CAMERA_REQUEST = 1888;
    private static final int CAMERA_REQUEST2 = 1889;
    private static final String IMAGE_DIRECTORY_NAME = "Image Personal";

    private static Bitmap photoImage1, photoImage2;
    private static ByteArrayOutputStream output = new ByteArrayOutputStream();
    private static byte[] phtImage1;
    private static byte[] phtImage2;
    private static Bitmap mybitmap1;
    private static Bitmap mybitmap2;
    final int PIC_CROP = 2;
    final int PIC_CROP2 = 3;
    final int SELECT_FILE = 1;
    final int SELECT_FILE2 = 4;

    List<clsUserMember> dataMember = null;
    List<clsUserMemberImage> dataMemberImage = null;
    clsUserMemberRepo repoUserMember = null;
    clsUserMemberImageRepo repoUserMemberImage = null;
    clsUserMemberImage dtImage;
    boolean validate = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_personal_data,container,false);
        context = getActivity().getApplicationContext();

        etKontakId = (TextView) v.findViewById(R.id.etKontakId);
        etMemberId = (TextView) v.findViewById(R.id.etMemberId);
        etNama = (EditText) v.findViewById(R.id.etNama);
        etNamaKeluarga = (EditText) v.findViewById(R.id.etNamaKeluarga);
        etNamaPanggilan = (EditText) v.findViewById(R.id.etNamaPanggilan);
        etEmail = (EditText) v.findViewById(R.id.etEmail);
        etTelpon = (EditText) v.findViewById(R.id.etTelpon);
        etAlamat = (EditText) v.findViewById(R.id.etAlamat);
        radioGenderGroup = (RadioGroup) v.findViewById(R.id.radioGroupGender);
        radioPria = (RadioButton) v.findViewById(R.id.radioButton1);
        radiowanita = (RadioButton) v.findViewById(R.id.radioButton2);
        etNoKTP = (EditText) v.findViewById(R.id.etNoKTP);
        image1 = (ImageView) v.findViewById(R.id.image1);
        image2 = (ImageView) v.findViewById(R.id.image2);
        btnUpdate = (Button) v.findViewById(R.id.btnUpdate);
        etBasePoin = (EditText) v.findViewById(R.id.etBasePoin);

        try {
            repoUserMember = new clsUserMemberRepo(context);
            dataMember = (List<clsUserMember>) repoUserMember.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        etKontakId.setText(dataMember.get(0).getTxtKontakId().toString());
        etMemberId.setText(dataMember.get(0).getTxtMemberId().toString());
        etNama.setText(dataMember.get(0).getTxtNama().toString());
        etAlamat.setText(dataMember.get(0).getTxtAlamat().toString());
        etEmail.setText(dataMember.get(0).getTxtEmail().toString());
        etTelpon.setText(dataMember.get(0).getTxtNoTelp().toString());
        etBasePoin.setText(dataMember.get(0).getTxtBasePoin().toString());
        etBasePoin.setEnabled(false);


        if (dataMember.get(0).getTxtNoKTP().toString().equals("null")) {
            etNoKTP.setText("");
        } else {
            etNoKTP.setText(dataMember.get(0).getTxtNoKTP().toString());
        }

        if (dataMember.get(0).getTxtNamaKeluarga().toString().equals("null")) {
            etNamaKeluarga.setText("");
        } else {
            etNamaKeluarga.setText(dataMember.get(0).getTxtNamaKeluarga().toString());
        }

        if (dataMember.get(0).getTxtNamaPanggilan().toString().equals("null")) {
            etNamaPanggilan.setText("");
        } else {
            etNamaPanggilan.setText(dataMember.get(0).getTxtNamaPanggilan().toString());
        }

        if (dataMember.get(0).txtJenisKelamin.equals("Perempuan")) {
            radioPria.setChecked(false);
            radiowanita.setChecked(true);
        } else {
            radioPria.setChecked(true);
            radiowanita.setChecked(false);
        }

        try {
            repoUserMemberImage = new clsUserMemberImageRepo(context);
            dataMemberImage = (List<clsUserMemberImage>) repoUserMemberImage.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (dataMemberImage.size() > 0) {
            viewImage();
        }

        phtImage1 = null;
        phtImage2 = null;

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
                        dataUser.setTxtKontakId(etKontakId.getText().toString());
                        dataUser.setTxtMemberId(etMemberId.getText().toString());
                        dataUser.setTxtNama(etNama.getText().toString());
                        dataUser.setTxtAlamat(etAlamat.getText().toString());
                        dataUser.setTxtNoKTP(etNoKTP.getText().toString());
                        dataUser.setTxtNamaPanggilan(etNamaPanggilan.getText().toString());
                        dataUser.setTxtNamaKeluarga(etNamaKeluarga.getText().toString());
                        dataUser.setTxtBasePoin(etBasePoin.getText().toString());

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
        } else if (requestCode == PIC_CROP2) {
            if (resultCode == -1) {
                //get the returned data
                Bundle extras = data.getExtras();
                //get the cropped bitmap
                Bitmap thePic = extras.getParcelable("data");

                previewCaptureImage2(thePic);
            } else if (resultCode == 0) {
                new clsActivity().showCustomToast(getContext(), "User canceled to capture image", false);
            }
        } else if (requestCode == SELECT_FILE) {
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
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
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

    private void performCropGallery(){
        try {
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(selectedImage, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
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
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
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
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
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

    // select take image from camera or gallery
    private void selectImage1() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(getActivity());
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
                boolean result=Utility.checkPermission(getActivity());
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

    public static class Utility {
        public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public static boolean checkPermission(final Context context)
        {
            int currentAPIVersion = Build.VERSION.SDK_INT;
            if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
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
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , SELECT_FILE);//one can be replaced with any action code
    }

    private void galleryIntent2() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , SELECT_FILE2);//one can be replaced with any action code
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
