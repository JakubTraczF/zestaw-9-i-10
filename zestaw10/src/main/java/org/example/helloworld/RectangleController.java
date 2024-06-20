package org.example.helloworld;

import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/rectangle")
public class RectangleController {
    private List<Rectangle> rectangles = new ArrayList<>();
    public RectangleController() {
        addRectangle(new Rectangle(10, 20, 30, 40, "blue"));
        addRectangle(new Rectangle(50, 60, 70, 80, "red"));
    }
    @PostMapping("/add")
    public void addRectangle(@RequestBody Rectangle rectangle) {
        rectangles.add(rectangle);
    }
    @GetMapping("/all")
    public List<Rectangle> getAllRectangles() {
        return rectangles;
    }
    @GetMapping("/{index}")
    public Rectangle getRectangleByIndex(@PathVariable int index) {
        if (index < 0 || index >= rectangles.size()) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
        return rectangles.get(index);
    }
    @PutMapping("/{index}")
    public void updateRectangle(@PathVariable int index, @RequestBody Rectangle rectangle) {
        if (index < 0 || index >= rectangles.size()) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
        rectangles.set(index, rectangle);
    }
    @DeleteMapping("/{index}")
    public void deleteRectangle(@PathVariable int index) {
        if (index < 0 || index >= rectangles.size()) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
        rectangles.remove(index);
    }
    @GetMapping("/svg")
    public String generateSVG() {
        StringBuilder svg = new StringBuilder();
        svg.append("<svg width=\"500\" height=\"500\" xmlns=\"http://www.w3.org/2000/svg\">");
        for (Rectangle rect : rectangles) {
            svg.append("<rect x=\"")
                    .append(rect.getX())
                    .append("\" y=\"")
                    .append(rect.getY())
                    .append("\" width=\"")
                    .append(rect.getWidth())
                    .append("\" height=\"")
                    .append(rect.getHeight())
                    .append("\" fill=\"")
                    .append(rect.getColor())
                    .append("\" />");
        }
        svg.append("</svg>");
        return svg.toString();
    }
    @GetMapping
    public Rectangle getRectangle() {
        return new Rectangle(10, 20, 30, 40, "blue");
    }
}
