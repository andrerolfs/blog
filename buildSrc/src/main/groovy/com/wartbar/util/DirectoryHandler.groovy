package com.wartbar.rss

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.file.FileTree

import java.text.SimpleDateFormat

class DirectoryHandler {

    private FileTree tree
    private List<String> linesToCollect

    public DirectoryHandler(String path) {
        tree = fileTree(dir: '.', include: path + "/*.html", exclude: 'excluded/')
    }

    public List<List<String>> collectDescriptions() {
        linesToCollect.collect {
          def entry = []
          entry << description(line)
          entry << url(line)
          return entry
        }
    }

    private String description(String line) {
        return line[8..line.size()-4].replaceAll("_"," ")
    }

    private url(String line) {
      return "http://www.wartbar.de/" + line
    }

    private void collectLines() {
       List<String> lines = tree.collect {
         it.getName()
       }

       linesToCollect = lines.collect {
         if (it.startsWith("20")) {
           return name
         }
       }

       linesToCollect = linesToCollect.toSorted()
    }
}
