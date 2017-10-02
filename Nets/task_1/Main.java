public class Main{
  public static void main(String[] args) throws Exception {
    if(args.length == 2){
      CopyCounter c = new CopyCounter(args[0], new Integer(args[1]));
      c.startCount();
    }
  }
}
