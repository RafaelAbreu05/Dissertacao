package atividadePrincipal.pontosAcesso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import classes.PontoAcesso;
import classes.Sinal;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.MenuItem;
import com.lomza.R;

/**
 * This is the First tab list fragment.
 */
public class PontosAcessoFragment extends SherlockListFragment {
	private static View _fragmentView;
	private static SimpleAdapter adapter;

	private static WifiManager mainWifi;
	private static WifiReceiver receiverWifi;
	private static List<ScanResult> wifiList;
	
	ConnectivityManager myConnManager;
	NetworkInfo myNetworkInfo;
	

	private static TextView titleText;

	private static TreeMap<Integer, PontoAcesso> pontosAcesso = new TreeMap<Integer, PontoAcesso>();
	public static ArrayList<Sinal> networkList = new ArrayList<Sinal>();

	private static ArrayList<HashMap<String, String>> list;

	public static Context baseContext;

	@SuppressWarnings("unchecked")
	public static void setPontosAcesso(TreeMap<Integer, PontoAcesso> pa) {
		pontosAcesso = (TreeMap<Integer, PontoAcesso>) pa.clone();
		refreshList();
	}

	public static ArrayList<Sinal> getNetworkList() {
		refreshList();
		refreshList();
		refreshList();
		refreshList();
		return networkList;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		baseContext = getActivity();
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		_fragmentView = inflater.inflate(R.layout.pontos_acesso, container,
				false);
		if (_fragmentView == null)
			return null;

		titleText = (TextView) _fragmentView.findViewById(R.id.titleText);
		list = new ArrayList<HashMap<String, String>>();

		myConnManager = (ConnectivityManager) inflater
				.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		myNetworkInfo = myConnManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);		

		
		mainWifi = (WifiManager) inflater.getContext().getSystemService(
				Context.WIFI_SERVICE);
		WifiInfo myWifiInfo = mainWifi.getConnectionInfo();
		System.out.println(myWifiInfo.getRssi());
		
		// Ligar o Wifi caso esteja desligado
		if (!mainWifi.isWifiEnabled()) {
			mainWifi.setWifiEnabled(true);
		}

		// Analisar os Pontos de Acesso
		receiverWifi = new WifiReceiver();
		inflater.getContext().registerReceiver(receiverWifi,
				new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		inflater.getContext().registerReceiver(receiverWifi,
				new IntentFilter(WifiManager.RSSI_CHANGED_ACTION));
		mainWifi.startScan();
		titleText.setText("\nAnalisando...\n");

		adapter = new SimpleAdapter(inflater.getContext(), list,
				R.layout.custom_row_view, new String[] { "Icon", "SSID",
						"BSSID", "frequencia", "sinal" }, new int[] {
						R.id.icon, R.id.text1, R.id.text2, R.id.text3,
						R.id.text4 });
		setListAdapter(adapter);

		return _fragmentView;
	}

	public void onListItemClick(ListView l, View v, int position, long id) {

		super.onListItemClick(l, v, position, id);
		Object o = this.getListAdapter().getItem(position);
		String network = o.toString();

		Toast.makeText(getActivity(), "Network: " + " " + network.toString(),
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().equals(getString(R.string.action_refresh))) {
			refreshList();
			return true;
		}

		return false;
	}

	/**
	 * Refreshes the list.
	 */
	private static void refreshList() {
		titleText.setText("Analisando...");
		list.clear();
		adapter.notifyDataSetChanged();
		mainWifi.startScan();
	}

