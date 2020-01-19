
package mygame.astar;

    public class SearchParameters  {
        public Point StartLocation;
        public Point EndLocation;
        public boolean[][] Map;
        public SearchParameters(Point startLocation, Point endLocation, boolean[][] map)  {
            this.StartLocation = startLocation;
            this.EndLocation = endLocation;
            this.Map = map;
        }
    }
