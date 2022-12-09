import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.StringReader;

public class ParseFile {
    String path;
    List<String> file_lines;

    public ParseFile(String path) {
        this.path = path;
        file_lines = new ArrayList<>();
    }

    // Get file converted to list
    public List<String> getList() {
        try {
            String content = Files.readString(Paths.get(path));
            createList(content);
        } catch (Exception e) {
            System.out.println("ParseFile.getList() >> Cannot find file");
        }
        return file_lines;
    }

    // Perform file list conversion
    private void createList(String content) {
        String line;
        try {
            boolean in_block_comment = false;
            BufferedReader br = new BufferedReader(new StringReader(content));
            while ((line = br.readLine()) != null) {
                if (in_block_comment) {
                    if (line.contains("*/")) {
                        in_block_comment = false;
                        String[] block_split = line.split(Pattern.quote("*/"));
                        if (block_split.length < 2) {
                            file_lines.add("");
                        } else {
                            file_lines.add(block_split[1].trim());
                        }

                    } else {
                        file_lines.add("");
                    }
                    continue;
                }

                if (line.contains("/*")) {
                    in_block_comment = true;
                    String[] block_split = line.split(Pattern.quote("/*"));
                    if (block_split.length == 0) {
                        file_lines.add("");
                    } else {
                        file_lines.add(block_split[0].trim());
                    }
                } else if (line.contains("//")) {
                    String[] line_split = line.split(Pattern.quote("//"));
                    if (line_split.length == 0) {
                        file_lines.add("");
                    } else {
                        file_lines.add(line_split[0].trim());
                    }
                } else {
                    file_lines.add(line.trim());
                }
            }
            br.close();            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ParseFile.createList() >> Cannot create file list");
        }
    }
}

