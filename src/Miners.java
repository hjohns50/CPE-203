import java.util.*;
import processing.core.PImage;
public abstract class Miners extends Motion
{
    private int resourceLimit;
    private int resourceCount;
    //private PathingStrategy strategy = new SingleStepPathingStrategy();
    private PathingStrategy strategy = new AStarPathingStrategy();
    public Miners(String id, 
                  Point position,
                  List<PImage> images,
                  int imageIndex,
                  int actionPeriod,
                  int animationPeriod,
                  int resourceLimit,
                  int resourceCount)
    {
        super(id, 
        position, images, imageIndex, actionPeriod, animationPeriod);
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
    }
    public abstract void executeActivity(EventScheduler scheduler, WorldModel world, ImageStore imageStore);

    public int getResourceCount()
    {
        return this.resourceCount;
    }
    public void setResourceCount(int a)
    {
        this.resourceCount = a;
    }
    public int getResourceLimit()
    {
        return this.resourceLimit;
    }
    public Point nextPositionMiner(WorldModel world, Point destPos)
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
        /*
        int horiz = Integer.signum(destPos.x - this.getPosition().x);
        int vert = Integer.signum(destPos.y - this.getPosition().y);

        Point newPos = new Point(this.getPosition().x + horiz, this.getPosition().y + vert);

        if (world.isOccupied(newPos)) {

            newPos = new Point(this.getPosition().x, this.getPosition().y + vert);

            if (vert == 0 || world.isOccupied(newPos)) {
                newPos = new Point(this.getPosition().x + horiz, this.getPosition().y);
                if (horiz == 0 || world.isOccupied(newPos))
                {
                    newPos = this.getPosition();
                }
            }

        }
        
        return newPos;
        */
    }
    public boolean moveTo(
        WorldModel world,
        Entity target,
        EventScheduler scheduler)
{
    if (this.getPosition().adjacent(target.getPosition())) {
        if(this.getClass().equals(MinerNotFull.class))
        {
            this.setResourceCount(this.getResourceCount() + 1);
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
        }
        
        return true;
    }
    else {
        Point nextPos = this.nextPositionMiner(world, target.getPosition());

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

}
