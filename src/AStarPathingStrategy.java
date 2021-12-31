import java.util.List;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.HashMap;
import java.util.*;

class AStarPathingStrategy implements PathingStrategy
{
    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        List<Point> path = new LinkedList<Point>(); //path to be returned
        Comparator<Node> order = (n1, n2) -> n1.getF() - n2.getF();
        Comparator<Node> tie = (n1, n2) -> n1.getH() - n2.getH();
        PriorityQueue<Node> queue = new PriorityQueue<>(order.thenComparing(tie)); //quick way to store next nearest
        HashMap<Integer, Node> openList = new HashMap<>(); //nodes we have seen
        HashMap<Integer, Node> closedList = new HashMap<>(); //nodes we have moved from
        Node current = new Node(start, null, calcH(start, end), 0);
        openList.put(current.code, current);
        queue.add(current);
        while(!(withinReach.test(current.getPosition(), end)))
        {
            //gets list of neighbors
            List<Point> neighbors = potentialNeighbors.apply(current.getPosition())
            .filter(canPassThrough)
            .filter(point -> !closedList.containsKey(point.hashCode()))
            .collect(Collectors.toList());
            //loops through list of neighbors
            for(Point p : neighbors)
            {
                //if its not been visited and not in open list
                if((openList.get(p.hashCode()) == null) && (closedList.get(p.hashCode()) == null))
                {
                    //new node added to openList and queue
                    //System.out.print("first if ");
                    Node temp = new Node(p, current, calcH(p, end), current.getG() + 1);
                    openList.put(temp.getPosition().hashCode(), temp);
                    queue.add(temp);
                }
                //in openList checks g value and updates
                else if(!(openList.get(p.hashCode()) == null) && (closedList.get(p.hashCode()) == null))
                {
                    //System.out.print("second if ");
                    Node prior = openList.get(p.hashCode());
                    int tempG = current.getG() + 1;
                    if(tempG < prior.getG())
                    {
                        openList.get(p.hashCode()).setG(tempG);
                        openList.get(p.hashCode()).setPrev(current);
                        Node temp = openList.get(p.hashCode());
                        queue.add(temp);
                    }
                }
            }
            
            //System.out.println(current.getH());
            //System.out.println(current.getG());
            //System.out.println(current.getF());
            //System.out.println();
            if(queue.peek() == null)
            {
                //System.out.print("whats up");
                return path;
            }

            closedList.put(current.getCode(), current);
            current = queue.remove();
            //System.out.println("this is next one" + current.getH());
        }

        while(!(current.getPrev() == null))
        {
            //System.out.print("final while");
            path.add(current.getPosition());
            current = current.getPrev();
        }
        Collections.reverse(path);
        return path;
    }
    public int calcH(Point current, Point end)//finds the manahattan distance
    {
        int xdis = Math.abs(current.x - end.x);
        int ydis = Math.abs(current.y - end.y);
        return xdis + ydis;
    }
    private class Node // stores basic information
    {
        private Point position;
        private int code;
        private Node previousNode;
        private int h;
        private int g;
        private int f;

        private Node(Point p, Node pn, int hval, int gval)
        {
            this.position = p;
            this.code = this.position.hashCode();
            this.previousNode = pn;
            this.h = hval;
            this.g = gval;
            this.f = gval + hval;
        }
        private Point getPosition(){return this.position;}
        private int getH(){return this.h;}
        private int getG(){return this.g;}
        private int getF(){return this.f;}
        private Node getPrev(){return this.previousNode;}
        private void setPrev(Node n){ this.previousNode = n;}
        private int getCode(){return this.code;}
        private void setG(int x)
        {
            this.g = x;
            this.recalcF();
        }
        private void recalcF()
        {
            this.f = this.getG() + this.getH();
        }
        
    }
}