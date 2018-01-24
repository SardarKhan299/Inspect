package co.uk.inspection.TableClasses;

/**
 * Created by Android on 3/24/2016.
 */
public class Areas implements Comparable{

    public  Areas() {
    }

    // set a boolaen to check if the image in the listView single row is checked or not checked
    public boolean ischecked() {
        return ischecked;
    }

    public void setIschecked(boolean ischecked) {
        this.ischecked = ischecked;
    }

    private boolean ischecked;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;


    public Areas(String name) {
        this.name = name;
    }


    @Override
    public int compareTo(Object another) {
        return 0;
    }
}
