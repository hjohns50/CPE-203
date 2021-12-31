import java.util.List;
import java.util.*;
import processing.core.PImage;
public class OreBlob extends Motion
{
    private final String QUAKE_KEY = "quake";
    //private PathingStrategy strategy = new SingleStepPathingStrategy();
    private PathingStrategy strategy = new AStarPathingStrategy();
    public OreBlob(
            String id,
            Point position,
            List<PImage> images,
            int actionPeriod,
            int animationPeriod)
    {
        super(id, 
        position, images, 0, actionPeriod, animationPeriod);
    }
    
    public Point nextPositionOreBlob(WorldModel world, Point destPos)
    {
        List<Point> pnt_lst;
        //System.out.println("test");
        pnt_lst = strategy.computePath(this.getPosition(), destPos,
                point -> ( !(point.equals(destPos) || (world.getOccupant(point).isPresent() && !((Ore.class).isInstance(world.getOccupant(point).get())))) && world.withinBounds(point) ),
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
        Point newPos = new Point(this.getPosition().x + horiz, this.getPosition().y);

        Optional<Entity> occupant = world.getOccupant(newPos);

        if (horiz == 0 || (occupant.isPresent() && !(occupant.get().getClass()
                == Ore.class)))
        {
            int vert = Integer.signum(destPos.y - this.getPosition().y);
            newPos = new Point(this.getPosition().x, this.getPosition().y + vert);
            occupant = world.getOccupant(newPos);

            if (vert == 0 || (occupant.isPresent() && !(occupant.get().getClass()
                    == Ore.class)))
            {
                newPos = this.getPosition();
            }
        }

        return newPos;
        */
    }

    
    public boolean moveToOreBlob(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (this.getPosition().adjacent(target.getPosition())) {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else {
            Point nextPos = this.nextPositionOreBlob(world, target.getPosition());

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

    public void executeActivity(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        Optional<Entity> blobTarget =
                world.findNearest(this.getPosition(), Vein.class);
        long nextPeriod = this.getActionPeriod();

        if (blobTarget.isPresent()) {
            Point tgtPos = blobTarget.get().getPosition();

            if (this.moveToOreBlob(world, blobTarget.get(), scheduler)) {
                AnimationEntity quake = Functions.createQuake(tgtPos,
                                           imageStore.getImageList(QUAKE_KEY));

                world.addEntity(quake);
                nextPeriod += this.getActionPeriod();
                quake.scheduleActions(scheduler, world, imageStore);
            }
        }
        scheduler.scheduleEvent(this, this.createActivityAction(world, imageStore), nextPeriod);
    }
}

