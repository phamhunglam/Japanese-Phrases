package com.burningteam.phamhunglamsp.japanesephrases;


import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Hung Lam Pham on 1/28/2018.
 */

public class ContentAdapter extends ArrayAdapter<Content> implements View.OnClickListener {
    private ArrayList<Content> dataset;
    Context mContext;

    public static class ViewHolder{

        TextView target1;
        TextView target2;
        TextView translate;
        ImageButton button;

    }

    public ContentAdapter(ArrayList<Content> data, Context context){
        super(context,R.layout.content_layout,data);
        this.dataset = data;
        this.mContext = context;

    }

    @Override
    public void onClick(View view) {

        int position = (int) view.getTag();
        Object object = getItem(position);
        Content content = (Content) object;

        // set information each size

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        final Content content = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder = null; // view lookup cache stored in tag



        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.content_layout,null);

            viewHolder.target1 = (TextView) convertView.findViewById(R.id.target1Text);
            viewHolder.target2 = (TextView) convertView.findViewById(R.id.target2Text);
            viewHolder.translate = (TextView) convertView.findViewById(R.id.translateText);
            viewHolder.button = (ImageButton) convertView.findViewById(R.id.button);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.target1.setText(content.getTarget1());
        viewHolder.target2.setText(content.getTarget2());
        viewHolder.translate.setText(content.getTranslate());

        final View finalConvertView = convertView;
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaPlayer mAudio = MediaPlayer.create(mContext, content.getAudio());
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(2000);
                view.startAnimation(animation1);
                mAudio.start();
            }
        });


        // Return the completed view to render on screen
        return convertView;

    }
}
