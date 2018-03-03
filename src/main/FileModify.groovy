package main

import groovy.io.FileType

class FileModify {
    static void main(String[] args) {
        copyFile()
        listFiles()
        replace()
    }

    static void replace() {
        def list = []
        def dir = new File("src/resources/NewFiles")
        dir.eachFileRecurse(FileType.FILES) { file ->
            list << file
        }
        list.each {
            new AntBuilder().replace(file: it.path, token: "Spyros", value: "5678")

        }
    }

    static def listFiles() {
        def list = []
        def dir = new File("src/resources/TextFiles")
        dir.eachFileRecurse(FileType.FILES) { file ->
            list << file
        }
        list.each {
            def dest = new File("src/resources/NewFiles/list.txt")
            def source = it.path
            find(source, dest, "Spyros")
        }

    }

    static def copyFile() {
        String sourceDir = "src/resources/TextFiles"
        String destinationDir = "src/resources/NewFiles"
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
