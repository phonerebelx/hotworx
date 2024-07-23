package com.hotworx.requestEntity  ;



import java.io.Serializable;
import java.util.ArrayList;

public class ViewSummaryResponse extends BaseModel<ViewSummaryResponse> implements Serializable {


    private ArrayList<SummaryPOJO> summary;
    private ArrayList<CompletedClassesPOJO> classes_completed;

    public ArrayList<SummaryPOJO> getSummary() {
        return summary;
    }

    public void setSummary(ArrayList<SummaryPOJO> summary) {
        this.summary = summary;
    }

    public ArrayList<CompletedClassesPOJO> getClasses_completed() {
        return classes_completed;
    }

    public void setClasses_completed(ArrayList<CompletedClassesPOJO> classes_completed) {
        this.classes_completed = classes_completed;
    }
}

