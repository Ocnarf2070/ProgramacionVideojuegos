package Auxiliares;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
public class Mapa {
	
	private char[][]map;
	private String mov;
        private List<Pair<Integer,Integer>> libreJ1, libreJ2, libres;
        
	public Mapa(File fl) {
            int i = 0,j=0,i2=0,j2=0;
            boolean mapa=false;
            List<Pair<Integer,Integer>> pares = new ArrayList<>();
            int libre=0;
            try(Scanner sc = new Scanner(fl)){
                while(sc.hasNextLine()&&!mapa) {
                    String line=sc.nextLine();
                    //System.out.print(line+"->");
                    try(Scanner ss = new Scanner(line)){
                        while(ss.hasNext()&&!mapa) {
                            String pal=ss.next();
                            //System.out.print(pal+"->");
                            if(pal.equals("type")) {
                                mov=ss.next();
                            } else if(pal.equals("height")) {
                                i=ss.nextInt();
                            } else if(pal.equals("width")) {
                                j=ss.nextInt();
                                map = new char [i][j];
                            }else if(pal.equals("map")) {
                                mapa=true;
                            }
                        }
                    }
                }
                
                
                
                while(sc.hasNextLine()) {
                    String line=sc.nextLine();
                    //System.out.println(line+" "+line.length()+" "+map.length);
                    for(j2=0;j2<line.length();j2++) {
                        char pal=line.charAt(j2);
                        map[i2][j2]=pal;
                        if(pal=='.'){
                            Pair <Integer,Integer> par= new Pair<>(i2,j2);
                            pares.add(par);
                            libre++;
                        }
                    }
                    i2++;
                }
                
            if(!pares.isEmpty()){
                libreJ1 = pares.subList(0, pares.size()/4);
                libres = pares.subList(pares.size()/4+1,pares.size()*3/4);
                libreJ2 = pares.subList(pares.size()*3/4+1, pares.size());
            }
                
                
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                System.out.println("Este mapa no existe");
                System.exit(10);
            }
        }
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		// TODO Auto-generated method stub
		for(int i=0;i<getMap().length;i++) {
			for(int j=0;j<getMap()[0].length;j++) {
				sb.append(getMap()[i][j]);
			}
			sb.append("\n");
		}
		return sb.toString();
	}

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Arrays.deepHashCode(this.map);
        hash = 89 * hash + Objects.hashCode(this.mov);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Mapa other = (Mapa) obj;
        if (!Objects.equals(this.mov, other.mov)) {
            return false;
        }
        if (!Arrays.deepEquals(this.map, other.map)) {
            return false;
        }
        return true;
    }

        
    /**
     * @return the map
     */
    public char[][] getMap() {
        return map;
    }

    /**
     * @param map the map to set
     */
    public void setMap(char[][] map) {
        this.map = map;
    }

    /**
     * @return the mov
     */
    public String getMov() {
        return mov;
    }

    /**
     * @param mov the mov to set
     */
    public void setMov(String mov) {
        this.mov = mov;
    }

    /**
     * @return the val_x
     */
    public int getVal_y() {
        return map.length;
    }

    /**
     * @return the val_y
     */
    public int getVal_x() {
        return map[0].length;
    }
    
    public Vector3f PPosVal(){
        Pair<Integer,Integer> pares = libreJ1.get(0);
        return new Vector3f(pares.getFirst(), 0.5f, pares.getSecond());
    }

    public List<Pair<Integer, Integer>> getLibreJ1() {
        return libreJ1;
    }

    public void setLibreJ1(List<Pair<Integer, Integer>> libreJ1) {
        this.libreJ1 = libreJ1;
    }

    public List<Pair<Integer, Integer>> getLibreJ2() {
        return libreJ2;
    }

    public void setLibreJ2(List<Pair<Integer, Integer>> libreJ2) {
        this.libreJ2 = libreJ2;
    }

    public List<Pair<Integer, Integer>> getLibres() {
        return libres;
    }

    public void setLibres(List<Pair<Integer, Integer>> libres) {
        this.libres = libres;
    }

}
