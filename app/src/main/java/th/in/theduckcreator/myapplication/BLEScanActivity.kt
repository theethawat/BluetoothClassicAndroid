package th.`in`.theduckcreator.myapplication

import android.app.ListActivity
import android.bluetooth.BluetoothAdapter
import java.util.logging.Handler

private const val SCAN_PERIOD : Long = 10000
class BLEScanActivity(
    private val bluetoothAdapter: BluetoothAdapter,
    private val handler: Handler
):ListActivity(){
    private var myScanning: Boolean = false

    private fun scanLeDevice(enable:Boolean){
        when(enable){
            true -> {
                handler.postDelayed({
                    myScanning = false
                    bluetoothAdapter.stopLeScan(leScanCallback)
                }, SCAN_PERIOD)
                myScanning = true
                bluetoothAdapter.stopLeScan(leScanCallback)
            }
            else -> {
                myScanning = false
                bluetoothAdapter.stopLeScan(leScanCallback)
            }
        }
    }
    private lateinit var leDeviceListAdapter: LeDeviceListAdapter
    private val leScanCallback = BluetoothAdapter.LeScanCallback({device,rssi,scanRecord ->
        runOnUiThread{
            leDeviceListAdapter.addDevice(device)
            leDeviceListAdapter.notifyDataSetChanged()
        }
    })
}