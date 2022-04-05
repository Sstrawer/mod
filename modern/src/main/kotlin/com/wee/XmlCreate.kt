package com.wee

import org.dom4j.DocumentHelper
import org.dom4j.io.OutputFormat
import org.dom4j.io.XMLWriter
import java.io.FileWriter

/**
 *@author young
 *@create 22/4/5 10:13
 */
fun xmlCreate(args: Array<String>) {
    val document = DocumentHelper.createDocument()
    val root = document.addElement("users")
    root.addAttribute("name", "users")
    val user = DocumentHelper.createElement("user")
    user.addAttribute("name", "username")
    user.addElement("id").addAttribute("name", "userId").setText("this is user's ID number")
    val out = FileWriter("C:\\SomeProject\\mod\\target\\test.xml")
    val compactPrint = OutputFormat.createPrettyPrint()
    compactPrint.setEncoding("UTF-8")
    val writer = XMLWriter(out, compactPrint)
    writer.startDocument()
    writer.writeOpen(root)

    for (i in 1..70000000) {
        user.element("id").setText("$i")
        writer.write(user)
        if (i % 1000000 == 0) {
            printMemoryInfo()
        }
    }
    writer.writeClose(root)
    writer.endDocument()
    writer.close()
}

