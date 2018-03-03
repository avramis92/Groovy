package main

import groovy.json.JsonSlurper
import groovy.time.*
import groovy.io.FileType
import groovy.util.logging.Log


@Log
class FileModify {
    static void main(String[] args) {
        def timeStart = new Date()

        def jsonSlurper = new JsonSlurper()
        def reader = new BufferedReader(new InputStreamReader(new FileInputStream("src/resources/data.json"), "UTF-8"))
        Map data = jsonSlurper.parse(reader)
        Map jsonResult = (Map) data
        Map user = (Map) jsonResult.get("data")
        def sourceDir = (String) user.get("sourceDir")
        def destinationDir = (String) user.get("destinationDir")
        def listMod = (String) user.get("listMod")
        def oldString = (String) user.get("oldString")
        def newString = (String) user.get("newString")

        copyFile(sourceDir, destinationDir)
        listFiles(sourceDir, listMod, oldString)
        replace(oldString, newString, destinationDir)
        def timeStop = new Date()
        TimeDuration duration = TimeCategory.minus(timeStop, timeStart)
        log.info("Duration= ${duration}")
    }

    static void replace(oldString, newString, destinationDir) {
        def list = []
        def dir = new File(destinationDir)
        dir.eachFileRecurse(FileType.FILES) { file ->
            list << file
        }
        log.info("Replacing ${oldString} in files with ${newString} ")
        list.each {
            new AntBuilder().replace(file: it.path, token: oldString, value: newString)

        }
    }

    static def listFiles(sourceDir, listMod, oldString) {
        def list = []
        def dir = new File(sourceDir)
        dir.eachFileRecurse(FileType.FILES) { file ->
            list << file
        }
        log.info("Listing files that contain ${oldString} ")
        list.each {
            def dest = new File(listMod)
            def source = it.path
            find(source, dest, oldString)
        }
    }

    static def copyFile(sourceDir, destinationDir) {
        log.info("Greating copy of the files ")
        new AntBuilder().copy(todir: destinationDir) {
            fileset(dir: sourceDir)
        }
    }

    static def find(source, dest, term) {
        File source2 = new File(source)
        if (source2.getText("UTF-8").find(term)) {
            dest.append("${source}\n")
        }
    }
}
