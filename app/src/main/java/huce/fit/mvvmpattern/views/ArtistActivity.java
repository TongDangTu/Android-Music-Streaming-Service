package huce.fit.mvvmpattern.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import huce.fit.mvvmpattern.R;
import huce.fit.mvvmpattern.model.Song;
import huce.fit.mvvmpattern.views.adapter.ArtistAdapter;
import huce.fit.mvvmpattern.views.adapter.PlaylistAdapter;
import huce.fit.mvvmpattern.views.appInterface.IClickSongOption;
import huce.fit.mvvmpattern.views.fragments.home.itemPopular.PopularAdapter;
import huce.fit.mvvmpattern.views.fragments.library.itemPlaylist.Playlist;

public class ArtistActivity extends AppCompatActivity {

    private ImageButton btnGoBack;

    private RecyclerView recyclerView;
    private ArtistAdapter adapter;
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        constraintLayout = findViewById(R.id.clPlaylistInfo);

        recyclerView = findViewById(R.id.rcvArtistSongs);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ArtistAdapter();
        adapter.setItems(getSong(), new IClickSongOption() {
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


    }
    public void openMusicPlayer() {
        Intent intent = new Intent(this, MusicPlayerActivity.class);
        startActivity(intent);
    }
    public void openSongBottomSheet() {
        View viewBottom = getLayoutInflater().inflate(R.layout.layout_bottom_sheet_song, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(viewBottom);
        bottomSheetDialog.show();
    }
    private List<Song> getSong(){
        List<Song> list = new ArrayList<>();
        return list;
    }
}
