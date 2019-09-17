package com.example.wojciech.iotmonitor;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ColorAdapter extends ArrayAdapter<Color> implements View.OnClickListener {

    private static final String TAG = ColorAdapter.class.getSimpleName();
    private List<Color> colors;
    private Context context;

    public ColorAdapter(Context context, List<Color> colors) {
        super(context, R.layout.color_list, colors);
        this.colors = colors;
        this.context = context;
    }


    @Override
    public void onClick(View v) {

        int position = (Integer) v.getTag();
        Color color = colors.get(position);
        switch (v.getId()) {
            case R.id.tv_color_item:
                Snackbar.make(v, "Color" + color.getValue(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
                break;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Color color = colors.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.color_item, parent, false);
            viewHolder.button = convertView.findViewById(R.id.tv_color_item);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.up_from_bottom);
        result.startAnimation(animation);

        viewHolder.button.setBackground(new ColorDrawable(android.graphics.Color.parseColor(color.getValue())));//setText(color.getValue());
        return convertView;
    }

    private static class ViewHolder {
        TextView button;
    }
}
