/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.eps.tfg.twitterrecommender;

/**
 *
 * @author ADRIAN
 */
public class QueryStruct {

    private Long QueryId;
    private String Query;
    private Long DomainId;

    public QueryStruct(Long QueryId, String Query, Long DomainId) {
        this.QueryId = QueryId;
        this.Query = Query;
        this.DomainId = DomainId;
    }

    public Long getDomainId() {
        return DomainId;
    }

    public void setDomainId(Long DomainId) {
        this.DomainId = DomainId;
    }

    public String getQuery() {
        return Query;
    }

    public void setQuery(String Query) {
        this.Query = Query;
    }

    public Long getQueryId() {
        return QueryId;
    }

    public void setQueryId(Long QueryId) {
        this.QueryId = QueryId;
    }
}
