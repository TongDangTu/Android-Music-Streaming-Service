package huce.fit.mvvmpattern.views.appInterface;

import huce.fit.mvvmpattern.model.Artist;
import huce.fit.mvvmpattern.model.Category;
import huce.fit.mvvmpattern.model.Song;

public interface IClickSongOption {
    void onClickSongOption(Song song);

    void onClickSong(Song song);


}
