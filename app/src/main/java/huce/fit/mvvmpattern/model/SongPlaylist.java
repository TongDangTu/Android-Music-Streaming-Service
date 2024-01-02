package huce.fit.mvvmpattern.model;

public class SongPlaylist {
    private String id;
    private String id_song;
    private String id_playlist;

    public SongPlaylist(String id, String id_song, String id_playlist) {
        this.id = id;
        this.id_song = id_song;
        this.id_playlist = id_playlist;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_song() {
        return id_song;
    }

    public void setId_song(String id_song) {
        this.id_song = id_song;
    }

    public String getId_playlist() {
        return id_playlist;
    }

    public void setId_playlist(String id_playlist) {
        this.id_playlist = id_playlist;
    }
}
