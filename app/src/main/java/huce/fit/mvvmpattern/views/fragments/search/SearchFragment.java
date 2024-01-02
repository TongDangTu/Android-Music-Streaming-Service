package huce.fit.mvvmpattern.views.fragments.search;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import huce.fit.mvvmpattern.R;
import huce.fit.mvvmpattern.api.SongInfoService;
import huce.fit.mvvmpattern.model.DataJson;
import huce.fit.mvvmpattern.model.Song;
import huce.fit.mvvmpattern.model.SongInfo;
import huce.fit.mvvmpattern.views.MainActivity;
import huce.fit.mvvmpattern.views.appInterface.IClickSongOption;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {
    private RecyclerView recyclerView;
    private SearchAdapter songAdapter;
    private MainActivity mainActivity;
    private SearchView searchView;
    private List<Song> list;
    private ProgressBar progressBar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_search,container,false);
        mainActivity = (MainActivity) getActivity();
        progressBar = view.findViewById(R.id.pb_search);
        searchView = view.findViewById(R.id.searchView);
        searchView.setOnClickListener(view1 -> {
            searchView.onActionViewExpanded();
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                getListSong(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        recyclerView = view.findViewById(R.id.rcvSearchItems);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mainActivity, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        list = new ArrayList<>();
        songAdapter = new SearchAdapter();
        songAdapter.setItems(list, new IClickSongOption() {
            @Override
            public void onClickSongOption(Song song) {
                mainActivity.openSongBottomSheet(song);
            }
            @Override
            public void onClickSong(Song song) {
                mainActivity.openMusicPlayer();
                MainActivity.song = song;
                MainActivity.songList = null;
            }
        });
        recyclerView.setAdapter(songAdapter);
        return view;
//        return super.onCreateView(inflater, container, savedInstanceState);
    }
    private void getListSong(String query){
        progressBar.setVisibility(View.VISIBLE);
        SongInfoService.songInfoService.search(query)
                .enqueue(new Callback<DataJson<SongInfo>>() {
                    @Override
                    public void onResponse(Call<DataJson<SongInfo>> call, Response<DataJson<SongInfo>> response) {
                        list.clear();
                        DataJson<SongInfo> dataJson = response.body();
                        if (dataJson != null) {
                            if (dataJson.isStatus() == true) {
                                List<SongInfo> songInfoList = dataJson.getData();
                                for (int i = 0; i < songInfoList.size(); i++) {
                                    list.add(new Song(songInfoList.get(i).getId_song(), songInfoList.get(i).getLinkPicture(), songInfoList.get(i).getName_song(), songInfoList.get(i).getName_artist(), songInfoList.get(i).getLinkSong(), songInfoList.get(i).getName_category(), songInfoList.get(i).getPlayedTime()));
                                }
                                updateListSongAdapter();
                            }
                            else {
                                updateListSongAdapter();
                                Toast.makeText(mainActivity, dataJson.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<DataJson<SongInfo>> call, Throwable t) {
                        Log.e("ERROR", this.getClass().getName()+"getListSong()->onFailure: "+t.getMessage());
                        Toast.makeText(mainActivity, R.string.not_connection, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateListSongAdapter() {
        songAdapter.setItems(list, new IClickSongOption() {
            @Override
            public void onClickSongOption(Song song) {
                mainActivity.openSongBottomSheet(song);
            }
            @Override
            public void onClickSong(Song song) {
                mainActivity.openMusicPlayer();
                MainActivity.song = song;
                MainActivity.songList = null;
            }
        });
    }
}
