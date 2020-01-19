package mygame;



import java.util.List;
import mygame.astar.PathFinder;
import mygame.astar.Point;
import mygame.astar.SearchParameters;

    class Prueba_Aestrella {   
        boolean[][] mapa;
        Point inicio;
        Point objetivo;
        SearchParameters parametrosBusqueda;

        public static void main(String[] args)  {
            Prueba_Aestrella p = new Prueba_Aestrella();
            p.InicialiyaMapa();
            p.ColocaObstaculos();
            PathFinder pathFinder = new PathFinder(p.parametrosBusqueda);

            //Ejecucion del algortimo A*
            List<Point> path = pathFinder.FindPath();

            p.MuestraMapa();
            p.ImprimeRuta(path);
        }

        private void MuestraMapa()  {
            System.out.println("=0==1==2==3==4=");  
            for (int y = this.mapa[0].length - 1; y >= 0; y--)   {
                for (int x = 0; x < this.mapa.length; x++)     {
                    if ((inicio.X == x) && (inicio.Y == y))
                       System.out.print(" S ");
                    else if ((objetivo.X == x) && (objetivo.Y == y))
                       System.out.print(" G ");
                    else if (this.mapa[x][y] == true)
                    System.out.print("XXX");
                    else
                    System.out.print("   ");
                }
                System.out.println("   ");
            }
            System.out.println("=0==1==2==3==4=");      }

        private void ImprimeRuta(List<Point> camino)         {
            for (Point punto : camino)           {
               
            for (int y = this.mapa[0].length - 1; y >= 0; y--)            {
                for (int x = 0; x < this.mapa.length; x++)                {
                    if ((inicio.X == x) && (inicio.Y == y))
                       System.out.print(" S ");
                    else if ((objetivo.X == x) && (objetivo.Y == y))
                       System.out.print(" G ");
                    else if ((punto.X == x) && (punto.Y == y))
                       System.out.print(" n ");
                    else if (this.mapa[x][y] == true)
                    System.out.print("XXX");
                    else
                    System.out.print("   ");
                }
                System.out.println("   ");
            }
            System.out.println("=0==1==2==3==4=");  
            }
        }

        private void InicialiyaMapa()  {
            this.mapa = new boolean[5][5];
            inicio = new Point(0, 0);
            objetivo = new Point(4, 4);
            this.parametrosBusqueda = new SearchParameters(inicio, objetivo, mapa);
        }
/*   =0==1==2==3==4=
               XXX G    
         XXX   XXX      
         XXX            
         XXXXXXXXX      
      S        XXX      
     =0==1==2==3==4=
*/
        private void ColocaObstaculos()
        {
          this.mapa[1][1] = true;
          this.mapa[1][2] = true;
          this.mapa[1][3] = true; 
          this.mapa[2][1] = true;
          this.mapa[3][1] = true;
          this.mapa[3][4] = true;
          this.mapa[3][3] = true;
          this.mapa[3][0] = true;

        }
    }