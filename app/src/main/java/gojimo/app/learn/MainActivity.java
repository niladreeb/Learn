package gojimo.app.learn;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends ListActivity {

    private static String jsonStr;
    public static String lastModified;
    private static String name;
    private static JSONObject jsonObject;
    private static RelativeLayout topLevelLayout;
    private static ListAdapter adapter;

    private SwipeRefreshLayout swipeContainer;
    private ProgressDialog pDialog;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> qualificationList;

    private static final String TAG_NAME = "name";
    private static final String url = "demo_url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatCallback callback = new AppCompatCallback() {
            @Override
            public void onSupportActionModeStarted(ActionMode actionMode) {
            }

            @Override
            public void onSupportActionModeFinished(ActionMode actionMode) {
            }

            @Nullable
            @Override
            public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
                return null;
            }
        };

        AppCompatDelegate delegate = AppCompatDelegate.create(this, callback);

        delegate.onCreate(savedInstanceState);
        delegate.setContentView(R.layout.activity_main);
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        delegate.setSupportActionBar(toolbar);

        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        final boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

       jsonStr = getIntent().getStringExtra("JSON");
        if(jsonStr!=null && !jsonStr.equalsIgnoreCase("")){

                UpdateList();
        }
        else{
            if(isConnected)
                new GetQualifications().execute();  //Call async task
            else
                Toast.makeText(getApplicationContext(),"Please connect to the internet to continue",Toast.LENGTH_LONG).show();
        }

        topLevelLayout = (RelativeLayout) findViewById(R.id.top_layout);


        // Guide layout
        if (isFirstTime()) {
            topLevelLayout.setVisibility(View.INVISIBLE);
        }

        if(!topLevelLayout.hasFocus()){
            RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout);
            relativeLayout.setBackgroundResource(R.drawable.learn);
            relativeLayout.getBackground().setAlpha(20);

        }

        // Refresh request
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                // Your code to refresh the list here.
                if(isConnected)
                    new GetQualifications().execute();
                else
                    Toast.makeText(getApplicationContext(),"Please connect to the internet to continue",Toast.LENGTH_LONG).show();

            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        //Call next activity
        final ListView lv = getListView();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent intent = new Intent(getApplicationContext(), SubjectActivity.class);
                    intent.putExtra("JSON",jsonStr);
                    intent.putExtra("pos",position);
                    startActivity(intent);

            }
        });

        //Swipe conditions
        lv.setOnScrollListener(new AbsListView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState)
            {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
            {
                int topRowVerticalPosition = (lv == null || lv.getChildCount() == 0) ? 0 : lv.getChildAt(0).getTop();
                swipeContainer.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
            }
        });


    }

    @Override
    public void onBackPressed(){

        new AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Are you sure?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        finishAffinity();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    private boolean isFirstTime()
    {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean ranBefore = preferences.getBoolean("RanBefore", false);
        if (!ranBefore) {

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("RanBefore", true);
            editor.commit();
            topLevelLayout.setVisibility(View.VISIBLE);
            topLevelLayout.setOnTouchListener(new View.OnTouchListener(){

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    topLevelLayout.setVisibility(View.INVISIBLE);
                    return false;
                }

            });


        }
        return ranBefore;

    }

    private void UpdateList(){

        qualificationList = new ArrayList<HashMap<String, String>>();
        if(jsonStr!=null && !jsonStr.equalsIgnoreCase("")){

            try {

                JSONArray jsonArray = new JSONArray(jsonStr);
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    name = jsonObject.getString(TAG_NAME);

                    HashMap<String, String> qualifications = new HashMap<String, String>();
                    qualifications.put(TAG_NAME, name);
                    qualificationList.add(qualifications);
                }


            }  catch (JSONException ex) {
                ex.printStackTrace();
            }

            adapter = new SimpleAdapter(
                    MainActivity.this, qualificationList,
                    R.layout.list_item, new String[] { TAG_NAME}, new int[] { R.id.name});

            setListAdapter(adapter);
        }
    }

    /**
     * Async task class to get json by making HTTP call
     * */
    private class GetQualifications extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            lastModified = sh.makeServiceCall(url, ServiceHandler.GET)[0];
            jsonStr = sh.makeServiceCall(url, ServiceHandler.GET)[1];

            if(jsonStr!=null && !jsonStr.equalsIgnoreCase("")){

                // Local data storage
                String FILENAME = "json_data";
                FileOutputStream fos = null;

                try {
                    fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
                    fos.write(jsonStr.getBytes());
                    fos.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        UpdateList();
                    }
                });
            }



            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            if(swipeContainer.isRefreshing())
            swipeContainer.setRefreshing(false);

        }
    }
}