	class WifiReceiver extends BroadcastReceiver {
		public void onReceive(Context c, Intent intent) {
			wifiList = mainWifi.getScanResults();

			networkList.clear();

			for (int i = 0; i < wifiList.size(); i++) {
				PontoAcesso p = new PontoAcesso(
						(wifiList.get(i).BSSID).toString(),
						(wifiList.get(i).SSID).toString());

				for (PontoAcesso aux : pontosAcesso.values()) {
					if (aux.equals(p)) {
						p.setID(aux.getID());
						p.setCoordenadas(aux.getCoordenadas());
					}
				}

				boolean test = false;
				if (!networkList.isEmpty()) {
					Iterator<Sinal> it = (Iterator<Sinal>) networkList
							.iterator();

					while (it.hasNext()) {
						Sinal s = it.next();
						if (s.getPontoAcesso().equals(p))
							test = true;
					}
				}
				if (test == false) {
					Sinal s = new Sinal(p, wifiList.get(i).frequency,
							wifiList.get(i).level);
					networkList.add(s);
				}
			}

			if (networkList.isEmpty()) {
				titleText.setText("Não foi encontrado nenhuma rede!!!");

			} else if (networkList.size() == 1) {
				titleText.setText("Foi encontrado a seguinte rede: ");
			} else {
				titleText.setText("Foram encontrados as seguintes "
						+ networkList.size() + " redes: ");
			}
			populateList();
		}
	}

	public int getImage(int i) {
		int image = 0;
		switch (i) {
		case 1:
			image = R.drawable.w1;
			break;
		case 2:
			image = R.drawable.w2;
			break;
		case 3:
			image = R.drawable.w3;
			break;
		case 4:
			image = R.drawable.w4;
			break;
		case 5:
			image = R.drawable.w5;
			break;
		case 6:
			image = R.drawable.w6;
			break;
		case 7:
			image = R.drawable.w7;
			break;
		case 8:
			image = R.drawable.w8;
			break;
		case 9:
			image = R.drawable.w9;
			break;
		default:
			image = R.drawable.unknown;
		}
		return image;
	}

	private int getPosicaoPontosAcesso(PontoAcesso p) {
		int posicao = 0;
		Iterator<Entry<Integer, PontoAcesso>> iter = pontosAcesso.entrySet()
				.iterator();
		while (iter.hasNext()) {
			Entry<Integer, PontoAcesso> entry = iter.next();
			if (entry.getValue().equals(p)) {
				posicao = entry.getKey();
			}
		}
		return posicao;
	}

	private void populateList() {
		HashMap<String, String> temp;
		;
		TreeMap<PontoAcesso, Sinal> aux = new TreeMap<PontoAcesso, Sinal>();
		list.clear();
		if (!pontosAcesso.isEmpty()) {
			if (!networkList.isEmpty()) {
				for (int i = 0; i < networkList.size(); i++) {
					PontoAcesso p = networkList.get(i).getPontoAcesso();
					if (pontosAcesso.containsValue(p)) {
						aux.put(p, networkList.get(i));
					}
				}
			}

			for (PontoAcesso p : pontosAcesso.values()) {
				temp = new HashMap<String, String>();
				String icon = Integer
						.toString(getImage(getPosicaoPontosAcesso(p)));
				// ITEM
				temp.put("SSID", p.getSSID().toString());
				// SUBITEMS
				temp.put("Icon", icon);
				temp.put("BSSID", "BSSID: " + p.getBSSID());
				if (aux.containsKey(p)) {
					temp.put("frequencia", "Frequência: "
							+ aux.get(p).getFrequencia() + "MHz");
					temp.put("sinal", "Nível do sinal: "
							+ aux.get(p).getNivel() + "dBm");
				} else {
					temp.put("frequencia", "Frequência: Desconhecida");
					temp.put("sinal", "Nível do sinal: Desconhecido");
				}
				list.add(temp);
			}
		}
		if (!networkList.isEmpty()) {
			for (int i = 0; i < networkList.size(); i++) {
				temp = new HashMap<String, String>();
				PontoAcesso p = networkList.get(i).getPontoAcesso();
				if (!pontosAcesso.containsValue(p)) {
					// ITEM
					temp.put("SSID", p.getSSID().toString());
					// SUBITEMS
					temp.put("Icon", Integer.toString(R.drawable.unknown));
					temp.put("BSSID", "BSSID: " + p.getBSSID());
					temp.put("frequencia", "Frequência: "
							+ networkList.get(i).getFrequencia() + "MHz");
					temp.put("sinal", "Nível do sinal: "
							+ networkList.get(i).getNivel() + "dBm");

					list.add(temp);
				}

			}
		}
		adapter.notifyDataSetChanged();
	}
}
