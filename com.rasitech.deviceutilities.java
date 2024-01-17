package com.rasitech.deviceutilities;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.LocaleList;
import android.os.PowerManager;
import android.provider.Settings;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.TelephonyManager;
import android.view.WindowManager;

import android.content.pm.ApplicationInfo;  // Add this import
import android.content.pm.PackageManager;  // Add this import
import android.Manifest;
import android.app.Activity;

import android.os.StatFs;
import android.os.Environment;
import android.media.AudioManager;
import android.bluetooth.BluetoothAdapter;
import android.net.Network;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.service.dreams.DreamService;
import android.nfc.NfcAdapter;
import android.content.ActivityNotFoundException;


import android.app.ActivityManager;
import android.content.res.Configuration;
import android.os.SystemClock;



import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat; // Add this import

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.runtime.util.YailList;

import java.util.List;
import java.util.ArrayList;
import java.util.Locale;


@DesignerComponent(
        version = 1,
        description = "Device Utilities Extension By RasiTech",
        category = ComponentCategory.EXTENSION,
        nonVisible = true,
        iconName = "https://hosting.rasitechchannel.my.id/sc/file_113.png")
@SimpleObject(external = true)
@UsesPermissions(permissionNames = "android.permission.ACCESS_NETWORK_STATE, android.permission.READ_SMS, android.permission.READ_PHONE_STATE, android.permission.NFC, android.permission.CALL_PHONE, android.permission.READ_EXTERNAL_STORAGE, android.permission.ACCESS_COARSE_LOCATION, android.permission.ACCESS_FINE_LOCATION")

public class DeviceUtilities extends AndroidNonvisibleComponent {

    // Context
    private Context context;

    public DeviceUtilities(ComponentContainer container) {
        super(container.$form());
        this.context = container.$context();
    }

public class Constants {
    public static final int READ_PHONE_STATE_PERMISSION_REQUEST_CODE = 1;
}


    @SimpleFunction(description = "Get Language")
    public String GetLanguage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList locales = context.getResources().getConfiguration().getLocales();
            if (locales.size() > 0) {
                return locales.get(0).getLanguage();
            }
        } else {
            Locale locale = context.getResources().getConfiguration().locale;
            return locale.getLanguage();
        }
        return "";
    }

    @SimpleFunction(description = "Get Country")
    public String GetCountry() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList locales = context.getResources().getConfiguration().getLocales();
            if (locales.size() > 0) {
                return locales.get(0).getCountry();
            }
        } else {
            Locale locale = context.getResources().getConfiguration().locale;
            return locale.getCountry();
        }
        return "";
    }

    @SimpleFunction(description = "Check If Device is in Power Save Mode")
    public boolean IsPowerSaveMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            return powerManager != null && powerManager.isPowerSaveMode();
        }
        return false;
    }

@SimpleFunction(description = "Get Cell Tower Information")
public YailList GetCellTowerInfo() {
    TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    if (telephonyManager != null) {
        List<CellInfo> cellInfoList = telephonyManager.getAllCellInfo();

        YailList cellInfoData = new YailList();

        if (cellInfoList != null) {
            for (CellInfo cellInfo : cellInfoList) {
                String type = "";
                String info = "";

                if (cellInfo instanceof CellInfoGsm) {
                    type = "GSM";
                    info = "CID: " + ((CellInfoGsm) cellInfo).getCellIdentity().getCid();
                } else if (cellInfo instanceof CellInfoLte) {
                    type = "LTE";
                    info = "PCI: " + ((CellInfoLte) cellInfo).getCellIdentity().getPci();
                } else if (cellInfo instanceof CellInfoWcdma) {
                    type = "WCDMA";
                    info = "CID: " + ((CellInfoWcdma) cellInfo).getCellIdentity().getCid();
                }

                if (!type.isEmpty() && !info.isEmpty()) {
                    cellInfoData.add(info);  // Use add instead of addPos
                }
            }
        }

        return cellInfoData;
    }
    return new YailList();
}


