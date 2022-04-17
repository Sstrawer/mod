package com.wee

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.beust.jcommander.JCommander
import com.beust.jcommander.Parameter
import com.beust.jcommander.internal.Lists
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset
import java.util.*

/**
 *@author young
 *@create 22/4/13 19:29
 */
fun main(args: Array<String>) {
    val argument = Argument()
    val jCommander = JCommander.newBuilder()
        .addObject(argument)
        .build()
    try {
        jCommander.parse(*args)
    } catch (e: Exception) {
        jCommander.usage()
        return
    }
//    getGiteeRepo()
    downRepos(argument)
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

fun downRepos(argument: Argument) {
    val sshUrlFile = argument.repos
    val regs = argument.filter?.replace(",", " ")
    var base = "."
    val baseDir = File(base)

    var count = 0
    FileReader(sshUrlFile).forEachLine { sshUrl ->
        var repo = sshUrl.substringAfterLast(":")
            .substringBeforeLast(".")
            .replace("/", "_")
        println("${count++}: $repo")
        try {
            exec(
                "git clone --filter=blob:none --depth 1 --single-branch --no-checkout $sshUrl $repo",
                null,
                baseDir
            )
            exec("git -C $repo sparse-checkout init", null, baseDir)
            exec("git -C $repo sparse-checkout set $regs", null, baseDir)
            exec("git -C $repo read-tree -mu HEAD", null, baseDir)
            exec("rm -rf $repo/.git", null, baseDir)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

fun exec(command: String, envp: Array<String>? = null, dir: File? = File(".")) {
    val runTime = Runtime.getRuntime()
    println("$command")
    val process = runTime.exec(command, envp, dir)
    process.inputStream.copyTo(System.out)
    process.errorStream.copyTo(System.err)
//    process.inputStream.bufferedReader(Charset.forName(System.getProperty("sun.jnu.encoding"))).forEachLine { println(it) }
//    process.errorStream.bufferedReader(Charset.forName(System.getProperty("sun.jnu.encoding"))).forEachLine { println(it) }
    process.waitFor()
}

fun exec(command: Array<String>, envp: Array<String>? = null) {
    val runTime = Runtime.getRuntime()
    val process = runTime.exec(command, envp)
    process.inputStream.bufferedReader(Charset.forName(System.getProperty("sun.jnu.encoding")))
        .forEachLine { println(it) }
    process.errorStream.bufferedReader(Charset.forName(System.getProperty("sun.jnu.encoding")))
        .forEachLine { println(it) }
    println(process.waitFor())
}

class Argument {
    @Parameter
    var parameters: List<String> = Lists.newArrayList()

    @Parameter(names = ["-repos"], required = true, description = "仓库文件, eg: Java.repos")
    var repos: String? = null

    @Parameter(names = ["-filter"], required = true, description = "文件过滤逗号分隔, eg: *.java,*.javacode")
    var filter: String? = null
}

