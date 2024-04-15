package ru.snowadv.utils

object EmojiUtils {

    // This is a "prohibited" emoji that appeared in unicode 6. It will exist on devices with api 21+
    private const val BAD_EMOJI_PLACEHOLDER = "\uD83D\uDEAB"

    /**
     * This function _WILL TRY_ to decode received emoji. In case it fails, it will return
     * placeholder emoji (prohibited sign. Exists on android devices with API 21+)
     */
    fun combinedHexToString(combinedHex: String): String {
        return try {
            String(combinedHexToChars(combinedHex))
        } catch (e: NumberFormatException) {
            BAD_EMOJI_PLACEHOLDER
        }
    }

    @Throws(NumberFormatException::class)
    fun combinedHexToChars(combinedHex: String): CharArray {
        return combinedHex.split("-").map { hexToChars(it) }.flatMap { it.toList() }.toCharArray()
    }

    @Throws(NumberFormatException::class)
    fun hexToChars(hex: String): CharArray {
        return Character.toChars(Integer.parseInt(hex, 16))
    }

    /**
     * This function _WILL TRY_ to decode received emoji. In case it fails, it will return
     * placeholder emoji (prohibited sign. Exists on android devices with API 21+)
     */
    fun codePointToStringOrDefault(code: Int): String {
        return try {
            String(codePointToChars(code))
        } catch (e: NumberFormatException) {
            BAD_EMOJI_PLACEHOLDER
        }
    }

    @Throws(NumberFormatException::class)
    fun codePointToChars(code: Int): CharArray {
        return Character.toChars(code)
    }


}