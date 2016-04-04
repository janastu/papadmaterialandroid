package annotationapp.janastu.org.papadapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ns.developer.tagview.entity.Tag;
import com.ns.developer.tagview.widget.TagCloudLinkView;

import org.codehaus.jackson.map.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import annotationapp.janastu.org.papadapp.rest.AudioAnnotationFile;
import annotationapp.janastu.org.papadapp.rest.JsonRestData;
import annotationapp.janastu.org.papadapp.rest.RestStationDetails;
import annotationapp.janastu.org.papadapp.rest.SimpleHttpClient;
import annotationapp.janastu.org.papadapp.rest.Station;
import annotationapp.janastu.org.papadapp.rest.StationRestData;
import annotationapp.janastu.org.papadapp.services.TagDictionary;

public class StationNavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , TagClickedAdapterCallback {

    RestStationDetails stationDetails;
    RestStationDetailsAsyncTask restStationDetailsAsyncTask;
    JsonRestData user;
    String stationName;
    String stationUrl;
    StationRestData stationRestData;
    TagCloudLinkView filteredtagView;

    StationNavigationActivity reference;
    TagDictionary tagDictionary;

    List<Station> stationList = new ArrayList<Station>();
    List<AudioAnnotationFile> annotationFileList;

    final List<MenuItem> stationMenuItems =new ArrayList<>();
    NavigationView navigationView;
    private static String LOGGER = "StationNavigationActivity";

    static  ArrayList<AudioAnnotationFile> filteredList;
    static     ArrayList<AudioAnnotationFile> parentList;
    RecyclerView recyclerView;

    AnnotationFileAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                if(filteredtagView.getTags().size()!=0)
                filterBasedOnTagSelection();
                else
                {

                    Runnable run = new Runnable() {
                        public void run() {
                            //reload content




                            recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            mAdapter  = new AnnotationFileAdapter(annotationFileList, getApplicationContext());

                            mAdapter.setTagAdaptor((TagClickedAdapterCallback) reference);
                            recyclerView.setAdapter(mAdapter);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                        }
                    };

                    runOnUiThread(run);
                }


            }
        });
        reference = this;
        filteredtagView = (TagCloudLinkView) findViewById(R.id.filterTags);

        filteredtagView.setOnTagDeleteListener(new TagCloudLinkView.OnTagDeleteListener() {
            @Override
            public void onTagDeleted(Tag tag, int position) {

                try {
                    filteredtagView.remove(position);
                    Log.d(LOGGER, "remove" + position);

                }catch(Exception e){Log.d(LOGGER, "EX"+e);}
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        restStationDetailsAsyncTask = new RestStationDetailsAsyncTask();
        restStationDetailsAsyncTask.execute();

    }

    private void filterBasedOnTagSelection()
    {
        //get the current list of annotations;
        ArrayList<String>  searchList = new ArrayList<>();
        List<Tag> searchTags = filteredtagView.getTags();
        for(Tag temp: searchTags)
        {
            searchList.add(temp.getText());
        }

        final ArrayList<AudioAnnotationFile> filterList =  getSubListContainingStringList(searchList);




        Runnable run = new Runnable() {
            public void run() {
                //reload content

                recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                mAdapter  = new AnnotationFileAdapter(filterList, getApplicationContext());

                mAdapter.setTagAdaptor((TagClickedAdapterCallback) reference);
                recyclerView.setAdapter(mAdapter);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
            }
        };

        runOnUiThread(run);
    }

      ArrayList<AudioAnnotationFile> getSubListContainingStringList( ArrayList<String>  searchList)
    {

        Log.d(LOGGER,"searching for " + searchList.toString()) ;

        filteredList     = new ArrayList<AudioAnnotationFile>();
        for( AudioAnnotationFile   temp :  annotationFileList  )
        {
            ArrayList<String>  tagList =  temp.getTags();
            Log.d(LOGGER, "seacrhing INSIDE " + tagList.toString()) ;

            if(tagList.containsAll(searchList))
            {
                Log.d(LOGGER,"found for " + temp.getId()) ;

                filteredList.add(temp);
            }
        }
        return filteredList;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.station_navigation, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        }

        //

        item.setChecked(true);
        int position=stationMenuItems.indexOf(item);

      //  displayChapter(position+1 );
        //stationMenuItems.

        //removing all prev tags

        filteredtagView.invalidate();
        //filteredtagView.add(new Tag(1,"Tag Here"));

        //separate thread
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //filteredtagView.invalidate();
                filteredtagView.removeAllViews();
                //filteredtagView.
                int size =  filteredtagView.getTags().size();

                List<Tag> tagList = filteredtagView.getTags();
                Log.d(LOGGER, "TAG size" + size);

                    if(size!=0) {
                        for (int i = 0; i <= tagList.size(); ++i) {
                            Tag t = tagList.get(i);
                            int id2 = t.getId();
                            Log.d(LOGGER, "TAG remove id2" + id2);
                            try {
                            filteredtagView.remove(id2);
                            if(id2==1)
                                filteredtagView.remove(id2 - 1);


                            Log.d(LOGGER, "TAG remove i-- indexing" + i);
                            filteredtagView.remove(i);
                            filteredtagView.remove(0);
                            }catch(Exception e){Log.d(LOGGER, "REMOVING TAGS"+e);}


                        }
                        //
                    }



                filteredtagView.drawTags();
            }
        });



        Log.d(LOGGER, "position IS " + position);
        if(position >= 5)
        stationUrl = stationList.get(position-5).getStore();
        AsyncListViewLoader aloader = new AsyncListViewLoader();
        aloader.execute();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onTagSelectedMethodCallback(String s) {

        //add to the tag list and search the same;
        List<Tag> tags = filteredtagView.getTags();
        for (Tag t : tags)
        {


            String temp = t.getText();
            if (temp.compareToIgnoreCase(s) == 0) {
                 return;
            }
        }
        filteredtagView.add(new Tag(1, s));
        filteredtagView.drawTags();
    }

    class RestStationDetailsAsyncTask extends AsyncTask<String, Void, String> {
        String g = null;

        private final ProgressDialog dialog = new ProgressDialog(StationNavigationActivity.this);






        @Override
        protected String doInBackground(String... params) {
            List<Station> result = new ArrayList<Station>();

            try {
                g = SimpleHttpClient.executeHttpGet("http://da.pantoto.org/station.json");

                // make sure the quotes are escaped

                Log.d("S", "STATION DETAILS"+g);
                ObjectMapper mapper = new ObjectMapper();

                stationRestData = mapper.readValue(g,
                        StationRestData.class);



            } catch (Exception e) {

                Log.e(LOGGER, "  EXCEPTION GETTING     "+e
                );
                e.printStackTrace();
            }


            return "Executed";
        }


        @Override
        protected void onPostExecute(String res ) {
            dialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {

                    try {
                        Menu navMenu = navigationView.getMenu();
                        List<Station> stations = stationRestData.getStations();
                        int index = 1;
                        for (Station t : stations) {
                            Log.d(LOGGER, "FETCHING AND Adding" + t);
                            navMenu.add(0, index, Menu.NONE, t.getName());
                            stationList.add(t);
                            //   drawerListViewItems.add( t.getStore());
                        }


                        for (int k = 0; k < navMenu.size(); k++) {
                            stationMenuItems.add(navMenu.getItem(k));
                        }
                    }catch(Exception e)
                    {
                        //report error in getting stations
                    }
                }
            });

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    ////

    private class AsyncListViewLoader extends AsyncTask<String, Void, List<AudioAnnotationFile>> {
        private final ProgressDialog dialog = new ProgressDialog(StationNavigationActivity.this);
        String g = null;
        JsonRestData user;
        @Override
        protected void onPostExecute(List<AudioAnnotationFile> result) {

            annotationFileList = result;
            dialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {

                    recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    mAdapter  = new AnnotationFileAdapter(annotationFileList, getApplicationContext());

                    mAdapter.setTagAdaptor((TagClickedAdapterCallback)reference);
                    recyclerView.setAdapter(mAdapter);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());

                }
            });
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Downloading Annotations...");
            dialog.show();
        }

        @Override
        protected List<AudioAnnotationFile> doInBackground(String... params) {



            List<AudioAnnotationFile> result = new ArrayList<AudioAnnotationFile>();

            try {
                g = SimpleHttpClient.executeHttpGet(stationUrl);
                Log.d("STATION GETTING", " DATA FROM SERVER    " + g);
                // make sure the quotes are escaped
                ObjectMapper mapper = new ObjectMapper();

                user = mapper.readValue(g,
                        JsonRestData.class);

             tagDictionary = new TagDictionary(user);

                Log.d(LOGGER, "  SIZE OF THE ARAY    "
                        + user.getFiles().size());

                result = user.getFiles();


            } catch (Exception e) {

                Log.e(LOGGER, "  EXCEPTION GETTING     "
                );
                e.printStackTrace();
            }


            return result;
        }


    }
}
