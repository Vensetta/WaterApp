package org.turter.water_app_mobile.presentation.logged_out.components.auth_by_code

class AuthCodeItem {
    companion object {
        private const val SIZE = 4
    }

    private val code = mutableListOf<Digit>().apply {
        (0 until SIZE).forEach {
            add(Digit.Empty)
        }
    }

    private var point = 0

    fun addLast(digit: Char) {
        if (point < SIZE && digit.isDigit()) {
            code[point++] = Digit.Present(digit)
        }
    }

    fun removeLast() {
        if (point > 0) {
            code[--point] = Digit.Empty
        }
    }

    fun cleanUp() {
        for (i in 0 until SIZE) code[i] = Digit.Empty
        point = 0
    }

    fun isComplete(): Boolean = point >= SIZE

    fun isEmpty(): Boolean = point == 0

    fun getCode(): Int {
        val sb = StringBuilder(4)
        for (i in 0 until SIZE) {
            sb.append(
                when (val symbol = code[i]) {
                    Digit.Empty -> '0'
                    is Digit.Present -> symbol.digit
                }
            )
        }
        return sb.toString().toInt()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as AuthCodeItem

        if (point != other.point) return false
        if (code != other.code) return false

        return true
    }

    override fun hashCode(): Int {
        var result = point
        result = 31 * result + code.hashCode()
        return result
    }


    private sealed interface Digit {
        data object Empty : Digit
        data class Present(val digit: Char) : Digit
    }
}