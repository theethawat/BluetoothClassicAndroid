package th.`in`.theduckcreator.myapplication


import android.bluetooth.*
import android.bluetooth.BluetoothDevice.ACTION_FOUND
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import th.`in`.theduckcreator.myapplication.databinding.BtClassicActivityBinding
import java.io.IOException
import java.util.*

class BTClassicActivity :Fragment()   {
    /**
     * -------------------------------
     * Field Zone | Declare Variable
     * --------------------------------
     * */

    private lateinit var binding:BtClassicActivityBinding
    // Create Bluetooth Adapter
    val mybluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    val discoverableIntent:Intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
        putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,150)
    }

    private val profileListener = object : BluetoothProfile.ServiceListener{
        override fun onServiceConnected(myProfile: Int, profileProxy: BluetoothProfile) {
            Log.i("Bluetooth",myProfile.toString())
        }

        override fun onServiceDisconnected(p0: Int) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            Log.i("Bluetooth","Cannot find Any Profile")
        }
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



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.bt_classic_activity,container,false)
        //Checking User Bluetooth Adapter is on or Off
        if (mybluetoothAdapter?.isEnabled == false) {
            val userEnableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(userEnableBtIntent, 12)
        }

        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
            activity?.registerReceiver(receiver, filter)

        binding.startDiscoverBT.setOnClickListener{
            Log.i("Bluetooth","Discover Button is Clicked")
            Log.i("Bluetooth",filter.toString())
            mybluetoothAdapter?.startDiscovery()
            startActivity(discoverableIntent)
            binding.profileFindBT.visibility = View.VISIBLE
        }

        binding.profileFindBT.setOnClickListener{
            Log.i("Bluetooth","Profile Listener is Clicked")
            mybluetoothAdapter?.getProfileProxy(context,profileListener,BluetoothProfile.HEALTH)
        }

        //Discovering Broadcast the Device and get an Information to my Bluetooth Reciever
        return binding.root
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
        activity?.unregisterReceiver(receiver)
    }

    //Let's it act as a bluetooth Server
    private  inner class AcceptThread : Thread(){
        //Use LazyThreadSafety is easier than Try-Catch Exception
        private val myServerSocket:BluetoothServerSocket ? by lazy(LazyThreadSafetyMode.NONE){
            mybluetoothAdapter?.listenUsingInsecureRfcommWithServiceRecord("EHealthRFCOMM", UUID.fromString("00001400-0000-1000-8000-00805F9B34FB"))
        }

        override fun run() {
            var shouldLoop = true //Listen until exception until socket return
            while (shouldLoop){
                val socket:BluetoothSocket? = try {
                    myServerSocket?.accept()
                } catch (e: IOException){
                    Log.e("Bluetooth","Socket accept() method fail",e)
                    shouldLoop = false
                    null
                }
                socket?.also {
                   // manageMyConnecedSocket(it)
                    myServerSocket?.close()
                    shouldLoop = false
                }
            }
        }

        fun cancle(){
            try {
                myServerSocket?.close()
            }catch (e:IOException){
                Log.e("Bluetooth","Cannot Close the Socket",e)
            }
        }
    }
}
