package com.ajay.spacex;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.lang.reflect.Member;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.ItemClickListener {


    ProgressDialog pd;
    ArrayList<JSONObject> jsonObjects = new ArrayList<JSONObject>();
    RecyclerViewAdapter adapter;
    int flag = 0;

    public static boolean isNetworkOnline(Context con)
    {
        boolean status = false;
        try
        {
            ConnectivityManager cm = (ConnectivityManager) con
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);

            if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                status = true;
            } else {
                netInfo = cm.getNetworkInfo(1);

                if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                    status = true;
                } else {
                    status = false;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return status;
    }

    public void inflateLayout(){
        RecyclerView recyclerView = findViewById(R.id.crewMembers);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new RecyclerViewAdapter(getApplicationContext(), jsonObjects);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MemberDatabase memberDatabase = MemberDatabase.getInstance(this);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isNetworkOnline(MainActivity.this)){
                    findViewById(R.id.textView1).setVisibility(View.INVISIBLE);
                    new JsonTask().execute("https://api.spacexdata.com/v4/crew");
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            MemberModel memberModel = new MemberModel("ABC","EFG","HIJ","LMN","OPQ");
                            memberDatabase.Dao().insert(memberModel);
                        }
                    };
                    thread.start();
                    Log.d("ZZZ",memberDatabase.Dao().getAllMembers().toString());
                }
                else{
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            if(memberDatabase.Dao().getAllMembers()!=null) {
                                findViewById(R.id.textView1).setVisibility(View.INVISIBLE);
                            }
                            else{
                                findViewById(R.id.textView1).setVisibility(View.VISIBLE);
                            }
                        }
                    };
                    thread.start();
                    Toast.makeText(MainActivity.this, "CHECK YOUR INTERNET CONNECTIVITY...", Toast.LENGTH_LONG).show();
                }

            }
        });

        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        MemberModel memberModel = new MemberModel("ABC","EFG","HIJ","LMN","OPQ");
                        memberDatabase.Dao().deleteAllMembers();
                        Log.d("OOO",memberDatabase.Dao().getAllMembers().toString());
                    }
                };
                thread.start();
            }
        });


    }

    @Override
    public void onItemClick(View view, int position) {

    }


    public class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {
            String line = "";
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String response = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                    response = response.concat(line);
                }
//                Log.d("KK",response);
                JSONObject uniObject = new JSONObject("{res:"+response+"}");

                JSONObject jsonObject;
                for(int i=0;i<uniObject.getJSONArray("res").length();i++){
                    jsonObject = uniObject.getJSONArray("res").getJSONObject(i);

                    jsonObjects.add(jsonObject);
                }
                Log.d("ZZZ",jsonObjects.toString());

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return line;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(result!=null)
                    inflateLayout();
            if (pd.isShowing()) {
                pd.dismiss();
            }
        }
    }
}
