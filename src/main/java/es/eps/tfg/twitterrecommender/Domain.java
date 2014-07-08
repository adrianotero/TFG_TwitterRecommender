/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.eps.tfg.twitterrecommender;

/**
 *
 * @author ADRIAN
 */
public class Domain {

    private long domainid;
    private String domain;

    public Domain(long domainid, String domain) {
        this.domainid = domainid;
        this.domain = domain;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public long getDomainid() {
        return domainid;
    }

    public void setDomainid(long domainid) {
        this.domainid = domainid;
    }
}
