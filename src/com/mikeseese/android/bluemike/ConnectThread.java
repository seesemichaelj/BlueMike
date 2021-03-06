package com.mikeseese.android.bluemike;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Message;
import android.widget.Toast;

public class ConnectThread extends Thread
{
	private final Network mNetwork;
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    private final BluetoothAdapter mBluetoothAdapter;
 
    public ConnectThread(Network n, BluetoothDevice device)
    {
    	mNetwork = n;
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothSocket tmp = null;
        mmDevice = device;
 
        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try
        {
            // MY_UUID is the app's UUID string, also used by the server code
            tmp = device.createRfcommSocketToServiceRecord(mNetwork.mUUID);
        }
        catch (IOException e){ }
        mmSocket = tmp;
    }
 
    public void run()
    {
        // Cancel discovery because it will slow down the connection
        mBluetoothAdapter.cancelDiscovery();
 
        try
        {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            mmSocket.connect();
        }
        catch (IOException connectException)
        {
            // Unable to connect; close the socket and get out
            try
            {
                mmSocket.close();
            }
            catch (IOException closeException) { }
            return;
        }
 
        mNetwork.addDevice(mmSocket);
		/*Message msg = mNetwork.mHandler.obtainMessage();
		msg.what = BMConstants.NETWORK_JOINED;
		msg.sendToTarget();*/
    }
 
    /** Will cancel an in-progress connection, and close the socket */
    public void cancel()
    {
        try
        {
            mmSocket.close();
        }
        catch (IOException e) { }
    }
}