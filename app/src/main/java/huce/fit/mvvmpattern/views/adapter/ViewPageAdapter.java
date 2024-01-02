package huce.fit.mvvmpattern.views.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import huce.fit.mvvmpattern.views.fragments.home.HomeFragment;
import huce.fit.mvvmpattern.views.fragments.library.LibraryFragment;
import huce.fit.mvvmpattern.views.fragments.profile.ProfileFragment;
import huce.fit.mvvmpattern.views.fragments.search.SearchFragment;

public class ViewPageAdapter extends FragmentStateAdapter {


    public ViewPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        switch (position) {
            case 1:
                fragment = new SearchFragment();
                break;
            case 2:
                fragment = new LibraryFragment();
                break;
            case 3:
                fragment = new ProfileFragment();
                break;
            default:
                fragment = new HomeFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
