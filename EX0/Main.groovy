
import java.io.File
import java.io.FileFilter
import java.util.Scanner

class Main {

    static String currentFileName = ''
    static int logicalCounter = 0

    // ──────────────────────────────────────────────
    //  Arithmetic & Logical Command Handlers
    // ──────────────────────────────────────────────

    /**
     * Handles the {@code add} command – integer addition of the top two stack values.
     * @param outputFile destination assembly file
     */
    static void handleAdd(File outputFile) {
        outputFile.append('command: add\n')
    }

    /**
     * Handles the {@code sub} command – integer subtraction (second-from-top minus top).
     * @param outputFile destination assembly file
     */
    static void handleSub(File outputFile) {
        outputFile.append('command: sub\n')
    }

    /**
     * Handles the {@code neg} command – arithmetic negation of the top stack value.
     * @param outputFile destination assembly file
     */
    static void handleNeg(File outputFile) {
        outputFile.append('command: neg\n')
    }

    /**
     * Handles the {@code eq} command – equality comparison.
     * Increments {@link #logicalCounter} to produce a unique label.
     * @param outputFile destination assembly file
     */
    static void handleEq(File outputFile) {
        outputFile.append('command: eq\n')
        logicalCounter++
        outputFile.append("counter: ${logicalCounter}\n")
    }

    /**
     * Handles the {@code gt} command – greater-than comparison.
     * Increments {@link #logicalCounter} to produce a unique label.
     * @param outputFile destination assembly file
     */
    static void handleGt(File outputFile) {
        outputFile.append('command: gt\n')
        logicalCounter++
        outputFile.append("counter: ${logicalCounter}\n")
    }

    /**
     * Handles the {@code lt} command – less-than comparison.
     * Increments {@link #logicalCounter} to produce a unique label.
     * @param outputFile destination assembly file
     */
    static void handleLt(File outputFile) {
        outputFile.append('command: lt\n')
        logicalCounter++
        outputFile.append("counter: ${logicalCounter}\n")
    }

    // ──────────────────────────────────────────────
    //  Memory Access Command Handlers
    // ──────────────────────────────────────────────

    /**
     * Handles the {@code push} command – pushes a value onto the stack from the given memory segment.
     * @param outputFile destination assembly file
     * @param segment    memory segment name (e.g. local, argument, this, that, constant, static, pointer, temp)
     * @param index      offset within the segment
     */
    static void handlePush(File outputFile, String segment, String index) {
        outputFile.append("command: push segment ${segment} index ${index}\n")
    }

    /**
     * Handles the {@code pop} command – pops the top stack value into the given memory segment.
     * @param outputFile destination assembly file
     * @param segment    memory segment name
     * @param index      offset within the segment
     */
    static void handlePop(File outputFile, String segment, String index) {
        outputFile.append("command: pop segment ${segment} index ${index}\n")
    }

    // ──────────────────────────────────────────────
    //  Entry Point
    // ──────────────────────────────────────────────

    /**
     * Application entry point.
     *
     * Prompts the user for a directory path, locates all .vm files within it,
     * and translates each file's commands into a single .asm output file.
     *
     * @param args command-line arguments (unused)
     */
    static void main(String[] args) {
        Scanner scanner = new Scanner(System.in)

        // Prompt the user for the target directory
        print('Please enter the directory path: ')
        String directoryPath = scanner.nextLine()

        File dir = new File(directoryPath)

        if (!dir.exists() || !dir.isDirectory()) {
            println 'Invalid directory path'
            return
        }

        // Derive the output file name from the directory name
        String dirName = dir.name
        String outputFileName = "${dirName}.asm"
        File outputFile = new File(dir, outputFileName)
        outputFile.text = ''  // Clear any previous content

        // Collect all .vm files in the directory
        File[] vmFiles = dir.listFiles({ File f -> f.isFile() && f.name.endsWith('.vm') } as FileFilter)

        if (vmFiles == null) {
            vmFiles = new File[0]
        }

        // Process each .vm file sequentially
        for (File vmFile : vmFiles) {
            logicalCounter = 0
            currentFileName = vmFile.name.replace('.vm', '')

            vmFile.eachLine { line ->
                String trimmed = line.trim()

                // Skip blank lines
                if (trimmed.isEmpty()) {
                    return
                }

                // Tokenize the line and dispatch to the appropriate handler
                String[] words = trimmed.split('\\s+')
                String command = words[0]

                switch (command) {
                    case 'add':
                        handleAdd(outputFile)
                        break
                    case 'sub':
                        handleSub(outputFile)
                        break
                    case 'neg':
                        handleNeg(outputFile)
                        break
                    case 'eq':
                        handleEq(outputFile)
                        break
                    case 'gt':
                        handleGt(outputFile)
                        break
                    case 'lt':
                        handleLt(outputFile)
                        break
                    case 'push':
                        handlePush(outputFile, words[1], words[2])
                        break
                    case 'pop':
                        handlePop(outputFile, words[1], words[2])
                        break
                }
            }

            println "End of input file: ${vmFile.name}"
        }

        println "Output file is ready: ${outputFileName}"
    }

}
