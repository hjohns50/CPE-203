import java.util.List;
import processing.core.PImage;
public class Ore extends ActivityEntity
{   
    private final String BLOB_KEY = "blob";
    private final String BLOB_ID_SUFFIX = " -- blob";
    private final int BLOB_PERIOD_SCALE = 4;
    private final int BLOB_ANIMATION_MIN = 50;
    private final int BLOB_ANIMATION_MAX = 150;

    public Ore(
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
        Point pos = this.getPosition();

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        AnimationEntity blob = Functions.createOreBlob(this.getId() + BLOB_ID_SUFFIX, 
        pos,
         this.getActionPeriod() / BLOB_PERIOD_SCALE,
         BLOB_ANIMATION_MIN + Functions.rand.nextInt(BLOB_ANIMATION_MAX- BLOB_ANIMATION_MIN),
         imageStore.getImageList(BLOB_KEY));

        world.addEntity(blob);
        blob.scheduleActions(scheduler, world, imageStore);
    }
}
