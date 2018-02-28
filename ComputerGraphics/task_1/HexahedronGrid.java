
public class HexahedronGrid{
  private Hexahedron[][] field;
  private int hexWidth;
  private int hexHeight;
  private int hexRadius;
  private int normal;
  private int fieldWidth;
  private int fieldHeight;

  public HexahedronGrid(int N, int M, int hexRadius){
    this.hexWidth = M;
    this.hexHeight = N;
    this.hexRadius = hexRadius;
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
        field[i][j].construct(X, Y, R);
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

  public int getHeight(){
    return this.fieldHeight;
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

  public void stepChange(){
    double impact = 0.0;
    int fst_count = 0;
    int snd_count = 0;
    int step;
    for(int i = 0; i < hexHeight; i++){
      for(int j = 0; j < hexWidth; j++){

        if(i > 0) if(field[i - 1][j].isAlive()) {++fst_count; System.out.print("9");}
        if(i < hexHeight - 1) if(field[i + 1][j].isAlive()) {++fst_count; System.out.print("10");}
        if(j < hexWidth - 2) if(field[i][j + 2].isAlive()) {++snd_count; System.out.print("11");}
        if(j > 1) if(field[i][j - 2].isAlive()) {++snd_count; System.out.print("12");}

        if(j % 2 == 1) step = 1;
        else step = 0;
        if(i + step > 0 && j < hexWidth - 1) {if(field[i - 1 + step][j + 1].isAlive()) {++fst_count; System.out.print("1");}}
        if(i + step < hexHeight && j < hexWidth - 1) {if(field[i + step][j + 1].isAlive()) {++fst_count; System.out.print("2");}}
        if(i + step < hexHeight - 1 && j < hexWidth - 1) {if(field[i + 1 + step][j + 1].isAlive()) {++snd_count;System.out.print("3");}}
        if(i + step < hexHeight && j > 0) {if(field[i + step][j - 1].isAlive()) {++fst_count;System.out.print("4");}}
        if(i + step < hexHeight - 1 && j > 0) {if(field[i + 1 + step][j - 1].isAlive()) {++snd_count;System.out.print("5");}}
        if(i + step > 0 && j > 0) {if(field[i - 1 + step][j - 1].isAlive()) {++fst_count;System.out.print("6");}}
        if(i + step > 1 && j > 0) {if(field[i - 2 + step][j - 1].isAlive()) {++snd_count;System.out.print("7");}}

        if(j % 2 == 1) step = 0;
        else step = 1;
        if(i - step > 0 && j < hexWidth - 1) {if(field[i - 1 - step][j + 1].isAlive()) {++snd_count; System.out.print("8");}}

        impact = 1.0 * fst_count + 0.3 * snd_count;
        if(impact < 2.0 || impact > 3.3){
          field[i][j].setDead();
        }
        else{
          field[i][j].setSurvive();
        }
        System.out.println("(" + i + ", " + j + ")" + " " + impact + " " + field[i][j].isAlive());
        fst_count = 0;
        snd_count = 0;
      }
    }
    for(int i = 0; i < hexHeight; i++){
      for(int j = 0; j < hexWidth; j++){
        field[i][j].setAlive(field[i][j].getSurvive());
      }
    }
    System.out.println("step");
  }

  void clearField(){
    this.field = new HexahedronGrid(this.hexHeight, this.hexWidth, this.hexRadius).getField();
  }
}
