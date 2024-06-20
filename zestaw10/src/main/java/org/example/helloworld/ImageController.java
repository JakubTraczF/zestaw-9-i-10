package org.example.helloworld;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
@RestController
@RequestMapping("/image")
public class ImageController {
    @GetMapping("/adjustBrightness")
    public String adjustBrightness(@RequestParam String base64Image, @RequestParam int brightness) throws IOException {
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
        BufferedImage brightenedImage = changeBrightness(image, brightness);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(brightenedImage, "jpg", baos);
        byte[] brightenedImageBytes = baos.toByteArray();
        return Base64.getEncoder().encodeToString(brightenedImageBytes);
    }
    @GetMapping("/adjustBrightnessRaw")
    public ResponseEntity<byte[]> adjustBrightnessRaw(@RequestParam String base64Image, @RequestParam int brightness) throws IOException {
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
        BufferedImage brightenedImage = changeBrightness(image, brightness);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(brightenedImage, "jpg", baos);
        byte[] brightenedImageBytes = baos.toByteArray();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return ResponseEntity.ok().headers(headers).body(brightenedImageBytes);
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
