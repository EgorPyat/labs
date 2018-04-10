
public abstract class Function{
  public abstract double function(double x, double y);

  public double getMinimum(double x1, double y1, double x2, double y2, double stepX, double stepY){
    double min = function(x1, y1);
    double t;

    for(double i = x1; i < x2; i += stepX){
      for(double j = y1; j < y2; j+= stepY){
        t = this.function(i, j);
        if(t < min) min = t;
      }
    }

    return min;
  }

  public double getMaximum(double x1, double y1, double x2, double y2, double stepX, double stepY){
    double max = function(x1, y1);
    double t;

    for(double i = x1; i < x2; i += stepX){
      for(double j = y1; j < y2; j+= stepY){
        t = this.function(i, j);
        if(t > max) max = t;
      }
    }

    return max;
  }
}
