package rl.util;

import java.util.Collection;

public abstract class GameMenu {
  final static int PER_PAGE = 10;
  int currentPage, totalPages;
  boolean showPagination;
  String messageQueue;
  
  public GameMenu() {
    currentPage = 1;
    totalPages = 1;
    showPagination = false;
    messageQueue = "";
  }
  
  public abstract String display();
  
  public String listPage(int page, Collection<?> items) {
    Object[] itemArray = items.toArray();
    String menuDisplay = "";
    if(page <= totalPages) {
      for(int item=0; item<PER_PAGE; item++) {
        int itemIndex = item + ((currentPage-1) * PER_PAGE);
        if(itemIndex < items.size()) {
          Object listItem = itemArray[itemIndex];
          menuDisplay += ((char) ('a' + item)) + ") " + listItem.toString() + "\n";
        } else {
          break;
        }
      }
    }
    return menuDisplay;
  }
  
  public String displayPagination() {
    return "(p)rev | [ "+currentPage+" / "+totalPages+" ] | (n)ext";
  }
  
  String flushMessages() {
    String message = messageQueue;
    messageQueue = "";
    return message;
  }
  
  public Object handleInput(int key, int keyCode) {
    if(key == 'n') {
      currentPage++;
      if(currentPage > totalPages) {
        currentPage = 1;
      }
    } else if (key == 'p') {
      currentPage--;
      if(currentPage == 0) {
        currentPage = totalPages;
      }
    }
    return null;
  }
}
