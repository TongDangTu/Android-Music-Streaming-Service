package huce.fit.mvvmpattern.views;

import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager2.widget.ViewPager2;

import huce.fit.mvvmpattern.databinding.ActivityMusicPlayerBinding;

import huce.fit.mvvmpattern.R;
import huce.fit.mvvmpattern.views.fragments.nowPlaying.ViewPagerPlayerAdapter;

public class MusicPlayerActivity extends AppCompatActivity {
private ViewPager2 viewPager;
    private AppBarConfiguration appBarConfiguration;
private ActivityMusicPlayerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        viewPager = findViewById(R.id.viewPagerPlayer);

        ViewPagerPlayerAdapter vpPlayerAdapter = new ViewPagerPlayerAdapter(this);
        viewPager.setAdapter(vpPlayerAdapter);
    }
}