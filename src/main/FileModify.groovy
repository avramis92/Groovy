package main

import groovy.io.FileType
import org.apache.tools.ant.util.FileUtils

class FileModify {
    static void main(String[] args) {
//        FileModify fileModify = new FileModify()
        copyFile()
        replace()
    }

    static void replace() {
        def list = []
        def dir = new File("src/resources/NewFiles")
        dir.eachFileRecurse(FileType.FILES) { file ->
            list << file
        }
        list.each {
            def dest = new File("src/new/list.txt")
            def source = it.path
            find(source, dest, "Spyros")

            println it.path
            new AntBuilder().replace(file: it.path, token: "Spyros", value: "5678")

        }
    }

    static def copyFile() {
        String sourceDir = "src/resources/TextFiles"
        String destinationDir = "src/resources/NewFiles"
        new AntBuilder().copy(todir: destinationDir) {
            fileset(dir: sourceDir)
        }
    }

//    def static writeToFile(def directory, def infoList) {
//        File file = new File("$directory")
//
//        infoList.each {
//            file << ("${it}\r\n")
//        }
//
//        def fileName = "list"
//        def inputFile = new File(directory)
//        inputFile.write(infoList)
//    }

     static find(source, dest, term) {

        if (source.contains(term)) {
            dest.write(term)
        }
    }
}
