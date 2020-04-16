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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListApplicationActivity extends AppCompatActivity {
    ApplicationAdapter adapter = null;
    private static List<ApplicationDTO> listApp = null;
    private static int APPLICATION_REQUEST_CODE = 1;
    private ListView lvApp;
    final static String url = "http://192.168.1.100:80/api/applications?ref_fields=application_instance";
    public static ApplicationDTO currentApp = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_application);
        adapter = null;
        listApp = null;

        final Intent intent = getIntent();
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
                                for (int pos = 0; pos < response.length(); pos++) {

                                    int id = response.getJSONObject(pos).getInt("id");
                                    String name = response.getJSONObject(pos).getString("name") == null ? "" : response.getJSONObject(pos).getString("name");
                                    int count = response.getJSONObject(pos).getJSONArray("application_instance").length();
                                    LoginDTO dto = MainActivity.login;
                                    for (int appId : dto.getListApplicattion()
                                    ) {
                                        if (appId == id && id != 22) {
                                            listApp.add(new ApplicationDTO(id, name, count));
                                        }
                                    }

                                    adapter = new ApplicationAdapter();
                                    adapter.setRegister(listApp);

                                    lvApp = (ListView) findViewById(R.id.lvListApplication);
                                    lvApp.setAdapter(adapter);

                                    lvApp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            ApplicationDTO dto = (ApplicationDTO) lvApp.getItemAtPosition(position);
                                            currentApp = dto;
                                            Intent i = new Intent(getApplicationContext(), ListApplicationInstanceActivity.class);
                                            Toast.makeText(ListApplicationActivity.this, "App: " + dto.getId(), Toast.LENGTH_SHORT).show();
                                            i.putExtra("application_id", dto.getId()+"");
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
                                Toast.makeText(ListApplicationActivity.this, "This user is not available at the moment, please log in again", Toast.LENGTH_LONG).show();
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
