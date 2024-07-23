package com.hotworx.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.hotworx.R;
import com.hotworx.activities.DockActivity;
import com.hotworx.interfaces.OnItemClickInterface;
import com.hotworx.requestEntity.AnsModel;

import java.util.ArrayList;

public class MyCustomAdapter extends BaseAdapter {

    private ArrayList<AnsModel> ansList;
    boolean array[];
    private DockActivity dockActivity;
    private OnItemClickInterface mClickListener;

    public MyCustomAdapter(DockActivity dockActivity, ArrayList<AnsModel> ansList) {
        this.ansList = new ArrayList<>();
        this.ansList.addAll(ansList);
        array = new boolean[ansList.size()];
        this.dockActivity = dockActivity;
    }

    private class ViewHolder {
        TextView code;
        CheckBox name;
    }

    @Override
    public int getCount() {
        return ansList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) dockActivity.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.item_row_answer, null);

            holder = new ViewHolder();
            holder.code = (TextView) convertView.findViewById(R.id.tvAns);
            holder.name = (CheckBox) convertView.findViewById(R.id.cvAns);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setChecked(array[position]);
        holder.name.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                for (int i = 0; i < array.length; i++) {
                    if (i == position) {
                        array[i] = true;
                        if (mClickListener!=null) mClickListener.onItemClick(ansList.get(i).getName());
                    } else {
                        array[i] = false;
                    }
                }
                notifyDataSetChanged();
            }
        });

        AnsModel country = ansList.get(position);
        holder.code.setText(country.getName());
        return convertView;

    }

    public void setClickListener(OnItemClickInterface onItemClickInterface) {
        this.mClickListener = onItemClickInterface;
    }
}

