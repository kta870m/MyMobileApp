// TrackingFragment
package vn.swinburne.mymobileapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import vn.swinburne.mymobileapp.databinding.FragmentBookingBinding
import vn.swinburne.mymobileapp.databinding.FragmentTrackingBinding
import vn.swinburne.mymobileapp.databinding.FragmentHistoryBinding

class TrackingFragment : Fragment() {

    private var _binding: FragmentTrackingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTrackingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}