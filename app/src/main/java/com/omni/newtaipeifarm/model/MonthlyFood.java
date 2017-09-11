package com.omni.newtaipeifarm.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by wiliiamwang on 08/09/2017.
 */

public class MonthlyFood implements Serializable {

    @SerializedName("month")
    private MonthData monthData;
    @SerializedName("list")
    private FoodData[] foodsData;

    public MonthData getMonthData() {
        return monthData;
    }

    public void setMonthData(MonthData monthData) {
        this.monthData = monthData;
    }

    public FoodData[] getFoodsData() {
        return foodsData;
    }

    public void setFoodsData(FoodData[] foodsData) {
        this.foodsData = foodsData;
    }
}
