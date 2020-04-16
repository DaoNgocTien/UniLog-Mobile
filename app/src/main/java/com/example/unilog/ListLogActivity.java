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

public class ListLogActivity extends AppCompatActivity {
    LogAdapter adapter = null;
    private static List<LogDTO> listApp = null;
    private ListView lvLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_log);
        adapter = null;
        listApp = null;

        final Intent intent = getIntent();
        final String appCode = intent.getStringExtra("app_code");
        final String url = "http://192.168.1.100:80/api/logs?app_code=" + appCode;
        Log.d("AppCode 1 ", appCode);
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
                                listApp.add(new LogDTO(0, "APP CODE", "FILE NAME", "LINE CODE", "LOG DATE", "MESSAGE", "PROJECT"));
                                for (int pos = 0; pos < response.length(); pos++) {

                                    int id = response.getJSONObject(pos).getInt("id");
                                    String app_code = response.getJSONObject(pos).getString("app_code") == null ? "" : response.getJSONObject(pos).getString("app_code");
                                    String file_name = response.getJSONObject(pos).getString("file_name") == null ? "" : response.getJSONObject(pos).getString("file_name");
                                    String line_code = response.getJSONObject(pos).getString("line_code") == null ? "" : response.getJSONObject(pos).getString("line_code");
                                    String log_date = response.getJSONObject(pos).getString("log_date") == null ? "" : response.getJSONObject(pos).getString("log_date");
                                    String message = response.getJSONObject(pos).getString("message") == null ? "" : response.getJSONObject(pos).getString("message");
                                    String project_name = response.getJSONObject(pos).getString("project_name") == null ? "" : response.getJSONObject(pos).getString("project_name");

                                    Log.d("AppCode 2 ", app_code + " " + response.length());

                                    listApp.add(new LogDTO(id, app_code, file_name, line_code, log_date, message, project_name));
                                    adapter = new LogAdapter();
                                    adapter.setRegister(listApp);

                                    lvLog = (ListView) findViewById(R.id.lvListLog);
                                    lvLog.setAdapter(adapter);

                                    lvLog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            LogDTO dto = (LogDTO) lvLog.getItemAtPosition(position);
                                            Intent i = new Intent(getApplicationContext(), LogDetailActivity.class);
                                            i.putExtra("log_date", dto.getLog_date());
                                            i.putExtra("app_code", dto.getApp_code());
                                            i.putExtra("project_name", dto.getProject_name());
                                            i.putExtra("file_name", dto.getFile_name());
                                            i.putExtra("line_code", dto.getLine_code());
                                            i.putExtra("message", dto.getMessage());
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
                                Toast.makeText(ListLogActivity.this, "This user is not available at the moment, please log in again", Toast.LENGTH_LONG).show();
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
