package com.omni.newtaipeifarm.index;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.omni.newtaipeifarm.R;
import com.omni.newtaipeifarm.adapter.FarmInfoAdapter;
import com.omni.newtaipeifarm.model.AreaFarm;
import com.omni.newtaipeifarm.model.Farm;
import com.omni.newtaipeifarm.model.OmniEvent;
import com.omni.newtaipeifarm.network.FarmApi;
import com.omni.newtaipeifarm.network.NetworkManager;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by wiliiamwang on 07/09/2017.
 */

public class IndexFarmFragment extends Fragment {

    private Context mContext;
    private View mView;
    private RecyclerView mRV;
    private IndexFarmAdapter mIndexFarmAdapter;

    public static IndexFarmFragment newInstance() {
        IndexFarmFragment fragment = new IndexFarmFragment();

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.index_farm_fragment_view, container, false);

            mRV = (RecyclerView) mView.findViewById(R.id.index_farm_fragment_view_rv);

            FarmApi.getInstance().getAllFarmsByArea(mContext, new NetworkManager.NetworkManagerListener<AreaFarm[]>() {
                @Override
                public void onSucceed(AreaFarm[] areaFarms) {
                    setRVData(areaFarms);
                }

                @Override
                public void onFail(VolleyError error, boolean shouldRetry) {

                }
            });
        }

        return mView;
    }

    private void setRVData(AreaFarm[] areaFarms) {

        if (mIndexFarmAdapter == null) {
            mIndexFarmAdapter = new IndexFarmAdapter(mContext, areaFarms, new IndexFarmAdapter.IFarmViewHolderListener() {
                @Override
                public void onItemClicked(int position, Farm farm) {
                    EventBus.getDefault().post(new OmniEvent(OmniEvent.TYPE_INDEX_FARM_ITEM_CLICKED, farm));
                }
            });

            mRV.setLayoutManager(new LinearLayoutManager(mContext));

            mRV.setAdapter(mIndexFarmAdapter);
        } else {
            mIndexFarmAdapter.notifyDataSetChanged();
        }
    }

    static class IndexFarmAdapter extends RecyclerView.Adapter<IndexFarmAdapter.FarmViewHolder> {

        public interface IFarmViewHolderListener {
            void onItemClicked(int position, Farm farm);
        }

        private Context mContext;
        private LayoutInflater mInflater;
        private AreaFarm[] mAreaFarms;
        private IFarmViewHolderListener mListener;

        public IndexFarmAdapter(Context context, AreaFarm[] areaFarms, IFarmViewHolderListener listener) {
            mContext = context;
            mAreaFarms = areaFarms;
            mListener = listener;
        }


        @Override
        public FarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            mInflater = LayoutInflater.from(parent.getContext());
            View view = mInflater.inflate(R.layout.index_farm_list_item_view, null);

            FarmViewHolder viewHolder = new FarmViewHolder(view);
            return viewHolder;
        }

        @Override
        public int getItemCount() {
            return mAreaFarms.length;
        }

        @Override
        public void onBindViewHolder(FarmViewHolder holder, int position) {
            final AreaFarm areaFarm = mAreaFarms[position];

            holder.titleTV.setText(areaFarm.getAreaName());
            holder.farmRV.setLayoutManager(new LinearLayoutManager(mContext));
            holder.farmRV.setAdapter(new FarmInfoAdapter(mContext, areaFarm.getStores(), new FarmInfoAdapter.FIViewHolderListener() {
                @Override
                public void onClickItem(View itemView, int position) {
                    mListener.onItemClicked(position, areaFarm.getStores()[position]);
                }

                @Override
                public void onClickMore(TextView moreView, int position) {
                    mListener.onItemClicked(position, areaFarm.getStores()[position]);
                }
            }));
        }

        class FarmViewHolder extends RecyclerView.ViewHolder {

            protected TextView titleTV;
            protected RecyclerView farmRV;

            public FarmViewHolder(View itemView) {
                super(itemView);

                titleTV = (TextView) itemView.findViewById(R.id.index_farm_list_item_view_tv_township);
                farmRV = (RecyclerView) itemView.findViewById(R.id.index_farm_list_item_view_rv);
            }
        }
    }

}
