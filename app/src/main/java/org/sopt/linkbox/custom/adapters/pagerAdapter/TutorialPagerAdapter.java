package org.sopt.linkbox.custom.adapters.pagerAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.sopt.linkbox.fragment.BackgroundFragment;

/**
 * Created by sy on 2015-09-02.
 */
public class TutorialPagerAdapter extends FragmentPagerAdapter {
    private int pagerCount = 5;


    public TutorialPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override public Fragment getItem(int i) {
        return BackgroundFragment.newInstance(i);

    }

    @Override public int getCount() {
        return pagerCount;
    }
}