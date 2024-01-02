package huce.fit.mvvmpattern.model;

import huce.fit.mvvmpattern.R;

public class Artist {
    private String id;
    private String name;
    private String linkPicture;

    public Artist() {
    }

    public Artist(String id, String name, String linkPicture) {
        this.id = id;
        this.name = name;
        this.linkPicture = linkPicture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLinkPicture() {
        return linkPicture;
    }

    public void setLinkPicture(String linkPicture) {
        this.linkPicture = linkPicture;
    }
}
