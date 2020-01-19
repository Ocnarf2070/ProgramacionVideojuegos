package mygame.astar;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class PathFinder  {
        private int width;
        private int height;
        private Node[][] nodes;
        private Node startNode;
        private Node endNode;
        private SearchParameters searchParameters;

        public PathFinder(SearchParameters searchParameters)        {
            this.searchParameters = searchParameters;
            InitializeNodes(searchParameters.Map);
       
            this.startNode = this.nodes[searchParameters.StartLocation.X] [searchParameters.StartLocation.Y];
            this.startNode.State = NodeState.Open;
            this.endNode = this.nodes[searchParameters.EndLocation.X][searchParameters.EndLocation.Y];
        }

       public List<Point> FindPath() {
            // The start node is the first entry in the 'open' list
            ArrayList<Point> path = new ArrayList<Point>();
            boolean success = Search(startNode);
            if (success)   {
                // If a path was found, follow the parents from the end node to build a list of locations
                Node node = this.endNode;
                while (node.parentNode != null)    {
                    path.add(node.location);
                    node = node.parentNode;
                }
                // Reverse the list so it's in the correct order when returned
                Collections.reverse(path);
            }
             return path;
        }

       private void InitializeNodes(boolean[][] map)        {
            this.width = map.length;
            this.height = map[0].length;
            
            this.nodes = new Node[this.width][ this.height];
            for (int z = 0; z < this.height; z++) {
                for (int x = 0; x < this.width; x++)  {
                    this.nodes[x][ z] = new Node(x, z, map[x][z], this.searchParameters.EndLocation, this.searchParameters.StartLocation);
                }
            }
        }

        private boolean Search(Node currentNode)        {
           // Set the current node to Closed since it cannot be traversed more than once
           currentNode.State = NodeState.Closed;
           List<Node> nextNodes = GetAdjacentWalkableNodes(currentNode);

           //Actualiza G,H y F y ordena con respecto a F, para que la rutas más cortas sean consideradas primero
           for (Node nextNode: nextNodes)   nextNode.actualizarFGH();
           Collections.sort(nextNodes,   (Node node1, Node node2) -> node1.F.compareTo(node2.F));

           for (Node nextNode: nextNodes)  {
                System.out.println("Nodo seleccionado=("+nextNode.location.X+","+nextNode.location.Y +")  G="+nextNode.G+"  H="+nextNode.H+" ==>  F="+nextNode.F);
                // Check whether the end node has been reached
                if (nextNode.location == this.endNode.location) {
                    return true;
                } else   {
                    // If not, check the next set of nodes
                    if (Search(nextNode)) // Note: Recurses back into Search(Node)
                        return true;
                }
            }
            // The method returns false if this path leads to be a dead end
            return false;
        }

        private List<Node> GetAdjacentWalkableNodes(Node fromNode)        {
            ArrayList<Node> walkableNodes = new ArrayList<Node>();
            Iterable<Point> nextLocations = GetAdjacentLocations(fromNode.location);

            for (Point location : nextLocations)            {
                int x = location.X;
                int z = location.Y;

                // Stay within the grid's boundaries
                if (x < 0 || x >= this.width || z < 0 || z >= this.height)
                    continue;

                Node node = this.nodes[x][ z];
                // Ignore non-walkable nodes
                if (!node.IsWalkable)
                    continue;

                // Ignore already-closed nodes
                if (node.State == NodeState.Closed)
                    continue;

                // Already-open nodes are only added to the list if their G-value is lower going via this route.
                if (node.State == NodeState.Open) {
                    float traversalCost = Node.GetTraversalCost(node.location, node.parentNode.location);
                    float gTemp = fromNode.G + traversalCost;
                    if (gTemp < node.G)   {
                        node.parentNode = fromNode;
                        walkableNodes.add(node);
                    }
                } else {
                    // If it's untested, set the parent and flag it as 'Open' for consideration
                    node.parentNode = fromNode;
                    node.State = NodeState.Open;
                    walkableNodes.add(node);
                }
            }
            return walkableNodes;
        }

        private static Iterable<Point> GetAdjacentLocations(Point fromLocation)        {
            Point x1= new Point(fromLocation.X-1, fromLocation.Y-1);
            Point x2= new Point(fromLocation.X-1, fromLocation.Y );
            Point x3= new Point(fromLocation.X-1, fromLocation.Y+1);
            Point x4=    new Point(fromLocation.X,   fromLocation.Y+1);
            Point x5=    new Point(fromLocation.X+1, fromLocation.Y+1);
            Point x6=    new Point(fromLocation.X+1, fromLocation.Y  );
            Point x7=    new Point(fromLocation.X+1, fromLocation.Y-1);
            Point x8=     new Point(fromLocation.X,   fromLocation.Y-1);
            ArrayList<Point> X = new ArrayList<Point>();
            X.add(x1);
            X.add(x2);
            X.add(x3);
            X.add(x4);
            X.add(x5);
            X.add(x6);
            X.add(x7);
            X.add(x8);
            Iterable<Point> r = X;
            return X;
        }
    }