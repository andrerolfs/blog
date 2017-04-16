package com.wartbar.topic

import com.wartbar.util.HtmlToolbox

class TopicMenuGenerator {

    public TopicMenuGenerator() {}

    void writeData(File f, String x) {
        f << '<td>'

        if (!x.isEmpty()) {
            f << '<a class="paddedTable" href="topic_'
            f << x.replace(" ","_")
            f << '.html">'
            f << x
            f << '</a>'
        }

        f << '</td>\n'
    }

    void writeTopicRow(File f, String a, String b, String c, String d) {
        f << '<tr>\n'

        writeData(f,a)
        writeData(f,b)
        writeData(f,c)
        writeData(f,d)

        f << '</tr>\n'
    }

    String element(ArrayList<String> list, int index) {
        if (index < list.size()) {
            return list.get(index)
        } else {
            return ""
        }
    }
/*
    void writeTopicListHeader(File f, String topic) {

        f << '''<!DOCTYPE html>
<html style="background-color:black;text-align:center;">
<head>
<link rel="stylesheet" type="text/css" href="format.css">
<link rel="alternate" type="application/rss+xml" title="RSS" href="http://www.wartbar.de/rss.xml">
</head>
<br>
<h1>'''

        f << topic

        f << '''</h1>
<br>
<a href="menu.html"><img src="pic/menu.jpg" width="50" ></a>
<br>
<br>
<section id="section">'''
    }
*/

    void create(def map) {
        File f = new File(HtmlToolbox.currentDir() + "/outputCompiledMD/topicsCompiled.md")
        f.delete()

        // "Andre's Blog Post Topics"
        List<String> header = HtmlToolbox.standardHeader()
        for (String line : header) {
          f << line << '\n'
        }

        ArrayList<String> orderedList = new ArrayList<>()
        orderedList.addAll(map.keySet())
        Collections.sort(orderedList)

        println "Topic Count : " + orderedList.size()

        f << '<table class="paddedTable" align="center">\n'


        int divider = (orderedList.size() / 4) + 1

        for (int i=0; i<divider; i++) {
            String a = element(orderedList, i)
            String b = element(orderedList, i + divider)
            String c = element(orderedList, i + (2*divider))
            String d = element(orderedList, i + (3*divider))

            writeTopicRow(f, a, b, c, d )
        }

        f << "</table>\n"

        List<String> menu = HtmlToolbox.menu()
        for (String line : menu) {
          f << line << '\n'
        }

        List<String> footer = HtmlToolbox.footer()
        for (String line : footer) {
          f << line << '\n'
        }
    }
}
