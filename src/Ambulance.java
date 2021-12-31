import java.util.List;
import java.util.*;
import processing.core.PImage;

public class Ambulance extends Motion
{
    private PathingStrategy strategy = new AStarPathingStrategy();
    private Snowman target;
    public Ambulance(
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
    public void setTarget(Snowman s){this.target = s;}
    public void executeActivity(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        //Optional<Entity> fullTarget =
                //world.findNearest(this.getPosition(), Snowman.class);

        if (//fullTarget.isPresent() && 
            this.moveTo(world, this.target, scheduler))
        {
            //this.transform(world, scheduler, imageStore, fullTarget.get());
            
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
            ImageStore imageStore, Entity target)
    {
        
        Explosion e1 = Functions.createExplosion(this.getId(),
                                          this.getPosition(), imageStore.images.get("explosion"),
                                          200, 20);
        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);
        world.addEntity(e1);
        e1.scheduleActions(scheduler, world, imageStore);
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
