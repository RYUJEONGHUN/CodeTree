
import java.util.*;
import java.lang.*;
import java.io.*;

class Box{
    int k;
    int h; //높이
    int w; //넓이
    
    int c;//좌즉 하단 j    
    int r; //좌측 하단 r     (r,c)
    
    boolean out;
    public Box(int k,int h,int w,int c,boolean out) {
        this.k=k;
        this.h=h;
        this.w=w;
        this.c=c;
        this.out=out;
    }
}

class Pointer{
    int i;
    int j;
    
    public Pointer(int i,int j) {
        this.i=i;
        this.j=j;
    }
}

public class Main {
    static Box[][] arr;
    static int N;
    static ArrayList<Box> blist = new ArrayList<Box>();
    //static HashMap<Integer,Integer> map = new HashMap<Integer,Integer>();
    public static void main(String[] args) throws IOException{
        System.setIn(new FileInputStream("input.txt"));
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        StringTokenizer st =new StringTokenizer(br.readLine());
        
        N =Integer.parseInt(st.nextToken());
        int M =Integer.parseInt(st.nextToken());
                
        arr = new Box[N+1][N+1];
        
        
        for(int i=1;i<=M;i++) {
            st =new StringTokenizer(br.readLine());
            int tk = Integer.parseInt(st.nextToken());
            int th = Integer.parseInt(st.nextToken());
            int tw = Integer.parseInt(st.nextToken());
            int tc = Integer.parseInt(st.nextToken());
            
            blist.add(new Box(tk,th,tw,tc,false));
            insert(blist.get(i-1));
        }
        
        while(true) {
            
            leftAndRemove();
            if(stateAllOut()) {
                break;
            }
            Move();
            
            
            rightAndRemove();
            if(stateAllOut()) {
                break;
            }
            Move();
            
        }
        
        System.out.println();
    }
    
    public static void insert(Box e) {
        ArrayList<Pointer> temparr= startfindbottom(e);
        bottommove(e,temparr);
    }
    
    public static ArrayList<Pointer> startfindbottom(Box e) {
        ArrayList<Pointer> temparr = new ArrayList<Pointer>();
        
        int bh = e.h;
        int bw = e.w;
        int bc = e.c;
        
        for(int j=bc;j<=bc+bw-1;j++) {
            temparr.add(new Pointer(bh,j));
        }
        
        return temparr;
    }
    
    public static void bottommove(Box e,ArrayList<Pointer> plist) {
        int bh = plist.get(0).i;
        boolean find = false;
        int realk=0;
        
        for(int k=0;k<=(N-bh);k++) {
            find = true;
            for(Pointer p : plist) {
                int i = p.i+k;
                int j = p.j;
                
                if(arr[i][j]!=null) {
                    find = false;
                }
            }
            
            if(find) {
                realk=k;
            }else {
                break;
            }
        }
        
        e.r = bh+realk;
        
        makesquare(e);
    }
    
    public static void makesquare(Box e) {
        int h = e.h;
        int c = e.c;
        int r = e.r;
        int w = e.w;
        
        for(int a=r-(h-1);a<=r;a++) {
            for(int b=c;b<=c+w-1;b++) {
                arr[a][b] = e;
            }
        }
    }
    
    public static void deletesquare(Box e) {
        int h = e.h;
        int c = e.c;
        int r = e.r;
        int w = e.w;
        
        for(int a=r-(h-1);a<=r;a++) {
            for(int b=c;b<=c+w-1;b++) {
                arr[a][b] = null;
            }
        }
        e.out=true;
    }
    
    public static void deletesquarewhenmoving(Box e) {
        int h = e.h;
        int c = e.c;
        int r = e.r;
        int w = e.w;
        
        for(int a=r-(h-1);a<=r;a++) {
            for(int b=c;b<=c+w-1;b++) {
                arr[a][b] = null;
            }
        }
    }
    
