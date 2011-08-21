package rl.util;

public class Chance {
  public static int rollDie(int number, int faces) {
    int total = 0;

    for(int i=0;i<number;i++) {
      total += Math.ceil(Math.random() * faces);
    }

    return total;
  }
  
  public static int from(int low, int high) {
    int diff = high - low + 1;
    return low + Chance.rollDie(1, diff) - 1;
  }

  public static boolean flipCoin() {
    return Math.ceil(Math.random() * 2) == 2;
  }
}


