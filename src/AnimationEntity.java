import processing.core.PImage;
import java.util.*;

public abstract class AnimationEntity extends ActivityEntity
{
    private int animationPeriod;

    public AnimationEntity(String id,
                           Point position,
                           List<PImage> images, 
                           int imageIndex, 
                           int actionPeriod, 
                           int animationPeriod)
    {
        super(id, 
        position, images, imageIndex, actionPeriod);
        this.animationPeriod = animationPeriod;
    }
    public int getAnimationPeriod()
    {
        return this.animationPeriod;
    }
    public Animation createAnimationAction(int repeatCount) {
        return new Animation(this, repeatCount);
    }
    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore)
    {
                scheduler.scheduleEvent(this, this.createActivityAction(world, imageStore), this.getActionPeriod());
                scheduler.scheduleEvent(this, this.createAnimationAction(0), this.getAnimationPeriod());
    }
    public void nextImage() 
    {
        this.setImageIndex((this.getImageIndex() + 1) % this.getImages().size());
    }
}
