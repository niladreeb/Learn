package gojimo.app.learn;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SubjectActivity extends AppCompatActivity {

    private static String jsonStr;
    private static int position;
    private static String subjectTitle;
    private static String colour;
    private static JSONObject jsonObject;
    private static JSONArray subjects;
    private static LinearLayout linearLayout;


    private static final String TAG_SUBJECTS = "subjects";
    private static final String TAG_SUBJECT_TITLE = "title";
    private static final String TAG_COLOUR = "colour";
    private static final String DEFAULT_COLOUR = "#F0FFFF";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        linearLayout = (LinearLayout)findViewById(R.id.linearlayout);

        jsonStr = getIntent().getStringExtra("JSON");
        position = getIntent().getIntExtra("pos",0);

        if(jsonStr!=null && !jsonStr.equalsIgnoreCase("")){

            try {

                JSONArray jsonArray = new JSONArray(jsonStr);
                for (int i = position; i <= position; i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    subjects = jsonObject.getJSONArray(TAG_SUBJECTS);

                    if(subjects.length()==0) {

                        new AlertDialog.Builder(this)
                                .setTitle("Subjects Unavailable")
                                .setMessage("Do you want to see reference books?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent intent = new Intent(getApplicationContext(), ProductActivity.class);
                                        intent.putExtra("JSON",jsonStr);
                                        intent.putExtra("pos",position);
                                        startActivity(intent);
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        finish();
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                          }

                    for (int j = 0; j < subjects.length(); j++) {
                        JSONObject subjectObject = subjects.getJSONObject(j);
                        if(subjectObject.has(TAG_SUBJECT_TITLE))
                            subjectTitle = subjectObject.getString(TAG_SUBJECT_TITLE);
                        if(subjectObject.has(TAG_COLOUR)) {
                            colour = subjectObject.getString(TAG_COLOUR);

                            final TextView[] myTextViews = new TextView[subjects.length()]; // create an empty array;

                                // create a new textview
                                final TextView rowTextView = new TextView(this);

                                // set some properties of rowTextView or something
                                rowTextView.setText(subjectTitle);
                                rowTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);

                                // add the textview to the linearlayout
                                linearLayout.addView(rowTextView);

                                // save a reference to the textview for later
                                myTextViews[j] = rowTextView;

                            if(!colour.equalsIgnoreCase("") && !colour.equalsIgnoreCase("null"))
                                myTextViews[j].setBackgroundColor(Color.parseColor(colour));
                            else
                                myTextViews[j].setBackgroundColor(Color.parseColor(DEFAULT_COLOUR));
                        }


                    }


                }


            }  catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
    }
}
