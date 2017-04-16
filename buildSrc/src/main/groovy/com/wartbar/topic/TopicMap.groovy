package com.wartbar.topic

class TopicMap {

    def map = [:]

    def accessMap() {
        return map
    }

    String getTopic(String line) {
        return line.replaceAll("\\[", "").replaceAll("\\]","")
    }

    String getPost(String line) {
        String[] parts = line.split(" ")
        String post = ""

        parts[2..parts.length-1].each {
            post += "${it} "
        }

        post = post[0..post.size()-2]

        return post
    }

    void add(String line, String lastPost) {
        String topic = getTopic(line)
        if (map[(topic)] == null) {
            def posts = []
            posts.add(lastPost)
            map.put((topic), posts)
        } else {
            def posts = map[(topic)]
            posts << lastPost
        }
    }

    public processFile(File inputFile) {
      inputFile.readLines().each {
        if (it.startsWith("[[")) {
          add(it,inputFile.name.replace(".md",".html"))
        }
      }
    }

    public TopicMap() {}
}

