/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.eps.tfg.twitterrecommender;

/**
 *
 * @author ADRIAN
 */
public class Translation {

    private long id;
    private long pListaId;
    private long domainId;
    private String category;

    public Translation(long id, long pListaId, long domainId, String category) {
        this.id = id;
        this.pListaId = pListaId;
        this.domainId = domainId;
        this.category = category;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getpListaId() {
        return pListaId;
    }

    public void setpListaId(long pListaId) {
        this.pListaId = pListaId;
    }
}
