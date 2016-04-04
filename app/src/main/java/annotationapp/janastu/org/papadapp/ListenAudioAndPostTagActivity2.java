package annotationapp.janastu.org.papadapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ns.developer.tagview.entity.Tag;
import com.ns.developer.tagview.widget.TagCloudLinkView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import annotationapp.janastu.org.papadapp.services.TagDictionary;
import annotationapp.janastu.org.papadapp.rest.AudioAnnotationFile;
import annotationapp.janastu.org.papadapp.rest.DownloadModel;
import annotationapp.janastu.org.papadapp.rest.HtmlParser;
import annotationapp.janastu.org.papadapp.rest.PostTagOperation;

import  com.phillipcalvin.iconbutton.IconButton;
public class ListenAudioAndPostTagActivity2 extends Activity implements SearchView.OnQueryTextListener, View.OnClickListener, View.OnTouchListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener {


    private static String LOGGER = "ListenAudioAndPostTagActivity2";
    private SearchView mSearchView;
    private ListView mListView;
    Context context;
    String selectedTag;
    boolean toggleSearch = false;

    private final List<String> mStrings = new TagDictionary().getTagsDirectory();
    private ArrayList<String> tagRef;
    private LinearLayout linearLayout3;
    private TextView audioTitleOrName;
    private TextView audioUrlTextView;

    //MEDIA PLAYER
    private IconButton buttonPlayPause;
    private IconButton buttonDisLikeButton;
    private IconButton  buttonDownload;
    private IconButton  buttonLikeButton;
    private IconButton buttonAddTagButton;
    //

    private SeekBar seekBarProgress;

    private SeekBar seekBarProgressForDownload;

    private MediaPlayer mediaPlayer;
    private int mediaFileLengthInMilliseconds; // this value contains the song duration in milliseconds. Look at getDuration() method in MediaPlayer class
    private final Handler handler = new Handler();

    List<DownloadModel> downloads;
    ListView downLoadProgressFileList;

