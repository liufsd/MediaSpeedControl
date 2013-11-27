// This file was written by me, Bill Cox in 2011.
// I place this file into the public domain.  Feel free to copy from it.
// Note, however that libsonic, which this application links to,
// is licensed under LGPL.  You can link to it in your commercial application,
// but any changes you make to sonic.c or sonic.h need to be shared under
// the LGPL license.

package org.vinuxproject.sonic;

import com.falconware.prestissimo.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.falconware.prestissimo.*;
import com.aocate.presto.service.*;

public class SonicTest extends Activity
{
	private IPlayMedia_0_8.Stub binder;
	private boolean mComplete;
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);                      
        setContentView(R.layout.main);
        try {
        	Intent intent = new Intent(this,SoundService.class);
            bindService(intent, conn, Context.BIND_AUTO_CREATE);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
        
    }
    
    public void play(View view)
    {
    	final EditText speedEdit = (EditText) findViewById(R.id.speed);
    	final EditText pitchEdit = (EditText) findViewById(R.id.pitch);
    	final EditText rateEdit = (EditText) findViewById(R.id.rate);
    	float speed = Float.parseFloat(speedEdit.getText().toString());
    	float pitch = Float.parseFloat(pitchEdit.getText().toString());
    	float rate = Float.parseFloat(rateEdit.getText().toString());
    	try {
    		if (binder!=null&&!mComplete) {
        		try {
    				if (!binder.isPlaying(0)) {
    					binder.start(0);
    					binder.setPlaybackSpeed(0, 1.0f);
    				}else{
    					Log.e("TAG", "binder.getCurrentSpeedMultiplier(0): "+binder.getCurrentSpeedMultiplier(0));
    					try {
    						binder.setPlaybackSpeed(0, speed);
    					} catch (RemoteException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					}
    				}
    				if (binder.getCurrentSpeedMultiplier(0)==1.5) {
    					Log.e("TAG", "1.5 speed");
    					binder.pause(0);
    				}
    			} catch (RemoteException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    			}
    			
    		}else {
				binder.setDataSourceString(0, "/sdcard/eudb_fr/.media/.articles_mp3/79302fbb-b01c-40bc-bd42-827c284551ad.mp3");
				binder.prepare(0);
				binder.start(0);
                mComplete = false;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
    }
    
  private ServiceConnection conn = new ServiceConnection() {
        
		@SuppressLint("SdCardPath")
		@Override
		public void onServiceConnected(ComponentName arg0, final IBinder arg1) {
			// TODO Auto-generated method stub
			 binder = (IPlayMedia_0_8.Stub)arg1;
				try {
					binder.startSession(new IDeathCallback_0_8() {
						
						@Override
						public IBinder asBinder() {
							// TODO Auto-generated method stub
							
							return (IPlayMedia_0_8.Stub)arg1;
						}
					});
					binder.registerOnPreparedCallback(0, new IOnPreparedListenerCallback_0_8() {
						
						@Override
						public IBinder asBinder() {
							// TODO Auto-generated method stub
							return (IPlayMedia_0_8.Stub)arg1;
						}
						
						@Override
						public void onPrepared() throws RemoteException {
							// TODO Auto-generated method stub
							Log.e("TAG", "registerOnPreparedCallback");
						}
					});
					binder.registerOnCompletionCallback(0, new IOnCompletionListenerCallback_0_8() {
						
						@Override
						public IBinder asBinder() {
							// TODO Auto-generated method stub
							return (IPlayMedia_0_8.Stub)arg1;
						}
						
						@Override
						public void onCompletion() throws RemoteException {
							// TODO Auto-generated method stub
							Log.e("TAG", "registerOnCompletionCallback");
							binder.reset(0);
							mComplete = true;
						}
					});
					binder.registerOnPitchAdjustmentAvailableChangedCallback(0, new IOnPitchAdjustmentAvailableChangedListenerCallback_0_8() {
						
						@Override
						public IBinder asBinder() {
							// TODO Auto-generated method stub
							return (IPlayMedia_0_8.Stub)arg1;
						}
						
						@Override
						public void onPitchAdjustmentAvailableChanged(boolean arg0)
								throws RemoteException {
							// TODO Auto-generated method stub
							Log.e("TAG", "registerOnPitchAdjustmentAvailableChangedCallback");
						}
					});
					binder.registerOnBufferingUpdateCallback(0, new IOnBufferingUpdateListenerCallback_0_8() {
						
						@Override
						public IBinder asBinder() {
							// TODO Auto-generated method stub
							return (IPlayMedia_0_8.Stub)arg1;
						}
						
						@Override
						public void onBufferingUpdate(int arg0) throws RemoteException {
							// TODO Auto-generated method stub
							Log.e("TAG", "registerOnBufferingUpdateCallback : "+arg0);	
						}
					});
					binder.registerOnSpeedAdjustmentAvailableChangedCallback(0, new IOnSpeedAdjustmentAvailableChangedListenerCallback_0_8() {
						
						@Override
						public IBinder asBinder() {
							// TODO Auto-generated method stub
							return (IPlayMedia_0_8.Stub)arg1;
						}
						
						@Override
						public void onSpeedAdjustmentAvailableChanged(boolean arg0)
								throws RemoteException {
							// TODO Auto-generated method stub
							Log.e("TAG", "registerOnSpeedAdjustmentAvailableChangedCallback : "+arg0);
						}
					});
					binder.registerOnErrorCallback(0, new IOnErrorListenerCallback_0_8() {
						
						@Override
						public IBinder asBinder() {
							// TODO Auto-generated method stub
							return (IPlayMedia_0_8.Stub)arg1;
						}
						
						@Override
						public boolean onError(int arg0, int arg1) throws RemoteException {
							// TODO Auto-generated method stub
							Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
							return false;
						}
					});
					binder.setDataSourceString(0, "/sdcard/eudb_fr/.media/.articles_mp3/79302fbb-b01c-40bc-bd42-827c284551ad.mp3");
					binder.prepare(0);
					binder.start(0);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	           
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			// TODO Auto-generated method stub
			
		}
    };
    
}
