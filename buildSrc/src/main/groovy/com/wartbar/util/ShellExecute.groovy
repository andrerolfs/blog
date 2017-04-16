package com.wartbar.util

class ShellExecute {

    public static int exec(String command) {
        return exec(command, new File(System.properties.'user.dir'))
    }

    public static int exec(String command, File workingDir) {
        println command
        def process = new ProcessBuilder(addShellPrefix(command))
                .directory(workingDir)
                .redirectErrorStream(true)
                .start()
        process.inputStream.eachLine {println it}
        process.waitFor();
        return process.exitValue()
    }

    public static String[] addShellPrefix(String command) {
        def commandArray = new String[3]
        commandArray[0] = "bash"
        commandArray[1] = "-c"
        commandArray[2] = command
        return commandArray
    }
}