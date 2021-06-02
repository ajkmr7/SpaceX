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

import androidx.lifecycle.Observer;
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
    MemberDatabase memberDatabase;
    ProgressDialog pd;
    ArrayList<JSONObject> jsonObjects = new ArrayList<JSONObject>();
    ArrayList<JSONObject> crewMembers = new ArrayList<JSONObject>();
    RecyclerViewAdapter adapter;
    MemberModel members;


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

        memberDatabase = MemberDatabase.getInstance(this);
        if(isNetworkOnline(MainActivity.this)){
            findViewById(R.id.textView1).setVisibility(View.INVISIBLE);
            new JsonTask().execute("https://api.spacexdata.com/v4/crew");
        }
        else{
            memberDatabase.Dao().getAllMembers().observe(MainActivity.this,new Observer<List<MemberModel>>() {

                @Override
                public void onChanged(List<MemberModel> models) {
                    // when the data is changed in our models we are
                    // adding that list to our adapter class.

                    if(models.toArray().length != 0){
                        for(int i=0;i<models.toArray().length;i++){
                            Log.d("QQQQ",models.get(i).getMemberName());

                            String name = models.get(i).getMemberName();
                            String agency = models.get(i).getMemberAgency();
                            String status = models.get(i).getMemberStatus();
                            String wikipedia = models.get(i).getMemberLink();
                            String id = models.get(i).getMemberID();
                            String image = models.get(i).getMemberImage();

                            JSONObject member;
                            try {
                                member = new JSONObject("{\"name\":"+name+",\"agency\":"+agency+",\"status\":"+status+",\"wikipedia\":"+wikipedia+",\"id\":"+id+",\"image\":"+image+"}");
                                jsonObjects.add(member);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        inflateLayout();
                        findViewById(R.id.textView1).setVisibility(View.INVISIBLE);
                    }
                    else
                        findViewById(R.id.textView1).setVisibility(View.VISIBLE);

                }
            });
            Toast.makeText(MainActivity.this, "CHECK YOUR INTERNET CONNECTIVITY...", Toast.LENGTH_LONG).show();
        }
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isNetworkOnline(MainActivity.this)){
                    findViewById(R.id.textView1).setVisibility(View.INVISIBLE);
                    new JsonTask().execute("https://api.spacexdata.com/v4/crew");
                }
                else{
                    memberDatabase.Dao().getAllMembers().observe(MainActivity.this,new Observer<List<MemberModel>>() {
                        @Override
                        public void onChanged(List<MemberModel> models) {
                            // when the data is changed in our models we are
                            // adding that list to our adapter class.

                            if(models.toArray().length != 0){
                                for(int i=0;i<models.toArray().length;i++){
                                    Log.d("QQQQ",models.get(i).getMemberName());

                                    String name = models.get(i).getMemberName();
                                    String agency = models.get(i).getMemberAgency();
                                    String status = models.get(i).getMemberStatus();
                                    String wikipedia = models.get(i).getMemberLink();
                                    String id = models.get(i).getMemberID();
                                    String image = models.get(i).getMemberImage();

                                    JSONObject member;
                                    try {
                                        member = new JSONObject("{\"name\":\""+name+"\",\"agency\":\""+agency+"\",\"status\":\""+status+"\",\"wikipedia\":\""+wikipedia+"\",\"id\":\""+id+"\",\"image\":\""+image+"\"}");
                                        jsonObjects.add(member);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                inflateLayout();
                                findViewById(R.id.textView1).setVisibility(View.INVISIBLE);
                            }
                            else
                                findViewById(R.id.textView1).setVisibility(View.VISIBLE);

                        }
                    });
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
                        memberDatabase.Dao().deleteAllMembers();

//                        Log.d("OOO",memberDatabase.Dao().getAllMembers().toString());
                    }
                };
                thread.start();
                jsonObjects = new ArrayList<JSONObject>();
                inflateLayout();
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
                    MemberModel memberModel = new MemberModel(jsonObject.getString("name"),jsonObject.getString("agency"),jsonObject.getString("status"),jsonObject.getString("wikipedia"), jsonObject.getString("id"), jsonObject.getString("image"));
                    memberDatabase.Dao().insert(memberModel);
                    jsonObjects.add(jsonObject);
                }

//                Log.d("PPP",jsonObjects.toString());

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
