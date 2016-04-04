package annotationapp.janastu.org.papadapp.rest;

/**
 * Created by murali on 12/11/15.
 */

import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;


public class AudioAnnotationFile {


    private String id;

    private ArrayList<String> tags = new ArrayList<String>();

    private String uploadDate;

    private String url;

    private String stationName;


    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public AudioAnnotationFile() {
    }

    /**
     *
     * @return
     * The id
     */
     public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
     public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The tagsDirectory
     */
     public ArrayList<String> getTags() {
        return tags;
    }

    /**
     *
     * @param tags
     * The tagsDirectory
     */
     public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    /**
     *
     * @return
     * The uploadDate
     */
     public String getUploadDate() {
        return uploadDate;
    }

    /**
     *
     * @param uploadDate
     * The uploadDate
     */
     public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    /**
     *
     * @return
     * The url
     */
     public String getUrl() {
        return url;
    }

    /**
     *
     * @param url
     * The url
     */
     public void setUrl(String url) {
        this.url = url;
    }




    @Override
    public String toString() {
        return "AudioAnnotationFile{" +
                "id='" + id + '\'' +
                ", tags =" + tags +
                ", uploadDate='" + uploadDate + '\'' +
                ", url='" + url + '\'' +

                '}';
    }
}