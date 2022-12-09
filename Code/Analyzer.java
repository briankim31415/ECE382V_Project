import java.util.ArrayList;
import java.util.List;

public class Analyzer {
    int bug_count;
    List<String> file_lines;
    List<Integer> bug_lines;

    public Analyzer(List<String> file_lines) {
        this.file_lines = file_lines;
        bug_count = 0;
        bug_lines = new ArrayList<>();
    }

    // Get total number of bugs
    public int getBugCount() {
        checkForLoops();
        return bug_count;
    }

    // Get line numbers for each bug
    public List<Integer> getBugLines() {
        return bug_lines;
    }

    // Check for any try/catch in a loop
    private void checkForLoops() {
        String[] loops = { "for(", "while(", "do{", "for (", "while (", "do {"};

        for (int i = 0; i < file_lines.size(); i++) {
            int potential_bug;
            String line = file_lines.get(i);
            if (stringContains(line, loops)) {
                potential_bug = i + 1;
                boolean in_loop = true;
                boolean start_brace = false;
                boolean is_trycatch = false;
                int brace_count = 0;
                while (in_loop && (line != null)) {
                    brace_count += countMatches(line);
                    if (brace_count != 0) {
                        start_brace = true;
                    }

                    if (line.contains("try")) {
                        is_trycatch = true;
                        if (!start_brace) {
                            in_loop = false;
                            break;
                        }
                    }

                    if (brace_count == 0 && start_brace == true) {
                        in_loop = false;
                    }

                    i++;
                    if (i < file_lines.size()) {
                        line = file_lines.get(i);
                    } else {
                        break;
                    }
                }

                if (is_trycatch) {
                    bug_count++;
                    bug_lines.add(potential_bug);
                }
            }
        }
    }

    // Count number of '{' (+1) and '}' (-1) in a line
    private int countMatches(String line) {
        int count = 0;
        char open = '{';
        char close = '}';
        for (int i = 0; i < line.length(); i++) {
            char cur = line.charAt(i);
            if (cur == open) {
                count++;
            } else if (cur == close) {
                count--;
            }
        }
        return count;
    }

    // Check if string contains a word in a list
    private boolean stringContains(String inputStr, String[] items) {
        for (String string : items) {
            if (inputStr.contains(string)) {
                return true;
            }
        }
        return false;
    }
}