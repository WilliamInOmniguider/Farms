package com.omni.newtaipeifarm;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.omni.newtaipeifarm.adapter.FarmInfoAdapter;
import com.omni.newtaipeifarm.adapter.SearchOptionAdapter;
import com.omni.newtaipeifarm.model.Area;
import com.omni.newtaipeifarm.model.Farm;
import com.omni.newtaipeifarm.model.FarmCategory;
import com.omni.newtaipeifarm.network.FarmApi;
import com.omni.newtaipeifarm.network.NetworkManager;
import com.omni.newtaipeifarm.tool.DialogTools;

/**
 * Created by wiliiamwang on 28/06/2017.
 */

public class FarmContentPagerAdapter extends PagerAdapter {

    public interface FarmContentPagerAdapterListener {
        void onClickFarmList(Farm farm);
        void onClickMore(Farm farm);
        void onClickSearch(String selectedAreaId, String selectedCategoryId, int searchRange);
    }

    private Context mContext;
    private FarmContentPagerAdapterListener mListener;
    private SwipeRefreshLayout mSRL;
    private RecyclerView mRV;
    private FarmInfoAdapter mFarmInfoAdapter;
    private TextView mRetryTV;
    private TextView mErrorMsgTV;
    private Area mSelectedArea;
    private FarmCategory mSelectedFarmCategory;

    public FarmContentPagerAdapter(Context context, FarmContentPagerAdapterListener listener) {
        mContext = context;
        mListener = listener;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getCount() {
        return FarmContentPagerModule.values().length;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(FarmContentPagerModule.values()[position].getLayoutResId(), container, false);

        switch (position) {
            case 0:
                mSRL = (SwipeRefreshLayout) view.findViewById(R.id.pager_farm_list_layout_srl);
                mSRL.setColorSchemeResources(R.color.default_background_color,
                        R.color.farm_green);
                mSRL.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        updateRVData();
                    }
                });

                mRetryTV = (TextView) view.findViewById(R.id.pager_farm_list_layout_tv_retry);
                mRetryTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getAllFarmsData();
                    }
                });
                mErrorMsgTV = (TextView) view.findViewById(R.id.pager_farm_list_layout_tv_error_message);

                mRV = (RecyclerView) view.findViewById(R.id.pager_farm_list_layout_rv);

                if (DataCacheManager.getInstance().getAllFarms() == null) {
                    getAllFarmsData();
                } else {
                    setRVData();
                }

                break;

            case 1:
//                pager_farm_search_layout

