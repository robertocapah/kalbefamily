package kalbefamily.crm.kalbe.kalbefamily.Data;

import android.os.Environment;

import java.io.File;

/**
 * Created by Rian Andrivani on 6/22/2017.
 */

public class clsHardCode {
    public String dbName = "KalbeFamily.db";
    public String txtFolderData = Environment.getExternalStorageDirectory()+ File.separator+"Android"+File.separator+"data"+File.separator+"KalbeFamily"+File.separator+"image_Person"+File.separator;
    public String txtFolderAbsen = Environment.getExternalStorageDirectory()+ File.separator+"Android"+File.separator+"data"+File.separator+"KalbeFamily"+File.separator+"image_Person"+File.separator;
}
