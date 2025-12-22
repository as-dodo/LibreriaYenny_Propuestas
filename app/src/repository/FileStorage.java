package repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class FileStorage {
    private static final String BASE_DIR = "files";

    private static Path ensureBaseDir() throws IOException {
        Path dir = Paths.get(System.getProperty("user.dir"), BASE_DIR);
        Files.createDirectories(dir);
        return dir;
    }

    public static String saveFile(String localPath) throws IOException {
        if (localPath == null || localPath.isBlank()) return null;
        Path source = Paths.get(localPath);
        if (!Files.exists(source)) {
            throw new IOException("El archivo no existe: " + localPath);
        }
        Path base = ensureBaseDir();
        String original = source.getFileName().toString();
        String ext = "";
        int dot = original.lastIndexOf('.');
        if (dot >= 0) ext = original.substring(dot);
        String storedName = UUID.randomUUID() + ext;
        Path destination = base.resolve(storedName);
        Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
        return BASE_DIR + "/" + storedName;
    }

    public static Path resolve(String storedPath) throws IOException {
        if (storedPath == null || storedPath.isBlank()) return null;
        Path base = ensureBaseDir();
        Path rel = Paths.get(storedPath);
        if (rel.getNameCount() > 0 && rel.getName(0).toString().equals(BASE_DIR)) {
            rel = rel.subpath(1, rel.getNameCount());
        }
        return base.resolve(rel);
    }

    public static void copyTo(String storedPath, Path destination) throws IOException {
        Path source = resolve(storedPath);
        if (source == null || !Files.exists(source)) {
            throw new IOException("Archivo no encontrado en almacenamiento");
        }
        Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
    }

    public static String getFileName(String storedPath) {
        if (storedPath == null || storedPath.isBlank()) return "";
        Path p = Paths.get(storedPath);
        Path name = p.getFileName();
        return name != null ? name.toString() : storedPath;
    }
}
