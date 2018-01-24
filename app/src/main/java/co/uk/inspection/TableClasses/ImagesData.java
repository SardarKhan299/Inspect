package co.uk.inspection.TableClasses;

/**
 * Created by Android on 5/17/2016.
 */
public class ImagesData {
     int id ;
    String name ;
    String path;
    String AscName;


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAscName() {
        return AscName;
    }

    public void setAscName(String ascName) {
        AscName = ascName;
    }

    String time;

    public ImagesData(int id, String name, String path) {
        this.id = id;
        this.name = name;
        this.path = path;
    }

    public ImagesData() {

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }




}
