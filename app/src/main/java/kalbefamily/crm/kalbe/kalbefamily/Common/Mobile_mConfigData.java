package kalbefamily.crm.kalbe.kalbefamily.Common;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by rhezaTesar on 3/13/2017.
 */
@Table(name = "Mobile_mConfigData")
public class Mobile_mConfigData extends Model {
    @Column(name = "idConfig")
    public Long idConfig;

    @Column(name = "txtName")
    public String txtName;

    @Column(name = "txtValue")
    public String txtValue;

    @Column(name = "txtDefaultValue")
    public String txtDefaultValue;
}
