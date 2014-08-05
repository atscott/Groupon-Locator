package groupon;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * User: atscott
 * Date: 11/9/13
 * Time: 12:28 PM
 */
public class Deal
{
  public String dealUrl;
  public String shortAnnouncementTitle;
  public String smallImageUrl;
  public String mediumImageUrl;
  @SerializedName("options")
  public List<DealOption> dealOptions;
  public Merchant merchant;
  public int soldQuantity;
  public boolean isSoldOut;
  public List<Tag> tags;
}