@SimpleFunction(description = "Get Location Information")
public YailList GetLocationInfo() {
    LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    if (locationManager != null) {
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (location != null) {
                YailList locationInfo = new YailList();
                locationInfo.add("Latitude: " + location.getLatitude());
                locationInfo.add("Longitude: " + location.getLongitude());
                locationInfo.add("Altitude: " + location.getAltitude());
                locationInfo.add("Accuracy: " + location.getAccuracy());
                return locationInfo;
            }
        }
    }
    return new YailList();
}



    @SimpleFunction(description = "Get Device Manufacturer")
    public String GetDeviceManufacturer() {
        return Build.MANUFACTURER;
    }

    @SimpleFunction(description = "Get Device Model")
    public String GetDeviceModel() {
        return Build.MODEL;
    }

    @SimpleFunction(description = "Get Device Product")
    public String GetDeviceProduct() {
        return Build.PRODUCT;
    }

    @SimpleFunction(description = "Get Device Hardware")
    public String GetDeviceHardware() {
        return Build.HARDWARE;
    }

    @SimpleFunction(description = "Get Device Android Version")
    public String GetAndroidVersion() {
        return Build.VERSION.RELEASE;
    }

    @SimpleFunction(description = "Get Device API Level")
    public int GetApiLevel() {
        return Build.VERSION.SDK_INT;
    }

    @SimpleFunction(description = "Get Device ID (Secure Settings)")
    public String GetDeviceId() {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    @SimpleFunction(description = "Get SIM Card Operator Name")
    public String GetSimOperator() {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            return telephonyManager.getSimOperatorName();
        }
        return "";
    }
@SimpleFunction(description = "Get SIM Serial Number")
public String GetSerialNumber() {
    TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    if (telephonyManager != null) {
        return telephonyManager.getDeviceId();
    }
    return "";
}


@SimpleFunction(description = "Get Phone Number")
public String GetPhoneNumber() {
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            return telephonyManager.getLine1Number();
        }
    } else {
        // Request permission
ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_PHONE_STATE}, Constants.READ_PHONE_STATE_PERMISSION_REQUEST_CODE);
        // You can handle the permission result in onRequestPermissionsResult method
    }
    return "";
}



    @SimpleFunction(description = "Get Screen Width (Pixels)")
    public int GetScreenWidth() {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm != null) {
            return wm.getDefaultDisplay().getWidth();
        }
        return 0;
    }

    @SimpleFunction(description = "Get Screen Height (Pixels)")
    public int GetScreenHeight() {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm != null) {
            return wm.getDefaultDisplay().getHeight();
        }
        return 0;
    }

    @SimpleFunction(description = "Check If Device is Tablet")
    public boolean IsTablet() {
        return (context.getResources().getConfiguration().screenLayout & android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK)
                >= android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    @SimpleFunction(description = "Check If Device Has Telephony Features")
    public boolean HasTelephony() {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager != null && telephonyManager.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE;
    }

@SimpleFunction(description = "Get Installed Apps")
public YailList GetInstalledApps() {
    PackageManager packageManager = context.getPackageManager();
    List<ApplicationInfo> apps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

    YailList appList = new YailList();

    for (ApplicationInfo app : apps) {
        appList.add(app.packageName);  // Use add instead of addPos
    }

    return appList;
}


        @SimpleFunction(description = "Check If Device is Connected to a Network")
    public boolean IsConnectedToNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected();
        }
        return false;
    }

    @SimpleFunction(description = "Get Network Type")
    public String GetNetworkType() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            if (activeNetwork != null) {
                return activeNetwork.getTypeName();
            }
        }
        return "";
    }

    @SimpleFunction(description = "Check If Device is Connected to WiFi")
    public boolean IsConnectedToWiFi() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
        }
        return false;
    }

    @SimpleFunction(description = "Get WiFi SSID")
    public String GetWiFiSSID() {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo != null) {
                return wifiInfo.getSSID();
            }
        }
        return "";
    }

    @SimpleFunction(description = "Dial a Phone Number")
    public void DialPhoneNumber(String phoneNumber) {
        Intent dialIntent = new Intent(Intent.ACTION_CALL);
        dialIntent.setData(Uri.parse("tel:" + phoneNumber));
        context.startActivity(dialIntent);
    }


