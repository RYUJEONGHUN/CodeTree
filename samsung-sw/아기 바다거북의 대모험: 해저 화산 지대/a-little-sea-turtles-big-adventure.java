import java.util.*;
import java.io.*;
import java.lang.*;


class Tutle{
    int r;
    int c;
    boolean dead;
    boolean exit;
    int exitturn;
    public Tutle(int r,int c,boolean dead,boolean exit) {
        this.r=r;
        this.c=c;
        this.dead=dead;
        this.exit=exit;
    }
}

class Volcation{
    int r;
    int c;
    
    int nowpower; //현재마그마 압력
    int P; //분출 임계치
    
    boolean currentfire = false;
    
    public Volcation(int r,int c,int nowpower,int P) {
        this.r = r;
        this.c = c;
        this.nowpower = nowpower;
        this.P = P;
    }
}

class Point{
    int d;//첫 방향
    int r;
    int c;
    int length;
    public Point(int d,int r,int c,int length) {
        this.d=d;
        this.r=r;
        this.c=c;
        this.length=length;
    }
    public int getd(){
        return d;
    }
    public int getlength() {
        return length;
    }
}



public class Main {
    static int[][] cucumedfirepress;
    static int[][] ocean;
    static int N;
    static int[] dc = {1,0,-1,0};
    static int[] dr = {0,1,0,-1};
    static ArrayList<Tutle> tutlearr = new ArrayList<Tutle>();
    static ArrayList<Volcation> volcationarr = new ArrayList<Volcation>();
    
    public static void main(String[] args) throws IOException{
        
        System.setIn(new FileInputStream("input.txt"));
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());
        int K = Integer.parseInt(st.nextToken());
        
        ocean =  new int[N][N];
        cucumedfirepress =  new int[N][N];
        
        for(int i=0;i<N;i++) {
            st = new StringTokenizer(br.readLine());
            for(int j=0;j<N;j++) {
                 int t = Integer.parseInt(st.nextToken());
                 ocean[i][j] = t;
            }
        }
        
        for(int i=0;i<M;i++) {
            st = new StringTokenizer(br.readLine());
            int r = Integer.parseInt(st.nextToken());
            int c = Integer.parseInt(st.nextToken());
            ocean[r][c] = 2;
            tutlearr.add(new Tutle(r,c,false,false));
        }
        
        for(int i=0;i<K;i++) {
            st = new StringTokenizer(br.readLine());
            int r = Integer.parseInt(st.nextToken());
            int c = Integer.parseInt(st.nextToken());
            int P = Integer.parseInt(st.nextToken());
            
            volcationarr.add(new Volcation(r,c,0,P));
        }

        
        for(int i=1;i<=100;i++) {

            tutlemove(i);
            if(checkAllexit()) {
                break;
            }
            moutainincrease();
            eruption();
            reset();
                    
        }
        
