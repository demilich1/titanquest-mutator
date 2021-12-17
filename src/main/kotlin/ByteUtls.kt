fun indexOf(haystack: ByteArray?, needle: ByteArray?): Int {
    // needle is null or empty
    if (needle == null || needle.size == 0) return 0

    // haystack is null, or haystack's length is less than that of needle
    if (haystack == null || needle.size > haystack.size) return -1

    // pre construct failure array for needle pattern
    val failure = IntArray(needle.size)
    val n = needle.size
    failure[0] = -1
    for (j in 1 until n) {
        var i = failure[j - 1]
        while (needle[j] != needle[i + 1] && i >= 0) i = failure[i]
        if (needle[j] == needle[i + 1]) failure[j] = i + 1 else failure[j] = -1
    }

    // find match
    var i = 0
    var j = 0
    val haystackLen = haystack.size
    val needleLen = needle.size
    while (i < haystackLen && j < needleLen) {
        if (haystack[i] == needle[j]) {
            i++
            j++
        } else if (j == 0) i++ else j = failure[j - 1] + 1
    }
    return if (j == needleLen) i - needleLen else -1
}