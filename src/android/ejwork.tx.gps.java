package com.plugin.ejworktxgps;

import android.app.Activity;

import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.LOG;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * This class echoes a string called from JavaScript.
 */
public class ejwork.tx.gps extends CordovaPlugin implements TencentLocationListener {

    private volatile TencentLocationManager mLocationManager;
    private CallbackContext callbackContext = null;
    private final static String TAG = "LocationUtil";
    private boolean isStartLocation=false;
    private boolean not_keep_location=true;
    private  Activity context;

    private void setLocationManager(){
        if(null==mLocationManager){
            mLocationManager=TencentLocationManager.getInstance(context);
            // 设置坐标系为 gcj-02, 缺省坐标为 gcj-02, 所以通常不必进行如下调用
            mLocationManager.setCoordinateType(TencentLocationManager.COORDINATE_TYPE_GCJ02);
        }
    }
    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView){
        LOG.e(TAG,"onLocationChanged=====");
        try{
            context=this.cordova.getActivity();
            context.runOnUiThread(new Runnable(){
                @Override
                public void run(){
                    setLocationManager();
                }
            });
            super.initialize(cordova,webView);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean execute(String action, JSONArray args,
            CallbackContext callbackContext) throws JSONException{
        this.callbackContext=callbackContext;
        if(action.equals("getLocation")){
            not_keep_location=args.getBoolean(0);
            startLocation();
            PluginResult result=new PluginResult(
            PluginResult.Status.NO_RESULT);
            result.setKeepCallback(true);
            callbackContext.sendPluginResult(result);
            return true;
        }else if(action.equals("stopLocation")){
            stopLocation();
        }

        return false;
    }

    @Override
    public void onLocationChanged(TencentLocation location, int error,
            String reason) {
            if (error == TencentLocation.ERROR_OK) {
                // mLocation = location;
                // 定位成功
                // StringBuilder sb = new StringBuilder();
                // sb.append("(纬度=").append(location.getLatitude()).append(",经度=")
                // .append(location.getLongitude()).append(",精度=")
                // .append(location.getAccuracy()).append("), 来源=")
                // .append(location.getProvider()).append(", 地址=")
                // .append(location.getAddress());
                //LOG.e(TAG,"============"+location.getLatitude());
                JSONArray jo = new JSONArray();
                try {
                    jo.put(location.getLatitude());
                    jo.put(location.getLongitude());
                } catch (JSONException e) {
                    jo = null;
                    e.printStackTrace();
                }
                if (jo != null&&callbackContext!=null) {
                    PluginResult result = new PluginResult(
                    PluginResult.Status.OK,jo);
                    result.setKeepCallback(true);
                    callbackContext.sendPluginResult(result);
                    if(not_keep_location){
                        startLocation();
                    }
        //				callbackContext.success(jo);
                }
            }else{
                if(null!=callbackContext){
                    callbackContext.error(reason);
                }
            }

    }

    @Override
    public void onStatusUpdate(String arg0, int arg1, String arg2) {

    }

    //
    // @Override
    public void onResume(boolean multitasking) {
            super.onResume(multitasking);
            startLocation();
    }

    @Override
    public void onPause(boolean multitasking) {
            super.onPause(multitasking);
            stopLocation();
    }

    private void startLocation() {
            if(!isStartLocation){
                isStartLocation=true;
                TencentLocationRequest request = TencentLocationRequest.create();
                request.setInterval(5000);
                setLocationManager();
                mLocationManager.requestLocationUpdates(request, this);
            }

    }

    private void stopLocation() {
            if(isStartLocation) {
                isStartLocation=false;
                callbackContext=null;
                if(null!=mLocationManager){
                    mLocationManager.removeUpdates(this);
                }
            }
    }
}
