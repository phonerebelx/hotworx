package com.hotworx.requestEntity;

import java.util.ArrayList;
import java.util.List;

public class ParentNutritionistItem {
    // Declaration of the variables
    private String ParentItemTitle;
    private String totalCal;
    private List<DayData> ChildItemList;
    private List<ExerciseData> ExerciseChildItemList;

    // Constructor of the class
    // to initialize the variables
    public ParentNutritionistItem(
            String ParentItemTitle,
            String mTotalCal,
            List<DayData> ChildItemList,
            List<ExerciseData> mExerciseChildItemList)
    {

        this.ParentItemTitle = ParentItemTitle;
        this.ChildItemList = ChildItemList;
        this.totalCal = mTotalCal;
        this.ExerciseChildItemList = mExerciseChildItemList;
    }

    public String getTotalCal()
    {
        return totalCal;
    }

    public void setTotalCal(
            String mTotalCal)
    {
        totalCal = mTotalCal;
    }

    public String getParentItemTitle()
    {
        return ParentItemTitle;
    }

    public void setParentItemTitle(
            String parentItemTitle)
    {
        ParentItemTitle = parentItemTitle;
    }

    public List<DayData> getChildItemList()
    {
        return ChildItemList;
    }

    public void setChildItemList(
            List<DayData> childItemList)
    {
        ChildItemList = childItemList;
    }

    public List<ExerciseData> getExerciseChildItemList()
    {
        return ExerciseChildItemList;
    }

    public void setExerciseChildItemList(
            List<ExerciseData> exerciseChildItemList)
    {
        ExerciseChildItemList = exerciseChildItemList;
    }
}
