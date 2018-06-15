package com.snipermob.sample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Jerome on 2018/2/26.
 */

public class AdSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    public List<AdUnit> mAdUnits ;
    Context mCtx ;
    public AdSpinnerAdapter(Context ctx, List<AdUnit> adUnits) {
        this.mAdUnits = adUnits ;
        this.mCtx = ctx ;
    }

    @Override
    public int getCount() {
        return mAdUnits.size() ;
    }

    @Override
    public Object getItem(int position) {
        return mAdUnits.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if( convertView == null ) {
            convertView = LayoutInflater.from(mCtx).inflate(R.layout.layout_spinner_view,null);
            Holder holder = new Holder();
            holder.textViewTitle = (TextView) convertView.findViewById(R.id.txtViewTitle);
            convertView.setTag(holder);
        }
        Holder holder = (Holder) convertView.getTag();
        holder.textViewTitle.setText(mAdUnits.get(position).title);
        return convertView;
    }

    class Holder {
         TextView textViewTitle ;
    }

}
