package mygame.astar;

    public class Node    {
        public Node parentNode;
        public Point location;
        public boolean IsWalkable;
        public float G;
        public float H;
        public Float F;
        public NodeState State;

        public void actualizarFGH(){
             this.G = (this.parentNode==null)? 0 : this.parentNode.G + 1;     //Reemplazar el coste "1" por el coste de ir desde el padre a "this" nodo.
             this.F = this.G+ this.H;
        }
        public Node(int x, int z, boolean Obstacle, Point endLocation, Point startLocation) {
            this.location = new Point(x, z);
            this.State = NodeState.Untested;
            this.IsWalkable = !Obstacle;
            this.H = GetTraversalCost(this.location, endLocation);  //Se calcula al principio ya que no cambia
        }

        public String ToString() {
            return String.format("{0}, {1}: {2}", this.location.X, this.location.Y, this.State);
        }

        static float GetTraversalCost(Point location, Point otherLocation) {
            float deltaX = otherLocation.X - location.X;
            float deltaZ = otherLocation.Y - location.Y;
            return (float)Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
        }
    }
