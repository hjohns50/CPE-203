import java.util.List;
import java.util.*;
import processing.core.PImage;
public class Vein extends ActivityEntity
{
    public Vein(
            String id,
            Point position,
            List<PImage> images,
            int actionPeriod)
    {
        super(id, 
        position, images, 0, actionPeriod);
    }

    public void executeActivity(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        Optional<Point> openPt = world.findOpenAround(this.getPosition());

        if (openPt.isPresent()) {
            ActivityEntity ore = Functions.createOre(Functions.ORE_ID_PREFIX + this.getId(), 
            openPt.get(),
            Functions.ORE_CORRUPT_MIN + Functions.rand.nextInt(
            Functions.ORE_CORRUPT_MAX - Functions.ORE_CORRUPT_MIN), imageStore.getImageList(Functions.ORE_KEY));
            world.addEntity(ore);
            ore.scheduleActions(scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(this,
                      this.createActivityAction(world, imageStore),
                      this.getActionPeriod());
    }
}
