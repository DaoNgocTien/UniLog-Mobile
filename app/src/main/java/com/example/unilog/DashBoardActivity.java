package com.example.unilog;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class DashBoardActivity extends AppCompatActivity {
    private static final int DASH_BOARD_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
    }

    public void onClickToShowApplication(View view) {
        Intent i = getIntent();

        Intent newIntent = new Intent(this, ListApplicationActivity.class);
        newIntent.putExtra("token", i.getStringExtra("token"));
        startActivity(newIntent);
    }

    public void onClickToShowEmployee(View view) {
        Intent i = getIntent();

        Intent newIntent = new Intent(this, ListEmployeeActivity.class);
        newIntent.putExtra("token", i.getStringExtra("token"));
        startActivity(newIntent);
    }

    public void onClickToBack(View view) {
        finish();
    }

}

