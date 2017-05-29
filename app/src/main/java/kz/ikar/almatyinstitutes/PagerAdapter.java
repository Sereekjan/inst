package kz.ikar.almatyinstitutes;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import kz.ikar.almatyinstitutes.tabs.TabFragment1;
import kz.ikar.almatyinstitutes.tabs.TabFragment2;

/**
 * Created by User on 26.05.2017.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumsOfTabs;

    public PagerAdapter(FragmentManager manager, int mNumsOfTabs) {
        super(manager);
        this.mNumsOfTabs = mNumsOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                TabFragment1 tab1 = new TabFragment1();
                return tab1;
            case 1:
                TabFragment2 tab2 = new TabFragment2();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumsOfTabs;
    }
}
