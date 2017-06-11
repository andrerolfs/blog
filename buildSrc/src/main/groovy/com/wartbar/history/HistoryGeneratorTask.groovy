package com.wartbar.history

import com.wartbar.util.HtmlToolbox
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.file.FileTree

class HistoryGeneratorTask extends DefaultTask {

    List<String> sourceList

    File destinationFile

    String getSubject(String fileName) {
      String input = fileName[0..fileName.size()-4] //remove .md
      return input.replaceAll("_"," ")
    }

    String getLink(String fileName) {
      return '<a href="' + fileName.replace("md", "html") + '">' + getSubject(fileName) + '</a><br/>'
    }

    private void generateOutput() {
      List<String> historyList = new ArrayList<>()

      sourceList.eachWithIndex { it, index ->
        if (!it.startsWith("20")) {
          return
        }

        println index+1 + " generate history for " + it
        historyList.add(getLink(it))
      }

      List<String> content = HtmlToolbox.standardHeader()
      content.addAll(historyList)
      content.addAll(HtmlToolbox.menu())
      content.addAll(HtmlToolbox.footer())

      HtmlToolbox.writeToFile(destinationFile, content)

    }

    private void init() {

      List<String> inputList = project.fileTree(project.outputMD).files.collect { it.name }
      sourceList = inputList.toSorted().reverse()

      String destinationFilePath = HtmlToolbox.currentDir()
      destinationFilePath += "/" + project.outputCompiledMD
      destinationFilePath += "indexCompiled.md"
      println "generateHistory : " + destinationFilePath

      destinationFile = new File(destinationFilePath)
      destinationFile.delete()
      destinationFile.getParentFile().mkdirs()
    }

    @TaskAction
    public void generate() {
        init()
        generateOutput()
    }
}
