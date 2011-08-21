package rl.util;

public class Scheduler {
  PriorityQueue pq;

  public Scheduler() {
    pq = new PriorityQueue();
  }

  public void scheduleEvent(Object o, float delay) {
    pq.enqueue(o, delay);
  }

  public Object nextEvent() {
    QueueItem qi = pq.dequeue();
    pq.adjustPriorities(-qi.priority);
    return qi.item;
  }

  public void cancelEvent(Object e) {
    pq.eraseRef(e);
  }
}