package gallery.os.com.gallery.Model;

import java.util.ArrayList;



public class Directorywise {
    private String DirectoryName;
    private ArrayList<String> DataList;

    public Directorywise(String directoryName, ArrayList<String> dataList) {
        DirectoryName = directoryName;
        DataList = dataList;
    }

    public String getDirectoryName() {
        return DirectoryName;
    }

    public void setDirectoryName(String directoryName) {
        DirectoryName = directoryName;
    }

    public ArrayList<String> getDataList() {
        return DataList;
    }

    public void setDataList(ArrayList<String> dataList) {
        DataList = dataList;
    }


}
