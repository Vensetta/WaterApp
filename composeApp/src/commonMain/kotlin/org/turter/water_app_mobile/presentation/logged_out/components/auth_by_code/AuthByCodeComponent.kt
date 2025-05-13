package org.turter.water_app_mobile.presentation.logged_out.components.auth_by_code

interface AuthByCodeComponent {

    fun onDigitClick(digit: Char)

    fun onBackspaceClick()

    fun onToAuthDefaultClick()

}