import java.awt.Point;
import java.util.LinkedList;

public class HexahedronGrid{
  private Hexahedron[][] field;
  private int hexWidth;
  private int hexHeight;
  private int hexRadius;
  private int hexSideThick;
  private int normal;
  private int fieldWidth;
  private int fieldHeight;
  private boolean extinction = true;
  private double LIVE_BEGIN = 2.0;
  private double BIRTH_BEGIN = 2.3;
  private double BIRTH_END = 2.9;
  private double LIVE_END = 3.3;
  private double FST_IMPACT = 1.0;
  private double SND_IMPACT = 0.3;

  public HexahedronGrid(int N, int M, int hexRadius, int sideThick){
    this.hexWidth = M;
    this.hexHeight = N;
    this.hexRadius = hexRadius;
    this.hexSideThick = sideThick;
    this.fieldWidth = (20 + this.hexRadius * M + 25) * 3 / 2;
    this.normal = (int)(this.hexRadius * Math.sqrt(3)/2);
    this.fieldHeight = (20 + this.normal * N + 20) * 2;
    this.field = new Hexahedron[N][M];

    int R = hexRadius;
    int X = R + 20;
    int tx = X;
    int yl = this.normal;
    int Y = yl - 1 + 20;
    int ty = Y;
    int xl = R + R / 2;

    for(int i = 0; i < N; i++){
      for(int j = 0; j < M; j++){
        field[i][j] = new Hexahedron();
        field[i][j].construct(X, Y, R, this.hexSideThick);
        X += xl;
        Y = j % 2 == 0 ? Y + yl : Y - yl;
      }
      X = tx;
      Y = ty + yl * 2;
      ty = Y;
    }

  }

  public Hexahedron[][] getField(){
    return this.field;
  }

  public int getWidth(){
    return this.fieldWidth;
  }

  public void changeWidth(int w){
    Hexahedron[][] nf = new Hexahedron[this.hexHeight][w];
    this.fieldWidth = (20 + this.hexRadius * w + 25) * 3 / 2;

    int R = hexRadius;
    int X = R + 20;
    int tx = X;
    int yl = this.normal;
    int Y = yl - 1 + 20;
    int ty = Y;
    int xl = R + R / 2;

    for(int i = 0; i < this.hexHeight; i++){
      for(int j = 0; j < w; j++){
        if(j < this.hexWidth){
          nf[i][j] = field[i][j];
        }
        else{
          nf[i][j] = new Hexahedron();
          nf[i][j].construct(X, Y, R, this.hexSideThick);
        }
        X += xl;
        Y = j % 2 == 0 ? Y + yl : Y - yl;
      }
      X = tx;
      Y = ty + yl * 2;
      ty = Y;
    }
    this.field = nf;
    this.hexWidth = w;
  }

  public int getHeight(){
    return this.fieldHeight;
  }

  public void changeHeight(int h){
    Hexahedron[][] nf = new Hexahedron[h][this.hexWidth];
    this.normal = (int)(this.hexRadius * Math.sqrt(3)/2);
    this.fieldHeight = (20 + this.normal * h + 20) * 2;

    int R = hexRadius;
    int X = R + 20;
    int tx = X;
    int yl = this.normal;
    int Y = yl - 1 + 20;
    int ty = Y;
    int xl = R + R / 2;

    for(int i = 0; i < h; i++){
      for(int j = 0; j < this.hexWidth; j++){
        if(i < this.hexHeight){
          nf[i][j] = field[i][j];
        }
        else{
          nf[i][j] = new Hexahedron();
          nf[i][j].construct(X, Y, R, this.hexSideThick);
        }
        X += xl;
        Y = j % 2 == 0 ? Y + yl : Y - yl;
      }
      X = tx;
      Y = ty + yl * 2;
      ty = Y;
    }
    this.field = nf;
    this.hexHeight = h;
  }

  public int getHexWidth(){
    return this.hexWidth;
  }

  public int getHexHeight(){
    return this.hexHeight;
  }

  public int getHexRadius(){
    return this.hexRadius;
  }

  public void setRadius(int r){
    this.hexRadius = r;
    int M = this.hexWidth;
    int N = this.hexHeight;
    int R = this.hexRadius;
    this.fieldWidth = (20 + this.hexRadius * M + 25) * 3 / 2;
    this.normal = (int)(this.hexRadius * Math.sqrt(3)/2);
    this.fieldHeight = (20 + this.normal * N + 20) * 2;
    int X = R + 20;
    int tx = X;
    int yl = this.normal;
    int Y = yl - 1 + 20;
    int ty = Y;
    int xl = R + R / 2;
    for(int i = 0; i < N; i++){
      for(int j = 0; j < M; j++){
        field[i][j].construct(X, Y, R, this.hexSideThick);
        X += xl;
        Y = j % 2 == 0 ? Y + yl : Y - yl;
      }
      X = tx;
      Y = ty + yl * 2;
      ty = Y;
    }
  }

  public int getHexSideThick(){
    return this.hexSideThick;
  }

  public void setSideThickness(int s){
    this.hexSideThick = s;
  }

