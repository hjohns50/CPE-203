import java.util.List;
import java.util.*;
import processing.core.PImage;
public class Snowman extends Motion
{
    private PathingStrategy strategy = new AStarPathingStrategy();
    private Ambulance target;
    public Snowman(
            String id,
            Point position,
            List<PImage> images,
            int imageIndex,
            int actionPeriod,
            int animationPeriod)
    {
        super(id, 
        position, images, imageIndex, actionPeriod, animationPeriod);
    }
    public void setTarget(Ambulance a){this.target = a;}
    public void executeActivity(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        //Optional<Entity> fullTarget =
                //world.findNearest(this.getPosition(), Ambulance.class);

        if (//fullTarget.isPresent() && 
            this.moveTo(world, this.target, scheduler) )
        {
            //System.out.println("found target, semding to transform, ");
            this.transform(world, scheduler, imageStore);
        }
        else {
            scheduler.scheduleEvent(this,
                          this.createActivityAction(world, imageStore),
                          this.getActionPeriod());
        }
    }
    public boolean moveTo(
        WorldModel world,
        Entity target,
        EventScheduler scheduler)
    {
    if (this.getPosition().adjacent(target.getPosition())) 
    {
        return true;
    }

    else {
        Point nextPos = this.nextPosition(world, target.getPosition());

        if (!this.getPosition().equals(nextPos)) {
            Optional<Entity> occupant = world.getOccupant(nextPos);
            if (occupant.isPresent()) {
                scheduler.unscheduleAllEvents(occupant.get());
            }

            world.moveEntity(this, nextPos);
        }
        return false;
    }
    }
    
    public void transform(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        //System.out.println("transform 1, ");
        MinerFull miner = Functions.createMinerFull(this.getId(), 2,
                                          this.getPosition(), this.getActionPeriod(),
                                          this.getAnimationPeriod(),
                                          imageStore.images.get("miner"));
        this.target.transform(world, scheduler, imageStore, target);
        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);
        miner.transformFull(world, scheduler, imageStore);
        
    }
    

public Point nextPosition(WorldModel world, Point destPos)
    {
        List<Point> pnt_lst;
        //System.out.println("test");
        pnt_lst = strategy.computePath(this.getPosition(), destPos,
                point -> !(world.isOccupied(point) || point.equals(destPos)) && world.withinBounds(point),
                (p1, p2) -> this.getPosition().neighbor(p1,p2),
                PathingStrategy.CARDINAL_NEIGHBORS);
        //return single point
        //Return original point if null
        if (pnt_lst.isEmpty() )
        {
            return this.getPosition();
        }

        return pnt_lst.get(0);
    }
}
