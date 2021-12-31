import java.util.List;
import java.util.*;
import processing.core.PImage;
public class MinerFull extends Miners
{
    public MinerFull(
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
    public void executeActivity(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        Optional<Entity> fullTarget =
                world.findNearest(this.getPosition(), BlackSmith.class);

        if (fullTarget.isPresent() && this.moveTo(world, fullTarget.get(), scheduler))
        {
            this.transformFull(world, scheduler, imageStore);
        }
        else {
            scheduler.scheduleEvent(this,
                          this.createActivityAction(world, imageStore),
                          this.getActionPeriod());
        }
    }
    
    
    public void transformFull(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        //System.out.print(this.getId());
        Miners miner = Functions.createMinerNotFull(this.getId(), 
                                          this.getResourceLimit(),
                                          this.getPosition(), this.getActionPeriod(),
                                          this.getAnimationPeriod(),
                                          this.getImages());

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        miner.scheduleActions(scheduler, world, imageStore);
    }
}
