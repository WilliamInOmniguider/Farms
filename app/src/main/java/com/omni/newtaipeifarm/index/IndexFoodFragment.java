package com.omni.newtaipeifarm.index;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.omni.newtaipeifarm.R;
import com.omni.newtaipeifarm.model.FoodData;
import com.omni.newtaipeifarm.model.MonthlyFood;
import com.omni.newtaipeifarm.model.OmniEvent;
import com.omni.newtaipeifarm.network.FarmApi;
import com.omni.newtaipeifarm.network.NetworkManager;
import com.omni.newtaipeifarm.view.FoodGridViewAdapter;
import com.omni.newtaipeifarm.view.SquareImageView;
import com.omni.newtaipeifarm.view.WGridView;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by wiliiamwang on 07/09/2017.
 */

public class IndexFoodFragment extends Fragment {

    private Context mContext;
    private View mView;
    private RecyclerView mRV;
    private IndexFoodAdapter mIndexFoodAdapter;

    public static IndexFoodFragment newInstance() {
        IndexFoodFragment fragment = new IndexFoodFragment();

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
            mView = inflater.inflate(R.layout.index_food_fragment_view, container, false);

            mRV = (RecyclerView) mView.findViewById(R.id.index_food_fragment_view_rv);

            FarmApi.getInstance().getAllMonthsFoodList(mContext, new NetworkManager.NetworkManagerListener<MonthlyFood[]>() {
                @Override
                public void onSucceed(MonthlyFood[] monthlyFoods) {
                    setRVData(monthlyFoods);
                }

                @Override
                public void onFail(VolleyError error, boolean shouldRetry) {

                }
            });
        }

        return mView;
    }

    private void setRVData(MonthlyFood[] monthlyFoods) {

        if (mIndexFoodAdapter == null) {
            mIndexFoodAdapter = new IndexFoodAdapter(mContext, monthlyFoods, new IndexFoodAdapter.IFViewHolderListener() {
                @Override
                public void onItemClicked(FoodData foodData) {
                    EventBus.getDefault().post(new OmniEvent(OmniEvent.TYPE_INDEX_FOOD_ITEM_CLICKED, foodData));
                }
            });

            mRV.setLayoutManager(new LinearLayoutManager(mContext));

            mRV.setAdapter(mIndexFoodAdapter);
        } else {
            mIndexFoodAdapter.notifyDataSetChanged();
        }
    }

    static class IndexFoodAdapter extends RecyclerView.Adapter<IndexFoodAdapter.FoodViewHolder> {

        public interface IFViewHolderListener {
            void onItemClicked(FoodData foodData);
        }

        private Context mContext;
        private MonthlyFood[] mMonthlyFoods;
        private IFViewHolderListener mListener;
        private LayoutInflater mInflater;

        public IndexFoodAdapter(Context context, MonthlyFood[] monthlyFoods, IFViewHolderListener listener) {
            mContext = context;
            mMonthlyFoods = monthlyFoods;
            mListener = listener;
        }

        @Override
        public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            mInflater = LayoutInflater.from(parent.getContext());
            View view = mInflater.inflate(R.layout.index_food_list_item_view, null);

            FoodViewHolder viewHolder = new FoodViewHolder(view);
            return viewHolder;
        }

        @Override
        public int getItemCount() {
            return mMonthlyFoods.length;
        }

        @Override
        public void onBindViewHolder(FoodViewHolder holder, final int position) {
            final MonthlyFood food = mMonthlyFoods[position];

            holder.titleTV.setText(food.getMonthData().getTitle());
            holder.contentWGV.setAdapter(new FoodGridViewAdapter(mContext, food.getFoodsData()));
            holder.contentWGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mListener.onItemClicked(food.getFoodsData()[position]);
                }
            });

//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mListener.onItemClicked(food);
//                }
//            });
        }

        class FoodViewHolder extends RecyclerView.ViewHolder {

            protected TextView titleTV;
            protected WGridView contentWGV;

            public FoodViewHolder(View itemView) {
                super(itemView);

                titleTV = (TextView) itemView.findViewById(R.id.index_food_list_item_view_tv_month);
                contentWGV = (WGridView) itemView.findViewById(R.id.index_food_list_item_view_wgv);
            }
        }
    }

}
