package main

import groovy.json.JsonSlurper
import groovy.time.*
import groovy.io.FileType
import groovy.util.logging.Log


@Log
class FileModify {
    static void main(String[] args) {
        def timeStart = new Date()

//      Reading data from json file that user can modify.
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

        listFiles(destinationDir, sourceDir, listMod, oldString)
        replace(oldString, newString, destinationDir)
        def timeStop = new Date()
        TimeDuration duration = TimeCategory.minus(timeStop, timeStart)
        log.info("Duration= ${duration}")
    }

//    method for replacing string in the files
    static void replace(oldString, newString, destinationDir) {
        def list = []
        def dir = new File(destinationDir)
        dir.eachFileRecurse(FileType.FILES) { file ->           //put in list all the files that are in the given directory(NewFiles)
            list << file
        }
        log.info("Replacing ${oldString} in files with ${newString} ")
        list.each {
            new AntBuilder().replace(file: it.path, token: oldString, value: newString)

        }
    }

//    method for finding the files that contains the given string, that we want to change, and list them
    static def listFiles(destinationDir, sourceDir, listMod, oldString) {
        def list = []
        def dir = new File(sourceDir)
        dir.eachFileRecurse(FileType.FILES) { file ->     //put in list all the files that are in the given directory(TextFiles)
            list << file
        }
        log.info("Listing files that contain ${oldString} ")
        list.each {                                     //look inside each file that is in the list
            def dest = new File(listMod)                //for the given string that we want to change
            def source = it.path
            find(destinationDir, source, dest, oldString)
        }
    }

//    method for creating copy of the original files in new directory (back up) and work with the new ones
    static def copyFile(sourceDir, destinationDir) {
        log.info("Greating copy of the files ")
        new AntBuilder().copy(todir: destinationDir) {
            file(file: sourceDir)
        }
    }

//    method for writing in the list.txt the files tha contain the given string for replace and creating a copy of the original file
    static def find(destinationDir, source, dest, term) {
        File source2 = new File(source)
        if (source2.getText("UTF-8").find(term)) {
            dest.append("${source}\n")
            copyFile(source2, destinationDir)          //copy the original file to an other directory
        }                                             // so we can have a back up of it
    }
}
