package com.dkproject.presentation.util

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class NetworkManager @Inject constructor(
    private val connectivityManager: ConnectivityManager
) {
    private val _isConnected = MutableStateFlow<Boolean?>(null)
    val isConnected: StateFlow<Boolean?> = _isConnected.asStateFlow()

    //네트워크의 변화를 감지해 상태를 변화하는 콜백
    private val networkCallBack = object: ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            _isConnected.update { true }
        }

        override fun onUnavailable() {
            _isConnected.update { false }
        }

        override fun onLost(network: Network) {
            _isConnected.update { false }
        }
    }

    //콜백 등록
    fun registerNetworkCallback() {
        checkInitialNetworkState()
        connectivityManager.registerDefaultNetworkCallback(networkCallBack)
    }
    //콜백 해제
    fun unRegisterNetworkCallback() {
        connectivityManager.unregisterNetworkCallback(networkCallBack)
    }
    //현재 네트워크 상태를 가져와서 상태 업데이트 하는 함수
    private fun checkInitialNetworkState() {
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        _isConnected.update { capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true }
    }


}