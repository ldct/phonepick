package mchacks.phonepick;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

public class WiFiDirectBroadcastReceiver extends BroadcastReceiver
{

    WifiP2pManager.PeerListListener myPeerListListener;

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private MainActivity mActivity;

    public WiFiDirectBroadcastReceiver(WifiP2pManager wifiManager, WifiP2pManager.Channel wifiChannel, MainActivity mainActivity)
    {
        this.mManager = wifiManager;
        this.mChannel = wifiChannel;
        this.mActivity = mainActivity;

        this.myPeerListListener = new WifiP2pManager.PeerListListener() {
            @Override
            public void onPeersAvailable(WifiP2pDeviceList peers) {
                Log.w("wifi", "peers available");
                if (peers.equals(null)) {
                    Log.w("wifi", "peers list is null :(");
                } else {
                    Log.w("wifi", "peers list is not null");
                    Log.w("wifi", peers.toString());
                }
            }
        };
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        String action = intent.getAction();
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                Log.w("wifi", "p2p enabled");
                if (mManager != null) {
                    mManager.requestPeers(mChannel, myPeerListListener);
                }
            } else {
                Log.w("wifi", "p2p not enabled");
            }
        }
    }
}
