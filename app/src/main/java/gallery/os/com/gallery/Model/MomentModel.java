package gallery.os.com.gallery.Model;

public class MomentModel {
    private String id, name, path;
    private long date;


    public MomentModel(String id, String name, String path, long date) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}