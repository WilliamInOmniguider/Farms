package com.omni.newtaipeifarm.popularity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.omni.newtaipeifarm.DataCacheManager;
import com.omni.newtaipeifarm.R;
import com.omni.newtaipeifarm.adapter.FarmInfoAdapter;
import com.omni.newtaipeifarm.model.Farm;
import com.omni.newtaipeifarm.model.OmniEvent;
import com.omni.newtaipeifarm.network.FarmApi;
import com.omni.newtaipeifarm.network.NetworkManager;
import com.omni.newtaipeifarm.tool.DialogTools;
import com.omni.newtaipeifarm.view.FarmInfoFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by wiliiamwang on 28/08/2017.
 */

public class PopularityFragment extends Fragment {

    private Context mContext;
    private View mView;
    private SwipeRefreshLayout mSRL;
    private RecyclerView mRV;
    private FarmInfoAdapter mFarmInfoAdapter;
    private TextView mRetryTV;
    private TextView mErrorMsgTV;
    private EventBus mEventBus;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(OmniEvent event) {
        if (event.getType() == OmniEvent.TYPE_REFRESH_FARM_LIST_DATA) {
            getAllFarmsData();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mEventBus == null) {
            mEventBus = EventBus.getDefault();
        }
        mEventBus.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mEventBus != null) {
            mEventBus.unregister(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.popularity_fragment_view, null, false);

            Toolbar toolbar = (Toolbar) mView.findViewById(R.id.popularity_fragment_view_action_bar);
            TextView titleTV = (TextView) toolbar.findViewById(R.id.farm_action_bar_tv_title);
            titleTV.setText(R.string.action_bar_title_popularity);

            mSRL = (SwipeRefreshLayout) mView.findViewById(R.id.popularity_result_fragment_view_srl);
            mSRL.setColorSchemeResources(R.color.default_background_color,
                    R.color.farm_color);
            mSRL.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    updateRVData();
                }
            });

            mRetryTV = (TextView) mView.findViewById(R.id.popularity_result_fragment_view_tv_retry);
            mRetryTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getAllFarmsData();
                }
            });
            mErrorMsgTV = (TextView) mView.findViewById(R.id.popularity_result_fragment_view_tv_error_message);

            mRV = (RecyclerView) mView.findViewById(R.id.popularity_result_fragment_view_rv);

            getAllFarmsData();
        }

        return mView;
    }

    private void getAllFarmsData() {
        if (!mSRL.isRefreshing()) {
            mSRL.setRefreshing(true);
        }

        FarmApi.getInstance().getAllFarms(mContext, FarmApi.SortMode.POPULAR, new NetworkManager.NetworkManagerListener<Farm[]>() {
            @Override
            public void onSucceed(Farm[] response) {

                setRVData(response);
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

    private void setRVData(final Farm[] farms) {

//        if (mFarmInfoAdapter == null) {
            mFarmInfoAdapter = new FarmInfoAdapter(mContext, farms, new FarmInfoAdapter.FIViewHolderListener() {
                @Override
                public void onClickItem(View itemView, int position) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .add(R.id.popularity_fragment_view_rl, FarmInfoFragment.newInstance(farms[position]), FarmInfoFragment.TAG).addToBackStack(null).commit();
                }

                @Override
                public void onClickMore(TextView moreView, int position) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .add(R.id.popularity_fragment_view_rl, FarmInfoFragment.newInstance(farms[position]), FarmInfoFragment.TAG).addToBackStack(null).commit();
                }
            });

            mRV.setLayoutManager(new LinearLayoutManager(mContext));

            mRV.setAdapter(mFarmInfoAdapter);
//        } else {
//            mFarmInfoAdapter.notifyDataSetChanged();
//        }
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
