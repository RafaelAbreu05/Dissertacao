package atividadePrincipal;

import android.view.View;

import com.actionbarsherlock.app.ActionBar;

/**
 * Custom Action Bar Tab Changed Listener.
 */
public interface ITabChangedListener {
    /**
     * This method is called when the user has changed page of the tab view.
     * @param pageIndex Index of the current page.
     * @param tab Instance of the selected tab control.
     * @param tabView Instance of the tab view.
     */
    void onTabChanged(int pageIndex, ActionBar.Tab tab, View tabView);
}