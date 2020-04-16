package com.example.unilog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LogDetailActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_detail);

        Intent intent = getIntent();
        TextView log_date = (TextView)findViewById(R.id.tvLogDate);
        TextView app_code = (TextView)findViewById(R.id.tvAppCode);
        TextView project_name = (TextView)findViewById(R.id.tvProject);
        TextView file_name = (TextView)findViewById(R.id.tvFileName);
        TextView line_code = (TextView)findViewById(R.id.tvLineCode);
        TextView message = (TextView)findViewById(R.id.tvMessage);

        log_date.setText(intent.getStringExtra("log_date"));
        app_code.setText(intent.getStringExtra("app_code"));
        project_name.setText(intent.getStringExtra("project_name"));
        file_name.setText(intent.getStringExtra("file_name"));
        line_code.setText(intent.getStringExtra("line_code"));
        message.setText(intent.getStringExtra("message"));

    }

    @Override
    public void onClick(View v) {

    }
}
