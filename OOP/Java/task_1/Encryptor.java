import java.io.*;
import java.util.*;

class MorseABC{
  private Map<Character, String> table;
  MorseABC(){
    this.table = new HashMap<Character, String>();
  }
  void makeNote(Character ch, String str){
    this.table.put(ch, str);
  }
  String getCode(Character ch){
    return this.table.get(ch);
  }
  Character getDeCode(String str){
    for(Character key : this.table.keySet()){
      if (this.table.get(key).equals(str)){
        return key;
      }
    }
    return null;
  }
}

class CharStat{
  private char ch;
  private int stat;

  CharStat(char ch){
    this.ch = ch;
    this.stat = 1;
  }
  char getCh(){
    return this.ch;
  }

  int getStat(){
    return this.stat;
  }

  void incCh(){
    ++(this.stat);
  }
}

class Statistic{
  private Set<CharStat> stat;

  Statistic(){
    this.stat = new HashSet<CharStat>();
  }

  void makeNote(char ch){
    boolean add = false;
    for(CharStat s : this.stat) {
      if(s.getCh() == ch) {
        s.incCh();
        add = true;
        break;
      }
    }
    if(add == false) this.stat.add(new CharStat(ch));
  }

  void printToFile(){
    try
    (
      Writer ou = new OutputStreamWriter(new FileOutputStream("stat.st"));
    )
    {
      for(CharStat s : this.stat) {
        String str = s.getCh() + " " + s.getStat() + "\n";
        ou.write(str, 0, str.length());
      }
    }
    catch (IOException e){
      System.err.println("Error while writing file: " + e.getLocalizedMessage());
    }
  }
}

interface Handler{
  void secret(InputStreamReader file, MorseABC abc) throws java.io.IOException;
}

class Code implements Handler{
  Code(){}
  public void secret(InputStreamReader file, MorseABC abc) throws java.io.IOException{
    Statistic stat = new Statistic();
    int ch;
    try
    (
      Writer ou = new OutputStreamWriter(new FileOutputStream("init.decode"));
    )
    {
      while((ch = file.read()) != -1){
        ch = Character.toUpperCase(ch);
        stat.makeNote((char)ch);
        if(ch == ' '){
          ou.write("      ", 0, 6);
        }
        else if(ch == '\n'){
          String str = abc.getCode('$') + " ";
          ou.write(str, 0, str.length());
        }
        else{
          String str = abc.getCode((char)ch) + " ";
          ou.write(str, 0, str.length());
        }
      }
      stat.printToFile();
    }
    catch (IOException e){
      System.err.println("Error while writing file: " + e.getLocalizedMessage());
    }

  }
}

class DeCode implements Handler{
  DeCode(){}
  public void secret(InputStreamReader file, MorseABC abc) throws java.io.IOException{
    Statistic stat = new Statistic();
    int ch;
    int count = 0;
    StringBuilder strb = new StringBuilder();
    try
    (
      Writer ou = new OutputStreamWriter(new FileOutputStream("decoded.code"));
    )
    {
      while((ch = file.read()) != -1){
        if(ch == ' '){
          if(count == 0){
            char c = abc.getDeCode(strb.toString());
            if(c == '$') ou.write("\n", 0, 1);
            else ou.write(Character.toString(c), 0, 1);
            strb = new StringBuilder();
          }
          ++count;
        }
        else{
          if(count > 1){
            for(int i = 0; i < count/6; i++) ou.write(" ", 0, 1);
          }
          count = 0;
          strb.append((char)ch);
        }
        stat.makeNote((char)ch);
      }
      stat.printToFile();
    }
    catch (IOException e){
      System.err.println("Error while writing file: " + e.getLocalizedMessage());
    }
  }
}

class Encryptor{
  public static void main(String[] args){
    Map<String, Handler> make = new HashMap<String, Handler>();
    make.put("code", new Code());
    make.put("decode", new DeCode());
    Scanner scan = new Scanner(System.in);
    while(true){
      String[] comm;
      while(true){
        System.out.print("> ");
        String command = scan.nextLine();
        if(command.equals("exit")) return;
        else if(command.equals("help")){
          System.out.println("\n=============================\n\t  COMMANDS:\n=============================\n  code FILE - encrypt file.\n decode FILE - decrypt file.\n    exit - stop program.\n=============================\n");
          continue;
        }
        else{
          comm = command.split(" ");
          if((comm[0].equals("code") || comm[0].equals("decode")) && comm.length == 2){
            break;
          }
          else{
            System.out.println("!Bad command format. Type 'help' for instructions.");
            continue;
          }
        }
      }
      MorseABC abc = new MorseABC();
      try
      (
        BufferedReader readABC = new BufferedReader(new InputStreamReader(new FileInputStream("morse.abc")));
        InputStreamReader file = new InputStreamReader(new FileInputStream(comm[1]));
      )
      {
        String line;
        String splited[];
        while((line = readABC.readLine()) != null){
          splited = line.split(" ");
          abc.makeNote(splited[0].charAt(0), splited[1]);
        }
        make.get(comm[0]).secret(file, abc);
      }
      catch (IOException e){
        System.err.println("Error while reading file: " + e.getLocalizedMessage());
      }

    }

  }
}

// finally{
//   if (readABC != null){
//     try{
//       readABC.close();
//     }
//     catch (IOException e){
//       e.printStackTrace(System.err);
//     }
//   }
// }
