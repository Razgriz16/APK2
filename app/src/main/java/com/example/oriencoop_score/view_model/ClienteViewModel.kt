package com.example.oriencoop_score.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oriencoop_score.model.AddressData
import com.example.oriencoop_score.model.UserData
import com.example.oriencoop_score.repository.ClienteRepository
import com.example.oriencoop_score.utility.Result
import com.example.oriencoop_score.utility.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ClienteViewModel @Inject constructor(
    private val clienteRepository: ClienteRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    // Estado combinado para la información del cliente y la dirección
    data class ClienteInfoState(
        val userData: UserData? = null,
        val addressData: AddressData? = null,
        val isLoading: Boolean = false,
        val error: String? = null
    )

    private val _clienteInfoState = MutableStateFlow(ClienteInfoState())
    val clienteInfoState: StateFlow<ClienteInfoState> = _clienteInfoState.asStateFlow()

    /**
     * Obtiene la información del cliente y su dirección para un RUT dado.
     *
     * @param rut El RUT del cliente.
     */
    init {
        fetchClienteInfo()
    }
    fun fetchClienteInfo() {
        val rut = sessionManager.getUserRut().toString()
        viewModelScope.launch {
            _clienteInfoState.value = ClienteInfoState(isLoading = true)

            // Obtener información del usuario
            val userResult = clienteRepository.getUserInfo(rut)
            when (userResult) {
                is Result.Success -> {
                    val userData = userResult.data.data.firstOrNull()
                    if (userData != null) {
                        // Obtener dirección del usuario
                        val addressResult = clienteRepository.getAddresses(rut)
                        when (addressResult) {
                            is Result.Success -> {
                                val addressData = addressResult.data.data.firstOrNull()
                                _clienteInfoState.value = ClienteInfoState(
                                    userData = userData,
                                    addressData = addressData,
                                    isLoading = false
                                )
                            }
                            is Result.Error -> {
                                _clienteInfoState.value = ClienteInfoState(
                                    userData = userData,
                                    isLoading = false,
                                    error = addressResult.exception.message ?: "Error al obtener la dirección"
                                )
                            }

                            Result.Loading -> Result.Loading
                        }
                    } else {
                        _clienteInfoState.value = ClienteInfoState(
                            isLoading = false,
                            error = "No se encontraron datos del usuario"
                        )
                    }
                }
                is Result.Error -> {
                    _clienteInfoState.value = ClienteInfoState(
                        isLoading = false,
                        error = userResult.exception.message ?: "Error al obtener la información del usuario"
                    )
                }

                Result.Loading -> Result.Loading
            }
        }
    }
}