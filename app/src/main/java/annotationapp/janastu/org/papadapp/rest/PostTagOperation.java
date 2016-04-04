package annotationapp.janastu.org.papadapp.rest;

import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.ArrayList;

/**
 * Created by murali on 13/11/15.
 */
public class PostTagOperation extends AsyncTask<String, Void, String> {
    String g = null;
    String tag;

    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private static String LOGGER = "PostTagOperation";

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    protected String doInBackground(String... params) {

        try {

            String url = "http://da.pantoto.org/api/tags/"+ this.id;
            Log.d(LOGGER,"senindg"+url);
            Log.d(LOGGER,"adding tag"+tag);
            if(tag.isEmpty() || tag == null)
            {
                return "Failed";
            }


            ArrayList<NameValuePair> postParameters = new ArrayList<>();

            postParameters.add(new BasicNameValuePair("tags", tag.trim()));

            g = SimpleHttpClient.executeHttpPost(url,postParameters);
            Log.d(LOGGER,"HttpResponse response ="+g);
            // make sure the quotes are escaped
            ObjectMapper mapper = new ObjectMapper();




// get the Stri
        } catch (Exception e) {

            Log.e(LOGGER, "  PostTagOperation POST FAILURE     " +e   );
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