package com.omni.newtaipeifarm.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.omni.newtaipeifarm.R;

/**
 * Created by wiliiamwang on 29/08/2017.
 */

public class FarmFoodFragment extends Fragment {

    private Context mContext;
    private View mView;

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
        }

        return mView;
    }
}
