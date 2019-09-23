package healthcare.housing.models;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

public class Image {

    private String newFileName;

    public String getNewFileName() {
        return newFileName;
    }

    public void setNewFileName(String newFileName) {
        this.newFileName = newFileName;
    }

    public Image(MultipartFile uploadPic, String folder) throws IOException {
        Path postingPictureFolder = Paths.get("C:\\Users\\infin\\housing\\src\\main\\resources\\static" + folder);
        if (!Files.exists(postingPictureFolder)){
            Files.createDirectories(postingPictureFolder);
        }
        byte[] bytes = uploadPic.getBytes();
        String newFileName = "";
        String[] splitFileName = uploadPic.getOriginalFilename().split("[.]");
        String originalFileExtension = splitFileName[1];
        String alphabet = "kKm8MV6v1jJZzIiNnY4yTt0GgLlDdBbsSHh7cCXxf3FEeUu9Oo5WwaArRQ2qpP";
        Random rand = new Random();
        for (int i = 0; i < 128; i++){
            newFileName = newFileName + alphabet.charAt(rand.nextInt(alphabet.length()));
        }
        Path path = Paths.get("C:\\Users\\infin\\housing\\src\\main\\resources\\static" + folder + newFileName + "." + originalFileExtension);
        Files.write(path, bytes);
        this.newFileName = (folder + newFileName + "." + originalFileExtension);
    }
}
