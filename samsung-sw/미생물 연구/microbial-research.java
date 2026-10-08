package Year2023;

import java.io.*;
import java.lang.*;
import java.util.*;

class Virus{
    int r1; //기존꺼에 +1
    int c1; //기존꺼에 +1
    int r2;
    int c2;
    int index;
    boolean dead;
    int width=0;
    
    public Virus(int r1,int c1,int r2,int c2,int index,boolean state) {
        this.r1=r1;
        this.c1=c1;
        this.r2=r2;
        this.c2=c2;
        this.index=index;
        this.dead=state;
    }
}

class Point{
    int r;
    int c;
    public Point(int r,int c) {
        this.r=r;
        this.c=c;
    }
}

public class Misangmul {
    static ArrayList<Virus> vlist = new ArrayList<Virus>();
    static Virus[][] arr;
    static int[] dc= {0,1,0,-1};
    static int[] dr= {-1,0,1,0};
    static int N;
    
    public static void main(String[] args) throws IOException{
        System.setIn(new FileInputStream("input.txt"));
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        N = Integer.parseInt(st.nextToken());
        int Q = Integer.parseInt(st.nextToken());
        
        arr = new Virus[N+1][N+1];
        
        for(int i=0;i<Q;i++) {
            st = new StringTokenizer(br.readLine());
            int r1 = Integer.parseInt(st.nextToken());
            int c1 = Integer.parseInt(st.nextToken());
            int r2 = Integer.parseInt(st.nextToken());
            int c2 = Integer.parseInt(st.nextToken());
            Virus v = new Virus(r1+1,c1+1,r2,c2,i,false);
            
            vlist.add(v);
            
            insert(v);
            move();
            record();
        }
    }
    
    public static void insert(Virus v) {
        for(int i=v.r1;i<=v.r2;i++) {
            for(int j=v.c1;j<=v.c2;j++) {
                arr[i][j]=v;
            }
        }
    }
    
    public static void move() {
        check();
        ArrayList<Point> seq = widthcheckAndSequence(); //진짜 넓이 저장.
        realmove(seq);
        
    }
    
    public static void check() {
        for(Virus v : vlist) {
            if(v.dead==true) {
                continue;
            }else {
                int count=0;
                int bfscount=1;
                
                int[][] visited = new int[N+1][N+1];
                Queue<Point> q =new LinkedList<Point>(); //시작점
                
                for(int i=1;i<=N;i++) {
                    for(int j=1;j<=N;j++) {
                        if(arr[i][j]!=null && arr[i][j].index==v.index) {
                            count++;
                            if(count==1) {
                                q.add(new Point(i,j));
                                visited[i][j]=1;
                            }
                        }
                    }
                }
                
                //bfs
                while(!q.isEmpty()) {
                    Point p =q.poll();
                    int cc = p.c;
                    int cr = p.r;
                    
                    for(int k=0;k<4;k++) {
                        int tr = cr+dr[k];
                        int tc = cc+dc[k];
                        if(tr >=1 && tr<=N && tc >=1 && tc<=N && visited[tr][tc]==0) {
                            if(arr[tr][tc]!=null && arr[tr][tc].index==v.index) {
                                bfscount++;
                                visited[tr][tc]=1;
                                q.add(new Point(tr,tc));
                            }
                        }
                    }
                }
                
                
                
                //System.out.println(v.index+"의 count,bfscount : "+count+","+bfscount);
                if(count==0) {
                    v.dead=true;
                }
                if(bfscount!=count) {
                    v.dead=true;
                }
            }
        }
    }
    
    public static ArrayList<Point> widthcheckAndSequence() {
        ArrayList<Point> tp = new ArrayList<Point>();
        
        for(Virus v : vlist) {
            if(v.dead==true) {
                continue;
            }else {
                int count=0;
                for(int i=1;i<=N;i++) {
                    for(int j=1;j<=N;j++) {
                        if(arr[i][j]!=null && arr[i][j].index==v.index) {
                            count++;
                        }
                    }
                }
                v.width=count;
                
                tp.add(new Point(v.width,v.index));
            }
        }
        
        tp.sort((p1,p2)->{
            if(p1.r != p2.r) {
                return Integer.compare(p2.r, p1.r);
            }
            return Integer.compare(p1.c, p2.c);
        });
        
        return tp;
    }
    
    public static void realmove(ArrayList<Point> seq) {
        
        Virus[][] real = new Virus[N+1][N+1];
        
        for(Point s : seq) {
            int vwidth = s.r;
            int vindex = s.c;
            
            if(vlist.get(vindex).dead==true) { //생존 체크
                continue;
            }
            
            //Virus[][] temp = new Virus[N+1][N+1];
            
            ArrayList<Point> temparr = new ArrayList<Point>();
            ArrayList<Point> movedtemparr = new ArrayList<Point>();
            
            
            int diffr=0;
            int diffc=0;
            for(int i=1;i<=N;i++) {
                for(int j=1;j<=N;j++) {
                    if(arr[i][j]!=null && arr[i][j].index== vindex) {
                        temparr.add(new Point(i,j));
                        
                        if(temparr.size()==1) {
                            diffr = temparr.get(0).r-1;
                            diffc = temparr.get(0).c-1;
                        }
                        
                        movedtemparr.add(new Point(i-diffr,j-diffc));
                    }
                }
            }
            
            int realr=0;
            int realc=0;
            boolean movesuccess=false;
            
            for(int i=0;i<=N-1;i++) {
                for(int j=0;j<=N-1;j++) {
                    movesuccess=true;
                    for(Point p : movedtemparr) {
                        
                        int tr = p.r+i;
                        int tc = p.c+j;
                        
                        if(tr<1 || tr>N || tc<1 || tc>N || real[tr][tc]!=null) {
                            movesuccess = false;
                            break;
                        }
                    }
                    
                    if(movesuccess) {
                        realr=i;
                        realc=j;
                        break;
                    }else {
                        continue;
                    }
                    
                }
                
                if(movesuccess) {
                    break;
                }else {
                    continue;
                }
            }
            
            if(movesuccess) {
                for(Point p : movedtemparr) {
                    int tr = p.r+realr;
                    int tc = p.c+realc;
                    
                    real[tr][tc] = vlist.get(vindex);
                }
            }else {
                 vlist.get(vindex).dead=true;
            }
        }
        
        for(int i=1;i<=N;i++) {
            for(int j=1;j<=N;j++) {
                arr[i][j]=real[i][j];
            }
        }
        
        return;
    }
    
    
    
    
    public static void record() {
        int result=0;
        int[][] cross = new int[vlist.size()][vlist.size()];
        
        for(int r=1;r<=N;r++) {
            for(int c=1;c<=N;c++) {
                if(arr[r][c]==null) {
                    continue;
                }
                for(int k=0;k<4;k++) {
                    int tr = r+dr[k];
                    int tc = c+dc[k];
                    if(tr >=1 && tr<=N && tc >=1 && tc<=N && arr[tr][tc]!=null) {
                        if((arr[tr][tc]!=arr[r][c]) && cross[arr[tr][tc].index][arr[r][c].index]!=1 && cross[arr[r][c].index][arr[tr][tc].index]!=1 ) {
                            result+=arr[tr][tc].width*arr[r][c].width;
                            cross[arr[tr][tc].index][arr[r][c].index]=1;
                            cross[arr[r][c].index][arr[tr][tc].index]=1;
                        }
                    }
                }
            }
        }
        
        System.out.println(result);
        return;
    }
    
}
