package mchacks.phonepick;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
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
                }
                else
                {
                    if(peers.getDeviceList().size() > 0)
                    {
                        WifiP2pDevice device = new WifiP2pDevice();
                        for(WifiP2pDevice x : peers.getDeviceList())
                        {
                            Log.w("wifi", x.deviceName);
                            device = x;
                        }

                        WifiP2pConfig config = new WifiP2pConfig();
                        config.deviceAddress = device.deviceAddress;
                        try
                        {
                            mManager.connect(mChannel, config, new WifiP2pManager.ActionListener()
                            {
                                @Override
                                public void onSuccess()
                                {
                                    //success logic
                                    Log.w("wifi", "Connection success");
                                }

                                @Override
                                public void onFailure(int reason)
                                {
                                    //failure logic
                                    Log.w("wifi", "Connection Failure");
                                }
                            });
                        }
                        catch(Exception e)
                        {
                            Log.w("wifi", e);
                        }


                    }
                    else
                    {
                        Log.w("wifi", "No devices found");
                    }

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
