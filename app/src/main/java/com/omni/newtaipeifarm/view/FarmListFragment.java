package com.omni.newtaipeifarm.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.omni.newtaipeifarm.DataCacheManager;
import com.omni.newtaipeifarm.R;
import com.omni.newtaipeifarm.adapter.FarmInfoAdapter;
import com.omni.newtaipeifarm.model.Farm;
import com.omni.newtaipeifarm.network.FarmApi;
import com.omni.newtaipeifarm.network.NetworkManager;
import com.omni.newtaipeifarm.tool.DialogTools;

/**
 * Created by wiliiamwang on 18/07/2017.
 */

public class FarmListFragment extends Fragment {

    public interface FarmListListener {
        void onFarmsItemClick(Farm farm);
        void onItemMoreClick(Farm farm);
    }

    private static FarmListListener mListener = null;

    private Context mContext;
    private View mView;
    private SwipeRefreshLayout mSRL;
    private RecyclerView mRV;
    private FarmInfoAdapter mFarmInfoAdapter;
    private TextView mRetryTV;
    private TextView mErrorMsgTV;

    public static FarmListFragment newInstance(FarmListListener listener) {
        FarmListFragment fragment = new FarmListFragment();

        mListener = listener;

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mListener = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.pager_farm_list_layout, null, false);

            mSRL = (SwipeRefreshLayout) mView.findViewById(R.id.pager_farm_list_layout_srl);
            mSRL.setColorSchemeResources(R.color.default_background_color,
                    R.color.farm_color);
            mSRL.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    updateRVData();
                }
            });

            mRetryTV = (TextView) mView.findViewById(R.id.pager_farm_list_layout_tv_retry);
            mRetryTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getAllFarmsData();
                }
            });
            mErrorMsgTV = (TextView) mView.findViewById(R.id.pager_farm_list_layout_tv_error_message);

            mRV = (RecyclerView) mView.findViewById(R.id.pager_farm_list_layout_rv);

            if (DataCacheManager.getInstance().getAllFarms() == null) {
                getAllFarmsData();
            } else {
                setRVData();
            }
        }

        return mView;
    }

    private void getAllFarmsData() {
        if (!mSRL.isRefreshing()) {
            mSRL.setRefreshing(true);
        }

        FarmApi.getInstance().getAllFarms(mContext, FarmApi.SortMode.DEFAULT, new NetworkManager.NetworkManagerListener<Farm[]>() {
            @Override
            public void onSucceed(Farm[] response) {

                DataCacheManager.getInstance().setAllFarms(response);

                setRVData();
                mSRL.setRefreshing(false);

                setErrorMsg("");
            }

            @Override
            public void onFail(VolleyError error, boolean shouldRetry) {
                mSRL.setRefreshing(false);

                setErrorMsg(error.getMessage());
                DialogTools.getInstance().showErrorMessage(mContext, R.string.dialog_title_api_error, error.getMessage());
            }
        });
    }

    private void setRVData() {
        final Farm[] farms = DataCacheManager.getInstance().getAllFarms();

        if (mFarmInfoAdapter == null) {
            mFarmInfoAdapter = new FarmInfoAdapter(mContext, farms, new FarmInfoAdapter.FIViewHolderListener() {
                @Override
                public void onClickItem(View itemView, int position) {
                    if (mListener != null) {
                        Log.e("@W@", "*** FarmListFragment click");
                        mListener.onFarmsItemClick(farms[position]);
                    }
                }

                @Override
                public void onClickMore(TextView moreView, int position) {
                    if (mListener != null) {
                        mListener.onItemMoreClick(farms[position]);
                    }
                }
            });

            mRV.setLayoutManager(new LinearLayoutManager(mContext));
//            DividerItemDecoration divider = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
//            divider.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.rv_divider_farm_green));
//            mRV.addItemDecoration(divider);

            mRV.setAdapter(mFarmInfoAdapter);
        } else {
            mFarmInfoAdapter.notifyDataSetChanged();
        }
    }

    private void updateRVData() {
        getAllFarmsData();
    }

    private void setErrorMsg(String errorMsg) {
        mRV.setVisibility(TextUtils.isEmpty(errorMsg) ? View.VISIBLE : View.GONE);
        mRetryTV.setVisibility(TextUtils.isEmpty(errorMsg) ? View.GONE : View.VISIBLE);
        mErrorMsgTV.setText(errorMsg);
        mErrorMsgTV.setVisibility(TextUtils.isEmpty(errorMsg) ? View.GONE : View.VISIBLE);
    }
}
