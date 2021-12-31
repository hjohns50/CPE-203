import processing.core.PImage;
import java.util.*;

public abstract class Entity
{
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private String id;
    public Entity(Point p, List<PImage> images, int imageIndex, String id)
    {
        this.position = p;
        this.images = images;
        this.imageIndex = imageIndex;
        this.id = id;
    }
    public Point getPosition()
    {
        return this.position;
    }
    public void setPosition(Point p)
    {
        this.position = p;
    }
    public PImage getCurrentImage()
    {
        return this.images.get(this.imageIndex);
    }
    public List<PImage> getImages()
    {
        return this.images;
    }
    public int getImageIndex()
    {
        return this.imageIndex;
    }
    public void setImageIndex(int a)
    {
        this.imageIndex = a;
    }
    public String getId()
    {
        return this.id;
    }
}


