package co.uk.inspection.TableClasses;

/**
 * Created by Android on 3/28/2016.
 */
public class Accessories  {

    private String name;
    private int id;


    public boolean iscommentCheck() {
        return iscommentCheck;
    }

    public void setIscommentCheck(boolean iscommentCheck) {
        this.iscommentCheck = iscommentCheck;
    }

    public boolean ispicCheck() {
        return ispicCheck;
    }

    public void setIspicCheck(boolean ispicCheck) {
        this.ispicCheck = ispicCheck;
    }



    private boolean iscommentCheck;
    private boolean ispicCheck;

    public boolean isgoodRadioChecked() {
        return isgoodRadioChecked;
    }

    public void setIsgoodRadioChecked(boolean isgoodRadioChecked) {
        this.isgoodRadioChecked = isgoodRadioChecked;
    }

    public boolean isfairRadioChecked() {
        return isfairRadioChecked;
    }

    public void setIsfairRadioChecked(boolean isfairRadioChecked) {
        this.isfairRadioChecked = isfairRadioChecked;
    }

    public boolean isdirtyRadioChecked() {
        return isdirtyRadioChecked;
    }

    public void setIsdirtyRadioChecked(boolean isdirtyRadioChecked) {
        this.isdirtyRadioChecked = isdirtyRadioChecked;
    }

    public boolean israpiarRadioChecked() {
        return israpiarRadioChecked;
    }

    public void setIsrapiarRadioChecked(boolean israpiarRadioChecked) {
        this.israpiarRadioChecked = israpiarRadioChecked;
    }

    public boolean isreplaceRadioChecked() {
        return isreplaceRadioChecked;
    }

    public void setIsreplaceRadioChecked(boolean isreplaceRadioChecked) {
        this.isreplaceRadioChecked = isreplaceRadioChecked;
    }

    private boolean isgoodRadioChecked;
    private boolean isfairRadioChecked;
    private boolean isdirtyRadioChecked;
    private boolean israpiarRadioChecked;
    private boolean isreplaceRadioChecked;




    public int getId() {
        return id;
    }




    public void setId(int id) {
        this.id = id;
    }



    public Accessories()
    {}

    public Accessories(String name,int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}
