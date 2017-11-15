import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Input
{
    private BufferedReader in;

    public Input(String fileName) {
        try {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
        } catch (Exception e) {
            System.err.println("File error!");
            System.exit(1);
        }
    }

    public String readLine() {
        String line = null;

        try {
            line = in.readLine();
        } catch (Exception e) {
            System.err.println("Input error!");
            System.exit(1);
        }

        if(line == null)
            return null;
        else
            return line.toLowerCase().trim();
    }
}
