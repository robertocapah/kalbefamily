package kalbefamily.crm.kalbe.kalbefamily.ViewHolders;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;

import kalbefamily.crm.kalbe.kalbefamily.R;


/**
 * Created by reale on 23/11/2016.
 */

public class TitleChildViewHolder extends ChildViewHolder {
    public TextView option1,option2;
    public Button button;

    public TitleChildViewHolder(View itemView) {
        super(itemView);
        option1 = (TextView)itemView.findViewById(R.id.option1);
        option2 = (TextView)itemView.findViewById(R.id.option2);
        button = (Button) itemView.findViewById(R.id.buttonPilih);
    }
}
