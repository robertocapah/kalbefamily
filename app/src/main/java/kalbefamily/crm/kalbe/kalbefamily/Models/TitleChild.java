package kalbefamily.crm.kalbe.kalbefamily.Models;

/**
 * Created by reale on 23/11/2016.
 */

public class TitleChild {

    public String option1;
    public String option2;
    public String txtId;

    public TitleChild(String option1, String option2, String txtId) {
        this.option1 = option1;
        this.option2 = option2;
        this.txtId = txtId;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getTxtId() {
        return txtId;
    }

    public void setTxtId(String txtId) {
        this.txtId = txtId;
    }
}
