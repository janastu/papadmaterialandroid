package annotationapp.janastu.org.papadapp.rest;

import android.os.AsyncTask;
import android.util.Log;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * Created by murali on 13/11/15.
 */
public class RestStationDetails extends AsyncTask<String, Void, String> {
    String g = null;
    StationRestData stationRestData;
    private static String LOGGER = "RestStationDetails";

    public StationRestData getUser() {
        return stationRestData;
    }

    public void setUser(StationRestData user) {
        this.stationRestData = user;
    }

    @Override
    protected String doInBackground(String... params) {

        try {
            g = SimpleHttpClient.executeHttpGet("http://da.pantoto.org/station.json");

            // make sure the quotes are escaped

            Log.d(LOGGER, "STATION DETAILS"+g);
            ObjectMapper mapper = new ObjectMapper();

            stationRestData = mapper.readValue(g,
                    StationRestData.class);




// get the Stri
        } catch (Exception e) {

            Log.e(LOGGER, "  EXCEPTION GETTING     "
                    );
            e.printStackTrace();
        }


        return "Executed";
    }

    @Override
    protected void onPostExecute(String result) {
        //  TextView txt = (TextView) findViewById(R.id.output);
        // txt.setText("Executed"); // txt.setText(result);
        // might want to change "executed" for the returned string passed
        // into onPostExecute() but that is upto you



    }

    @Override
    protected void onPreExecute() {}

    @Override
    protected void onProgressUpdate(Void... values) {}
}