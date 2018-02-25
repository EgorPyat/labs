
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
}
