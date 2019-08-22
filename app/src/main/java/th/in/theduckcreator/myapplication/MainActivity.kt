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
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import th.`in`.theduckcreator.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    /**
     * -------------------------------
     * Field Zone | Declare Variable
     * --------------------------------
     * */

    private lateinit var binding:ActivityMainBinding
    // Create Bluetooth Adapter
    val mybluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    val discoverableIntent:Intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
        putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,150)
    }




    // Create Bluetooth Reciever for register from myFilter receiver as a object that are BroadcastReciever
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



    /**
     * -------------------------------
     * Method Zone | Set Working Activity Using Android Activity Lifecycle
     * --------------------------------
     * */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        //Checking User Bluetooth Adapter is on or Off
        if (mybluetoothAdapter?.isEnabled == false) {
            val userEnableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(userEnableBtIntent, 12)
        }




        //Discovering Broadcast the Device and get an Information to my Bluetooth Reciever
        startActivity(discoverableIntent)
        Log.i("Bluetooth","Ready To Processing")
        val filter = IntentFilter(ACTION_FOUND)
        Log.i("Bluetooth",filter.toString())
        registerReceiver(receiver, filter)
    }

    override fun onResume() {
        super.onResume()
        //Paired Device Find
        val pairedDevice: Set<BluetoothDevice>? = mybluetoothAdapter?.bondedDevices
        var deviceListString = "Device \n"
        pairedDevice?.forEach { device ->
            deviceListString = deviceListString +"\n Device Name \n" + device.name + "\n MAC Address" + device.address +"\n"
        }

        binding.devicePrint1.setText(deviceListString)
    }




    override fun onDestroy() {
        super.onDestroy()
        Log.i("Bluetooth","Close Correctly")
        unregisterReceiver(receiver)
    }
}
