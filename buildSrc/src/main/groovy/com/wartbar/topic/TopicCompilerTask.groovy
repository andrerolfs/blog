package com.wartbar.topic

import org.gradle.api.DefaultTask
import org.gradle.api.file.FileTree
import org.gradle.api.tasks.TaskAction

import com.wartbar.util.HtmlToolbox

class TopicCompilerTask extends DefaultTask {

  String getTopic(String line) {
      return line.replaceAll("\\[", "").replaceAll("\\]","")
  }

  private void deleteCompiledMarkdown() {
    FileTree treeDeleter = project.fileTree(dir: '.', include: HtmlToolbox.currentDir() + '/outputCompiledMD/*Compiled.md', exclude: 'excluded/')
    treeDeleter.each { File file ->
      file.delete()
    }
  }

  @TaskAction
  public void compile() {
    deleteCompiledMarkdown()

    FileTree tree = project.fileTree(dir: '.', include: 'outputMD/*.md', exclude: 'excluded/')
    tree.eachWithIndex { File file, index ->
      println index+1 + ". TopicCompilerTask for " + file.getName()

      File destinationFile = new File(HtmlToolbox.currentDir() + "/outputCompiledMD/" + file.getName().replaceAll(".md","")+"Compiled.md")
      destinationFile.delete()

      def lines = file.readLines()
      lines.each {
        if (it.startsWith("[[")) {
          String topic = getTopic(it)
          destinationFile << '[['
          destinationFile << topic
          destinationFile << ']]('
          destinationFile << 'topic_' + topic.replace(" ", "_")
          destinationFile << '.html)\n'
          return
        }

        destinationFile << it << '\n'
      }
    }
  }
}