//                OmniEditText searchOET = (OmniEditText) view.findViewById(R.id.pager_farm_search_layout_oet_search);
//                searchOET.setOnOmniEditTextActionListener(new OmniEditText.OnOmniEditTextActionListener() {
//                    @Override
//                    public void onSoftKeyboardDismiss() {
//
//                    }
//
//                    @Override
//                    public void onTouch(MotionEvent event) {
//
//                    }
//                });

                final AppCompatSeekBar seekBar = (AppCompatSeekBar) view.findViewById(R.id.pager_farm_search_layout_acsb);
                final TextView searchRangeTV = (TextView) view.findViewById(R.id.pager_farm_search_layout_tv_search_range);
                searchRangeTV.setText(mContext.getString(R.string.search_range, seekBar.getProgress()));

                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        searchRangeTV.setText(mContext.getString(R.string.search_range, progress));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                TextView venueTV = (TextView) view.findViewById(R.id.pager_farm_search_layout_tv_venue_search);
                venueTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        Area[] allAreas = DataCacheManager.getInstance().getAllAreas();

                        if (allAreas == null) {
                            FarmApi.getInstance().getAllAreas(mContext, new NetworkManager.NetworkManagerListener<Area[]>() {
                                @Override
                                public void onSucceed(Area[] areas) {
                                    Area.Builder builder = new Area.Builder();
                                    Area allAreas = builder
                                            .setId(Area.ALL_AREA_ID)
                                            .setName(mContext.getString(R.string.search_option_all))
                                            .build();

                                    Area[] array = new Area[areas.length + 1];
                                    array[0] = allAreas;
                                    for (int i = 0; i < areas.length; i++) {
                                        array[i + 1] = areas[i];
                                    }

                                    DataCacheManager.getInstance().setAllAreas(array);

                                    showSearchDialog(array, (TextView) v);
                                }

                                @Override
                                public void onFail(VolleyError error, boolean shouldRetry) {
                                    DialogTools.getInstance().showErrorMessage(mContext, R.string.dialog_title_api_error, error.getMessage());
                                }
                            });
                        } else {
                            showSearchDialog(allAreas, (TextView) v);
                        }
                    }
                });

                TextView categoryTV = (TextView) view.findViewById(R.id.pager_farm_search_layout_tv_category_search);
                categoryTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        FarmCategory[] allCategories = DataCacheManager.getInstance().getAllCategories();

                        if (allCategories == null) {
                            FarmApi.getInstance().getAllCategories(mContext, new NetworkManager.NetworkManagerListener<FarmCategory[]>() {
                                @Override
                                public void onSucceed(FarmCategory[] categories) {
                                    FarmCategory.Builder builder = new FarmCategory.Builder();
                                    FarmCategory allCategories = builder
                                            .setId(FarmCategory.ALL_CATEGORY_ID)
                                            .setName(mContext.getString(R.string.search_option_all))
                                            .setNameEN(mContext.getString(R.string.search_option_all)).build();

                                    FarmCategory[] array = new FarmCategory[categories.length + 1];
                                    array[0] = allCategories;
                                    for (int i = 0; i < categories.length; i++) {
                                        array[i + 1] = categories[i];
                                    }

                                    DataCacheManager.getInstance().setAllCategories(array);

                                    showSearchDialog(array, (TextView) v);
                                }

                                @Override
                                public void onFail(VolleyError error, boolean shouldRetry) {
                                    DialogTools.getInstance().showErrorMessage(mContext, R.string.dialog_title_api_error, error.getMessage());
                                }
                            });
                        } else {
                            showSearchDialog(allCategories, (TextView) v);
                        }
                    }
                });

                TextView searchTV = (TextView) view.findViewById(R.id.pager_farm_search_layout_tv_search);
                searchTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String selectedAreaId = mSelectedArea == null ? Area.ALL_AREA_ID : mSelectedArea.getId();
                        String selectedCategoryId = mSelectedFarmCategory == null ? FarmCategory.ALL_CATEGORY_ID : mSelectedFarmCategory.getId();

                        mListener.onClickSearch(selectedAreaId, selectedCategoryId, seekBar.getProgress());
                    }
                });

                break;
        }

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getString(FarmContentPagerModule.values()[position].getTitleResId());
    }

    private void getAllFarmsData() {
        if (!mSRL.isRefreshing()) {
            mSRL.setRefreshing(true);
        }

        FarmApi.getInstance().getAllFarms(mContext, new NetworkManager.NetworkManagerListener<Farm[]>() {
            @Override
            public void onSucceed(Farm[] response) {

                DataCacheManager.getInstance().setAllFarms(response);

                setRVData();
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

    private void setRVData() {
        final Farm[] farms = DataCacheManager.getInstance().getAllFarms();

        mRV.setLayoutManager(new LinearLayoutManager(mContext));
        DividerItemDecoration divider = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.rv_divider_farm_green));
        mRV.addItemDecoration(divider);

        if (mFarmInfoAdapter == null) {
            mFarmInfoAdapter = new FarmInfoAdapter(mContext, farms, new FarmInfoAdapter.FIViewHolderListener() {
                @Override
                public void onClickItem(View itemView, int position) {
                    mListener.onClickFarmList(farms[position]);
                }

                @Override
                public void onClickMore(TextView moreView, int position) {
                    mListener.onClickMore(farms[position]);
                }
            });

            mRV.setAdapter(mFarmInfoAdapter);
        } else {
            mFarmInfoAdapter.notifyDataSetChanged();
        }
    }

    private void updateRVData() {
        getAllFarmsData();
    }

    private void setErrorMsg(String errorMsg) {
        mRetryTV.setVisibility(TextUtils.isEmpty(errorMsg) ? View.GONE : View.VISIBLE);
        mErrorMsgTV.setText(errorMsg);
        mErrorMsgTV.setVisibility(TextUtils.isEmpty(errorMsg) ? View.GONE : View.VISIBLE);
    }

    private <T> void showSearchDialog(final T[] data, final TextView tv) {

        RecyclerView rv = new RecyclerView(mContext);

        final AlertDialog dialog = new AlertDialog.Builder(mContext).setView(rv).create();

        rv.setLayoutManager(new LinearLayoutManager(mContext));
        DividerItemDecoration divider = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.rv_divider_farm_green));
        rv.addItemDecoration(divider);
        rv.setAdapter(new SearchOptionAdapter(mContext, data, new SearchOptionAdapter.SOAListener() {
            @Override
            public void onClickItem(View itemView, Object obj) {
                if (obj instanceof Area) {
                    tv.setText(((Area) obj).getName());
                    mSelectedArea = (Area) obj;
                } else if (obj instanceof FarmCategory) {
                    tv.setText(((FarmCategory) obj).getName());
                    mSelectedFarmCategory = (FarmCategory) obj;
                }

                dialog.dismiss();
            }
        }));

        dialog.show();
    }
}
