
import java.util.*;
import java.io.*;
import java.lang.*;

class Student {
    String rep;
    int r;
    int c;
    int truth;
    
    public Student(String rep,int r,int c,int truth) {
        this.rep=rep;
        this.r=r;
        this.c=c;
        this.truth=truth;
    }
    
    public int getr() {
        return r;
    }
    
    public int getc() {
        return c;
    }
    
    public int gettruth() {
        return truth;
    }
}



public class Main {
    static int[][] arr; //신앙심
    static int[][] visited;
    static int N;
    static String[][] sarr; //초기 대표문자
    static Student[][] studentarr; 
    static int[] dr = {-1,1,0,0};
    static int[] dc = {0,0,-1,1};
    
    public static void main(String[] args)throws IOException{
        
        System.setIn(new FileInputStream("input.txt"));
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        N = Integer.parseInt(st.nextToken());
        int T = Integer.parseInt(st.nextToken());
        
        sarr = new String[N+1][N+1];
        
        for(int r=1;r<=N;r++) {
            st = new StringTokenizer(br.readLine());
            String s =st.nextToken();
            for(int c=1;c<=N;c++) {
                sarr[r][c] = Character.toString(s.charAt(c-1));            
            }
        }
        
        arr = new int[N+1][N+1];
        
        for(int r=1;r<=N;r++) {
            st = new StringTokenizer(br.readLine());
            for(int c=1;c<=N;c++) {
                arr[r][c] = Integer.parseInt(st.nextToken());
            }
        }
        
        studentarr = new Student[N+1][N+1];
        
        for(int r=1;r<=N;r++) {
            for(int c=1;c<=N;c++) {
                studentarr[r][c] = new Student(sarr[r][c],r,c,arr[r][c]);
            }
        }
        
        
        for(int i=0;i<T;i++) {
            moring();
            ArrayList<Student> replist=lunch();
            replist.sort(
                    Comparator.comparing(Student::gettruth,Comparator.reverseOrder())
                    .thenComparing(Student::getr)
                    .thenComparing(Student::getc)
            );
            
            dinner(replist);
            record();
        }
            
        
    }
    
    public static void moring() {
        for(int i=1;i<=N;i++) {
            for(int j=1;j<=N;j++) {
                studentarr[i][j].truth=studentarr[i][j].truth+1;
            }
        }
    }
    
    public static ArrayList<Student> lunch() {
        ArrayList<Student> replist = new ArrayList<Student>();
        visited = new int[N+1][N+1];
        
        for(int i=1;i<=N;i++) {
            for(int j=1;j<=N;j++) {
                if(visited[i][j]==0) {
                    Student e = studentarr[i][j];
                    
                    Queue<Student> q =new LinkedList<Student>();
                    
                    visited[i][j]=1;
                    
                    q.add(e);
                    
                    Student rep= searchList(e.rep,q,visited);
                    replist.add(rep);
                }
            }
        }
        return replist;
    }
    
    public static Student searchList(String repchar,Queue<Student> q,int[][] visited) {
        ArrayList<Student> plist = new ArrayList<Student>();
        
        while(!q.isEmpty()) {
            Student s = q.poll();
            plist.add(s);
            
            for(int i=0;i<4;i++) {
                int tr = s.r+dr[i];
                int tc = s.c+dc[i];
                
                if(tr>=1 && tr<=N && tc>=1 && tc<=N && visited[tr][tc]!=1 && studentarr[tr][tc].rep.equals(repchar)) {
                    visited[tr][tc]=1;
                    q.add(studentarr[tr][tc]);
                }
            }
            
        }
        
        plist.sort(
                Comparator.comparing(Student::gettruth,Comparator.reverseOrder())
                .thenComparing(Student::getr)
                .thenComparing(Student::getc)
        );
        
        Student rep = plist.get(0);
        
        rep.truth = rep.truth+plist.size()-1;
        
        for(int i=1;i<plist.size();i++) {
            Student e = plist.get(i);
            e.truth-=1;
        }
        return rep;
    }
    
