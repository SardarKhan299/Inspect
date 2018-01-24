package co.uk.inspection.TableClasses;

/**
 * Created by Android on 5/11/2016.
 */
public class AccessoryData {

    long id ;
    String asc_name;
    String quality;
    String condition;
    String comments;
    int area_data_id;
    String area_name;

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }



    public int getArea_data_id() {
        return area_data_id;
    }

    public void setArea_data_id(int area_data_id) {
        this.area_data_id = area_data_id;
    }




    public AccessoryData(long id, String asc_name, String quality, String condition, String comments) {
        this.id = id;
        this.asc_name = asc_name;
        this.quality = quality;
        this.condition = condition;
        this.comments = comments;
    }

    public AccessoryData()
    {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAsc_name() {
        return asc_name;
    }

    public void setAsc_name(String asc_name) {
        this.asc_name = asc_name;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }





}
