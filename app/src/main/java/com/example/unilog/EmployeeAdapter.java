package com.example.unilog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class EmployeeAdapter  extends BaseAdapter {

    private List<EmployeeDTO> register;

    @Override
    public int getCount() {
        return register.size();

    }

    @Override
    public Object getItem(int position) {
        return register.get(position);
    }

    @Override
    public long getItemId(int position) {
        return register.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from((parent.getContext()));
            convertView = inflater.inflate(R.layout.list_item, parent, false);
        }

        EmployeeDTO dto = register.get(position);
        TextView first = (TextView) convertView.findViewById(R.id.tvFirst);
        TextView second = (TextView) convertView.findViewById(R.id.tvSecond);
        TextView third = (TextView) convertView.findViewById(R.id.tvThird);
        first.setText(dto.getEmail() + "");
        second.setText(dto.getRole());
        third.setText(dto.getPhone() + "") ;
        return convertView;
    }

    public void setRegister(List<EmployeeDTO> register) {
        this.register = register;
    }
}