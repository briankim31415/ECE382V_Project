import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    final static String[] project_list = {
        "ant-master",
        "exoplayer-release-v2",
        "groovy-master",
        "java-design-patterns",
        "jmeter-master",
        "logging-log4j2-release-2.x",
        "lucene-main",
        "pdfbox-trunk",
        "solr-main",
        "spring-framework-main",
        "struts-master",
        "tika-main",
        "tomcat"
    };

    final static HashMap<String, Integer> track_bugs = initHash();
    
    // Get relevant paths
    final static String cur_path = (Paths.get("")).toAbsolutePath().toString();
    final static String input_path = parentDir(cur_path) + "\\Input";
    final static String output_path = parentDir(cur_path) + "\\Output\\Output.txt";
    final static String stats_path = parentDir(cur_path) + "\\Output\\Stats.txt";
    final static String file_extension = "java";

    public static void main(String args[]) {
        // Get list of files
        List<String> files = new ArrayList<>();
        try {
            GetFiles gf = new GetFiles();
            files = gf.findFiles(Paths.get(input_path), file_extension);
        } catch (IOException e) {
            System.out.println("Main >> Cannot get files");
        }


        // Find bugs in files and output to file
        int LOC = 0;
        int total_bugs = 0;
        try {
            FileWriter fw = new FileWriter(output_path);

            for (String string : files) {
                ParseFile pf = new ParseFile(string);
                List<String> file_lines = pf.getList();
                LOC += file_lines.size();
                Analyzer an = new Analyzer(file_lines);
                
                if (an.getBugCount() > 0) {
                    List<Integer> bug_list = an.getBugLines();
                    fw.write(string.substring(input_path.length()));
                    for (int bug : bug_list) {
                        fw.write("\n\t" + bug);
                        total_bugs++;
                    }
                    addBugsHash(string, bug_list.size());
                    fw.write("\n");
                }
            }

            fw.close();
        } catch (Exception e) {
            System.out.println("Main.main() >> Cannot create output file");
            e.printStackTrace();
        }

        // Output stats to file
        try {
            FileWriter fw = new FileWriter(stats_path);
            fw.write("Found " + total_bugs + " bugs across " + LOC + " lines of code");
            for (Map.Entry<String,Integer> entry : track_bugs.entrySet()) {
                fw.write("\n" + entry.getKey() + " >> " + entry.getValue() + " bugs");
            }
            fw.close();
        } catch (Exception e) {
            System.out.println("Main.main() >> Cannot create stats file");
        }

        System.out.println("Finished");
    }

    // Get parent directory path
    private static String parentDir(String path) {
        int i = path.lastIndexOf("\\");
        return path.substring(0, i);
    }

    // Init bugs hashmap
    private static HashMap<String, Integer> initHash() {
        HashMap<String, Integer> map = new HashMap<>();
        for (String string : project_list) {
            map.put(string, 0);
        }
        return map;
    }

    // Increment bugs hash count
    private static void addBugsHash(String path, int count) {
        String project = path.substring(input_path.length() + 1);
        project = project.substring(0, project.indexOf("\\")).toLowerCase();
        // System.out.println(project);
        track_bugs.put(project, track_bugs.get(project) + count);
    }
}