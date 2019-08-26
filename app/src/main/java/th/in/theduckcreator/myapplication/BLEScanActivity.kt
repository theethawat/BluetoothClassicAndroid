package th.`in`.theduckcreator.myapplication

import android.app.ListActivity
import android.bluetooth.BluetoothAdapter
import android.util.Log
import android.widget.ListAdapter
import java.util.logging.Handler

private const val SCAN_PERIOD : Long = 10000
class BLEScanActivity(
    private val bluetoothAdapter: BluetoothAdapter
    //,private val handler: Handler
):ListActivity(){
     fun running(){
        scanLeDevice(true)
    }
    private var myScanning: Boolean = false
    private fun scanLeDevice(enable:Boolean){
        when(enable){
            true -> {
                myScanning = true
                bluetoothAdapter.startLeScan(leScanCallback)
                Log.i("Bluetooth","Le Scan Start Method")
            }
            else -> {
                myScanning = false
                bluetoothAdapter.stopLeScan(leScanCallback)
                Log.i("Bluetooth","Le Scan Method not enable")
            }
        }
    }
    private val leScanCallback = BluetoothAdapter.LeScanCallback({device,rssi,scanRecord ->
        runOnUiThread{
            Log.i("Bluetooth","Device found "+device.name + " Mac " + device.address)
        }
    })



}