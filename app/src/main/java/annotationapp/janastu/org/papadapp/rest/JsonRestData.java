package annotationapp.janastu.org.papadapp.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by murali on 12/11/15.
 */
public class JsonRestData {

    public JsonRestData() {
    }

    private List<AudioAnnotationFile> files = new ArrayList<AudioAnnotationFile>();



    public List<AudioAnnotationFile> getFiles() {
        return files;
    }

    public void setFiles(List<AudioAnnotationFile> files) {
        this.files = files;
    }
}
