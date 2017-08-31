package com.omni.newtaipeifarm.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.omni.newtaipeifarm.DataCacheManager;
import com.omni.newtaipeifarm.R;
import com.omni.newtaipeifarm.adapter.SearchOptionAdapter;
import com.omni.newtaipeifarm.model.Area;
import com.omni.newtaipeifarm.model.FarmCategory;
import com.omni.newtaipeifarm.network.FarmApi;
import com.omni.newtaipeifarm.network.NetworkManager;
import com.omni.newtaipeifarm.tool.DialogTools;

/**
 * Created by wiliiamwang on 18/07/2017.
 */

public class FarmSearchFragment extends Fragment {

    public interface FarmSearchListener {
        void onClickSearch(String areaId, String categoryId, int range, String keywords);
    }

    private static FarmSearchListener mListener = null;

    private Context mContext;
    private View mView;
    private Area mSelectedArea;
    private FarmCategory mSelectedFarmCategory;

    public static FarmSearchFragment newInstance(@NonNull FarmSearchListener listener) {
        FarmSearchFragment fragment = new FarmSearchFragment();

        mListener = listener;

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mListener = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (mView == null) {
            mView = inflater.inflate(R.layout.pager_farm_search_layout, null, false);

            final AppCompatSeekBar seekBar = (AppCompatSeekBar) mView.findViewById(R.id.pager_farm_search_layout_acsb);
            final TextView searchRangeTV = (TextView) mView.findViewById(R.id.pager_farm_search_layout_tv_search_range);
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

            TextView venueTV = (TextView) mView.findViewById(R.id.pager_farm_search_layout_tv_venue_search);
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

            TextView categoryTV = (TextView) mView.findViewById(R.id.pager_farm_search_layout_tv_category_search);
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

            TextView searchTV = (TextView) mView.findViewById(R.id.pager_farm_search_layout_tv_search);
            searchTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String selectedAreaId = mSelectedArea == null ? Area.ALL_AREA_ID : mSelectedArea.getId();
                    String selectedCategoryId = mSelectedFarmCategory == null ? FarmCategory.ALL_CATEGORY_ID : mSelectedFarmCategory.getId();

                    if (mListener != null) {
//                        mListener.onClickSearch(selectedAreaId, selectedCategoryId, seekBar.getProgress(), "");
                        mListener.onClickSearch(selectedAreaId, selectedCategoryId, 0, "");
                    }
                }
            });
        }

        return mView;
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
