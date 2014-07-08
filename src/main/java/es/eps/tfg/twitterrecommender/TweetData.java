/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.eps.tfg.twitterrecommender;

import java.util.Date;

/**
 *
 * @author ADRIAN
 */
public class TweetData {

    private long id;
    private String tweeturl;
    private int retweets;
    private int favoritos;
    private Date createat;
    private String texto;
    private long userid;
    private String username;
    private long queryId;
    private Date lastUpdate;
    private String urls;
    private long domainid;


    public TweetData(long id, String tweeturl, int retweets, int favoritos, Date createat, String texto, long userid, String username, long queryId, Date lastUpdate, String urls) {
        this.id = id;
        this.tweeturl = tweeturl;
        this.retweets = retweets;
        this.favoritos = favoritos;
        this.createat = createat;
        this.texto = texto;
        this.userid = userid;
        this.username = username;
        this.queryId = queryId;
        this.lastUpdate = lastUpdate;
        this.urls = urls;
        this.domainid = -1l;
    }

    public Date getCreateat() {
        return createat;
    }

    public void setCreateat(Date createat) {
        this.createat = createat;
    }

    public int getFavoritos() {
        return favoritos;
    }

    public void setFavoritos(int favoritos) {
        this.favoritos = favoritos;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public int getRetweets() {
        return retweets;
    }

    public void setRetweets(int retweets) {
        this.retweets = retweets;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getTweeturl() {
        return tweeturl;
    }

    public void setTweeturl(String tweeturl) {
        this.tweeturl = tweeturl;
    }

    public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getQueryId() {
        return queryId;
    }

    public void setQueryId(long queryId) {
        this.queryId = queryId;
    }

    public long getDomainid() {
        return domainid;
    }

    public void setDomainid(long domainid) {
        this.domainid = domainid;
    }
}