  public void recountImpact(){
    double impact = 0.0;
    int fst_count = 0;
    int snd_count = 0;
    int step;
    for(int i = 0; i < hexHeight; i++){
      for(int j = 0; j < hexWidth; j++){

        if(i > 0) if(field[i - 1][j].isAlive()) {++fst_count;}
        if(i < hexHeight - 1) if(field[i + 1][j].isAlive()) {++fst_count;}
        if(j < hexWidth - 2) if(field[i][j + 2].isAlive()) {++snd_count;}
        if(j > 1) if(field[i][j - 2].isAlive()) {++snd_count;}

        if(j % 2 == 1) step = 1;
        else step = 0;
        if(i + step > 0 && j < hexWidth - 1) {if(field[i - 1 + step][j + 1].isAlive()) {++fst_count;}}
        if(i + step < hexHeight && j < hexWidth - 1) {if(field[i + step][j + 1].isAlive()) {++fst_count;}}
        if(i + step < hexHeight - 1 && j < hexWidth - 1) {if(field[i + 1 + step][j + 1].isAlive()) {++snd_count;}}
        if(i + step < hexHeight && j > 0) {if(field[i + step][j - 1].isAlive()) {++fst_count;}}
        if(i + step < hexHeight - 1 && j > 0) {if(field[i + 1 + step][j - 1].isAlive()) {++snd_count;}}
        if(i + step > 0 && j > 0) {if(field[i - 1 + step][j - 1].isAlive()) {++fst_count;}}
        if(i + step > 1 && j > 0) {if(field[i - 2 + step][j - 1].isAlive()) {++snd_count;}}

        if(j % 2 == 1) step = 0;
        else step = 1;
        if(i - step > 0 && j < hexWidth - 1) {if(field[i - 1 - step][j + 1].isAlive()) {++snd_count;}}

        impact = FST_IMPACT * fst_count + SND_IMPACT * snd_count;
        fst_count = 0;
        snd_count = 0;
        field[i][j].setImpact(impact);
      }
    }
  }
  public void stepChange(){
    double impact = 0.0;
    int fst_count = 0;
    int snd_count = 0;
    int step;

    for(int i = 0; i < hexHeight; i++){
      for(int j = 0; j < hexWidth; j++){

        if(i > 0) if(field[i - 1][j].isAlive()) {++fst_count;}
        if(i < hexHeight - 1) if(field[i + 1][j].isAlive()) {++fst_count;}
        if(j < hexWidth - 2) if(field[i][j + 2].isAlive()) {++snd_count;}
        if(j > 1) if(field[i][j - 2].isAlive()) {++snd_count;}

        if(j % 2 == 1) step = 1;
        else step = 0;
        if(i + step > 0 && j < hexWidth - 1) {if(field[i - 1 + step][j + 1].isAlive()) {++fst_count;}}
        if(i + step < hexHeight && j < hexWidth - 1) {if(field[i + step][j + 1].isAlive()) {++fst_count;}}
        if(i + step < hexHeight - 1 && j < hexWidth - 1) {if(field[i + 1 + step][j + 1].isAlive()) {++snd_count;}}
        if(i + step < hexHeight && j > 0) {if(field[i + step][j - 1].isAlive()) {++fst_count;}}
        if(i + step < hexHeight - 1 && j > 0) {if(field[i + 1 + step][j - 1].isAlive()) {++snd_count;}}
        if(i + step > 0 && j > 0) {if(field[i - 1 + step][j - 1].isAlive()) {++fst_count;}}
        if(i + step > 1 && j > 0) {if(field[i - 2 + step][j - 1].isAlive()) {++snd_count;}}

        if(j % 2 == 1) step = 0;
        else step = 1;
        if(i - step > 0 && j < hexWidth - 1) {if(field[i - 1 - step][j + 1].isAlive()) {++snd_count;}}

        impact = FST_IMPACT * fst_count + SND_IMPACT * snd_count;
        if(field[i][j].isAlive()){
          if(impact < LIVE_BEGIN || impact > LIVE_END){
            field[i][j].setDead();
          }
          else{
            this.extinction = false;
            field[i][j].setSurvive();
          }
        }
        else{
          if(impact < BIRTH_BEGIN || impact > BIRTH_END){
            field[i][j].setDead();
          }
          else{
            this.extinction = false;
            field[i][j].setSurvive();
          }
        }
        fst_count = 0;
        snd_count = 0;
        field[i][j].setImpact(impact);
      }
    }
    for(int i = 0; i < hexHeight; i++){
      for(int j = 0; j < hexWidth; j++){
        field[i][j].setAlive(field[i][j].getSurvive());
      }
    }
  }

  public boolean isExtinction(){
    boolean temp = this.extinction;
    this.extinction = true;
    return temp;
  }

  public void clearField(){
    this.field = new HexahedronGrid(this.hexHeight, this.hexWidth, this.hexRadius, this.hexSideThick).getField();
  }

  public LinkedList<Point> getAliveCells(){
    LinkedList<Point> ac = new LinkedList<Point>();
    for (int i = 0; i < this.hexHeight; i++) {
      for (int j = 0; j < this.hexWidth; j++) {
        if(field[i][j].isAlive()){
          ac.add(new Point(i, j));
        }
      }
    }

    return ac;
  }

  public void setAliveCells(LinkedList<Point> ac){
    for(Point p : ac){
      field[(int)p.getX()][(int)p.getY()].setAlive(true);
    }
  }

  public double[] getLiveProps(){
    double[] props = {FST_IMPACT, SND_IMPACT, BIRTH_BEGIN, BIRTH_END, LIVE_BEGIN, LIVE_END};
    return props;
  }

  public void setFI(double fi){
    FST_IMPACT = fi;
  }

  public void setSI(double si){
    SND_IMPACT = si;
  }

  public void setBB(double bb){
    BIRTH_BEGIN = bb;
  }

  public void setBE(double be){
    BIRTH_END = be;
  }

  public void setLB(double lb){
    LIVE_BEGIN = lb;
  }

  public void setLE(double le){
    LIVE_END = le;
  }
}
