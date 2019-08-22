package th.`in`.theduckcreator.myapplication


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import th.`in`.theduckcreator.myapplication.databinding.FragmentTitleBinding

/**
 * A simple [Fragment] subclass.
 */
class TitleFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

       val binding = DataBindingUtil.inflate<FragmentTitleBinding>(inflater,R.layout.fragment_title,container,false)
        binding.bluetoothClassicBT.setOnClickListener{view: View? ->
            binding.bluetoothClassicBT.findNavController().navigate(R.id.action_titleFragment_to_BTClassicActivity2)
        }
        return binding.root
    }


}
