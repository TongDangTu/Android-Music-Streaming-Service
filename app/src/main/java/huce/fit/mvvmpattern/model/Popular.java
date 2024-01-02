package huce.fit.mvvmpattern.model;

public class Popular {
    private String id_song;
    private String name_song;
    private int playedTime;
    private String linkSong;
    private String linkPicture;
    private String id_artist;
    private String name_artist;

    public Popular() {
    }

    public Popular(String id_song, String name_song, int playedTime, String linkSong, String linkPicture, String id_artist, String name_artist) {
        this.id_song = id_song;
        this.name_song = name_song;
        this.playedTime = playedTime;
        this.linkSong = linkSong;
        this.linkPicture = linkPicture;
        this.id_artist = id_artist;
        this.name_artist = name_artist;
    }

    public String getId_song() {
        return id_song;
    }

    public void setId_song(String id_song) {
        this.id_song = id_song;
    }

    public String getName_song() {
        return name_song;
    }

    public void setName_song(String name_song) {
        this.name_song = name_song;
    }

    public int getPlayedTime() {
        return playedTime;
    }

    public void setPlayedTime(int playedTime) {
        this.playedTime = playedTime;
    }

    public String getLinkSong() {
        return linkSong;
    }

    public void setLinkSong(String linkSong) {
        this.linkSong = linkSong;
    }

    public String getLinkPicture() {
        return linkPicture;
    }

    public void setLinkPicture(String linkPicture) {
        this.linkPicture = linkPicture;
    }

    public String getId_artist() {
        return id_artist;
    }

    public void setId_artist(String id_artist) {
        this.id_artist = id_artist;
    }

    public String getName_artist() {
        return name_artist;
    }

    public void setName_artist(String name_artist) {
        this.name_artist = name_artist;
    }
}
