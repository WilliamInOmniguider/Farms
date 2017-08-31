package com.omni.newtaipeifarm.index;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.omni.newtaipeifarm.R;

/**
 * Created by wiliiamwang on 28/08/2017.
 */

public class IndexFragment extends Fragment {

    private Context mContext;
    private View mView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.index_fragment_view, null, false);
        }

        return mView;
    }
}
