package com.omni.newtaipeifarm.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.omni.newtaipeifarm.R;
import com.omni.newtaipeifarm.adapter.FarmInfoAdapter;
import com.omni.newtaipeifarm.model.Farm;
import com.omni.newtaipeifarm.model.SearchFarmResult;

/**
 * Created by wiliiamwang on 29/08/2017.
 */

public class SearchResultFragment extends Fragment {

    public static final String TAG = "tag_search_result_fragment";
    private static final String S_KEY_SEARCH_FARM_RESULT = "s_key_search_farm_result";

    private Context mContext;
    private View mView;
    private SwipeRefreshLayout mSRL;
    private RecyclerView mRV;
    private SearchFarmResult[] mResults;
    private FarmInfoAdapter mFarmInfoAdapter;

    public static SearchResultFragment newInstance(SearchFarmResult[] results) {
        SearchResultFragment fragment = new SearchResultFragment();

        Bundle arg = new Bundle();
        arg.putSerializable(S_KEY_SEARCH_FARM_RESULT, results);
        fragment.setArguments(arg);

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
            mView = inflater.inflate(R.layout.search_result_fragment, container, false);

            TextView backTV = (TextView) mView.findViewById(R.id.search_result_fragment_view_tv_back);
            backTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    manager.beginTransaction().remove(SearchResultFragment.this).commit();
                    manager.popBackStack();
                }
            });

            mResults = (SearchFarmResult[]) getArguments().getSerializable(S_KEY_SEARCH_FARM_RESULT);
            final Farm[] farms = new Farm[mResults.length];
            for (int i = 0; i < mResults.length; i++) {
                farms[i] = mResults[i].toFarm();
            }

            mSRL = (SwipeRefreshLayout) mView.findViewById(R.id.search_result_fragment_view_srl);
            mSRL.setColorSchemeResources(R.color.default_background_color,
                    R.color.farm_color);
            mSRL.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    mSRL.setRefreshing(false);
                }
            });

            mRV = (RecyclerView) mView.findViewById(R.id.search_result_fragment_view_rv);

            if (mFarmInfoAdapter == null) {
                mFarmInfoAdapter = new FarmInfoAdapter(mContext, farms, new FarmInfoAdapter.FIViewHolderListener() {
                    @Override
                    public void onClickItem(View itemView, int position) {
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .add(R.id.search_result_fragment_view_rl, FarmInfoFragment.newInstance(farms[position]), FarmInfoFragment.TAG).addToBackStack(null).commit();
                    }

                    @Override
                    public void onClickMore(TextView moreView, int position) {
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .add(R.id.search_result_fragment_view_rl, FarmInfoFragment.newInstance(farms[position]), FarmInfoFragment.TAG).addToBackStack(null).commit();
                    }
                });

                mRV.setLayoutManager(new LinearLayoutManager(mContext));
//                DividerItemDecoration divider = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
//                divider.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.rv_divider_farm_green));
//                mRV.addItemDecoration(divider);

                mRV.setAdapter(mFarmInfoAdapter);
            } else {
                mFarmInfoAdapter.notifyDataSetChanged();
            }
        }

        return mView;
    }
}
