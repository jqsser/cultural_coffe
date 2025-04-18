package tools;

import java.nio.file.Path;
import java.nio.file.Paths;

public class config {
    // Chemin relatif pour le stockage des images (sera créé dans le répertoire du projet)
    public static final String IMAGE_UPLOAD_DIR = "uploads/images/";

    // Chemin absolu vers le dossier d'upload
    public static final Path UPLOAD_DIR_PATH = Paths.get(IMAGE_UPLOAD_DIR).toAbsolutePath();

    // Chemin relatif aux ressources pour les images par défaut
    public static final String DEFAULT_IMAGE_PATH = "/images/default-product.png";

    // Méthode pour extraire l'extension d'un fichier
    public static String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? "" : filename.substring(dotIndex).toLowerCase();
    }

    // Méthode pour vérifier si un chemin est une URL web
    public static boolean isWebUrl(String path) {
        return path != null && (path.startsWith("http://") || path.startsWith("https://"));
    }
}