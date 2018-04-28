package sge.cs.stir.ac.uk.mygallery;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

//PagerAdapter class does the work of swtiching between fragments and Tabs.

class PagerAdapter extends FragmentStatePagerAdapter{
    private final Fragment[] fragments;



    public PagerAdapter(FragmentManager fragmentManager,Fragment... fragments){
        super(fragmentManager);
        this.fragments = fragments;

    }


    /**
     * Getting the current fragment position.
     * @param position
     * @return Fragment
     */

    @Override
    public Fragment getItem(int position) {

       return fragments[position];
    }

    /**
     * Getting total Number of Fragments.
     * @return int
     */

    @Override
    public int getCount(){

        return fragments.length;
    }
}