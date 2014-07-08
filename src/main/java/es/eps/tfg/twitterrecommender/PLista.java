/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.eps.tfg.twitterrecommender;

/**
 *
 * @author ADRIAN
 */
public class PLista {

    private long itemId;
    private long domainId;
    private String url;
    private String category;
    private int flag_Recommendable;
    private String tittle;
    private String text;

    public PLista(long itemId, long domainId, String url, String category, int flag_Recommendable, String tittle, String text) {
        this.itemId = itemId;
        this.domainId = domainId;
        this.url = url;
        this.category = category;
        this.flag_Recommendable = flag_Recommendable;
        this.tittle = tittle;
        this.text = text;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getDomainId() {
        return domainId;
    }

    public void setDomainId(long domainId) {
        this.domainId = domainId;
    }

    public int getFlag_Recommendable() {
        return flag_Recommendable;
    }

    public void setFlag_Recommendable(int flag_Recommendable) {
        this.flag_Recommendable = flag_Recommendable;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
