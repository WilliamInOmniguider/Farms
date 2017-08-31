package com.omni.newtaipeifarm.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.omni.newtaipeifarm.R;
import com.omni.newtaipeifarm.model.Farm;

/**
 * Created by wiliiamwang on 31/08/2017.
 */

public class FarmIntroFragment extends Fragment {

    private static final String KEY_FARM_INTRO = "key_farm_intro";

    private Context mContext;
    private View mView;

    public static FarmIntroFragment newInstance(Farm farm) {
        FarmIntroFragment fragment = new FarmIntroFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_FARM_INTRO, farm);
        fragment.setArguments(bundle);

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
            mView = inflater.inflate(R.layout.farm_intro_fragment_view, null, false);

            final Farm farm = (Farm) getArguments().getSerializable(KEY_FARM_INTRO);

            TextView introTV = (TextView) mView.findViewById(R.id.farm_intro_fragment_view_tv_intro);
            introTV.setText(farm.getIntro());
        }

        return mView;
    }
}
