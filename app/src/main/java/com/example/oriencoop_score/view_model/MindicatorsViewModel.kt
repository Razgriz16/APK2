import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oriencoop_score.repository.MindicatorsRepository
import kotlinx.coroutines.launch
import com.example.oriencoop_score.Result
import com.example.oriencoop_score.model.Indicador
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


class MindicatorsViewModel (private val repository: MindicatorsRepository) : ViewModel() {


    private val _indicadores = MutableStateFlow<Result<Indicador>>(Result.Loading) // Inicializa con un estado inicial
    val indicadores: StateFlow<Result<Indicador>> = _indicadores.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        fetchIndicadores()
    }

    fun fetchIndicadores() {
        viewModelScope.launch {
            _isLoading.value = true
            _indicadores.value = repository.getIndicadores()
            _isLoading.value = false
        }
    }

    // Optional: Helper functions to access data in a more convenient way
    fun getUF(): String? {
        return (_indicadores.value as? Result.Success)?.data?.UF
    }

    fun getDolar(): String? {
        return (_indicadores.value as? Result.Success)?.data?.Dolar
    }

    fun getEuro(): String? {
        return (_indicadores.value as? Result.Success)?.data?.Euro
    }

    fun getUTM(): String? {
        return (_indicadores.value as? Result.Success)?.data?.UTM
    }
}