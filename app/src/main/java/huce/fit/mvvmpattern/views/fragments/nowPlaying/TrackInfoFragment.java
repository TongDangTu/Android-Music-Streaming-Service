package huce.fit.mvvmpattern.views.fragments.nowPlaying;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import huce.fit.mvvmpattern.R;
import huce.fit.mvvmpattern.services.MediaService;
import huce.fit.mvvmpattern.views.MainActivity;
import huce.fit.mvvmpattern.views.MusicPlayerActivity;


public class TrackInfoFragment extends Fragment {
    private ImageButton btnGoBack;
    private ImageView ivSong;
    private TextView tvArtist;
    private TextView tvCategory;
    private TextView tvSongName;
    private TextView tvArtistName;
    private TextView tvPlayedTime;

    private MusicPlayerActivity musicPlayerActivity;
    public TrackInfoFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_track_info, container, false);
        btnGoBack = view.findViewById(R.id.btnBack);
        ivSong = view.findViewById(R.id.iv_song_info);
        tvArtist = view.findViewById(R.id.tvArtistInfo);
        tvCategory = view.findViewById(R.id.tvCategoryInfo);
        tvSongName = view.findViewById(R.id.tvSongName);
        tvArtistName = view.findViewById(R.id.tvArtistName);
        tvPlayedTime = view.findViewById(R.id.tv_played_time);

        musicPlayerActivity = (MusicPlayerActivity) getActivity();

        MediaService.getTitleMutableLiveData().observe(musicPlayerActivity, songName -> {
            tvSongName.setText(songName);
        });
        MediaService.getArtistMutableLiveData().observe(musicPlayerActivity, artistName -> {
            tvArtist.setText(artistName);
            tvArtistName.setText(artistName);
        });
        MediaService.getCategoryMutableLiveData().observe(musicPlayerActivity, categoryName -> {
            tvCategory.setText(categoryName);
        });
        MediaService.getPlayedTimeMutableLiveData().observe(musicPlayerActivity, playedTime -> {
            tvPlayedTime.setText(playedTime+"");
        });
        MediaService.getLinkPictureMutableLiveData().observe(musicPlayerActivity, linkPicture -> {
            Glide.with(ivSong.getContext()).load(linkPicture).into(ivSong);
        });

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getOnBackPressedDispatcher().onBackPressed();
            }
        });
        return view;
    }
}