package com.wee

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import jdk.nashorn.internal.objects.NativeArray.forEach
import java.io.FileReader
import java.io.FileWriter
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

/**
 *@author young
 *@create 22/4/13 19:29
 */
fun main(args: Array<String>) {
//    getGiteeRepo()
    downRepos()
}

fun getGiteeRepo() {
    val writer = FileWriter("repos")
    for (pageNo in 1..100) {
        println("pageNo:$pageNo")
        val url =
            URL("https://gitee.com/api/v5/search/repositories?access_token=093ab7eef6f2615c640c615ba8f7dbf7&q=java&page=$pageNo&per_page=100&language=Java&sort=stars_count&order=desc")
        val httpConn = url.openConnection() as HttpURLConnection
        httpConn.requestMethod = "GET"
        httpConn.setRequestProperty("Content-Type", "application/json;charset=UTF-8")

        val responseStream = if (httpConn.getResponseCode() / 100 === 2) httpConn.inputStream else httpConn.errorStream
        val s = Scanner(responseStream).useDelimiter("\\A")
        val response = if (s.hasNext()) s.next() else ""

        JSON.parseArray(response, JSONObject::class.java)
            .forEach {
                val sshUrl = it.getString("ssh_url")
                writer.appendLine(sshUrl)
//                Runtime.getRuntime().exec("git clone $sshUrl")
            }
    }
    writer.close()
}

fun downRepos(){
    val target = "../repocode"
    val reader = FileReader("repo100")
    var i = 0
    reader.forEachLine {
        println("${i++} git clone $it")
        val dir = it.substringAfterLast(":")
            .substringBeforeLast(".")
            .replace("/",".")
        Runtime.getRuntime().exec("git clone --filter=blob:none --depth 1 --single-branch --no-checkout $it $target/$dir")
        Runtime.getRuntime().exec("git -C $target/$dir sparse-checkout init")
        Runtime.getRuntime().exec("git -C $target/$dir sparse-checkout set *.java")
        Runtime.getRuntime().exec("git -C $target/$dir read-tree -mu HEAD")
    }
}