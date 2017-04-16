package com.wartbar.rss

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.file.FileTree

import java.text.SimpleDateFormat

class RSSGeneratorTask extends DefaultTask{

    @Input
    List<String> sourceList

    @OutputFile
    File destinationFile


    void writeBeginRss(File f) {
        f << '''<?xml version="1.0" encoding="ISO-8859-1" ?>
<rss version="2.0" xmlns:atom="http://www.w3.org/2005/Atom">
<channel>
<atom:link href="http://www.wartbar.de/rss.xml" rel="self" type="application/rss+xml" />
<title>Wartbar.de RSS-Feed</title>
<link>http://www.wartbar.de</link>
<description>Home of Andre's Blog</description>
<language>de-de</language>
<copyright>2016 by Wartbar.de</copyright>\n'''
    }

    String getSubject(String line) {
        String input = line[9..line.size()-4] //from after date to before .md
        return input.replaceAll("_"," ")
    }

    String getTitle(String line) {
        return "<title>" + getSubject(line) + "</title>\n"
    }

    String getSubjectUrl(String line) {
        String subjectUrl = "http://www.wartbar.de/" + line.replace(".md",".html")
        return subjectUrl.toLowerCase()
    }

    String getLink(String line) {
        return "<link>" + getSubjectUrl(line) + "</link>\n"
    }

    String getGuid(String line) {
        return "<guid>" + getSubjectUrl(line) + "</guid>\n"
    }

    String getDescription(String line) {
        return "<description>A new blog entry with the subject '" +
                getSubject(line) +
                "'</description>\n"
    }

    String getRFC882DateString(String line) {

        String input = line.split("_")[0]

        logger.info input[0..3] + ":" + input[4..5] + ":" + input [6..7]

        int year = Integer.parseInt(input[0..3])
        int month = Integer.parseInt(input[4..5])-1
        int day = Integer.parseInt(input[6..7])

        Calendar cal = Calendar.instance
        cal.set year, month, day, 0, 0, 0
        SimpleDateFormat sdf = new SimpleDateFormat( 'EEE, d MMM yyyy HH:mm:ss Z', Locale.US )
        return sdf.format( cal.getTime() )
    }

    String getPubDate(String line) {
        return "<pubDate>" + getRFC882DateString(line) + "</pubDate>\n"
    }

    void writeItem(File f, String line) {
        f << "<item>\n"
        f << getTitle(line)
        f << getLink(line)
        f << getDescription(line)
        f << getGuid(line)
        f << getPubDate(line)
        f << "</item>\n"
    }

    String writeEndRss(File f) {
        f << "</channel>\n"
        f << "</rss>\n"
    }

    @TaskAction
    public generate() {

        destinationFile.delete()

        writeBeginRss(destinationFile)

        sourceList.eachWithIndex { it, index ->
          if (!it.startsWith("20")) {
            return
          }

          println index+1 + ". genRSS for " + it
          writeItem(destinationFile, it)
        }

        writeEndRss(destinationFile)
    }
}
