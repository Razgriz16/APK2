package com.example.oriencoop_score.view_model

/*
class MovimientosTest2 : BaseTests<MovimientosViewModel, MovimientosRepository, MovimientosResponse, Movimiento>() {

    override val repositoryFunction: suspend MovimientosRepository.(String, String) -> Result<MovimientosResponse>
        get() = { token, rut -> getMovimientos(token, rut) }

    override val emptyResponse: MovimientosResponse
        get() = MovimientosResponse(emptyList())

    override val successResponse: MovimientosResponse
        get() = MovimientosResponse(
            movimientos = listOf(
                Movimiento(
                    CUENTA = 1234567890L,
                    FECHAPAGO = "2025-02-15",
                    MONTO = "1500.75",
                    NOMBREABRTRANSACCION = "PAG",
                    NOMBRETRANSACCION = "Pago de Servicio",
                    ESCARGO = "N"
                )
            )
        )

    override val errorMessage: String
        get() = "Token o Rut no pueden estar vacíos"

    override val expectedSuccessEntities: List<Movimiento>
        get() = successResponse.movimientos

    override fun createViewModel(): MovimientosViewModel {
        return MovimientosViewModel(repository, sessionManager)
    }

    override fun createRepository(): MovimientosRepository {
        return mockk()
    }

    override fun getErrorState(): StateFlow<String?> {
        return viewModel.error
    }

    override fun getDataState(): StateFlow<List<Movimiento>> {
        return viewModel.movimientos
    }

    override fun getLoadingState(): StateFlow<Boolean> {
        return viewModel.isLoading
    }

    override fun triggerDataFetch() {
        viewModel.obtenerMovimientos()
    }

    override fun MovimientosResponse.toEntityList(): List<Movimiento> {
        return movimientos
    }
}*/