    public static void leftAndRemove() {
        ArrayList<Box> temparr = new ArrayList<Box>();
        
        for(int i=1;i<=N;i++) {
            boolean check = false;
            for(int j=1;j<=N;j++) {
                if(arr[i][j]!=null) {
                    check=true;
                    if(!temparr.contains(arr[i][j])) {
                        temparr.add(arr[i][j]);
                    }
                    break;
                }
                if(check) {
                    break;
                }
            }
        }
        
        ArrayList<Box> tarr = leftLastCheck(temparr);
        
        tarr.sort((p1,p2) ->{
            return Integer.compare(p1.k,p2.k);
        });
        
        Box e = null;
        
        if(tarr.size()>0) {
            e = tarr.get(0);
            System.out.println(e.k);
        }
        
        deletesquare(e);
        
    }
    
    public static void rightAndRemove() {
        ArrayList<Box> temparr = new ArrayList<Box>();
        
        for(int i=1;i<=N;i++) {
            boolean check = false;
            for(int j=N;j>=1;j--) {
                if(arr[i][j]!=null) {
                    check=true;
                    if(!temparr.contains(arr[i][j])) {
                        temparr.add(arr[i][j]);
                    }
                    break;
                }
                if(check) {
                    break;
                }
            }
        }
        
        ArrayList<Box> tarr = rightLastCheck(temparr);
        
        tarr.sort((p1,p2) ->{
            return Integer.compare(p1.k,p2.k);
        });
        Box e = null;
        
        if(tarr.size()>0) {
            e = tarr.get(0);
            System.out.println(e.k);
        }
        
        deletesquare(e);
    }
    
    public static ArrayList<Box> rightLastCheck(ArrayList<Box> temparr){
        ArrayList<Box> trr = new ArrayList<Box>();
        
        for(Box b : temparr) {
            ArrayList<Pointer> pointlist = new ArrayList<Pointer>();
            
            int tc=b.c+b.w-1;
            int tr=b.r;
            int th=b.h;
            
            for(int a=tr-(th-1);a<=tr;a++) {
                pointlist.add(new Pointer(a,tc));
            }
            
            boolean find = true;
            
            for(int k=1;k<=(N-tc);k++) {
                find = true;
                for(Pointer p : pointlist) {
                    int i = p.i;
                    int j = p.j+k;
                    
                    if(arr[i][j]!=null) {
                        find = false;
                        break;
                    }
                }
                if(find==false) {
                    break;
                }
            }
            if(find) {
                trr.add(b);
            }

        }
        return trr;
    }
    
    public static ArrayList<Box> leftLastCheck(ArrayList<Box> temparr){
        ArrayList<Box> trr = new ArrayList<Box>();
        
        for(Box b : temparr) {
            ArrayList<Pointer> pointlist = new ArrayList<Pointer>();
            
            int tc=b.c;
            int tr=b.r;
            int th=b.h;
            
            for(int a=tr-(th-1);a<=tr;a++) {
                pointlist.add(new Pointer(a,tc));
            }
            
            boolean find = true;
            
            for(int k=1;k<=(tc-1);k++) {
                find = true;
                for(Pointer p : pointlist) {
                    int i = p.i;
                    int j = p.j-k;
                    
                    if(arr[i][j]!=null) {
                        find = false;
                        break;
                    }
                }
                if(find==false) {
                    break;
                }
            }
            
            if(find) {
                trr.add(b);
            }

        }
        return trr;
    }
    
    public static void Move() {
        for(Box e : blist) {
            if(e.out==true) {
                continue;
            }else {
                int tr = e.r;
                int tc = e.c;
                int tw = e.w;
                int th = e.h;
                
                boolean find;
                int realk=0;
                
                for(int k=1;k<=(N-tr);k++) {
                    find = true;
                    for(int j=tc; j<=tc+tw-1;j++) {
                        int ttr=tr+k;
                        int ttc=j;
                        
                        if(arr[ttr][ttc]!=null) {
                            find = false;
                        }
                    }
                    
                    if(find) {
                        realk=k;
                    }else {
                        break;
                    }
                }
                
                if(realk>=1) {
                    deletesquarewhenmoving(e);
                    e.r = tr+realk;
                    makesquare(e);
                }
            }
            
        }
        
    }
        
    public static boolean stateAllOut() {
        boolean result = true;
        
        for(Box b : blist) {
            if(b.out==false) {
                result = false;
                return result;
            }
        }
        return result;
    }
}
