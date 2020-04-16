package com.example.unilog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListApplicationInstanceActivity extends AppCompatActivity {
    ApplicationInstanceAdapter adapter = null;
    private static List<ApplicationInstanceDTO> listApp = null;
    private ListView lvAppIns;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_application_instance);
        adapter = null;
        listApp = null;

        final Intent intent = getIntent();
        final String url = "http://192.168.1.100:80/api/application_instances?application_id=" + ListApplicationActivity.currentApp.getId() + "&fields=name%2Capp_code%2Crelease_url%2Capp_id";
        try {

            RequestQueue queue = Volley.newRequestQueue(this);
            JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                if (listApp == null) {
                                    listApp = new ArrayList<>();
                                }
                                listApp.add(new ApplicationInstanceDTO("NAME", "APP CODE", "RELEASE URL"));

                                for (int pos = 0; pos < response.length(); pos++) {
                                    int id = response.getJSONObject(pos).getInt("id");
                                    String name = response.getJSONObject(pos).getString("name") == null ? "" : response.getJSONObject(pos).getString("name");
                                    String appCode = response.getJSONObject(pos).getString("app_code") == null ? "" : response.getJSONObject(pos).getString("app_code");
                                    String releaseUrl = response.getJSONObject(pos).getString("release_url") == null ? "" : response.getJSONObject(pos).getString("release_url");

                                    LoginDTO dto = MainActivity.login;

                                    for (int appInsId : dto.getListApplicationInstance()
                                    ) {
//                                        Log.d("appInsId 1" , appInsId + "");
//                                        Log.d("appInsId 2" , id + "");
//
//                                        Log.d("app_id 1" , response.getJSONObject(pos).getInt("app_id") + "");
//                                        Log.d("app_id 2" , intent.getStringExtra("application_id"));
//                                        Log.d("1122332" , " ");

                                        if ( appInsId == id && id != 22) {
                                            listApp.add(new ApplicationInstanceDTO(name, appCode, releaseUrl));
                                        }
                                    }



                                    adapter = new ApplicationInstanceAdapter();
                                    adapter.setRegister(listApp);

                                    lvAppIns = (ListView) findViewById(R.id.lvListApplicationInstance);
                                    lvAppIns.setAdapter(adapter);

                                    lvAppIns.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            ApplicationInstanceDTO dto = (ApplicationInstanceDTO) lvAppIns.getItemAtPosition(position);
                                            Intent i = new Intent(getApplicationContext(), ListLogActivity.class);
                                            i.putExtra("app_code", dto.getApp_code());
                                            i.putExtra("token", intent.getStringExtra("token"));
                                            startActivity(i);
                                        }
                                    });
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO: Handle error
                            NetworkResponse networkResponse = error.networkResponse;
                            if (networkResponse != null && networkResponse.statusCode == 401) {
                                // HTTP Status Code: 401 Unauthorized\
                                Toast.makeText(ListApplicationInstanceActivity.this, "This user is not available at the moment, please log in again", Toast.LENGTH_LONG).show();
                                listApp = null;
                                adapter = null;
                                backToMain();
                            }
                            error.printStackTrace(); //log the error resulting from the request for diagnosis/debugging
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    final Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    LoginDTO login = MainActivity.login;
                    headers.put("Authorization", login.getToken());//put your token here
                    return headers;
                }
            };
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    50000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(this).add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void backToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onClickToBack(View view) {
        finish();
    }
}

