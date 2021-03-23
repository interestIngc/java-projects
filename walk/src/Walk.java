
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Walk {
    public static void main(String[] args) {

    }
    private static long get_hash(InputStream reader) throws IOException {
        long h = 0, high;
        int c;
        while ((c = reader.read()) >= 0) {
            h = (h << 8) + c;
            high = h & 0xff00000000000000L;
            if (high != 0L) {
                h = h ^ (high >> 48);
                h = h & ~high;
            }
        }
        return h;
    }
    private static long calc(Path path) {
        long hash_sum;
        try (InputStream reader = Files.newInputStream(path)) {
            hash_sum = get_hash(reader);
        } catch (IOException e) {
            System.err.println("invalid path");
            return 0L;
        }
        return hash_sum;
    }
}