@SimpleFunction(description = "Get Battery Level")
public int GetBatteryLevel() {
    IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
    Intent batteryStatus = context.registerReceiver(null, ifilter);

    int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
    int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

    float batteryPct = level / (float) scale;
    return (int) (batteryPct * 100);
}

@SimpleFunction(description = "Check If Location Services are Enabled")
public boolean IsLocationEnabled() {
    LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    return locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
}

@SimpleFunction(description = "Open Location Settings")
public void OpenLocationSettings() {
    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
    context.startActivity(intent);
}

@SimpleFunction(description = "Check If Airplane Mode is On")
public boolean IsAirplaneModeOn() {
    return Settings.System.getInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
}

@SimpleFunction(description = "Open Airplane Mode Settings")
public void OpenAirplaneModeSettings() {
    Intent intent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
    context.startActivity(intent);
}

@SimpleFunction(description = "Check If Battery Saver Mode is On")
public boolean IsBatterySaverModeOn() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        return powerManager != null && powerManager.isPowerSaveMode();
    }
    return false;
}

@SimpleFunction(description = "Open Battery Saver Settings")
public void OpenBatterySaverSettings() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        Intent intent = new Intent(Settings.ACTION_BATTERY_SAVER_SETTINGS);
        context.startActivity(intent);
    }
}


@SimpleFunction(description = "Get Total RAM Size (in bytes)")
public long GetTotalRAM() {
    ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    if (activityManager != null) {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo.totalMem;
    }
    return 0;
}

@SimpleFunction(description = "Check If App is Installed")
public boolean IsAppInstalled(String packageName) {
    PackageManager packageManager = context.getPackageManager();
    try {
        packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
        return true;
    } catch (PackageManager.NameNotFoundException e) {
        return false;
    }
}

@SimpleFunction(description = "Open App in Google Play Store")
public void OpenAppInPlayStore(String packageName) {
    try {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    } catch (ActivityNotFoundException e) {
        // If Play Store app is not available, open the app page in a browser
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}

@SimpleFunction(description = "Check If Battery is Charging")
public boolean IsBatteryCharging() {
    Intent batteryIntent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    if (batteryIntent != null) {
        int status = batteryIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        return status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL;
    }
    return false;
}

@SimpleFunction(description = "Get Screen Brightness Level")
public int GetScreenBrightness() {
    try {
        int brightnessMode = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
        if (brightnessMode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
            return -1; // Return -1 for automatic brightness
        } else {
            return Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        }
    } catch (Settings.SettingNotFoundException e) {
        e.printStackTrace();
    }
    return -1;
}

@SimpleFunction(description = "Set Screen Brightness Level")
public void SetScreenBrightness(int brightnessLevel) {
    try {
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, brightnessLevel);
    } catch (Exception e) {
        e.printStackTrace();
    }
}

@SimpleFunction(description = "Get Current System Language")
public String GetCurrentSystemLanguage() {
    return Locale.getDefault().getLanguage();
}

// @SimpleFunction(description = "Get Available Sensors")
// public YailList GetAvailableSensors() {
//     SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
// List<Sensor> sensorList = sensorManager != null ? sensorManager.getSensorList(Sensor.TYPE_ALL) : new ArrayList<>();

//     YailList sensorNames = new YailList();
//     for (Sensor sensor : sensorList) {
//         sensorNames.add(sensor.getName());
//     }
//     return sensorNames;
// }

// @SimpleFunction(description = "Check If Device is in Daydream Mode")
// public boolean IsDaydreamModeEnabled() {
//     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
//         DreamService dreamService = (DreamService) context.getSystemService(Context.DREAM_SERVICE);

//         return dreamService != null && dreamService.isDreaming();
//     }
//     return false;
// }

