package com.wee

import java.io.Writer

/**
 *@author young
 *@create 22/4/5 13:42
 */
class XjsonParser(private val writer: Writer, val countNode: String) {

    private val str = StringBuilder("{")
    private var depth = 0
    var count = 0

    fun startElement(localName: String) {
        ++depth
        if (!(str.isNotEmpty() && str.last() == '{')) {
            str.append(",")
        }
        str.append("\n")
        for (i in 0 until depth) {
            str.append("\t")
        }
        str.append("\"$localName\": {")
    }

    fun characters(text: String) {
        if (text.isNotBlank()) {
            if (str.isNotEmpty() && str.last() == '{') {
                str.deleteCharAt(str.length - 1)
            }
            str.append("\"$text\"")
        }
    }

    fun endElement(localName: String) {
        if (!(str.isNotEmpty() && str.last() == '\"')) {
            str.append("\n")
            for (i in 0 until depth) {
                str.append("\t")
            }
            str.append("}")
        }
        writer.append(str)
        writer.flush()
        str.setLength(0)
        depth--
        if (!(countNode.isNotEmpty() && localName != countNode)) {
            count++
        }
        if (count % 100000 == 0 && localName == countNode) {
            printMemoryInfo("count:$count current:$localName")
        }
    }

    fun dealEnd() {
        while (depth >= 0) {
            str.append("\n")
            for (i in 0 until depth) {
                str.append("\t")
            }
            str.append("}")
            depth--
        }
        writer.append(str)
        writer.flush()
        writer.close()
    }
}