package huce.fit.mvvmpattern.views.fragments.nowPlaying;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerPlayerAdapter extends FragmentStateAdapter {
    public ViewPagerPlayerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new TrackInfoFragment();
            default:
                return new MediaPlayerFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
