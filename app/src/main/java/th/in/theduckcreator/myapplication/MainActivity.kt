package th.`in`.theduckcreator.myapplication

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothDevice.ACTION_FOUND
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {

    //1st Create Bluetooth Adapter
    val mybluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    val discoverableIntent:Intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
        putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,150)
    }

    //5th Create Bluetooth Reciever for register from myFilter
    // receiver as a object that are BroadcastReciever
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(myContext: Context, intent: Intent) {
            Log.i("Bluetooth ","Checkpoint Broadcast Receiver")
            when (val action = intent.action) {
                ACTION_FOUND -> {
                    Log.i("Bluetooth", "Device Found" + action)
                    //Found the Device get that object
                    val device: BluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val deviceName = device.name
                    val deviceHardwareAddress = device.address //MAC Address
                    Log.i(
                        "Bluetooth",
                        "New Device Found! Device Name :" + deviceName + " MAC Address " + deviceHardwareAddress
                    )
                }
                else -> {
                    Log.i("Bluetooth","Not Found "+ action)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //2nd Checking User Bluetooth Adapter is on or Off
        if (mybluetoothAdapter?.isEnabled == false) {
            val userEnableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(userEnableBtIntent, 12)
        }

        //3rd Paired Device Find
        val pairedDevice: Set<BluetoothDevice>? = mybluetoothAdapter?.bondedDevices
        pairedDevice?.forEach { device ->
            val deviceName = device.name
            val deviceHardwareAddress = device.address
            Log.i("Bluetooth", "Device Name " + deviceName + "MAC Address " + deviceHardwareAddress)
        }


        //4th Discovering
        //Broadcast the Device and get an Information to my Bluetooth Reciever

        startActivity(discoverableIntent)
        Log.i("Bluetooth","Ready To Processing")

        val filter = IntentFilter(ACTION_FOUND)
        //Debug
        Log.i("Bluetooth",filter.toString())

        registerReceiver(receiver, filter)

    }




    override fun onDestroy() {
        super.onDestroy()
        Log.i("Bluetooth","Close Correctly")
        unregisterReceiver(receiver)
    }
}
