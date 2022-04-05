package com.wee

import org.dom4j.Element
import org.dom4j.ElementHandler
import org.dom4j.ElementPath
import org.dom4j.io.SAXReader
import java.io.FileWriter

/**
 *@author young
 *@create 22/4/4 22:50
 */
fun domParse(args: Array<String>) {
    val source = if (args.isNotEmpty()) args[0] else "C:\\SomeProject\\mod\\target\\test.xml"
    val target = if (args.size > 1) args[1] else "C:\\SomeProject\\mod\\target\\out.json"
    val countTotal = if (args.size > 2) Integer.parseInt(args[2]) else 1000
    val countNode = if (args.size > 3) args[3] else "user"

    val writer = FileWriter(target)
    val xjsonParser = XjsonParser(writer, countNode)
    var count = 0

    val reader = SAXReader()
    reader.addHandler("/users/user", Dom4jParser(xjsonParser))
//    reader.setDefaultHandler(PruningElementHandler())
    reader.read(source)
//    reader.read(source).rootElement.elementIterator().forEach {
//        println("${it.qName} : ${it.text} : $count")
//        if (++count == countTotal) {
//            return@forEach
//        }
//    }
}

class Dom4jParser(val xjsonParser: XjsonParser) : ElementHandler {
    override fun onStart(elementPath: ElementPath) {
        val element = elementPath.current
//        xjsonParser.startElement(element.name)
        element.detach()
    }

    override fun onEnd(elementPath: ElementPath) {
        printMemoryInfo("${elementPath.current.name} : ${elementPath.current.text.trim()}")
        val element = elementPath.current
        buildElement(element)
        element.detach()
    }

    fun buildElement(element: Element) {
        xjsonParser.startElement(element.name)
        if(element.isTextOnly){
            xjsonParser.characters(element.text.trim())
        }else if (element.elementIterator().hasNext()) {
            element.elementIterator().forEach {
                buildElement(it)
            }
        }
        xjsonParser.endElement(element.name)
    }
}

class PruningElementHandler : ElementHandler {
    override fun onStart(elementPath: ElementPath) {
        elementPath.current.detach()
    }

    override fun onEnd(elementPath: ElementPath) {
        elementPath.current.detach()
    }
}