package com.example.helloworld

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class Arduino : AppCompatActivity() {

    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var bluetoothLeScanner: BluetoothLeScanner
    private lateinit var textView: TextView

    private var bluetoothGatt: BluetoothGatt? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.arduino_test)

        textView = findViewById(R.id.textView)

        // Bluetoothアダプターを取得
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        // パーミッションの確認
        checkPermissionsAndStart()
    }

    // パーミッションを確認し、足りない場合はリクエスト
    private fun checkPermissionsAndStart() {
        val permissionsNeeded = mutableListOf<String>()

        // 各パーミッションを確認
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)
            != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.BLUETOOTH_CONNECT)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN)
            != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.BLUETOOTH_SCAN)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        // リクエストする必要がある場合はリクエスト
        if (permissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsNeeded.toTypedArray(), 1)
        } else {
            // パーミッションがすでに許可されている場合、BluetoothLeScannerを初期化してスキャン開始
            initializeBluetoothLeScanner()
        }
    }

    // パーミッションリクエストの結果を処理
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // パーミッションが許可された場合、BluetoothLeScannerを初期化してスキャン開始
                initializeBluetoothLeScanner()
            } else {
                // パーミッションが拒否された場合の処理
                textView.text = "Bluetooth接続またはスキャンのパーミッションが拒否されました。"
            }
        }
    }

    // BluetoothLeScannerを初期化
    private fun initializeBluetoothLeScanner() {
        bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
        startScan()
    }

    // BLEデバイスのスキャンを開始
    private fun startScan() {
        bluetoothLeScanner.startScan(leScanCallback)
    }

    // スキャン結果のコールバック
    private val leScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            result?.let {
                val device: BluetoothDevice = it.device
                val deviceName = device.name ?: "Unknown Device"

                // ArduinoR4WiFiという名前のデバイスを見つけたら接続
                if (deviceName == "ArduinoR4WiFi") {
                    textView.text = "デバイス発見: $deviceName"
                    bluetoothLeScanner.stopScan(this)

                    // GATT接続を開始
                    bluetoothGatt = device.connectGatt(this@Arduino, false, gattCallback)
                }
            }
        }
    }

    // GATT接続のコールバック
    private val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
            if (newState == BluetoothGatt.STATE_CONNECTED) {
                gatt?.discoverServices()  // サービスの発見
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)

            // サービスが発見された後、キャラクタリスティックを読み取る
            val service = gatt?.getService(java.util.UUID.fromString("180C"))
            val characteristic = service?.getCharacteristic(java.util.UUID.fromString("2A56"))
            gatt?.readCharacteristic(characteristic)
        }

        override fun onCharacteristicRead(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int
        ) {
            super.onCharacteristicRead(gatt, characteristic, status)
            if (status == BluetoothGatt.GATT_SUCCESS) {
                // キャラクタリスティックの値を取得
                val value = characteristic?.getStringValue(0)
                runOnUiThread {
                    textView.text = "受信データ: $value"
                }
            }
        }
    }
}
