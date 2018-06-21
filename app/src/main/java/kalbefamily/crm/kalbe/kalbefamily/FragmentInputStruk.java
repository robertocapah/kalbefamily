package kalbefamily.crm.kalbe.kalbefamily;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import kalbefamily.crm.kalbe.kalbefamily.BL.clsActivity;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsImageStruk;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsSendData;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsToken;
import kalbefamily.crm.kalbe.kalbefamily.Common.clsUserMember;
import kalbefamily.crm.kalbe.kalbefamily.Common.mConfigData;
import kalbefamily.crm.kalbe.kalbefamily.Data.VolleyResponseListener;
import kalbefamily.crm.kalbe.kalbefamily.Data.VolleyUtils;
import kalbefamily.crm.kalbe.kalbefamily.Data.clsHardCode;
import kalbefamily.crm.kalbe.kalbefamily.Data.clsHelper;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsImageStrukRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsTokenRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserImageProfileRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.clsUserMemberRepo;
import kalbefamily.crm.kalbe.kalbefamily.Repo.mConfigRepo;
import kalbefamily.crm.kalbe.kalbefamily.addons.volley.VolleyMultipartRequest;

import static android.app.Activity.RESULT_OK;

/**
 * Created by User on 2/14/2018.
 */

public class FragmentInputStruk extends Fragment {
    View v;
    Context context;
    clsTokenRepo tokenRepo;
    clsUserMemberRepo repoUserMember;
    clsImageStrukRepo repoImageStruk;
    List<clsToken> dataToken;
    List<clsUserMember> dataMember;
    List<clsImageStruk> dataImageStruk;

