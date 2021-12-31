import java.util.List;
import java.util.*;
import processing.core.PImage;
public class MinerNotFull extends Miners
{
    public MinerNotFull(
            String id,
            Point position,
            List<PImage> images,
            int imageIndex,
            int actionPeriod,
            int animationPeriod,
            int resourceLimit,
            int resourceCount)
    {
        super(id, 
        position, images, imageIndex, actionPeriod, animationPeriod, resourceLimit, resourceCount);
    }
    
    public boolean transformNotFull(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        if (this.getResourceCount() >= this.getResourceLimit()) {
            Miners miner = Functions.createMinerFull(this.getId(), 
                                        this.getResourceLimit(),
                                           this.getPosition(), this.getActionPeriod(),
                                           this.getAnimationPeriod(),
                                           this.getImages());

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            miner.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }
    
    public void executeActivity(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        Optional<Entity> notFullTarget =
                world.findNearest(this.getPosition(), Ore.class);

        if (!notFullTarget.isPresent() || !this.moveTo(world,
                notFullTarget.get(),
                scheduler)
                || !this.transformNotFull(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    this.createActivityAction(world, imageStore),
                    this.getActionPeriod());
        }
    }
    
}
