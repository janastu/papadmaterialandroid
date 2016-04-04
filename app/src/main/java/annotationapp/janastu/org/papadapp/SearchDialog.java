package annotationapp.janastu.org.papadapp;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.List;

import annotationapp.janastu.org.papadapp.services.TagDictionary;

/**
 * Created by DESKTOP on 4/3/2016.
 */
public class SearchDialog extends Dialog implements View.OnClickListener, SearchView.OnQueryTextListener
{
    private ListenAudioPostTagCardActivity activity;
    private Button search, cancel;
    private EditText text;
    private SearchDialog thisDialog;

    private SearchView mSearchView;
    private ListView mListView;
    private final List<String> mStrings = new TagDictionary().getTagsDirectory();
    private String selectedTag;

    public SearchDialog(ListenAudioPostTagCardActivity context  ) {
        super(context);
        // TODO Auto-generated constructor stub
        this.activity = context;
        this.thisDialog = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchdialog);
        getWindow().setLayout(android.view.ViewGroup.LayoutParams.FILL_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);


        mSearchView = (SearchView) findViewById(R.id.search_view);
        mListView = (ListView) findViewById(R.id.list_view);
        mListView.setAdapter(new ArrayAdapter<String>(this.activity,
                android.R.layout.simple_list_item_1,
                mStrings));
        mListView.setTextFilterEnabled(true);
        mSearchView.setSubmitButtonEnabled(true);
        LinearLayout linearLayoutOfSearchView = (LinearLayout) mSearchView.getChildAt(0);
        initalize();
        setupSearchView();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                String item = ((TextView) view).getText().toString();

                //  Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG).show();
                // mSearchView.set.setFilterText(item.toString());

                mSearchView.setQuery(item, false);
            }
        });
    }
    private void setupSearchView() {
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Search Here");
    }

    private void initalize() {
        // TODO Auto-generated method stub
//        text = (EditText) findViewById(R.id.text);
        search = (Button) findViewById(R.id.addtag);
        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                thisDialog.cancel();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
               // String textString = text.getText().toString();

                //addtag
                 activity.postWord(selectedTag);

                thisDialog.cancel();
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d("Query", "query " + query);
        selectedTag = query;
        return false; 
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            mListView.clearTextFilter();
        } else {
            mListView.setFilterText(newText.toString());
        }

        selectedTag = newText;
        return true;
    }
}