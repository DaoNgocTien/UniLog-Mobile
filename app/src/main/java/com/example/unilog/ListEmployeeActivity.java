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

public class ListEmployeeActivity extends AppCompatActivity {
    EmployeeAdapter adapter = null;
    private static List<EmployeeDTO> listApp = null;
    private ListView lvEmp;
    final static String url = "http://192.168.1.100:80/api/accounts?fields=email%2Cname%2Cphone%2Crole";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_employee);
        adapter = null;
        lvEmp = null;

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
                                listApp.add(new EmployeeDTO("NAME", "EMAIL", "PHONE", "ROLE"));
                                for (int pos = 0; pos < response.length(); pos++) {

                                    String name = response.getJSONObject(pos).getString("name") == null ? "" : response.getJSONObject(pos).getString("name");
                                    String email = response.getJSONObject(pos).getString("email") == null ? "" : response.getJSONObject(pos).getString("email");
                                    String phone = response.getJSONObject(pos).getString("phone") == null ? "" : response.getJSONObject(pos).getString("phone");


                                    if (response.getJSONObject(pos).getInt("role") > 1 && response.getJSONObject(pos).getInt("role") < 5) {
                                        String role = response.getJSONObject(pos).getInt("role") == 2 ? "Manager" : response.getJSONObject(pos).getInt("role") == 3 ? "Developer" : "Tester";
                                        listApp.add(new EmployeeDTO(name, email, phone, role));
                                    }

//                                    Log.d("listApp" , listApp;)
                                    adapter = new EmployeeAdapter();
                                    adapter.setRegister(listApp);

                                    lvEmp = (ListView) findViewById(R.id.lvListEmployee);
                                    lvEmp.setAdapter(adapter);
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
                                Toast.makeText(ListEmployeeActivity.this, "This user is not available at the moment, please log in again", Toast.LENGTH_LONG).show();
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
