package com.omni.newtaipeifarm.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.omni.newtaipeifarm.R;
import com.omni.newtaipeifarm.model.Farm;

/**
 * Created by wiliiamwang on 31/08/2017.
 */

public class FarmContactInfoFragment extends Fragment {

    private static final String KEY_CONTACT_INFO = "key_contact_info";

    private Context mContext;
    private View mView;

    public static FarmContactInfoFragment newInstance(Farm farm) {
        FarmContactInfoFragment fragment = new FarmContactInfoFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_CONTACT_INFO, farm);
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
            mView = inflater.inflate(R.layout.farm_contact_info_fragment_view, null, false);

            final Farm farm = (Farm) getArguments().getSerializable(KEY_CONTACT_INFO);

            OmniIconItem ownerOII = (OmniIconItem) mView.findViewById(R.id.farm_contact_info_fragment_view_oii_owner);
            ownerOII.setTitleText(farm.getOwnerName());

            OmniIconItem addressOII = (OmniIconItem) mView.findViewById(R.id.farm_contact_info_fragment_view_oii_address);
            addressOII.setTitleText(farm.getAddress());

            OmniIconItem openTimeOII = (OmniIconItem) mView.findViewById(R.id.farm_contact_info_fragment_view_oii_open_time);
            openTimeOII.setTitleText(farm.getOpenTime());

            OmniIconItem categoryOII = (OmniIconItem) mView.findViewById(R.id.farm_contact_info_fragment_view_oii_category);
            categoryOII.setTitleText(farm.getCatecory());
        }

        return mView;
    }
}
