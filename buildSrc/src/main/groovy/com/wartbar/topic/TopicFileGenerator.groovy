package com.wartbar.topic

import com.wartbar.util.HtmlToolbox

class TopicFileGenerator {

    public TopicFileGenerator() {}

    void writePost(File f, String post) {
        f << '['
        f << post[0..post.size()-6].replaceAll("_"," ")
        f << ']('
        f << post
        f << ')<br/><br/>\n'
    }

    void create(def map) {
        map.each {
            String filename = "topic_" + it.key.replace(" ","_") + "Compiled"
            File f = new File(HtmlToolbox.currentDir() + "/outputMD/${filename}.md")
            f.delete()

            List<String> header = HtmlToolbox.standardHeader()
            for (String line : header) {
              f << line << '\n'
            }

            def posts = map[(it.key)]
            posts = posts.sort()
            posts[posts.size()-1..0].each {
                writePost(f,it)
            }

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
}
