package com.omni.newtaipeifarm.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.omni.newtaipeifarm.R;
import com.omni.newtaipeifarm.model.FoodData;
import com.omni.newtaipeifarm.network.NetworkManager;

/**
 * Created by wiliiamwang on 08/09/2017.
 */

public class FoodGridViewAdapter extends BaseAdapter {

    private Context mContext;
    private FoodData[] mFoodDatas;

    public FoodGridViewAdapter(Context context, FoodData[] foodDatas) {
        mContext = context;
        mFoodDatas = foodDatas;
    }

    @Override
    public int getCount() {
        return mFoodDatas.length;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public FoodData getItem(int position) {
        return mFoodDatas[position];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FoodGridViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.food_grid_item_view, parent, false);

            viewHolder = new FoodGridViewHolder((SquareImageView) convertView.findViewById(R.id.food_grid_item_view_siv_icon),
                    (TextView) convertView.findViewById(R.id.food_grid_item_view_tv_title));
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (FoodGridViewHolder) convertView.getTag();
        }

        FoodData foodData = getItem(position);

        viewHolder.titleTV.setText(foodData.getTitle());
        NetworkManager.getInstance().setNetworkImage(mContext, viewHolder.iconSIV, foodData.getImage(), R.mipmap.ntpc_icon);

        return convertView;
    }

    class FoodGridViewHolder {
        SquareImageView iconSIV;
        TextView titleTV;

        public FoodGridViewHolder(SquareImageView siv, TextView tv) {
            iconSIV = siv;
            titleTV = tv;
        }
    }
}
