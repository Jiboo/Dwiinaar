package com.github.jiboo.dwiinaar.bitmapmanager.tests.stress;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.github.jiboo.dwiinaar.bitmapmanager.AsyncImageView;
import com.github.jiboo.dwiinaar.bitmapmanager.BitmapCache;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BitmapCache.BitmapDiskCache.initInstance(this);

        setListAdapter(new BaseAdapter() {
            @Override
            public boolean hasStableIds() {
                return true;
            }

            @Override
            public int getCount() {
                return Integer.MAX_VALUE;
            }

            @Override
            public Object getItem(int position) {
                try {
                    return new URL("http://dummyimage.com/300x200&text=" + position);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null)
                    convertView = new AsyncImageView(MainActivity.this, R.drawable.ic_launcher);
                ((AsyncImageView) convertView).setImageURL((URL) getItem(position));
                return convertView;
            }
        });
    }
}
