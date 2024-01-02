package huce.fit.mvvmpattern.views.fragments.home.section;

import java.util.List;

public class Section {
    private String sectionName;
    private List items;

    public Section(String sectionName, List items) {
        this.sectionName = sectionName;
        this.items = items;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public List getItems() {
        return items;
    }

    public void setItems(List items) {
        this.items = items;
    }
}
