package com.closedcircle;

import android.util.Log;
import android.os.Build;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

// Google Nearby Imports
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.Strategy;

public class GoogleNearbyConnectionsModule extends ReactContextBaseJavaModule {

    private static final String TAG = "GoogleNearbyConnectionsModule";
    private static final Strategy strategy = Strategy.P2P_CLUSTER;
    private static final String SERVICE_ID = "com.closedcircle.google.nearby";
    private static final String LOCAL_ENDPOINT_NAME = Build.DEVICE;
    private final ConnectionLifecycleCallback connectionLifecycleCallback;
    private final EndpointDiscoveryCallback endpointDiscoveryCallback;
    private ConnectionsClient connectionsClient;

    public GoogleNearbyConnectionsModule(ReactApplicationContext reactContext) {

        this.connectionsClient = Nearby.getConnectionsClient(reactContext);

        this.connectionLifecycleCallback = new ConnectionLifecycleCallback() {
            @Override
            public void onConnectionInitiated(String endpointId, ConnectionInfo connectionInfo) {
                Log.d(TAG,
                        String.format(
                                "onConnectionInitiated(endpointId=%s, endpointName=%s)",
                                endpointId, connectionInfo.getEndpointName()));
            }

            @Override
            public void onConnectionResult(String endpointId, ConnectionResolution result) {
                Log.d(TAG,
                        String.format(
                                "onConnectionInitiated(endpointId=%s, result=%s)",
                                endpointId, result));
            }

            @Override
            public void onDisconnected(String endpointId) {
                Log.w(TAG, "Unexpected disconnection from endpoint " + endpointId);
            }
        };

        this.endpointDiscoveryCallback = new EndpointDiscoveryCallback() {
            @Override
            public void onEndpointFound(String endpointId, DiscoveredEndpointInfo info) {
                Log.i(TAG, "onEndpointFound: endpointId =" + endpointId);
                Log.i(TAG, "onEndpointFound: info.getEndpointName() =" + info.getEndpointName());
                // Save the endpoint data into the array
            }

            @Override
            public void onEndpointLost(String endpointId) {
                Log.i(TAG, "onEndpointLost: endpointId =" + endpointId);
                // Remove the endpoint data from the array
            }
        };
    }

    @Override
    public String getName() {
        return "GoogleNearbyConnectionsModule";
    }

    @ReactMethod
    private void startDiscovery() {
        DiscoveryOptions discoveryOptions = new DiscoveryOptions.Builder().setStrategy(strategy).build();
        connectionsClient.startDiscovery(SERVICE_ID, endpointDiscoveryCallback, discoveryOptions)
                .addOnSuccessListener(
                        (Void unused) -> {
                            // We're discovering!
                            Log.d(TAG, "Discovering");
                        })
                .addOnFailureListener(
                        (Exception e) -> {
                            // We're unable to start discovering.
                            Log.d(TAG, "Unable to discover", e);
                        });
    }

    @ReactMethod
    public void startAdvertising() {
        AdvertisingOptions advertisingOptions = new AdvertisingOptions.Builder().setStrategy(strategy).build();
        connectionsClient.startAdvertising(
                "Test_Device", SERVICE_ID, connectionLifecycleCallback, advertisingOptions)
                .addOnSuccessListener(
                        (Void unused) -> {
                            // We're advertising!
                            Log.d(TAG, "Advertising");
                        })
                .addOnFailureListener(
                        (Exception e) -> {
                            // We were unable to start advertising.
                            Log.d(TAG, "Unable to Advertise", e);
                        });
    }

    @ReactMethod
    public void stopDiscovery() {
        connectionsClient.stopDiscovery();
    }

    @ReactMethod
    public void stopAdvertising() {
        connectionsClient.stopAdvertising();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopAdvertising();
        stopDiscovery();
        connectionsClient.stopAllEndpoints();
    }

}