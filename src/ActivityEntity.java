import processing.core.PImage;
import java.util.*;

public abstract class ActivityEntity extends Entity
{
    private int actionPeriod;

    public ActivityEntity(String id, 
    Point position, List<PImage> images, int imageIndex, int actionPeriod)
    {
        super(position, images, imageIndex, id);
        this.actionPeriod = actionPeriod;
    }
    public abstract void executeActivity(EventScheduler scheduler, WorldModel world, ImageStore imageStore);
    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore)
    {
        scheduler.scheduleEvent(this, this.createActivityAction(world, imageStore), this.actionPeriod);
                
    }
    public Activity createActivityAction(WorldModel world, ImageStore imageStore)
    {
        return new Activity(this, world, imageStore);
    }
    public int getActionPeriod()
    {
        return this.actionPeriod;
    }
}