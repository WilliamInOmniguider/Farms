package com.omni.newtaipeifarm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.omni.newtaipeifarm.R;
import com.omni.newtaipeifarm.model.Farm;
import com.omni.newtaipeifarm.model.SearchFarmResult;
import com.omni.newtaipeifarm.network.NetworkManager;

/**
 * Created by wiliiamwang on 10/07/2017.
 */

public class SearchFarmResultAdapter extends RecyclerView.Adapter<SearchFarmResultAdapter.ViewHolder> {

    public interface SFRViewHolderListener {
        void onClickItem(View itemView, int position);

        void onClickMore(TextView moreView, int position);
    }

    Context mContext;
    SFRViewHolderListener mListener;
    SearchFarmResult[] mResults;

    public SearchFarmResultAdapter(Context context, SearchFarmResult[] results, SFRViewHolderListener listener) {
        mContext = context;
        mResults = results;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pager_farm_list_item_view, null);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return mResults.length;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final SearchFarmResult result = mResults[position];

        NetworkManager.getInstance().setNetworkImage(mContext, holder.iconNIV, result.getIcon());
        holder.indexTV.setText("" + (position + 1));
        holder.titleTV.setText(result.getName());
        holder.subtitleTV.setText(result.getAddress());
        holder.moreTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickMore((TextView) v, position);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClickItem(v, position);
                }
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        protected NetworkImageView iconNIV;
        protected TextView indexTV;
        protected TextView titleTV;
        protected TextView subtitleTV;
        protected TextView moreTV;

        public ViewHolder(View itemView) {
            super(itemView);

            iconNIV = (NetworkImageView) itemView.findViewById(R.id.pager_farm_list_item_view_niv);
            indexTV = (TextView) itemView.findViewById(R.id.pager_farm_list_item_view_tv_index);
            titleTV = (TextView) itemView.findViewById(R.id.pager_farm_list_item_view_tv_title);
            subtitleTV = (TextView) itemView.findViewById(R.id.pager_farm_list_item_view_tv_subtitle);
            moreTV = (TextView) itemView.findViewById(R.id.pager_farm_list_item_view_tv_more);
        }
    }
}