    CircleImageView ivStruk;
    FloatingActionButton addImage;
    Button btnUploadGambar;
    private Uri uriImage, selectedImage;
    private static final int CAMERA_REQUEST = 120;
    private static Bitmap photoImage, mybitmap;
    private static ByteArrayOutputStream output = new ByteArrayOutputStream();
    private static byte[] phtImage, phtImageNew;
    private Bitmap bitmap, thePic;
    final int SELECT_FILE = 1;
    final int PIC_CROP = 2;
    private String txtKontakID, txtMember, access_token;
    String linkImageStruk = "null";
    private static final String IMAGE_DIRECTORY_NAME = "Image Struk KF";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_input_struk, container, false);
        context = getActivity().getApplicationContext();
        ivStruk = (CircleImageView) v.findViewById(R.id.image_struk);
        addImage = (FloatingActionButton) v.findViewById(R.id.add_image_struk);
        btnUploadGambar = (Button) v.findViewById(R.id.btnUploadGambar);

        try {
            repoUserMember = new clsUserMemberRepo(context);
            dataMember = (List<clsUserMember>) repoUserMember.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        ivStruk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phtImage != null) {
                    File file = new File(Environment.getExternalStorageDirectory() + File.separator + "ImageStruk" + ".png");
                    file.delete();
                    FileOutputStream fOut = null;
                    try {
                        fOut = new FileOutputStream(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    mybitmap = ((BitmapDrawable)ivStruk.getDrawable()).getBitmap();
                    mybitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
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
                    intent.putExtra("gambar struk", "ImageStruk");
                    startActivity(intent);
                }
            }
        });

        btnUploadGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bitmapUpload != null) {
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                    alertDialog.setTitle("Konfirmasi");
                    alertDialog.setMessage("Apakah Anda yakin?");
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            bitmapToByte();
                            saveImageStrukNew();
                        }
                    });
                    alertDialog.setNegativeButton("BATAL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    });
                    alertDialog.show();
                } else {
                    new clsActivity().showToast(context, "Add Photo terlebih dahulu", false);
                }


                //sendData();
            }
        });

        if (photoImage != null){
            ivStruk.setImageBitmap(photoImage);
            photoImage.compress(Bitmap.CompressFormat.PNG, 100, output);
            phtImage = output.toByteArray();
        }

        return v;
    }

    private void bitmapToByte() {
        ProgressDialog dialog2 = new ProgressDialog(getActivity(), ProgressDialog.STYLE_SPINNER);
        dialog2.setIndeterminate(true);
        dialog2.setMessage("Refresh Data...");
        dialog2.setCancelable(false);
        dialog2.show();
        output = null;
        if(bitmapUpload!=null){
            try {
                output = new ByteArrayOutputStream();
                bitmapUpload.compress(Bitmap.CompressFormat.PNG, 100, output);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (output != null){
                        output.close();
                        dialog2.dismiss();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            phtImageNew = output.toByteArray();
//
//            //b is the Bitmap
//
//            //calculate how many bytes our image consists of.
//            int bytes = bitmapUpload.getByteCount();
//            //or we can calculate bytes this way. Use a different value than 4 if you don't use 32bit images.
//            //int bytes = b.getWidth()*b.getHeight()*4;
//
//            ByteBuffer buffer = ByteBuffer.allocate(bytes); //Create a new buffer
//            bitmapUpload.copyPixelsFromBuffer(buffer); //Move the byte data to the buffer
//
////            byte[] array = buffer.array(); //Get the underlying array containing the data.
//            phtImageNew = buffer.array();

//            phtImageNew = convertBitmapToByteArrayUncompressed(bitmapUpload);
//            Bitmap bitmap = BitmapFactory.decodeByteArray(phtImageNew, 0, phtImageNew.length);
//            Bitmap bitmapw = BitmapFactory.decodeByteArray(phtImageNew, 0, phtImageNew.length);
        }
    }

    public static byte[] convertBitmapToByteArrayUncompressed(Bitmap bitmap){
        ByteBuffer byteBuffer = ByteBuffer.allocate(bitmap.getByteCount());
        bitmap.copyPixelsToBuffer(byteBuffer);
        byteBuffer.rewind();
        return byteBuffer.array();
    }

    public  Bitmap rotateImageIfRequired(String imagePath) {
        int degrees = 0;

        try {
            ExifInterface exif = new ExifInterface(imagePath);
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degrees = 90;
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    degrees = 180;
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    degrees = 270;
                    break;
            }
        } catch (IOException e) {
            Log.e("ImageError", "Error in reading Exif data of " + imagePath, e);
        }

        BitmapFactory.Options decodeBounds = new BitmapFactory.Options();
        decodeBounds.inJustDecodeBounds = true;

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, decodeBounds);
        int numPixels = decodeBounds.outWidth * decodeBounds.outHeight;
        int maxPixels = 2048 * 1536; // requires 12 MB heap

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = (numPixels > maxPixels) ? 2 : 1;

        bitmap = BitmapFactory.decodeFile(imagePath, options);

        if (bitmap == null) {
            return null;
        }

        Matrix matrix = new Matrix();
        matrix.setRotate(degrees);

        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);

        try {
            FileOutputStream out = new FileOutputStream(getOutputMediaFile());
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private Bitmap bitmapUpload = null;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST) {
            if (resultCode == -1) {
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    String uri = uriImage.getPath().toString();

                    bitmapUpload = BitmapFactory.decodeFile(uri, bitmapOptions);

//                    output = null;
//                    try {
//                        output = new ByteArrayOutputStream();
//                        bitmap.compress(Bitmap.CompressFormat.PNG, 0, output);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    } finally {
//                        try {
//                            if (output != null){
//                                output.close();
//                            }
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    phtImageNew = output.toByteArray();

//                    rotateImageIfRequired(uri);


                    performCrop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (resultCode == 0) {
                new clsActivity().showCustomToast(getContext(), "User batal mengambil gambar", false);
            }  else {
                try {
                    photoImage = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), data.getData());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else if (requestCode == PIC_CROP) {
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

                previewCaptureImage(thePic);
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
//                    String uri = selectedImage.getPath().toString();
//                    bitmapUpload = BitmapFactory.decodeFile(uri, bitmapOptions);

                    Uri selectedImage = data.getData();
                    bitmapUpload = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);

                    performCropGallery();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void selectImage() {
        final CharSequence[] items = { "Ambil Foto", "Pilih dari Galeri",
                "Batal" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Tambah Foto Profile");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= FragmentNewPersonalData.Utility.checkPermission(getActivity());
                if (items[item].equals("Ambil Foto")) {
                    if(result)
                        captureImage();
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

    private void captureImage() {
        uriImage = getOutputMediaFileUri();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriImage);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    private void galleryIntent() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , SELECT_FILE);//one can be replaced with any action code
    }

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
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriImage);
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
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImage);
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

    private void previewCaptureImage(Bitmap photo){
        try {
            //Bitmap bitmap = new clsActivity().resizeImageForBlob(photo);
            ivStruk.setVisibility(View.VISIBLE);
            output = null;
            try {
                output = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 0, output);
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
            Bitmap photo_view = Bitmap.createScaledBitmap(photo, 150, 150, true);
            phtImage = output.toByteArray();
            ivStruk.setImageBitmap(photo);

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
    
    private clsImageStruk data = null;

    private void saveImageStrukNew() {
        try {
            repoImageStruk = new clsImageStrukRepo(context);
            dataImageStruk = (List<clsImageStruk>) repoImageStruk.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (dataImageStruk.size() == 0) {
            if (phtImageNew != null) {
                data = new clsImageStruk();
                data.setTxtGuiId(new clsActivity().GenerateGuid());
                data.setTxtKontakId(dataMember.get(0).txtKontakId);
                data.setTxtImg(phtImageNew);

                repoImageStruk.createOrUpdate(data);
            }
        } else {
            for (clsImageStruk imgStruk : dataImageStruk) {
                if (phtImageNew != null) {
                    data = new clsImageStruk();
                    data.setTxtGuiId(imgStruk.getTxtGuiId());
                    data.setTxtKontakId(dataMember.get(0).txtKontakId);
//                    data.setTxtImg(phtImage);

                    repoImageStruk.createOrUpdate(data);
                }
            }
        }
        //Toast.makeText(context, "Success save to SQLite", Toast.LENGTH_SHORT).show();
        sendData();
    }

    private void saveImageStruk() {
        try {
            repoImageStruk = new clsImageStrukRepo(context);
            dataImageStruk = (List<clsImageStruk>) repoImageStruk.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (dataImageStruk.size() == 0) {
            if (phtImage != null) {
                data = new clsImageStruk();
                data.setTxtGuiId(new clsActivity().GenerateGuid());
                data.setTxtKontakId(dataMember.get(0).txtKontakId);
                data.setTxtImg(phtImage);

                repoImageStruk.createOrUpdate(data);
            }
        } else {
            for (clsImageStruk imgStruk : dataImageStruk) {
                if (phtImage != null) {
                    data = new clsImageStruk();
                    data.setTxtGuiId(imgStruk.getTxtGuiId());
                    data.setTxtKontakId(dataMember.get(0).txtKontakId);
                    data.setTxtImg(phtImage);

                    repoImageStruk.createOrUpdate(data);
                }
            }
        }
        //Toast.makeText(context, "Success save to SQLite", Toast.LENGTH_SHORT).show();
        sendData();
    }


    private void viewImageStruk() {
        linkImageStruk = getArguments().getString("imageStruk");
        if (linkImageStruk != "null") {
            final ProgressDialog dialog2 = new ProgressDialog(getActivity(), ProgressDialog.STYLE_SPINNER);
            dialog2.setIndeterminate(true);
            dialog2.setMessage("Refresh Data...");
            dialog2.setCancelable(false);
            dialog2.show();

            Picasso.with(getContext()).load(linkImageStruk)
                    .placeholder(R.drawable.profile)
                    .error(R.drawable.profile)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .fit()
                    .into(ivStruk, new Callback() {
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
    }

    private void sendData() {

        String guuid= new clsActivity().GenerateGuid();
        clsSendData dtJson = new clsHelper().sendDataInputStrukNew(context, phtImageNew, guuid);
        if (dtJson != null) {
            try {
                String strLinkAPI = new clsHardCode().linkInputStruk;
                final String mRequestBody = "[" + dtJson.toString() + "]";

                volleyRequestSendData(strLinkAPI, dtJson, new VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, Boolean status, String strErrorMsg) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsn = jsonObject.getJSONObject("validJson");
                            String warn = jsn.getString("TxtMessage");
                            String result = jsn.getString("IntResult");
                            String res = response;

                            if (result.equals("1")) {
                                new clsActivity().showToast(context.getApplicationContext(), warn, true);
                                FragmentInfoContact ContactFragment = new FragmentInfoContact();
                                FragmentTransaction fragmentTransactionHome = getActivity().getSupportFragmentManager().beginTransaction();
                                fragmentTransactionHome.replace(R.id.frame, ContactFragment);
                                fragmentTransactionHome.commit();
                            } else {
                                new clsActivity().showToast(context.getApplicationContext(), warn, false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
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
            tokenRepo = new clsTokenRepo(context);
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
                                        sendData();
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
                    final String mRequestBody2 = "[" +  mRequestBody.getDtdataJson().txtJSONInputStruk().toString() + "]";
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
                if (mRequestBody.getFileUpload().get("input_struk") != null){
                    params.put("ImageStruk.jpg", new DataPart("ImageStruk.jpg", mRequestBody.getFileUpload().get("input_struk"), "image/jpeg"));
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
}
