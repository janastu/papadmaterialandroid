package annotationapp.janastu.org.papadapp.pojo;

import com.plumillonforge.android.chipview.Chip;

/**
 * Created by Graphics-User on 12/25/2015.
 */
public class AnnotTag implements Chip {
    private String mName;
    private int mType = 0;

    public AnnotTag(String name, int type) {
        this(name);
        mType = type;
    }

    public AnnotTag(String name) {
        mName = name;
    }

    @Override
    public String getText() {
        return mName;
    }

    public int getType() {
        return mType;
    }
}