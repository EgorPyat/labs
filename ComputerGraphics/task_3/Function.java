
public abstract class Function{
  public abstract double function(int x, int y);

  public double getMinimum(int x1, int y1, int x2, int y2){
    double min = function(x1, y1);
    double t;

    for(int i = x1; i < x2; i++){
      for(int j = y1; j < y2; j++){
        t = this.function(i, j);
        if(t < min) min = t;
      }
    }

    return min;
  }

  public double getMaximum(int x1, int y1, int x2, int y2){
    double max = function(x1, y1);
    double t;

    for(int i = x1; i < x2; i++){
      for(int j = y1; j < y2; j++){
        t = this.function(i, j);
        if(t > max) max = t;
      }
    }

    return max;
  }
}