@SimpleFunction(description = "Check If NFC is Available on Device")
public boolean IsNfcAvailable() {
    try {
        // Ensure the necessary permissions are declared in the AndroidManifest.xml file
        // <uses-permission android:name="android.permission.NFC" />
        // <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />

        // Check if NFC adapter is available
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(context);

        // Check if external storage is available
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());

        return nfcAdapter != null;
    } catch (Exception e) {
        // Handle exceptions here, log or return false based on your requirements
        e.printStackTrace(); // Log the exception for debugging
        return false;
    }
}


@SimpleFunction(description = "Check If Device Supports Bluetooth")
public boolean IsBluetoothSupported() {
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    return bluetoothAdapter != null;
}


@SimpleFunction(description = "Check If Location Services are Enabled")
public boolean IsLocationServiceEnabled() {
    LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    return locationManager != null && (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER));
}

@SimpleFunction(description = "Get Available Storage Space (in bytes)")
public long GetAvailableStorageSpace() {
    StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
        return statFs.getAvailableBytes();
    } else {
        // For versions before Jelly Bean MR2, use deprecated method
        return (long) statFs.getAvailableBlocks() * (long) statFs.getBlockSize();
    }
}

@SimpleFunction(description = "Get Total Storage Space (in bytes)")
public long GetTotalStorageSpace() {
    StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
        return statFs.getTotalBytes();
    } else {
        // For versions before Jelly Bean MR2, use deprecated method
        return (long) statFs.getBlockCount() * (long) statFs.getBlockSize();
    }
}

@SimpleFunction(description = "Check If External Storage (SD Card) is Available")
public boolean IsExternalStorageAvailable() {
    String state = Environment.getExternalStorageState();
    return Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
}

@SimpleFunction(description = "Get External Storage Directory")
public String GetExternalStorageDirectory() {
    return Environment.getExternalStorageDirectory().getAbsolutePath();
}


@SimpleFunction(description = "Get Device Screen Density")
public float GetScreenDensity() {
    return context.getResources().getDisplayMetrics().density;
}

@SimpleFunction(description = "Check If Device is in Landscape Orientation")
public boolean IsInLandscapeMode() {
    int orientation = context.getResources().getConfiguration().orientation;
    return orientation == Configuration.ORIENTATION_LANDSCAPE;
}

@SimpleFunction(description = "Get Device IMEI (Requires READ_PHONE_STATE permission)")
public String GetDeviceImei() {
    TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
        return telephonyManager != null ? telephonyManager.getImei() : "";
    } else {
        return "Permission Denied";
    }
}

@SimpleFunction(description = "Get WiFi MAC Address")
public String GetWifiMacAddress() {
    WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    if (wifiManager != null) {
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo != null ? wifiInfo.getMacAddress() : "";
    }
    return "";
}

@SimpleFunction(description = "Check If Do Not Disturb (DND) Mode is On")
public boolean IsDoNotDisturbModeOn() {
    AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    return audioManager != null && audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT;
}

@SimpleFunction(description = "Get Bluetooth Device Name")
public String GetBluetoothDeviceName() {
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    return bluetoothAdapter != null ? bluetoothAdapter.getName() : "";
}

// @SimpleFunction(description = "Check If Device is USB Tethered")
// public boolean IsUsbTethered() {
// ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
// NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();    if (connectivityManager != null) {
//         for (Network network : connectivityManager.getAllNetworks()) {
//             NetworkInfo networkInfo = connectivityManager.getNetworkInfo(network);
// if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_USB_TETHERING) {
//                 return networkInfo.isConnected();
//             }
//         }
//     }
//     return false;
// }

@SimpleFunction(description = "Get List of Running Apps")
public YailList GetRunningApps() {
    ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    if (activityManager != null) {
        List<ActivityManager.RunningAppProcessInfo> runningApps = activityManager.getRunningAppProcesses();
        YailList appList = new YailList();

        if (runningApps != null) {
            for (ActivityManager.RunningAppProcessInfo appInfo : runningApps) {
                appList.add(appInfo.processName);
            }
        }

        return appList;
    }
    return new YailList();
}

@SimpleFunction(description = "Get System Uptime (in milliseconds)")
public long GetSystemUptime() {
    return SystemClock.elapsedRealtime();
}

}
