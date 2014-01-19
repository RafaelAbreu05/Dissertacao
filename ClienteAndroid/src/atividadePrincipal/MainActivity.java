package atividadePrincipal;

import java.io.IOException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import atividadePrincipal.planta.PlantaFragment;
import atividadePrincipal.pontosAcesso.PontosAcessoFragment;
import clienteTCP.ClienteTCP;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.lomza.R;

/**
 * This is the main fragment activity with action bar tabs and view pager.
 */
public class MainActivity extends SherlockFragmentActivity implements
		ITabChangedListener {
	private ActionBar _actionBar;
	private ViewPager _viewPager;

	private static Menu _menuInstance;
	public static ClienteTCP mTcpClient = null;

	MainActivity THIS = null;

	/**
	 * Adds three tabs to the Action Bar (Ongoing, Previous and Best Rated tab).
	 */
	private void addTabs() {
		MyTabListener myTabListener = new MyTabListener(this, _viewPager);
		myTabListener.addTabChangedListener(this);

		ActionBar.Tab firstTab = _actionBar.newTab();
		firstTab.setText("Pontos de Acesso");
		myTabListener.addTab(firstTab, PontosAcessoFragment.class, null);
		firstTab.setTabListener(myTabListener);

		ActionBar.Tab secondTab = _actionBar.newTab();
		secondTab.setText("Planta");
		myTabListener.addTab(secondTab, PlantaFragment.class, null);
		secondTab.setTabListener(myTabListener);

	}

	/**
	 * Hides all action bar menu items.
	 * 
	 * @param menu
	 *            Action bar menu instance
	 */
	private void hideAllActionItems(Menu menu) {
		if (menu != null) {
			for (int i = 0; i < menu.size(); i++)
				menu.getItem(i).setVisible(false);
		}
	}

	/**
	 * Initializes the action bar and sets it's navigation mode.
	 */
	private void initActionBar() {
		_actionBar = getSupportActionBar();
		_actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	}

	/**
	 * Initializes the view pager and sets it as a content view.
	 */
	private void initViewPager() {
		_viewPager = new ViewPager(this);
		_viewPager.setId(R.id.pager);
		setContentView(_viewPager);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		THIS = this;
		initActionBar();
		initViewPager();
		addTabs();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		_menuInstance = menu;

		menu.add(getString(R.string.action_refresh))
				.setIcon(R.drawable.action_refresh).setVisible(true)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		menu.add(getString(R.string.action_connect))
				.setIcon(R.drawable.action_settings).setVisible(true)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		menu.add(getString(R.string.action_disconect))
				.setIcon(R.drawable.action_settings).setVisible(true)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		menu.getItem(2).setEnabled(false);
		return true;
	}

	public boolean onPrepareOptionsMenu(Menu menu) {
		if (mTcpClient != null) {
			menu.getItem(2).setEnabled(true);
			menu.getItem(1).setEnabled(false);
		} else {
			menu.getItem(2).setEnabled(false);
			menu.getItem(1).setEnabled(true);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().equals(getString(R.string.action_refresh))) {
			atualizar();
		}
		if (mTcpClient == null && _menuInstance != null) {
			_menuInstance.getItem(2).setEnabled(false);
			_menuInstance.getItem(1).setEnabled(true);
		}
		if (item.getTitle().equals(getString(R.string.action_connect))) {
			// Conectar com o servidor
			new ConnectTask().execute("");
			return true;
		} else if (item.getTitle().equals(getString(R.string.action_disconect))) {
			// Disconectar com o servidor
			try {
				mTcpClient.stopClient();
			} catch (IOException e) {
				e.printStackTrace();
			}
			mTcpClient = null;
			return true;
		} else
			return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabChanged(int pageIndex, ActionBar.Tab tab, View tabView) {
		resetVisibilityFields();
		if (_menuInstance != null) {
			hideAllActionItems(_menuInstance);

			switch (pageIndex) {
			case 0:
				showFirstTabActionItems(_menuInstance);
				atualizar();
				break;

			case 1:
				showSecondTabActionItems(_menuInstance);
				atualizar();
			}
		}
	}

	public class ConnectTask extends AsyncTask<String, String, ClienteTCP> {

		@Override
		protected ClienteTCP doInBackground(String... message) {

			// we create a TCPClient object and
			mTcpClient = new ClienteTCP(new ClienteTCP.OnMessageReceived() {
				// here the messageReceived method is implemented
				public void messageReceived(String message) {
					// this method calls the onProgressUpdate
					if (message.contains("SENDING_FILE")
							|| message.contains("PosicaoPontoAcesso")) {
						runOnUiThread(new Runnable() {
							public void run() {
								atualizar();
							}
						});
					} else if (message.contains("PontosAcesso")) {
						runOnUiThread(new Runnable() {
							public void run() {
								atualizar();
							}
						});
					} else if (message.contains("Sinais")) {
						runOnUiThread(new Runnable() {
							public void run() {
								mTcpClient.setSinais(PontosAcessoFragment
										.getNetworkList());
							}
						});
					} else {
						publishProgress(message);
					}
				}
			});
			mTcpClient.run();
			return null;
		}

		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);
		}
	}

	/**
	 * Sets all tabs action item visibility fields to false.
	 */
	private void resetVisibilityFields() {
	}

	/**
	 * Shows First tab action items.
	 * 
	 * @param menu
	 *            Action bar menu instance
	 */
	private void showFirstTabActionItems(Menu menu) {
		if (menu != null && menu.size() == 2) {
			menu.getItem(0).setVisible(true);
			atualizar();
		}
	}

	/**
	 * Shows Second tab action items.
	 * 
	 * @param menu
	 *            Action bar menu instance
	 */
	private void showSecondTabActionItems(Menu menu) {
		if (menu != null && menu.size() == 2) {
			menu.getItem(0).setVisible(true);
			atualizar();
		}
	}

	public void atualizar() {
		atualizarPlanta();
		atualizarPontosAcesso();
	}

	public void atualizarPlanta() {
		if (mTcpClient != null) {
			if (mTcpClient != null && mTcpClient.getPontosAcesso() != null) {
				PlantaFragment.setPontosAcesso(mTcpClient.getPontosAcesso());
			}
			if (mTcpClient.getSuccessFile() == true) {
				PlantaFragment.setNomeFicheiro(mTcpClient.getNomeFicheiro());
			}
		}
	}

	public void atualizarPontosAcesso() {
		if (mTcpClient != null && mTcpClient.getPontosAcesso() != null) {
			PontosAcessoFragment.setPontosAcesso(mTcpClient.getPontosAcesso());
			PlantaFragment.setPontosAcesso(mTcpClient.getPontosAcesso());
		}
	}
}