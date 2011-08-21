package rl.util;

import java.util.ArrayList;
import java.util.Iterator;

public class PriorityQueue {
  ArrayList<QueueItem> events;

  PriorityQueue() {
    events = new ArrayList<QueueItem>();
  }

  public int length() {
    return events.size();
  }

  public void enqueue(Object o, float priority) {
    QueueItem qi = new QueueItem(o, priority);
    int finalPos = 0, high = length();

    while(finalPos < high) {
      int middle = (int) Math.floor((finalPos + high) / 2);
      if(qi.priority < ((QueueItem) events.get(middle)).priority) {
        high = middle;
      } else {
        finalPos = middle + 1;
      }
    }

    events.add(finalPos, qi);
  }

  public void adjustPriorities(float add) {
    Iterator<QueueItem> itr = events.iterator();
    while(itr.hasNext()) {
      QueueItem qi = itr.next();
      qi.priority += add;
    }
  }

  public QueueItem dequeue() {
    return (QueueItem) events.remove(0);
  }

  public void erase() {
    events.clear();
  }

  public void eraseRef(Object o) {
    events.remove(o);
  }
}