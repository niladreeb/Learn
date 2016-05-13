package gojimo.app.learn;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ProductActivity extends AppCompatActivity {

    private static String jsonStr;
    private static int position;
    private static String productTitle;
    private static String authorStr;

    private static JSONObject jsonObject;
    private static JSONArray products;

    private static final String TAG_PRODUCTS = "default_products";
    private static final String TAG_PRODUCT_TITLE = "title";
    private static final String TAG_AUTHOR = "author";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        TextView reference_title = (TextView)findViewById(R.id.reference_title);
        TextView author = (TextView)findViewById(R.id.author);

        jsonStr = getIntent().getStringExtra("JSON");
        position = getIntent().getIntExtra("pos",0);

        if(jsonStr!=null && !jsonStr.equalsIgnoreCase("")){

            try {

                JSONArray jsonArray = new JSONArray(jsonStr);
                for (int i = position; i <= position; i++) {
                    jsonObject = jsonArray.getJSONObject(i);

                    products = jsonObject.getJSONArray(TAG_PRODUCTS);

                    if(products.length()==0) {

                        new AlertDialog.Builder(this)
                                .setTitle("References Unavailable")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        intent.putExtra("JSON",jsonStr);
                                        startActivity(intent);
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }

                    for (int j = 0; j < products.length(); j++) {
                        JSONObject productObject = products.getJSONObject(j);
                        if(productObject.has(TAG_PRODUCT_TITLE))
                            productTitle = productObject.getString(TAG_PRODUCT_TITLE);
                            reference_title.setText(productTitle);

                        if(productObject.has(TAG_AUTHOR))
                            authorStr = productObject.getString(TAG_AUTHOR);
                            author.setText(authorStr);
                    }


                }



            }  catch (JSONException ex) {
                ex.printStackTrace();
            }
        }



        }

    @Override
    public void onBackPressed(){

        Intent i = new Intent(ProductActivity.this, MainActivity.class);
        i.putExtra("JSON", jsonStr);
        startActivity(i);

    }
    }

