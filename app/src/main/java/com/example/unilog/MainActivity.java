package com.example.unilog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MainActivity extends AppCompatActivity {
    public static LoginDTO login = null;
    private static int LOGIN_REQUEST_CODE = 1;
    final static String url = "http://192.168.1.100:80/api/accounts/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = null;
    }

    public void onClickToLogin(View view) {
        final String email = ((EditText) findViewById(R.id.txtEmail)).getText().toString();
        final String password = ((EditText) findViewById(R.id.txtPassword)).getText().toString();
        // Instantiate the RequestQueue.
        try {

            RequestQueue queue = Volley.newRequestQueue(this);


            JSONObject jsonBody = new JSONObject();

            jsonBody.put("email", email);
            jsonBody.put("password", password);

            RequestFuture<JSONObject> future = RequestFuture.newFuture();

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if(response.getInt("role") < 2 || response.getInt("role") > 4){
                                    Toast.makeText(MainActivity.this, "This user is not available at the moment", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                int id = response.getInt("id");
                                String token = "bearer " + response.getString("token");
                                int role = response.getInt("role");
                                String email = response.getString("email");
                                JSONArray manage_project = response.getJSONArray("manage_project");
                                List<Integer> listApplicattion = null;
                                List<Integer> listApplicationInstance = null;

                                for (int pos = 0; pos < manage_project.length(); pos++) {
                                   if(listApplicationInstance == null){
                                       listApplicationInstance = new ArrayList<>();
                                   }
                                   if(listApplicattion == null){
                                       listApplicattion = new ArrayList<>();
                                   }
                                    listApplicattion.add(manage_project.getJSONObject(pos).getInt("application_id"));
                                    listApplicationInstance.add(manage_project.getJSONObject(pos).getInt("application_instance_id"));
                                }

                                login = new LoginDTO(id, token, role, email, listApplicattion, listApplicationInstance);

                                Intent i = new Intent(getBaseContext(), DashBoardActivity.class);
                                i.putExtra("token", login.getToken());
                                startActivityForResult(i, LOGIN_REQUEST_CODE);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO: Handle error
                            NetworkResponse networkResponse = error.networkResponse;
                            if (networkResponse != null && networkResponse.statusCode == 400) {
                                // HTTP Status Code: 401 Unauthorized\
                                Toast.makeText(MainActivity.this, "Please check email or password again", Toast.LENGTH_LONG).show();
                            }
                            error.printStackTrace(); //log the error resulting from the request for diagnosis/debugging
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    final Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    //headers.put("Authorization", "Bearer " + "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1bmlxdWVfbmFtZSI6IjI5IiwibmJmIjoxNTg2OTU5MDI2LCJleHAiOjE1ODc1NjM4MjYsImlhdCI6MTU4Njk1OTAyNn0.aRxzSFoS59-RePBKm3KiwOsB3LoIDZXqN9TFlpbomC8");//put your token here
                    return headers;
                }
            };
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    50000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(this).add(jsonObjectRequest);

//            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
//                    (Request.Method.POST, url, jsonBody, future, future) {
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    final Map<String, String> headers = new HashMap<>();
//                    headers.put("Content-Type", "application/json; charset=utf-8");
//                    //headers.put("Authorization", "Bearer " + "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1bmlxdWVfbmFtZSI6IjI5IiwibmJmIjoxNTg2OTU5MDI2LCJleHAiOjE1ODc1NjM4MjYsImlhdCI6MTU4Njk1OTAyNn0.aRxzSFoS59-RePBKm3KiwOsB3LoIDZXqN9TFlpbomC8");//put your token here
//                    return headers;
//                }
//            };
//            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
//                    50000,
//                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//            Volley.newRequestQueue(this).add(jsonObjectRequest);
//
//            try {
//                JSONObject response = future.get(60, TimeUnit.SECONDS);
//                int id = response.getInt("id");
//                String token = "bearer " + response.getString("token");
//                int role = response.getInt("role");
//                JSONArray manage_project = response.getJSONArray("manage_project");
//                List<Integer> listApplicattion = null;
//                List<Integer> listApplicationInstance = null;
//
//                for (int pos = 0; pos < manage_project.length(); pos++) {
//                    if (listApplicationInstance == null) {
//                        listApplicationInstance = new ArrayList<>();
//                    }
//                    if (listApplicattion == null) {
//                        listApplicattion = new ArrayList<>();
//                    }
//                    listApplicattion.add(manage_project.getJSONObject(pos).getInt("application_id"));
//                    listApplicationInstance.add(manage_project.getJSONObject(pos).getInt("application_instance_id"));
//                }
//
//                login = new LoginDTO(id, token, role, email, listApplicattion, listApplicationInstance);
//            } catch (InterruptedException e) {
//                Log.e("InterruptedException ", e.getMessage());
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                Log.e("ExecutionException ", e.getMessage());
//                e.printStackTrace();
//            } catch (TimeoutException e) {
//                Log.e("TimeoutException ", e.getMessage());
//                e.printStackTrace();
//            }
//
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void onClickToReset (View view){
        ((EditText) findViewById(R.id.txtEmail)).setText("");
        ((EditText) findViewById(R.id.txtPassword)).setText("");
    }

}
