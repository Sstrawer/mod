package com.wee

import java.io.FileReader
import java.io.FileWriter
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLStreamReader

/**
 *@author young
 *@create 22/4/4 20:40
 */
fun staxParse(args: Array<String>) {
    val source = if (args.isNotEmpty()) args[0] else "C:\\SomeProject\\mod\\target\\test.xml"
    val target = if (args.size > 1) args[1] else "C:\\SomeProject\\mod\\target\\out100.json"
    val countTotal = if (args.size > 2) Integer.parseInt(args[2]) else 100
    val countNode = if (args.size > 3) args[3] else ""

    val streamReader = XMLInputFactory.newFactory().createXMLStreamReader(FileReader(source))
    val writer = FileWriter(target)

    val xjsonParser= XjsonParser(writer, countNode)
    while (xjsonParser.count < countTotal && streamReader.hasNext()) {
        when (streamReader.next()) {
            XMLStreamReader.START_ELEMENT -> {
                xjsonParser.startElement(streamReader.localName)
            }
            XMLStreamReader.CHARACTERS -> {
                xjsonParser.characters(streamReader.text)
            }
            XMLStreamReader.END_ELEMENT -> {
                xjsonParser.endElement(streamReader.localName)
            }
        }
    }
    xjsonParser.dealEnd()
}