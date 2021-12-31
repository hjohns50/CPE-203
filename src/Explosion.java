import java.util.List;
import processing.core.PImage;
public class Explosion extends AnimationEntity
{

    public Explosion(
            String id,
            Point position,
            List<PImage> images,
            int actionPeriod,
            int animationPeriod)
    {
        super(id, 
        position, images, 0, actionPeriod, animationPeriod);
    }
    
    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore)
    {
                scheduler.scheduleEvent(this,
                              this.createActivityAction(world, imageStore),
                              this.getActionPeriod());
                scheduler.scheduleEvent(this, this.createAnimationAction(11),
                              this.getAnimationPeriod());
                
    }
    
    
    public void executeActivity(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }
}
