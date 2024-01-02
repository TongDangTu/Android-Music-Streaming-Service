package huce.fit.mvvmpattern.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import huce.fit.mvvmpattern.R;
import huce.fit.mvvmpattern.model.Song;
import huce.fit.mvvmpattern.views.adapter.FavoritesAdapter;
import huce.fit.mvvmpattern.views.adapter.HistoryAdapter;
import huce.fit.mvvmpattern.views.appInterface.IClickSongOption;

public class HistoryActivity extends AppCompatActivity {

    private ImageButton btnGoBack;

    private RecyclerView recyclerView;
    private FloatingActionButton fabPlay;
    private HistoryAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        btnGoBack = findViewById(R.id.btnBack);

        recyclerView = findViewById(R.id.rcvPlaylistItems);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new HistoryAdapter();
        adapter.setItems(getPlaylistSong(),new IClickSongOption() {
            @Override
            public void onClickSongOption(Song song) {
                openSongBottomSheet();
            }
            @Override
            public void onClickSong(Song song) {
                openMusicPlayer();
            }
        });
        recyclerView.setAdapter(adapter);

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                requireActivity().getOnBackPressedDispatcher().onBackPressed();

            }
        });
    }public void openMusicPlayer() {
        Intent intent = new Intent(this, MusicPlayerActivity.class);
        startActivity(intent);
    }
    public void openSongBottomSheet() {
        View viewBottom = getLayoutInflater().inflate(R.layout.layout_bottom_sheet_song, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(viewBottom);
        bottomSheetDialog.show();
    }
    private List<Song> getPlaylistSong(){
        List<Song> list = new ArrayList<>();
        return list;
    }
}