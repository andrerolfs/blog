package com.wartbar.util

import org.gradle.api.Project

class HtmlToolbox {

  public static String currentDirString

  public static String currentDir() {
   return currentDirString
  }

  public static List<String> menu() {
    File f = new File(currentDir() + "/blogentry/menu.insert")
    return HtmlToolbox.insertFiles(f)
  }

  public static List<String> standardHeader() {
    File f = new File(currentDir() + "/pageitem/standardHeader.insert")
    return HtmlToolbox.insertFiles(f)
  }

  public static List<String> footer() {
    File f = new File(currentDir() + "/pageitem/footer.insert")
    return HtmlToolbox.insertFiles(f)
  }

  private static List<String> readFile(String line) {
    String pathPrefix = currentDir() + "/" + line.split(":")[1] + "/"
    String fileName = line.split(":")[2]
    File f = new File(pathPrefix + fileName)
    return f.readLines()
  }

  private static List<String> replaceVariables(List<String> inputList, String title) {
    List<String> outList = []
    inputList.each {
      if (it.startsWith("replacevar:title")) {
        outList.add(title)
        return
      }

      outList.add(it)
    }
    return outList
  }

  public static void writeToFile(File outputFile, def inputList) {
    inputList.each {
      outputFile << it << '\n'
    }
  }

  private static List<String> insertVariables(File inputFile) {
    List<String> lines = inputFile.readLines()
    List<String> outputList = []
    lines.each {
      if (it.startsWith("insert:")) {
        outputList.addAll(readFile(it))
        return
      }

      outputList << it
    }
    return outputList
  }

  public static List<String> insertFiles(File inputFile) {
    List<String> inputList = insertVariables(inputFile)
    def resultList = replaceVariables(inputList, "Andre's Blog")
    return resultList
  }
}
