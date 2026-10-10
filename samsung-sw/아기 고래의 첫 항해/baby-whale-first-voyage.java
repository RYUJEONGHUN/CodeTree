
import java.util.*;
import java.lang.*;
import java.io.*;

class Whale{
    int r;
    int c;
    int d;
    
    public Whale(int r,int c,int d) {
        this.r=r;
        this.c=c;
        this.d=d;
    }
}

class Point{
    int length;
    int r;
    int c;
    
    public Point(int length,int r,int c) {
        this.length=length;
        this.r=r;
        this.c=c;
    }
    
    public int getlength() {
        return length;
    }
    public int getr() {
        return r;
    }
    public int getc() {
        return c;
    }
}

public class Main {
    static int[] dr = {-100,-1,1,0,0};
    static int[] dc = {-100,0,0,-1,1};
    static int[][] visited;
    static int[][] arr;
    static int N;
    static HashMap<Integer,ArrayList<Integer>> map = new HashMap<Integer,ArrayList<Integer>>();
    public static void main(String[] args) throws IOException {
        
        System.setIn(new FileInputStream("input.txt"));
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        
        N = Integer.parseInt(st.nextToken());
        int r = Integer.parseInt(st.nextToken());
        int c = Integer.parseInt(st.nextToken());
        int d = Integer.parseInt(st.nextToken());
        
        visited = new int[N+1][N+1];
        arr = new int[N+1][N+1];
        
        init();
        
        for(int i=1;i<=N;i++) {
            st = new StringTokenizer(br.readLine());
            for(int j=1;j<=N;j++) {
                int n = Integer.parseInt(st.nextToken());
                arr[i][j]=n;
            }
        }
        
        Queue<Whale> q = new LinkedList<Whale>();
        
        visited[r][c]=1;
        q.add(new Whale(r,c,d));
        
        
        while(!q.isEmpty()) {
            
            Whale nw = q.poll();
            int nd = nw.d;
            
            System.out.println(nw.r +" "+nw.c);
            
            if(allcheck()) {
                break;
            }
            
            ArrayList<Integer> ndlist = map.get(nd);
            
            boolean findnextoceancheck = false;
            
    
            for(int i=1;i<=4;i++) {
                int tr = nw.r+dr[ndlist.get(i)];
                int tc = nw.c+dc[ndlist.get(i)];
                
                if(tr>=1 && tr<=N && tc>=1 && tc<=N && arr[tr][tc]==0 && visited[tr][tc]==0) {
                    visited[tr][tc]=1;
                    q.add(new Whale(tr,tc,ndlist.get(i)));
                    findnextoceancheck=true;
                }
                
                if(findnextoceancheck) {
                    break;
                }
            }

            if(!findnextoceancheck) {
                Whale fw = findnextoceanminimum(nd,nw.r,nw.c,q);
                visited[fw.r][fw.c]=1;
                q.add(new Whale(fw.r,fw.c,fw.d));
            }
            
        }
        
        
    }
    
    public static Whale findnextoceanminimum(int sd,int sr, int sc,Queue<Whale> q) {
        
        ArrayList<Point> plist = new ArrayList<Point>(); //후보리스트
        
        for(int i=1;i<=N;i++) {
            for(int j=1;j<=N;j++) {
                if(arr[i][j]==0 && visited[i][j]==0) {
                    plist.add(new Point(0,i,j));
                }
            }
        }
        
         int[][] minimum = new int[N+1][N+1];
         Queue<Point> temp = new LinkedList<Point>(); //기준점으로 최단거리배열 구하기
         int[][] tempvisited = new int[N+1][N+1];
         
         tempvisited[sr][sc]=1;
         temp.add(new Point(0,sr,sc));
         
         while(!temp.isEmpty()) {
             Point cp = temp.poll();
             
             int d= cp.length;
             minimum[cp.r][cp.c]=d;
             
             for(int i=1;i<=4;i++) {
                 int tr = cp.r+dr[i];
                 int tc = cp.c+dc[i];
                 
                 if(tr>=1 && tr<=N && tc>=1 && tc<=N && arr[tr][tc]==0 && tempvisited[tr][tc]==0) {
                     tempvisited[tr][tc]=1;
                     temp.add(new Point(d+1,tr,tc));
                 }
             }
         }
        
        ArrayList<Point> plengthlist = new ArrayList<Point>(); //후보리스트

        for(Point p : plist) {
            if(minimum[p.r][p.c]>0) {
                plengthlist.add(new Point(minimum[p.r][p.c],p.r,p.c));
            }
        }
        
        plengthlist.sort(
                Comparator.comparing(Point::getlength)
                .thenComparing(Point::getr)
                .thenComparing(Point::getc)
        );
        
        Point finalp = plengthlist.get(0);
        
        //마지막 최단거리 구하기
        Queue<Whale> lastq = new LinkedList<Whale>();
        
        Whale result = null;
        
        int[][] lastqvisited = new int[N+1][N+1];
        
        ArrayList<Integer> lastarr = map.get(10);
        
        lastqvisited[sr][sc]=1;
        lastq.add(new Whale(sr,sc,sd));
        
        
        while(!lastq.isEmpty()) {
            
            Whale w = lastq.poll();
            
            if((w.r==finalp.r) && (w.c == finalp.c)){
                result = new Whale(w.r,w.c,w.d);
                break;
            }
            for(int i=1;i<=4;i++) {
                int tr = w.r+ dr[lastarr.get(i)];
                int tc = w.c+ dc[lastarr.get(i)];
                
                if(tr>=1 && tr<=N && tc>=1 && tc<=N && arr[tr][tc]==0 && lastqvisited[tr][tc]==0) {
                    lastqvisited[tr][tc]=1;
                    lastq.add(new Whale(tr,tc,lastarr.get(i)));
                }
            }
        }
        
        return result;
    }
    
    public static boolean allcheck() {
        boolean check =true;
        for(int i=1;i<=N;i++) {
            for(int j=1;j<=N;j++) {
                if(arr[i][j]==0) {
                    if(visited[i][j]==0) {
                        check = false;
                    }
                }
            }
            if(!check) {
                break;
            }
        }
        
        return check;
    }
    
    public static void init() {
        ArrayList<Integer> firstarr = new ArrayList<Integer>();
        
        firstarr.add(-1);
        firstarr.add(1);
        firstarr.add(3);
        firstarr.add(4);
        firstarr.add(2);

        map.put(1, firstarr);
        
        ArrayList<Integer> secondarr = new ArrayList<Integer>();
        
        secondarr.add(-1);
        secondarr.add(2);
        secondarr.add(4);
        secondarr.add(3);
        secondarr.add(1);

        map.put(2, secondarr);
        
        
        ArrayList<Integer> thirdarr = new ArrayList<Integer>();
        
        thirdarr.add(-1);
        thirdarr.add(3);
        thirdarr.add(2);
        thirdarr.add(1);
        thirdarr.add(4);

        map.put(3, thirdarr);
        
        
        ArrayList<Integer> fourtharr = new ArrayList<Integer>();
        
        fourtharr.add(-1);
        fourtharr.add(4);
        fourtharr.add(1);
        fourtharr.add(2);
        fourtharr.add(3);

        map.put(4, fourtharr);
        
        
        ArrayList<Integer> lastarr = new ArrayList<Integer>();
        
        lastarr.add(-1);
        lastarr.add(3);
        lastarr.add(2);
        lastarr.add(4);
        lastarr.add(1);

        map.put(10, lastarr);
        
    }
}
