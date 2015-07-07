/*
 * Developed by Sandeep Sasidharan
 * 10 June 2014
 * The app scans and lists the wifi access points and fingerprint
 * Option available to store the data in a data base
 * */
package com.example.wififp;

import java.util.List;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	WifiManager mainWifi;
    
    WifiReceiver receiverWifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        /*Button to Clear the text Fields*/
        Button bt_clear = (Button) findViewById(R.id.btclear);
        bt_clear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TextView txt_view = (TextView) findViewById(R.id.address1);
		        txt_view.setText("");
		        TextView no_ids = (TextView) findViewById(R.id.no_id);	        
		        no_ids.setText("Click retrive to view the Stored List");
				
			}
		});
        /*Button to display scan results to the text Fields*/
        Button bt_scan = (Button) findViewById(R.id.btscan);
        bt_scan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
						
				List<ScanResult> results;
		        WifiManager wifi;  
		        int size=0;
		        	        
		        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		        results = wifi.getScanResults();
		        size = results.size();
		        
		        TextView txt_view = (TextView) findViewById(R.id.address1);
		        txt_view.setText("");
		        TextView no_ids = (TextView) findViewById(R.id.no_id);
		        
		        no_ids.setText("No of WiFI Access Points = "+size);

		        
		        for (int i=0; i<size; i++) {
		        	
		            String temp = (String) txt_view.getText();	
		        	String ssid = results.get(i).SSID;
		        	String bssid = results.get(i).BSSID;
		        	int signal_level = results.get(i).level;

		        	String temp1 = (String) txt_view.getText();	
					txt_view.setText(temp1+(i+1)+". BSSID ="+bssid+" SSID ="+ssid+" DB LEVEL ="+signal_level+"\n \n");
		        }
		        				
			}
		});
        
        /*Button to clear database*/
        
        Button bt_drop = (Button) findViewById(R.id.btdrop);
        bt_drop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DatabaseHandler oDatabaseHandler = new DatabaseHandler(getBaseContext());
				oDatabaseHandler.open();
				oDatabaseHandler.clearData();
				oDatabaseHandler.close();
				TextView txt_view = (TextView) findViewById(R.id.address1);
		        txt_view.setText("");
		        TextView no_ids = (TextView) findViewById(R.id.no_id);	        
		        no_ids.setText("Data Base Empty");
				
			}
		});
        
        Button bt_fetch = (Button) findViewById(R.id.btfetch);      
        
        
        bt_fetch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				TextView txt_view = (TextView) findViewById(R.id.address1);
		        txt_view.setText("");
		        
				/*Retrieve data from DB*/
				DatabaseHandler oDatabaseHandler = new DatabaseHandler(getBaseContext());
				oDatabaseHandler.open();
				Cursor C = oDatabaseHandler.returnData();
				int ctr =0;
				if(C.moveToFirst())
				{
					do
					{
						ctr++;
						String uid = C.getString(0);
						String bssid = C.getString(1);
						String ssid = C.getString(2);
						String signal_level = C.getString(3);
						
						String temp = (String) txt_view.getText();	
						txt_view.setText(temp+ctr+" ."+uid+" BSSID ="+bssid+" SSID ="+ssid+" DB LEVEL ="+signal_level+"\n \n");
						
					}while(C.moveToNext());
				}
				
				oDatabaseHandler.close();
				TextView no_ids = (TextView) findViewById(R.id.no_id);
		        
		        no_ids.setText("No of WiFI Access Points = "+ctr);
				
			}
		});
        
        /*Insert Button Listener*/
        Button bt_insert = (Button) findViewById(R.id.btinsert);
        
        bt_insert.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				DatabaseHandler oDatabaseHandler = new DatabaseHandler(getBaseContext());
			
				List<ScanResult> results;
		        WifiManager wifi;  
		        int size=0;
		        	        
		        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		        //wifi.startScan();
		        results = wifi.getScanResults();
		        size = results.size();
		        
		        TextView txt_view = (TextView) findViewById(R.id.address1);
		        txt_view.setText("");
		        TextView no_ids = (TextView) findViewById(R.id.no_id);
		        
		        no_ids.setText("No of WiFI Access Points = "+size);

		        oDatabaseHandler.open();
		        
		        EditText et_uid = (EditText) findViewById(R.id.uid);
		        String uid = et_uid.getText().toString();
		        
		        for (int i=0; i<size; i++) {
		        	
		            String temp = (String) txt_view.getText();	
		        	String ssid = results.get(i).SSID;
		        	String bssid = results.get(i).BSSID;
		        	int signal_level = results.get(i).level;
		        	
		        	long id = oDatabaseHandler.insertData(uid,bssid,ssid,signal_level);
		        	String temp1 = (String) txt_view.getText();	
					txt_view.setText(temp1+(i+1)+". UID ="+uid+" BSSID ="+bssid+" SSID ="+ssid+" DB LEVEL ="+signal_level+"\n \n");
		        }
		        
		        oDatabaseHandler.close();
				
			}
		});

       /*Fresh wifi scanner*/ 
        Button bt_rawscan = (Button) findViewById(R.id.btrawscan);
        
        bt_rawscan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			   mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		       receiverWifi = new WifiReceiver();
		       registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		       mainWifi.startScan();
		       TextView no_ids = (TextView) findViewById(R.id.no_id);
		       no_ids.setText("Scanning in Progress ...");
				
			}
		});
        
        
    }
    class WifiReceiver extends BroadcastReceiver {
        public void onReceive(Context c, Intent intent) {
        	StringBuilder sb = new StringBuilder();
        	List<ScanResult> wifiList;
            wifiList = mainWifi.getScanResults();
	        int size = wifiList.size();
	        
	        TextView txt_view = (TextView) findViewById(R.id.address1);
	        txt_view.setText("");
	        TextView no_ids = (TextView) findViewById(R.id.no_id);
	        
	        no_ids.setText("No of WiFI Access Points = "+size);
	        
	        EditText et_uid = (EditText) findViewById(R.id.uid);

	        
	        for (int i=0; i<size; i++) {
	        	
	            String temp = (String) txt_view.getText();	
	            String uid = et_uid.getText().toString();
	        	String ssid = wifiList.get(i).SSID;
	        	String bssid = wifiList.get(i).BSSID;
	        	int signal_level = wifiList.get(i).level;

	        	String temp1 = (String) txt_view.getText();	
				txt_view.setText(temp1+(i+1)+". UID ="+uid+" BSSID ="+bssid+" SSID ="+ssid+" DB LEVEL ="+signal_level+"\n \n");
	        }
        }
    }
/*    protected void onPause() {
        unregisterReceiver(receiverWifi);
        super.onPause();
    }

    protected void onResume() {
        registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }*/
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
