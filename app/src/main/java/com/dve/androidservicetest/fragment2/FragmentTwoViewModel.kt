package com.dve.androidservicetest.fragment2

import androidx.lifecycle.ViewModel
import com.dve.androidservicetest.utils.ServiceCommunicator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FragmentTwoViewModel @Inject constructor(
    val serviceCommunicator: ServiceCommunicator
):ViewModel() {

}