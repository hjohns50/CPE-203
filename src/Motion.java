import processing.core.PImage;
import java.util.*;
public abstract class Motion extends AnimationEntity
{
    //private  PathingStrategy strategy = new SingleStepPathingStrategy();
    public Motion(String id,
                  Point position,
                  List<PImage> images,
                  int imageIndex,
                  int actionPeriod,
                  int animationPeriod)
        {
            super(id, 
            position, images, imageIndex, actionPeriod, animationPeriod);
        }


    
    
}
