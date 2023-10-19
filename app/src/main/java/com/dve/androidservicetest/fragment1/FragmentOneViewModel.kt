package com.dve.androidservicetest.fragment1

import androidx.lifecycle.ViewModel
import com.dve.androidservicetest.utils.ServiceCommunicator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FragmentOneViewModel @Inject constructor(
    val serviceCommunicator: ServiceCommunicator
): ViewModel() {

}