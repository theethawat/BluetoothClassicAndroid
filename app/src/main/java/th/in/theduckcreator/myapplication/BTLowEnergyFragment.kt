package th.`in`.theduckcreator.myapplication


import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Context.BLUETOOTH_SERVICE
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import th.`in`.theduckcreator.myapplication.databinding.FragmentBtlowEnergyBinding

class BTLowEnergyFragment : Fragment() {


    private lateinit var binding :FragmentBtlowEnergyBinding
    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE){
        val bluetoothManager = activity?.getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    private val BluetoothAdapter.isDisable:Boolean
        get() = !isEnabled




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_btlow_energy, container, false)
        bluetoothAdapter?.takeIf { it.isDisable }.apply{
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent,12)
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
            BLEScanActivity(bluetoothAdapter!!).running()

    }

}
