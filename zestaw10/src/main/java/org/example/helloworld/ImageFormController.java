package org.example.helloworld;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
@Controller
public class ImageFormController {
    @GetMapping("/")
    public String index() {
        return "index";
    }
    @PostMapping("/upload")
    public String upload(@RequestParam("image") MultipartFile image,
                         @RequestParam("brightness") int brightness,
                         Model model) throws IOException {
        if (!image.isEmpty()) {
            try {
                byte[] bytes = image.getBytes();
                String base64Image = Base64.getEncoder().encodeToString(bytes);
                String adjustedBase64Image = adjustBrightness(base64Image, brightness);
                model.addAttribute("image", adjustedBase64Image);
            } catch (IOException e) {
                e.printStackTrace();
                return "error";
            }
        }
        return "image";
    }
    private String adjustBrightness(String base64Image, int brightness) throws IOException {
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
        BufferedImage brightenedImage = changeBrightness(image, brightness);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(brightenedImage, "jpg", baos);
        byte[] brightenedImageBytes = baos.toByteArray();
        return Base64.getEncoder().encodeToString(brightenedImageBytes);
    }
    private BufferedImage changeBrightness(BufferedImage image, int brightness) {
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color color = new Color(image.getRGB(x, y));
                int r = clamp(color.getRed() + brightness);
                int g = clamp(color.getGreen() + brightness);
                int b = clamp(color.getBlue() + brightness);
                Color newColor = new Color(r, g, b);
                result.setRGB(x, y, newColor.getRGB());
            }
        }
        return result;
    }
    private int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }
}
