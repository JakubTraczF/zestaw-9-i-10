package org.example;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageProcessor {
    private BufferedImage image;
    public void loadImage(String path) throws IOException {
        File file = new File(path);
        this.image = ImageIO.read(file);
    }
    public void saveImage(String path) throws IOException {
        File file = new File(path);
        String format = path.substring(path.lastIndexOf('.') + 1);
        ImageIO.write(this.image, format, file);
    }
    public int[] calculateHistogram(int channel) {
        int[] histogram = new int[256];

        for (int y = 0; y < this.image.getHeight(); y++) {
            for (int x = 0; x < this.image.getWidth(); x++) {
                int rgb = this.image.getRGB(x, y);
                int value = 0;
                switch (channel) {
                    case BufferedImage.TYPE_INT_RGB:
                        value = (rgb >> 16) & 0xFF;
                        break;
                    case BufferedImage.TYPE_INT_ARGB:
                        value = (rgb >> 24) & 0xFF;
                        break;
                    case BufferedImage.TYPE_INT_BGR:
                        value = rgb & 0xFF;
                        break;
                    default:
                        throw new IllegalArgumentException("typ.");
                }
                histogram[value]++;
            }
        }
        return histogram;
    }
    public BufferedImage generateHistogramImage(int[] histogram) {
        int width = 256;
        int height = 300;
        BufferedImage histogramImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = histogramImage.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        int maxValue = 0;
        for (int value : histogram) {
            if (value > maxValue) {
                maxValue = value;
            }
        }
        g.setColor(Color.BLACK);
        double scale = (double) height / maxValue;
        for (int i = 0; i < histogram.length; i++) {
            int barHeight = (int) (histogram[i] * scale);
            g.drawLine(i, height - 1, i, height - 1 - barHeight);
        }
        g.dispose();
        return histogramImage;
    }
    public static void main(String[] args) {
        try {
            ImageProcessor processor = new ImageProcessor();
            String inputImagePath = "input.jpg";
            processor.loadImage(inputImagePath);
            int[] redHistogram = processor.calculateHistogram(BufferedImage.TYPE_INT_RGB);
            BufferedImage histogramImage = processor.generateHistogramImage(redHistogram);
            String outputHistogramPath = "histogram.png";
            ImageIO.write(histogramImage, "PNG", new File(outputHistogramPath));
            System.out.println("Histogram zapisany w: " + outputHistogramPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

