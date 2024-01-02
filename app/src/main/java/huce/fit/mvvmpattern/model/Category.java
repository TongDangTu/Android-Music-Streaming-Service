package huce.fit.mvvmpattern.model;

public class Category {
    private String id;
    private String name;
    private String linkPicture;

    public Category() {
    }

    public Category(String id, String name, String linkPicture) {
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
