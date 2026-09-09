package com.clogs.disclogs.data.model

import java.util.Calendar

/*
 * Função para obter o ID da semana atual.
 */
fun getCurrentWeekId(): String {
    val calendar =
        Calendar.getInstance() // retorna a data atual como: ano, mês, dia, hora, minuto, segundo, milissegundo
    val year = calendar.get(Calendar.YEAR) // retorna o ano atual
    val week =
        calendar.get(Calendar.WEEK_OF_YEAR) // retorna a semana do ano atual como: 1, 2, 3, ..., 52
    return "${year}_W$week" // retorna o ‘ID’ da semana atual no formato: ano_Wsemana
}