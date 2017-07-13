package com.omni.newtaipeifarm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.omni.newtaipeifarm.R;
import com.omni.newtaipeifarm.model.Area;
import com.omni.newtaipeifarm.model.FarmCategory;

/**
 * Created by wiliiamwang on 06/07/2017.
 */

public class SearchOptionAdapter <T> extends RecyclerView.Adapter<SearchOptionAdapter.ViewHolder> {

    public interface SOAListener<T> {
        void onClickItem(View itemView, T data);
    }

    private Context mContext;
    private T[] mData;
    private SOAListener<T> mListener;

    public SearchOptionAdapter(Context context, T[] data, SOAListener<T> listener) {
        mContext = context;
        mData = data;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_option_rv_item_view, null);
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return mData.length;
    }

    @Override
    public void onBindViewHolder(SearchOptionAdapter.ViewHolder holder, final int position) {

        if (mData instanceof Area[]) {
            holder.titleTV.setText(((Area[]) mData)[position].getName());
        } else if (mData instanceof FarmCategory[]) {
            holder.titleTV.setText(((FarmCategory[]) mData)[position].getName());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClickItem(v, mData[position]);
                }
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView titleTV;

        public ViewHolder(View itemView) {
            super(itemView);

            titleTV = (TextView) itemView.findViewById(R.id.search_option_rv_item_view_tv_title);
        }
    }

}
