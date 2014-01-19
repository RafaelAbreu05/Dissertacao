package atividadePrincipal;


import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;

/**
 * This is the custom pager tab listener. Tabs are implemented in ActionBar.
 * Every tab's content is a Sherlock fragment.
 */
public class MyTabListener extends FragmentStatePagerAdapter implements ActionBar.TabListener, ViewPager.OnPageChangeListener {
    private final Context _context;
    private final ActionBar _actionBar;
    private final ViewPager _viewPager;
    private final ArrayList<TabInfo> _tabs = new ArrayList<TabInfo>();
    final ArrayList<ITabChangedListener> _tabChangedListeners = new ArrayList<ITabChangedListener>();

    static final class TabInfo {
        private final Class<?> _class;
        private final Bundle _args;

        TabInfo(Class<?> clss, Bundle args) {
            _class = clss;
            _args = args;
        }
    }

    public MyTabListener(SherlockFragmentActivity activity, ViewPager pager) {
        super(activity.getSupportFragmentManager());
        _context = activity;
        _actionBar = activity.getSupportActionBar();
        _viewPager = pager;
        _viewPager.setAdapter(this);
       _viewPager.setOnPageChangeListener(this);
    }

    /**
     * Adds tabs to the ActionBar.
     *
     * @param tab  Tab which will added
     * @param clss Class which is connected with the tab
     * @param args Extra tab arguments
     */
    public void addTab(ActionBar.Tab tab, Class<?> clss, Bundle args) {
        TabInfo info = new TabInfo(clss, args);
        tab.setTag(info);
        tab.setTabListener(this);
        _tabs.add(info);
        _actionBar.addTab(tab);
        notifyDataSetChanged();
    }

    public void addTabChangedListener(ITabChangedListener listener) {
        _tabChangedListeners.add(listener);
    }

    @Override
    public int getCount() {
        return _tabs.size();
    }

    @Override
    public Fragment getItem(int position) {
        TabInfo info = _tabs.get(position);
        return Fragment.instantiate(_context, info._class.getName(), info._args);
    }

    private void notifyTabChangedListeners(int tabIndex, Tab tab, View tabView) {
        for (ITabChangedListener listener : _tabChangedListeners) {
            listener.onTabChanged(tabIndex, tab, tabView);
        }
    }
    
    
    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        _actionBar.setSelectedNavigationItem(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
    

    
    
    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        Object tag = tab.getTag();
        for (int i = 0; i < _tabs.size(); i++) {
            if (_tabs.get(i) == tag) {
                _viewPager.setCurrentItem(i);
                notifyTabChangedListeners(i, tab, tab.getCustomView());
            }
        }
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }
}