    public static void dinner(ArrayList<Student> replist) {
        
        ArrayList<Student> sortedlist = realsort(replist);
        
        boolean[][] protectedarr = new boolean[N+1][N+1];
        
        for(Student s : sortedlist) {
            String rep=s.rep;
            int r=s.r;
            int c=s.c;
            if(protectedarr[r][c]) {
                continue;
            }
            int truth=s.truth;
            
            int d = truth%4;
            int gansul = truth-1;
            s.truth=1;
            
            start(protectedarr,s,d,gansul);
        }
        
    }
    public static void start(boolean[][] protectedarr,Student s,int d,int gansul){
        
        int r=s.r;
        int c=s.c;
        
        
        while(true) {
            r=r+dr[d];
            c=c+dc[d];
            if((r>=1 && r<=N && c>=1 && c<=N) && gansul >0) {
                if(studentarr[r][c].rep.equals(s.rep)) {
                    continue;
                }else{
                    if(gansul > studentarr[r][c].truth) { //강한 전파
                        studentarr[r][c].rep=s.rep;
                        gansul-=(studentarr[r][c].truth+1); 
                        studentarr[r][c].truth+=1;
                        protectedarr[r][c]=true;
                        
                        if(gansul==0) {
                            break;
                        }
                    }else {//약한 전파
                        last(s,studentarr[r][c]);
                        studentarr[r][c].truth += gansul;
                        gansul=0;
                        protectedarr[r][c]=true;
                    }
                }
            }else {
                break;
            }
            
        }
        
    }
    public static void last(Student o,Student r) {
        ArrayList<String> olist =new ArrayList<String>();
        
        for(int i=0;i<o.rep.length();i++) {
            olist.add(Character.toString(o.rep.charAt(i)));    
        }
        
        for(int i=0;i<r.rep.length();i++) {
            if(olist.contains(Character.toString(r.rep.charAt(i)))) {
                continue;
            }else {
                olist.add(Character.toString(r.rep.charAt(i)));    
            }
        }
        
        String result ="";
        
        if(olist.size()==1) {
            if(olist.contains("T")) {
                result="T";
            }else if(olist.contains("C")) {
                result="C";
            }else if(olist.contains("M")) {
                result="M";
            }
        }else if(olist.size()==2) {
            if(olist.contains("T") && olist.contains("C")) {
                result="TC";
            }else if(olist.contains("C") && olist.contains("M")) {
                result="CM";
            }else if(olist.contains("M") && olist.contains("T")) {
                result="TM";
            }
        }else if(olist.size()==3) {
            result="TCM";
        }
        
        r.rep=result;
        
        return;
    }
    
    public static ArrayList<Student> realsort(ArrayList<Student> replist) {
        
        
        ArrayList<Student> sortedlist = new ArrayList<Student>();
        int count=0;
        
        while(true) {
            if(count==3) {
                break;
            }
            
            for (Student s : replist) {
                if(count==0) {
                    if(s.rep.length()==1) {
                        sortedlist.add(s);
                    }
                }
                if(count==1) {
                    if(s.rep.length()==2) {
                        sortedlist.add(s);
                    }
                }
                if(count==2) {
                    if(s.rep.length()==3) {
                        sortedlist.add(s);
                    }
                }
                
            }
            count++;
        }
        
        return sortedlist;
        
    }
    public static void record() {
        int ctmcount=0;
        int cmcount=0;
        int tmcount=0;
        int tccount=0;
        int tcount=0;
        int mcount=0;
        int ccount=0;
        
        for(int i=1;i<=N;i++) {
            for(int j=1;j<=N;j++) {
                String temp = studentarr[i][j].rep;
                
                if(temp.equals("TCM")) {
                    ctmcount+=studentarr[i][j].truth;
                }
                if(temp.equals("CM")) {
                    cmcount+=studentarr[i][j].truth;
                }
                if(temp.equals("TM")) {
                    tmcount+=studentarr[i][j].truth;
                }
                if(temp.equals("TC")) {
                    tccount+=studentarr[i][j].truth;
                }
                if(temp.equals("T")) {
                    tcount+=studentarr[i][j].truth;
                }
                if(temp.equals("M")) {
                    mcount+=studentarr[i][j].truth;
                }
                if(temp.equals("C")) {
                    ccount+=studentarr[i][j].truth;
                }
            }
        }
        
        System.out.println(ctmcount+" "+tccount+" "+tmcount+" "+cmcount+" "+mcount+" "+ccount+" "+tcount);
    }

}
