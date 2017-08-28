package kalbefamily.crm.kalbe.kalbefamily;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * Created by Rian Andrivani on 8/28/2017.
 */

public class FragmentDetailPersonalData extends Fragment {
    View v;
    Context context;
    TextView tvNoTelp;
    Button btnNoTelp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_detail_personal_data, container, false);
        context = getActivity().getApplicationContext();

        tvNoTelp = (TextView) v.findViewById(R.id.textViewNoTelp);
        btnNoTelp = (Button) v.findViewById(R.id.btnEdit1);

        btnNoTelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectOption();
            }
        });

        return v;
    }

    private void popupEditNoTelp() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View promptView = layoutInflater.inflate(R.layout.popup_add_edit_data, null);
        final EditText etKontak =(EditText) promptView.findViewById(R.id.etKontak);
        final RadioButton radioButtonPrioritas, radioButtonBknPrioritas, radioButtonActive, radioButtonInActive;
        final RadioGroup radioGenderradioGroupStatus, radioGenderradioGroupPrioritas;
        radioGenderradioGroupStatus = (RadioGroup) v.findViewById(R.id.radioGroupStatus);
        radioGenderradioGroupPrioritas = (RadioGroup) v.findViewById(R.id.radioGroupPrioritas);
        radioButtonActive = (RadioButton) v.findViewById(R.id.radioButtonActive);
        radioButtonInActive = (RadioButton) v.findViewById(R.id.radioButtonInActive);
        radioButtonPrioritas = (RadioButton) v.findViewById(R.id.radioButtonPrioritas);
        radioButtonBknPrioritas = (RadioButton) v.findViewById(R.id.radioButtonBknPrioritas);

//        etKontak.setText(etTelpon.getText().toString());
//        radioButtonActive.setChecked(true);
//        radioButtonPrioritas.setChecked(true);
//        radioButtonInActive.setChecked(false);
//        radioButtonBknPrioritas.setChecked(false);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getContext());
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
//                                etTelpon.setText(etKontak.getText().toString());
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();
    }

    private void selectOption() {
        final CharSequence[] items = { "Tambah Baru", "Edit",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Pilihan");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(getActivity());
                if (items[item].equals("Tambah Baru")) {
                    if(result)
                        dialog.dismiss();
                } else if (items[item].equals("Edit")) {
                    if(result)
                        popupEditNoTelp();
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
}
