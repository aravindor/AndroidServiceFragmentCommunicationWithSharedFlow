package com.dve.androidservicetest.fragment1

import android.content.Intent
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
import com.dve.androidservicetest.MyService
import com.dve.androidservicetest.databinding.FragmentFragmentOneBinding
import com.dve.androidservicetest.utils.ServiceCommunicator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentOne : Fragment() {

    private val viewModel by viewModels<FragmentOneViewModel>()
    private lateinit var binding: FragmentFragmentOneBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFragmentOneBinding.inflate(inflater)
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
            val action = FragmentOneDirections.actionFragmentOneToFragmentTwo()
            findNavController().navigate(action)
        }

        binding.startService.setOnClickListener {
            val intent  = Intent(requireContext(), MyService::class.java)
            requireActivity().startService(intent)
        }

        binding.stopService.setOnClickListener {
            val intent  = Intent(requireContext(), MyService::class.java)
            requireActivity().stopService(intent)
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

                    is ServiceCommunicator.Events.ServiceStatus -> {
                        if (events.running){
                            binding.startService.isEnabled = false
                            binding.stopService.isEnabled = true
                            binding.startCountButton.isEnabled = true
                            binding.stopCountButton.isEnabled = true
                        }else{
                            binding.startService.isEnabled = true
                            binding.stopService.isEnabled = false
                            binding.startCountButton.isEnabled = false
                            binding.stopCountButton.isEnabled = false
                        }
                    }
                    else -> {}
                }
            }
        }
        }

        viewModel.serviceCommunicator.checkServiceStatus()
    }

}