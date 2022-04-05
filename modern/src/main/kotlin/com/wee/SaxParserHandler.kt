package com.wee

import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler
import java.io.FileWriter
import javax.xml.parsers.SAXParserFactory

/**
 *@author young
 *@create 22/4/4 15:39
 */
fun saxParse(args: Array<String>) {
    val parser = SAXParserFactory.newInstance().newSAXParser()
    val writer = FileWriter("C:\\SomeProject\\mod\\target\\out.json")
    val xjsonParser = XjsonParser(writer, "user")
    parser.parse("C:\\SomeProject\\mod\\target\\test.xml", MySAXParserHandler(xjsonParser))
}

class MySAXParserHandler(private val xjsonParser: XjsonParser) : DefaultHandler() {

    override fun startElement(uri: String, localName: String, qName: String, attributes: Attributes) {
        xjsonParser.startElement(localName)
    }

    override fun characters(ch: CharArray, start: Int, length: Int) {
        xjsonParser.characters(String(ch, start, length))
    }

    override fun endElement(uri: String, localName: String, qName: String) {
        xjsonParser.endElement(localName)
//        if (xjsonParser.count == 100) {
//            xjsonParser.dealEnd()
//            throw RuntimeException()
//        }
    }

    override fun endDocument() {
        xjsonParser.dealEnd()
    }
}