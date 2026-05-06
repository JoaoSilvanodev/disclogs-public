package com.clogs.disclogs.ui.screens.details

import com.clogs.disclogs.data.repository.AlbumRepository
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test


class AlbumDetailViewModelTest {

    // duble da interface
    private val mockRepository = mockk<AlbumRepository>()

    // classe de teste
    private lateinit var viewModel: AlbumDetailViewModel

    // motor falso de tempo para testar coroutines (viewModelScope)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {

        // roda antes de cada teste
        Dispatchers.setMain(testDispatcher) // engana a viewmodel para achar que esta no celular
        viewModel = AlbumDetailViewModel(mockRepository)
    }

    @After
    fun tearDown() {
        // após cada teste, tudo é limpo
        Dispatchers.resetMain()
    }

    @Test
    fun `quando tentar salvar sem estar logado, deve atualizar uiState com erro`() = runTest {

        // --- ARRANGE (Preparar) ---
        // Dizemos para o dublê: "Quando a ViewModel perguntar-te quem tá logado, diga que é nulo"
        coEvery { mockRepository.getCurrentUserId() } returns null

        // --- ACT (Agir) ---
        // Simulamos o usuário clicando no botão vermelho da gaveta
        viewModel.saveReview(rating = 4.0, text = "Bom!", logToDiary = false, favorite = true)

        // Rodamos a "manivela" do tempo para a coroutine terminar de executar
        testDispatcher.scheduler.advanceUntilIdle()

        // --- ASSERT (Verificar) ---
        // Pegamos o estado que a ViewModel mandou pra tela
        val estadoAtual = viewModel.uiState.value

        assertEquals(
            "Você precisa estar logado para avaliar.",
            estadoAtual.errorMessage
        )
    }
}