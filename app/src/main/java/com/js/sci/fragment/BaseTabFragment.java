package com.js.sci.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jskim on 2017. 5. 18..
 */

public abstract class BaseTabFragment extends BaseFragment {
    private ViewPager viewPager;
    private JSPagerAdapter pagerAdapter;

    TabLayout.OnTabSelectedListener onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            int position = tab.getPosition();
            viewPager.setCurrentItem(position);
            BaseFragment fragment = pagerAdapter.getFragment(position);
            fragment.onRefresh();
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {}

        @Override
        public void onTabReselected(TabLayout.Tab tab) {}
    };

    public BaseTabFragment() {
    }

    @Override
    protected void initView() {
        setupViewPager();
        setupTabLayout();
    }

    @Override
    protected void clear() {
        if (pagerAdapter != null) {
            pagerAdapter.clear();
        }
    }

    /**
     * ViewPager 세팅
     */
    private void setupViewPager() {
        viewPager = (ViewPager) rootView.findViewById(getViewPagerId());
        pagerAdapter = new JSPagerAdapter(getActivity().getSupportFragmentManager());
        for (int i = 0; i < getFragmentList().size(); i++) {
            pagerAdapter.addFragment(getFragmentList().get(i), getFragmentTitleList().get(i));
        }
        viewPager.setAdapter(pagerAdapter);
    }

    /**
     * TabLayout 세팅
     */

    private void setupTabLayout() {
        TabLayout TabLayout = (android.support.design.widget.TabLayout) rootView.findViewById(getTabLayoutId());
        TabLayout.setupWithViewPager(viewPager);
        TabLayout.setOnTabSelectedListener(onTabSelectedListener);
    }

    /**
     * TabFragment 에서 사용되는 ViewPagerId 반환
     * @return
     */

    protected abstract int getViewPagerId();

    /**
     * TabFragment 에서 사용되는 TabLayoutId 반환
     * @return
     */

    protected abstract int getTabLayoutId();

    /**
     * TabFragment 에서 사용되는 FragmentList 반환
     * @return
     */

    protected abstract List<BaseFragment> getFragmentList();

    /**
     * TabFragment 에서 사용되는 FragmentTitleList 반환
     * @return
     */

    protected abstract List<String> getFragmentTitleList();


    /**
     * PagerAdapter Inner Class
     */

    class JSPagerAdapter extends FragmentStatePagerAdapter {
        private final List<BaseFragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        protected JSPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public BaseFragment getFragment(int position) {
            return fragmentList.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }

        public void addFragment(BaseFragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        public void clear() {
            fragmentList.clear();
            fragmentTitleList.clear();
        }
    }

}
