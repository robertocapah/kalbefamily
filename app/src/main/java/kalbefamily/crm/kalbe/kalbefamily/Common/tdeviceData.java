package kalbefamily.crm.kalbe.kalbefamily.Common;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by rhezaTesar on 3/13/2017.
 */
@Table(name = "Mobile_MPartnerProfile")
public class tdeviceData extends Model {

    @Column(name = "txtGUIDeviceId")
    public String txtGUIDeviceId;
    public final String ConsttxtGUIDeviceId  = "txtGUIDeviceId";

    @Column(name = "txtVersion")
    public String txtVersion;
    public final String ConsttxtVersion  = "txtVersion";

    @Column(name = "txtModel")
    public String txtModel;
    public final String ConsttxtModel  = "txtModel";

    @Column(name = "txtDevice")
    public String txtDevice;
    public final String ConsttxtDevice  = "txtDevice";

    @Column(name = "txtSessionVersion")
    public String txtSessionVersion;
    public final String ConsttxtSessionVersion  = "txtSessionVersion";
}
