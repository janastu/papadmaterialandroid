package annotationapp.janastu.org.papadapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ns.developer.tagview.entity.Tag;
import com.ns.developer.tagview.widget.TagCloudLinkView;

import com.plumillonforge.android.chipview.Chip;
import com.plumillonforge.android.chipview.ChipView;
import com.plumillonforge.android.chipview.OnChipClickListener;

import java.util.ArrayList;
import java.util.List;

import annotationapp.janastu.org.papadapp.pojo.AnnotTag;
import annotationapp.janastu.org.papadapp.rest.AudioAnnotationFile;

/**
 * Created by Graphics-User on 12/20/2015.
 */
public class AnnotationFileAdapter extends RecyclerView.Adapter < AnnotationFileAdapter.ViewHolder > {
    private static final String LOGGER ="AnnotationFileAdapter" ;
    private List < AudioAnnotationFile > audioAnnotationFileList;
    List<Chip> annotList = new ArrayList<Chip>();


    Context context;
    TagClickedAdapterCallback adaptor;

    public AnnotationFileAdapter(List < AudioAnnotationFile > annotationFileList, Context mainActivity ) {

        audioAnnotationFileList = annotationFileList;
        context = mainActivity;
    }

    public List<AudioAnnotationFile> getAudioAnnotationFileList() {
        return audioAnnotationFileList;
    }

    public void setAudioAnnotationFileList(List<AudioAnnotationFile> audioAnnotationFileListFresh) {



        this.audioAnnotationFileList = audioAnnotationFileListFresh;
        Log.d(LOGGER, "added list"+ audioAnnotationFileList.toString());
        notifyDataSetChanged();


    }

    public TagClickedAdapterCallback getAdaptor() {
        return adaptor;
    }

    public void setTagAdaptor(TagClickedAdapterCallback adaptor) {
        this.adaptor = adaptor;
    }

    RippleDrawable rippleDrawable;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, null);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {


        Log.d("S", "onBindViewHolderPOSITION " + position);
        viewHolder.cardView.setClickable(true);
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        String url = audioAnnotationFileList.get(position).getUrl();
        String title =  url.substring(url.lastIndexOf("/") + 1) ;
        viewHolder.annotationfile_Name.setText(title);
        Log.d(LOGGER, "UPLOAD title" + title);
//        viewHolder.annotationfile_Id.setText(audioAnnotationFileList.get(position).getId());
        Log.d("S", "UPLOAD DATE" + audioAnnotationFileList.get(position).getUploadDate().toString());
        viewHolder.annotationfile_Date.setText(audioAnnotationFileList.get(position).getUploadDate().toString());
        viewHolder.tagAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //pass URL
                //pass ID
                // pass tags

                AudioAnnotationFile f2 = audioAnnotationFileList.get(position);
                Intent intent = new Intent(context, ListenAudioPostTagCardActivity.class);
                Bundle b = new Bundle();
                b.putString("keyURL", f2.getUrl()); //Your id
                b.putString("keyID", f2.getId()); //Your id
                b.putStringArrayList("tagList", f2.getTags()); //Your id
                intent.putExtras(b); //Put your id to your next Intent
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);


            }
        });

        annotList.clear();
        int sizeOfTags = audioAnnotationFileList.get(position).getTags().size();
        ArrayList<String> allTags = audioAnnotationFileList.get(position).getTags();

        final  Chip one = new AnnotTag ("SSS");
        for(  int j = 0 ; j < sizeOfTags ; j++ )
        {

            Log.d(LOGGER, "ADDING TAG" + allTags.get(j));

            //viewHolder.annotationfile_Tags.add(new Tag(j, allTags.get(j)));

            annotList.add(new AnnotTag(allTags.get(j),j));
        }


        viewHolder.annotationfile_Tags.setChipList(annotList);



        viewHolder.annotationfile_Tags.setOnChipClickListener(new OnChipClickListener() {
            @Override
            public void onChipClick(Chip chip) {
                // Action here !

                Log.d("clicked", chip.getText());
                Log.d(LOGGER, "SELECTED TAG" + chip.getText());

                adaptor.onTagSelectedMethodCallback(chip.getText());
            }
        });

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        public ImageView imgViewIcon;
        public CardView cardView;
        public Button tagAudio;
        public ChipView annotationfile_Tags;
        public TextView annotationfile_Name;
        public TextView annotationfile_Date;
        public TextView annotationfile_Id;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            cardView = (CardView) itemLayoutView.findViewById(R.id.cv);
         //   annotationfile_Id = (TextView) itemLayoutView.findViewById(R.id.annotationfile_id);
            annotationfile_Tags = (ChipView) itemLayoutView.findViewById(R.id.annotationfile_tags);
            annotationfile_Name = (TextView) itemLayoutView.findViewById(R.id.annotationfile_name);
            annotationfile_Date = (TextView) itemLayoutView.findViewById(R.id.annotationfile_uploadDate);
            tagAudio = ( Button)itemLayoutView.findViewById(R.id.tagButton);
        }
    }

    @Override
    public int getItemCount() {
        if(audioAnnotationFileList!=null)
        return audioAnnotationFileList.size();
        else
            return 0;
    }
}