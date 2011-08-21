package rl.util;

public class QueueItem {
  public Object item;
  public float priority;

  public QueueItem(Object i, float p) {
    item = i;
    priority = p;
  }
}