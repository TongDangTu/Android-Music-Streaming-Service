package huce.fit.mvvmpattern.model;

public class SongInfo {
    private String id_song;
    private String name_song;
    private int playedTime;
    private String linkSong;
    private String linkPicture;
    private String name_artist;
    private String name_category;

    public SongInfo() {
    }

    public SongInfo(String id_song, String name_song, int playedTime, String linkSong, String linkPicture, String name_artist, String name_category) {
        this.id_song = id_song;
        this.name_song = name_song;
        this.playedTime = playedTime;
        this.linkSong = linkSong;
        this.linkPicture = linkPicture;
        this.name_artist = name_artist;
        this.name_category = name_category;
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


    public String getName_artist() {
        return name_artist;
    }

    public void setName_artist(String name_artist) {
        this.name_artist = name_artist;
    }

    public String getName_category() {
        return name_category;
    }

    public void setName_category(String name_category) {
        this.name_category = name_category;
    }
}
