package com.example.gdhb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    // json https://run.mocky.io/v3/a5b3524d-ec3b-43ab-9f78-f871c4a7e3b1

    private static String Json_URL = "https://run.mocky.io/v3/a5b3524d-ec3b-43ab-9f78-f871c4a7e3b1";

    List<MovieModelClass> movieList;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //aici de modificat la nume doar
        movieList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);


        GetData getData = new GetData();
        getData.execute();
    }

    public class GetData extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... strings) {

            String current = "";

            try {
                URL url;
                HttpURLConnection urlConnection = null;

                try {
                    url = new URL(Json_URL);
                    urlConnection = (HttpURLConnection) url.openConnection();

                    InputStream is = urlConnection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);

                    int data = isr.read();
                    while (data != -1) {
                        current += (char) data;
                        data = isr.read();
                    }

                    return current;


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            return current;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("moviz");

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    MovieModelClass model = new MovieModelClass();
        //aici de modificat
                    model.setId(jsonObject1.getString("id"));
                    model.setName(jsonObject1.getString("name"));
                    model.setImg(jsonObject1.getString("image"));

                    movieList.add(model);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            PutDataIntoRecyclerView(movieList);
        }
    }

    private void PutDataIntoRecyclerView(List<MovieModelClass> movieList){
        Adaptery adaptery = new Adaptery(this,movieList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(adaptery);
    }
}


