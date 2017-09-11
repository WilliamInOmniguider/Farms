package com.omni.newtaipeifarm.index;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.omni.newtaipeifarm.R;
import com.omni.newtaipeifarm.model.Area;
import com.omni.newtaipeifarm.model.Farm;
import com.omni.newtaipeifarm.model.FarmCategory;
import com.omni.newtaipeifarm.model.FoodData;
import com.omni.newtaipeifarm.model.OmniEvent;
import com.omni.newtaipeifarm.model.SearchFarmResult;
import com.omni.newtaipeifarm.network.FarmApi;
import com.omni.newtaipeifarm.network.NetworkManager;
import com.omni.newtaipeifarm.tool.DialogTools;
import com.omni.newtaipeifarm.view.FarmInfoFragment;
import com.omni.newtaipeifarm.view.SearchResultFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by wiliiamwang on 28/08/2017.
 */

public class IndexFragment extends Fragment {

    private Context mContext;
    private View mView;
    private LayoutInflater mInflater;
    private ViewPager mFarmContentVP;
    private TabLayout mFarmContentTL;
    private EventBus mEventBus;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(OmniEvent event) {
        if (event.getType() == OmniEvent.TYPE_INDEX_FARM_ITEM_CLICKED) {
            Farm farm = (Farm) event.getObj();
            showFarmInfoView(farm);
        } else if (event.getType() == OmniEvent.TYPE_INDEX_FOOD_ITEM_CLICKED) {
            FoodData foodData = (FoodData) event.getObj();
            searchFoodResultView(foodData);
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
            mView = inflater.inflate(R.layout.index_fragment_view, null, false);

            mInflater = inflater;

            Toolbar toolbar = (Toolbar) mView.findViewById(R.id.index_fragment_view_action_bar);
            TextView titleTV = (TextView) toolbar.findViewById(R.id.farm_action_bar_tv_title);
            titleTV.setText(R.string.action_bar_title_index);

            mFarmContentVP = (ViewPager) mView.findViewById(R.id.index_fragment_view_vp);
            mFarmContentVP.setAdapter(new IndexContentPagerAdapter(getChildFragmentManager(), mContext));

            mFarmContentTL = (TabLayout) mView.findViewById(R.id.index_fragment_view_tl);
            mFarmContentTL.setupWithViewPager(mFarmContentVP, true);
            for (int i = 0; i < mFarmContentTL.getTabCount(); i++) {
                TabLayout.Tab tab = mFarmContentTL.getTabAt(i);
                setFarmContentTabView(tab, i == 0);
            }
            mFarmContentTL.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    setFarmContentTabView(tab, true);
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    setFarmContentTabView(tab, false);
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }

        return mView;
    }

    private void setFarmContentTabView(TabLayout.Tab tab, boolean isSelected) {
        boolean shouldSetView = (tab.getCustomView() == null);

        TextView tv = shouldSetView ?
                (TextView) mInflater.inflate(R.layout.pager_farm_content_tab_view, null, false) :
                (TextView) tab.getCustomView();

        tv.setTextColor(ContextCompat.getColor(mContext, isSelected ? R.color.farm_pager_title_color : R.color.farm_pager_title_color));
        tv.setText(tab.getText());

        if (mFarmContentTL.getTabCount() == 1) {
            tv.setBackgroundResource(isSelected ? R.drawable.tab_only_one_bg : R.drawable.tab_only_one_bg);
        } else {

            if (tab.getPosition() == mFarmContentTL.getTabCount() - 1) {
                tv.setBackgroundResource(isSelected ? R.drawable.tab_right_farm_green_selected : R.drawable.tab_right_farm_green_default);
            } else if (tab.getPosition() == 0) {
                tv.setBackgroundResource(isSelected ? R.drawable.tab_left_farm_green_selected : R.drawable.tab_left_farm_green_default);
            } else {
                tv.setBackgroundResource(isSelected ? R.drawable.tab_center_farm_green_selected : R.drawable.tab_center_farm_green_default);
            }
        }

        if (shouldSetView) {
            tab.setCustomView(tv);
        }
    }

    private void searchFoodResultView(FoodData foodData) {
        FarmApi.getInstance().searchFarms(mContext, FarmCategory.ALL_CATEGORY_ID, Area.ALL_AREA_ID, foodData.getTitle(),
                new NetworkManager.NetworkManagerListener<SearchFarmResult[]>() {
                    @Override
                    public void onSucceed(SearchFarmResult[] results) {
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .add(R.id.index_fragment_view_rl, SearchResultFragment.newInstance(results), SearchResultFragment.TAG)
                                .addToBackStack(null)
                                .commit();
                    }

                    @Override
                    public void onFail(VolleyError error, boolean shouldRetry) {
                        if (error.getMessage().equals("INVALID STORE")) {
                            DialogTools.getInstance().showErrorMessage(getActivity(), R.string.dialog_title_text_normal_error, R.string.dialog_msg_search_no_result);
                        } else {
                            DialogTools.getInstance().showErrorMessage(getActivity(), R.string.dialog_title_api_error, error.getMessage());
                        }
                    }
                });
    }

    private void showFarmInfoView(Farm farm) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.index_fragment_view_rl, FarmInfoFragment.newInstance(farm), FarmInfoFragment.TAG).addToBackStack(null).commit();
    }
}
