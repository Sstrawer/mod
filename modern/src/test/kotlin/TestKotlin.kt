import org.junit.jupiter.api.Test

/**
 *@author young
 *@create 22/4/8 19:47
 */
class TestKotlin {
    @Test
    fun testUnicode() {
        var s = "1\u00012\u00093\u00204\t5\n6\"7\\8{9}0"
        println(s)
        /**
         * 四种unicode在正则中的表示方式
         */
        println(s.replace(Regex("\\u0001"), ""))
        println(s.replace(Regex("\\x01"), ""))
        println(s.replace(Regex("\\x{0001}"), ""))
        println(s.replace(Regex("\\x{01}"), ""))
        /**
         * unicode前32个字符中除了回车、换行、\u0009都是xml非法字符
         * 大括号、双引号、回车、换行、转义符都是影响json格式化的字符
         * 下面的正则将以上字符替换为空格
         */
        println(s.replace(Regex("[\\x00-\\x1f\\\\{\"}]"), "\u0020"))
        // \cx 去除控制字符x为控制字符
        println(s.replace(Regex("\\cx"), ""))
    }
}