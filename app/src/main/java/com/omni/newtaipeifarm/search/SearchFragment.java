package com.omni.newtaipeifarm.search;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.omni.newtaipeifarm.DataCacheManager;
import com.omni.newtaipeifarm.R;
import com.omni.newtaipeifarm.adapter.SearchOptionAdapter;
import com.omni.newtaipeifarm.model.Area;
import com.omni.newtaipeifarm.model.FarmCategory;
import com.omni.newtaipeifarm.model.SearchFarmResult;
import com.omni.newtaipeifarm.network.FarmApi;
import com.omni.newtaipeifarm.network.NetworkManager;
import com.omni.newtaipeifarm.tool.DialogTools;
import com.omni.newtaipeifarm.view.OmniEditText;
import com.omni.newtaipeifarm.view.SearchResultFragment;

/**
 * Created by wiliiamwang on 28/08/2017.
 */

public class SearchFragment extends Fragment {

    private Context mContext;
    private View mView;
    private OmniEditText mOmniET;
    private Area mSelectedArea;
    private FarmCategory mSelectedFarmCategory;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.search_fragment_view, null, false);

            Toolbar toolbar = (Toolbar) mView.findViewById(R.id.search_fragment_view_action_bar);
            TextView titleTV = (TextView) toolbar.findViewById(R.id.farm_action_bar_tv_title);
            titleTV.setText(R.string.action_bar_title_search);

            mOmniET = (OmniEditText) mView.findViewById(R.id.search_fragment_view_oet_search);
            mOmniET.setOnOmniEditTextActionListener(new OmniEditText.OnOmniEditTextActionListener() {
                @Override
                public void onSoftKeyboardDismiss() {

                }

                @Override
                public void onTouch(MotionEvent event) {

                }
            });

            TextView venueTV = (TextView) mView.findViewById(R.id.search_fragment_view_tv_venue_search);
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

            TextView categoryTV = (TextView) mView.findViewById(R.id.search_fragment_view_tv_category_search);
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

            TextView searchTV = (TextView) mView.findViewById(R.id.search_fragment_view_tv_search);
            searchTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String selectedAreaId = mSelectedArea == null ? Area.ALL_AREA_ID : mSelectedArea.getId();
                    String selectedCategoryId = mSelectedFarmCategory == null ? FarmCategory.ALL_CATEGORY_ID : mSelectedFarmCategory.getId();

                    FarmApi.getInstance().searchFarms(getActivity(), selectedCategoryId, selectedAreaId, mOmniET.getText().toString(),
                            new NetworkManager.NetworkManagerListener<SearchFarmResult[]>() {
                                @Override
                                public void onSucceed(SearchFarmResult[] results) {
                                    getActivity().getSupportFragmentManager().beginTransaction()
                                            .add(R.id.search_fragment_view_fl, SearchResultFragment.newInstance(results), SearchResultFragment.TAG)
                                            .addToBackStack(null)
                                            .commit();
                                }

                                @Override
                                public void onFail(VolleyError error, boolean shouldRetry) {
                                    DialogTools.getInstance().showErrorMessage(getActivity(), R.string.dialog_title_api_error, error.getMessage());
                                }
                            });
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
