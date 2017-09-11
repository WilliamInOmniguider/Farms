package com.omni.newtaipeifarm.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.volley.VolleyError;
import com.omni.newtaipeifarm.R;
import com.omni.newtaipeifarm.model.FoodData;
import com.omni.newtaipeifarm.model.MonthlyFood;
import com.omni.newtaipeifarm.model.OmniEvent;
import com.omni.newtaipeifarm.network.FarmApi;
import com.omni.newtaipeifarm.network.NetworkManager;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by wiliiamwang on 29/08/2017.
 */

public class FarmFoodFragment extends Fragment {

    private Context mContext;
    private View mView;
    GridView mGV;

    public static FarmFoodFragment newInstance() {
        FarmFoodFragment fragment = new FarmFoodFragment();

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
            mView = inflater.inflate(R.layout.pager_farm_food_layout, null, false);

            mGV = (GridView) mView.findViewById(R.id.pager_farm_food_layout_gv);

            FarmApi.getInstance().getFoodsListByMonth(mContext, new NetworkManager.NetworkManagerListener<MonthlyFood[]>() {
                @Override
                public void onSucceed(MonthlyFood[] monthlyFoods) {
                    if (monthlyFoods.length != 0) {
                        setGV(monthlyFoods[0].getFoodsData());
                    }
                }

                @Override
                public void onFail(VolleyError error, boolean shouldRetry) {

                }
            });
        }

        return mView;
    }

    private void setGV(final FoodData[] foods) {
        mGV.setAdapter(new FoodGridViewAdapter(mContext, foods));
        mGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventBus.getDefault().post(new OmniEvent(OmniEvent.TYPE_HOME_FOOD_ITEM_CLICKED, foods[position]));
            }
        });
    }
}
