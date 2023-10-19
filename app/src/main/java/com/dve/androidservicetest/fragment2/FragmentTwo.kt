package com.dve.androidservicetest.fragment2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.dve.androidservicetest.databinding.FragmentFragmentTwoBinding
import com.dve.androidservicetest.fragment1.FragmentOneDirections
import com.dve.androidservicetest.utils.ServiceCommunicator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentTwo : Fragment() {

    private val viewModel by viewModels<FragmentTwoViewModel>()
    private lateinit var binding: FragmentFragmentTwoBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFragmentTwoBinding.inflate(inflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setButtonClicks()
    }

    private fun setButtonClicks() {
        binding.startCountButton.setOnClickListener {
            val num = binding.countEdit.text.toString()
            if (num.isEmpty()){
                binding.countInput.error = "Enter count"
                return@setOnClickListener
            }
            viewModel.serviceCommunicator.startCount(num.toInt())
        }

        binding.stopCountButton.setOnClickListener {
            viewModel.serviceCommunicator.stopCount()
        }

        binding.navigateButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setListeners() {
        lifecycleScope.launch {repeatOnLifecycle(Lifecycle.State.RESUMED){
            viewModel.serviceCommunicator.events.collect{ events ->
                when(events){
                    is ServiceCommunicator.Events.Count -> {
                        binding.count.text = events.num.toString()
                    }
                    ServiceCommunicator.Events.Empty -> {
                        binding.count.text = ""
                    }
                    ServiceCommunicator.Events.StopCount -> {
                        binding.count.text = "Stopped"
                    }

                    else -> Unit
                }
            }
        }
        }
    }

}