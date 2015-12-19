package net.raysuhyunlee.avant_garde;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by SuhyunLee on 2015. 10. 20..
 */
public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    private ArrayList<String> titleList = new ArrayList<>();

    public SimpleFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * Add a new fragment to the adapter.
     * @param fragment A new fragment to add
     * @param title This is used to display the title
     *              in case it is used with TabLayout.
     *              Put null if you don't need it.
     */
    public void addFragment(Fragment fragment, String title) {
        fragmentList.add(fragment);
        titleList.add(title);
    }

    /**
     * Add a new fragment to the adapter, specifically at the given position.
     * @param index The position you want to put the fragment in
     * @param fragment A new fragment to add
     * @param title This is used to display the title in case it is used with TabLayout.
     *              Put null if you don't need it.
     */
    public void addFragment(int index, Fragment fragment, String title) {
        fragmentList.add(index, fragment);
        titleList.add(index, title);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public String getPageTitle(int position) {
        return titleList.get(position);
    }
}