        for(Tutle t : tutlearr) {
            if(t.exit) {
                System.out.println(t.exitturn);
            }else {
                System.out.println(-1);
            }
        }
        //System.out.print(Math.floor(3));
    
    }
    
    public static void tutlemove(int i) {
        for(Tutle t : tutlearr) {
            
            if(t.dead || t.exit) {
                continue;
            }
            
            int findMove = findminimum(t);
            
            if(findMove==-1) {
                continue;
            }else {
                int tr = t.r+dr[findMove];
                int tc = t.c+dc[findMove];

                ocean[t.r][t.c]=0;
                ocean[tr][tc]=2;
                t.r=tr;
                t.c=tc;
                
                if(t.r==N-1 && t.c==N-1) {
                    t.exit=true;
                    t.exitturn=i;
                    ocean[t.r][t.c]=0;
                    //System.out.println(t.exitturn);
                }
            }
            
        }
        
        return;
    }
        
    public static int findminimum(Tutle t) {
        ArrayList<Point> bfssuccesslist = new ArrayList<Point>();
        int[][] visited = new int[N][N];
        Queue<Point> q = new LinkedList<Point>();
        
        for(int i=0;i<4;i++) {
            int tr = t.r+dr[i];
            int tc = t.c+dc[i];
            
            if(tr>=0 && tr<=N-1 && tc>=0 && tc<=N-1 && ocean[tr][tc]==0 && visited[tr][tc]==0) {
                visited[tr][tc]=1;
                q.add(new Point(i,tr,tc,0));
            }
        } // 초기 세팅

        while(!q.isEmpty()) {
            
            Point cn = q.poll();
            
            if(cn.r==N-1 && cn.c == N-1) {
                bfssuccesslist.add(cn);
                continue;
            }
            
            int d = cn.d;
        
            for(int i=0;i<4;i++) {
                int tr = cn.r+dr[i];
                int tc = cn.c+dc[i];
                
                if(tr>=0 && tr<=N-1 && tc>=0 && tc<=N-1 && ocean[tr][tc]==0 && visited[tr][tc]==0) {
                    visited[tr][tc]=1;
                    q.add(new Point(d,tr,tc,cn.length+1));
                }
            }
        }
        
        if(bfssuccesslist.size()==0) {
            return -1;
        }else {
            bfssuccesslist.sort(
                    Comparator.comparing(Point::getlength)
                    .thenComparing(Point::getd)
            );
            
            return bfssuccesslist.get(0).d;
        }
    }
    
    
    public static void checkexit(int i) {
        for(Tutle t : tutlearr) {
            if(t.exit) {
                continue;
            }else {
                
            }
        }
    }
    
    public static boolean checkAllexit() {
        boolean check = true;
        for(Tutle t : tutlearr) {
            if(t.exit==false) {
                check = false;
                break;
            }
        }
        return check;
    }
    
    public static void moutainincrease() {
        for(Volcation v : volcationarr) {
            v.nowpower+=10;
        }
        
    }
    public static void eruption() {
        
        boolean onemorecheck = false;
        
        for(Volcation v : volcationarr) {
            if(v.currentfire== false && (v.nowpower + cucumedfirepress[v.r][v.c]) >= v.P) {
                firego(v);
                v.currentfire=true;
                onemorecheck=true;
            }else {
                continue;
            }    
        }
        
        while(onemorecheck) {
            onemorecheck = onedo();
        }
        
        for(Tutle t : tutlearr) {
            if(t.dead || t.exit) {
                continue;
            }
            
            if(cucumedfirepress[t.r][t.c]>=20) {
                t.dead =true;
                ocean[t.r][t.c]=3;
            }
        }
    }
    
    public static boolean onedo() {
        boolean check = false;
        for(Volcation v : volcationarr) {
            if(v.currentfire== false && (v.nowpower+cucumedfirepress[v.r][v.c]) >= v.P) {
                firego(v);
                v.currentfire=true;
                check=true;
                
            }else {
                continue;
            }    
        }
        return check;
    }
    
    public static void firego(Volcation v) {
        cucumedfirepress[v.r][v.c]+=v.P;
        
        for(int i=0;i<4;i++) {
            int cr=v.r+dr[i];
            int cc=v.c+dc[i];
            if(cr>=0 && cr<=N-1 && cc>=0 && cc<=N-1 && ocean[cr][cc]!=1 && v.P>0) {
                dfs(i,cr,cc,(int)Math.floor(v.P/2));
            }
            
        }
    }
    
    public static void dfs(int i,int r,int c,int damage) {
        cucumedfirepress[r][c]+=damage;
        int cr=r+dr[i];
        int cc=c+dc[i];
        if(cr>=0 && cr<=N-1 && cc>=0 && cc<=N-1 && ocean[cr][cc]!=1 && damage>0) {
            int newdamage = (int)Math.floor(damage/2);
            
            if(newdamage>0) {
                dfs(i,cr,cc,(int)Math.floor(damage/2));

            }
        }
        return;
    }
    
    public static void reset() {
        
        for(Volcation v : volcationarr) {
            if(v.currentfire) {
                v.currentfire=false;
                v.nowpower=0;
            }
        }
        
        for(int i=0;i<=N-1;i++) {
            for(int j=0;j<=N-1;j++) {
                cucumedfirepress[i][j]=0;
            }
        }
    }
}
