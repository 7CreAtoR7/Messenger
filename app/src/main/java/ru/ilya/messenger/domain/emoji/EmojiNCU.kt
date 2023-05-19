package ru.ilya.messenger.domain.emoji

data class EmojiNCU(
    val name: String,
    val code: Int
) {

    companion object {

        fun getCodeString(hexCode: String): String {
            return if (hexCode.contains("-")) {
                val parts = hexCode.split("-")
                val codePoints = parts.map { Integer.parseInt(it, 16) }
                String(codePoints.toIntArray(), 0, codePoints.size)
            } else {
                val codePoint = Integer.parseInt(hexCode, 16)
                String(Character.toChars(codePoint))
            }
        }

    }

}