    boolean isSearchWinVisible = false;
    private LinearLayout downLoadProgressLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_audio_and_post_tag2);

        // setContentView(R.layout.ly_player);




       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add tag - minimum chars 10", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


            }
        });*/

        //   Drawable myDrawable;
        Context context = getApplicationContext();
        linearLayout3 = (LinearLayout) findViewById(R.id.addTagLayout);
        downLoadProgressLayout = (LinearLayout) findViewById(R.id.downLoadProgressLayout);
        
        //View child = new TextView(this);

        Bundle b = getIntent().getExtras();
        tagRef          = b.getStringArrayList("tagList");




        TagCloudLinkView view = (TagCloudLinkView) findViewById(R.id.test);

        for(String t :tagRef)
            view.add(new Tag(1, t));


        view.drawTags();




        mSearchView = (SearchView) findViewById(R.id.search_view);
        mListView = (ListView) findViewById(R.id.list_view);
        mListView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                mStrings));
        mListView.setTextFilterEnabled(true);
        mSearchView.setSubmitButtonEnabled(true);
        LinearLayout linearLayoutOfSearchView = (LinearLayout) mSearchView.getChildAt(0);

        String keyID = b.getString("keyID");
        String url = b.getString("keyURL");

        audioUrlTextView = (TextView) findViewById(R.id.audioUrl);

        audioUrlTextView.setText(url);
        audioUrlTextView.setTextColor(getResources().getColor(R.color.white));
        audioTitleOrName = (TextView) findViewById(R.id.audioTitleOrName);
        audioTitleOrName.setText(keyID);
        initMediaLayout();
        Button yourButton = new Button(this); // and do whatever to your button

        yourButton.setText("ADD TAG");
        linearLayoutOfSearchView.addView(yourButton);

        yourButton.setOnClickListener(new View.OnClickListener() {@Override
                                                                  public void onClick(View v) {
            ///selectedTag
            Log.d(LOGGER, "findinfg" + selectedTag);


            PostTagOperation postTagOperation = new PostTagOperation();
            postTagOperation.setId(audioTitleOrName.getText().toString());
            postTagOperation.setTag(selectedTag);
            postTagOperation.execute();
          //  Toast.makeText(getBaseContext(), selectedTag, Toast.LENGTH_LONG).show();
        }
        });


        setupSearchView();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {@Override
                                                                                public void onItemClick(AdapterView <? > parent, View view, int position,
                                                                                                        long id) {

            String item = ((TextView) view).getText().toString();

            //  Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG).show();
            // mSearchView.set.setFilterText(item.toString());

            mSearchView.setQuery(item, false);
        }
        });
    }

    public void initMediaLayout()
    {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);
        buttonPlayPause = (IconButton)findViewById(R.id.ButtonTestPlayPause);
        //buttonPlayPause.setImageResource(R.drawable.play_button);
        buttonPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlayClick();

            }
        });

        buttonDownload = (IconButton)findViewById(R.id.DownloadButton);
      //  buttonDownload.setImageResource((R.drawable.buttondownload2));
        buttonDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDownloadClick();

            }
        });


        buttonLikeButton = (IconButton)findViewById(R.id.LikeButton);
     //   buttonLikeButton.setImageResource(R.drawable.buttonlike);
        buttonLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onLikeClick( );

            }
        });

        buttonDisLikeButton = (IconButton)findViewById(R.id.DisLikeButton);
    //    buttonDisLikeButton.setImageResource(R.drawable.buttondislike);
        buttonDisLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onDisLikeClick( );

            }
        });

        buttonAddTagButton = (IconButton)findViewById(R.id.AddTagButton);
      // buttonAddTagButton.setImageResource(R.drawable.add_tag_button);
        buttonAddTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toggleSearch) {
                    linearLayout3.setVisibility(LinearLayout.GONE);

                    downLoadProgressLayout.setVisibility(LinearLayout.VISIBLE);
                    //make all windows visible
                    toggleSearch = false;
                } else {

                    linearLayout3.setVisibility(LinearLayout.VISIBLE);
                    downLoadProgressLayout.setVisibility(LinearLayout.GONE);
                    //hide other window
                    toggleSearch = true;

                }

            }
        });

        seekBarProgress = (SeekBar)findViewById(R.id.seek_bar);
        seekBarProgress.setMax(99); // It means 100% .0-99

        seekBarProgressForDownload = (SeekBar) findViewById(R.id.downLoadSeekBar);
        seekBarProgressForDownload.setMax(99);
    }
    String title;
    private void onDownloadClick() {

        DownloadModel model;
        //for (String key : tb.keySet()) {
        String url = audioUrlTextView.getText().toString();

        title =  url.substring(url.lastIndexOf("/") + 1) ;
        model = new DownloadModel(title,url);
    //    downloads.clear();
      //  downloads.add(model);


        DownloadFilesTask executor = new DownloadFilesTask();
        executor.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, model);

 }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_play_media_and_tag, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupSearchView() {
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Search Here");
    }

    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            mListView.clearTextFilter();
        } else {
            mListView.setFilterText(newText.toString());
        }

        selectedTag = newText;
        return true;
    }


    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    //MEDIA FILES
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(v.getId() == R.id.seek_bar){
            /** Seekbar onTouch event handler. Method which seeks MediaPlayer to seekBar primary progress position*/
            if(mediaPlayer.isPlaying()){
                SeekBar sb = (SeekBar)v;
                int playPositionInMillisecconds = (mediaFileLengthInMilliseconds / 100) * sb.getProgress();
                mediaPlayer.seekTo(playPositionInMillisecconds);
            }
        }
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        /** MediaPlayer onCompletion event handler. Method which calls then song playing is complete*/
       // buttonPlayPause.setImageResource(R.drawable.play_button);
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        /** Method which updates the SeekBar secondary progress by current song loading from URL position*/
        seekBarProgress.setSecondaryProgress(percent);
    }

    @Override
    protected void onStop() {

        super.onStop();
        if(mediaPlayer.isPlaying())
            mediaPlayer.stop();

    }


    public void onPlayClick( ) {

        Log.d(LOGGER,"ON CLICK");


            Log.d(LOGGER,"CLICKED PLAY BUTTON");
            /** ImageButton onClick event handler. Method which start/pause mediaplayer playing */
            try {
                Log.d(LOGGER,"CLICKED PLAY BUTTON" +audioUrlTextView.getText().toString());

                String url = audioUrlTextView.getText().toString();

                title =  url.substring(url.lastIndexOf("/") + 1) ;

                String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath();
                File folder = new File(path, "/NCTDownloader");


                File musicFile = new File(folder.getAbsolutePath(), title);
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaPlayer.start();
                        mediaFileLengthInMilliseconds = mediaPlayer.getDuration(); // gets the song length in milliseconds from URL
                        primarySeekBarProgressUpdater();
                    }
                });
                if(musicFile.exists()) {
                    Log.d(LOGGER, "CLICKED PLAY BUTTON musicFileExists" + musicFile.getAbsolutePath() );
                   // mediaPlayer.setDataSource(musicFile.getPath());
                    Toast.makeText(getApplicationContext(),"Playing Existing Music File ",Toast.LENGTH_LONG).show();
                  //  String filePath = Environment.getExternalStorageDirectory()+"/NCTDownloader"+ title;

                    if(!mediaPlayer.isPlaying())
                    {

                        try {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(musicFile.getAbsolutePath());
                            mediaPlayer.prepareAsync();
                        } catch (IllegalStateException e) {

                            Log.d(LOGGER, "exception" +e );
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(musicFile.getAbsolutePath());

                        }
                    }


                } else {

                    Log.d(LOGGER, "CLICKED PLAY BUTTON music File NOT Existing Downloading" + musicFile.getAbsolutePath());

                    if(!mediaPlayer.isPlaying()) {

                        try {
                            mediaPlayer.setDataSource(audioUrlTextView.getText().toString());
                            mediaPlayer.prepareAsync();
                        } catch (IllegalStateException e) {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(audioUrlTextView.getText().toString());
                            mediaPlayer.prepareAsync();
                        }

                    }

                    Toast.makeText(getApplicationContext(),"Streaming from Internet Music File ",Toast.LENGTH_LONG).show();
                }

               // you must call this method after setup the datasource in setDataSource method. After calling prepare() the instance of MediaPlayer starts load data from URL to internal buffer.
            } catch (Exception e) {
                e.printStackTrace();
            }



            if(!mediaPlayer.isPlaying()){
              //  mediaPlayer.start();

                //mediaPlayer.prepareAsync();
                buttonPlayPause.setText("PAUSE");

                  //buttonPlayPause.se(R.drawable.button_pause);
            }else {
                mediaPlayer.pause();
                buttonPlayPause.setText("PLAY");
          //      buttonPlayPause.setImageResource(R.drawable.play_button);
            }

        primarySeekBarProgressUpdater();

    }



    /** Method which updates the SeekBar primary progress by current song playing position*/
    private void primarySeekBarProgressUpdater() {
        seekBarProgress.setProgress((int)(((float)mediaPlayer.getCurrentPosition()/mediaFileLengthInMilliseconds)*100)); // This math construction give a percentage of "was playing"/"song length"
        if (mediaPlayer.isPlaying()) {
            Runnable notification = new Runnable() {
                public void run() {
                    primarySeekBarProgressUpdater();
                }
            };
            handler.postDelayed(notification, 1000);
        }
    }

    @Override
    public void onClick(View v) {

    }



    private class DownloadFilesTask extends AsyncTask<DownloadModel, Integer, Void> {
        DownloadModel myModel;
        private double fileLength;
        private String status;
        int progress_status =0;
        @Override
        protected Void doInBackground(DownloadModel... models) {
            myModel = models[0];
            progress_status = myModel.getProcess();
            String downloadLink;
            Log.d("DownloadFilesTask", "Has Download Link: " + myModel.hasDownloadLink());
            if (!myModel.hasDownloadLink()) {
                downloadLink = getDownloadLink(myModel.getUrl());
            }
            else {
                downloadLink = myModel.getUrl();
            }
            Log.d("DownloadFilesTask", "Download link: " + downloadLink);
            //Downloading
            if (downloadLink != null) {
                HttpURLConnection connection = null;
                String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath();
                File folder = new File(path, "/NCTDownloader");
                if (!folder.exists()) {
                    if (folder.mkdir()) {
                        Log.d(LOGGER, "[Directory]: Create a folder named   "+ folder.getAbsolutePath());
                    }
                }
                String music = "/" + myModel.getTitle() ;
                File musicFile = new File(folder.getAbsolutePath(), music);
                if (!musicFile.exists()) {
                    try {
                        musicFile.createNewFile();

                        Log.d(LOGGER, "[Directory]: Create a musicFile named   " + musicFile.getAbsolutePath());
                        URL link = new URL(downloadLink);
                        connection = (HttpURLConnection) link.openConnection();
                        connection.connect();
                        int fileLength = connection.getContentLength();
                        this.fileLength = Math.round(fileLength/1000000.00);
                        InputStream is = connection.getInputStream();
                        FileOutputStream out = new FileOutputStream(musicFile);
                        int nRead;
                        long total = 0;

                        byte[] data = new byte[16384];
                        while ((nRead = is.read(data, 0, data.length)) != -1) {
                            total += nRead;
                            progress_status = (int) total*100/fileLength;
                           // System.out.println(progress_status);
                            Log.d(LOGGER, "progress_status" + progress_status);

                            publishProgress(progress_status);
                            out.write(data, 0, nRead);
                        }
                        out.flush();
                        out.close();
                        is.close();
                        //System.out.printf("[%s]: Achieved the song '%s'(%s)\n", Thread.currentThread().getName(), myModel.getTitle());
                    } catch (MalformedURLException e) {
                        Log.d(LOGGER, "[Download Music] ---> Error while connecting to network");
                        e.printStackTrace();
                    } catch (IOException e) {
                        Log.d(LOGGER, "[Download Music] ---> Error while reading and writting input stream");
                        e.printStackTrace();
                    } finally {
                        if(connection != null) { connection.disconnect(); }
                    }
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            status = fileLength + "MB  -  " + values[0] + "%";
            myModel.setProcess(values[0]);
            myModel.setStatus(status);
            seekBarProgressForDownload.setProgress(progress_status); // This math construction give a percentage of "was playing"/"song length"
            Log.d(LOGGER, "onProgressUpdate progress_status" + progress_status);
        }

        private String getDownloadLink(String url) {
            Log.d(LOGGER, "  Start getting the download link for the song" + Thread.currentThread().getName()+ myModel.getTitle());
            String xmlLink = null;
            HttpURLConnection connection = null;
            try {
                URL link = new URL(url);
                connection = (HttpURLConnection) link.openConnection();
                connection.connect();
                InputStream is = connection.getInputStream();
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                int nRead;
                byte[] data = new byte[16384];
                while ((nRead = is.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }
                buffer.flush();
                buffer.close();
                is.close();
                String content = new String(buffer.toByteArray());
                xmlLink = HtmlParser.parseToGetTheXMLLink(content);
                Log.d(LOGGER," Achieved the xml link for the song '%s'(%s)"+ Thread.currentThread().getName()+ myModel.getTitle()+ xmlLink);
            } catch (MalformedURLException e) {
                Log.d(LOGGER, "[Getting xml link] ---> Error while connecting to network: "  );
                e.printStackTrace();
            } catch (IOException e) {
                Log.d(LOGGER, "[Getting xml link] ---> Error while reading input stream");
                e.printStackTrace();
            } finally {
                if(connection != null) { connection.disconnect(); }
            }

            String downloadLink = null;
            if (xmlLink != null) {
                try {
                    URL link = new URL(xmlLink);
                    connection = (HttpURLConnection) link.openConnection();
                    connection.connect();
                    InputStream is = connection.getInputStream();
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                    int nRead;
                    byte[] data = new byte[16384];
                    while ((nRead = is.read(data, 0, data.length)) != -1) {
                        buffer.write(data, 0, nRead);
                    }
                    buffer.flush();
                    buffer.close();
                    is.close();
                    String xmlContent = new String(buffer.toByteArray());
                    downloadLink = HtmlParser.parseToGetTheDownloadLink(xmlContent);
                   // Log.d(LOGGER, "[%s]: Achieved the download link for the song '%s'(%s)\n", Thread.currentThread().getName(), myModel.getTitle(), downloadLink);
                } catch (MalformedURLException e) {
                    Log.d(LOGGER, "[Getting download link] ---> Error while connecting to network: "    );
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.d(LOGGER, "[Getting download link] ---> Error while reading input stream");
                    e.printStackTrace();
                } finally {
                    if(connection != null) { connection.disconnect();}
                }
            }
            return downloadLink;
        }
    }

}
