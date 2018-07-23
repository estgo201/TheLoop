package Data;

import lombok.Getter;

@Getter
public class BlogInfo {
  private String title;
  private String url;
  
  public BlogInfo(String title, String url) {
    this.title = title;
    this.url = url;
  }
  
  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append("title : " + title);
    sb.append(", ");
    sb.append("" + url);
    
    return sb.toString();
  }
}
