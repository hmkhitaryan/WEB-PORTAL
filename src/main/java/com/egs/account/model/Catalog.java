package com.egs.account.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "catalog")
public class Catalog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String link;
    private String comment;
    private String type;
    private Date insertDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Lob @Basic(fetch = FetchType.LAZY)
    @Column(name="content", nullable = false)
    private byte[] content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLink() { return link; }

    public void setLink(String link) { this.link = link; }

    public String getComment() { return comment; }

    public void setComment(String comment) { this.comment = comment; }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getInsertDate() { return insertDate; }

    public void setInsertDate(Date insertDate) { this.insertDate = insertDate; }

    public User getUser() { return user;}

    public void setUser(User user) { this.user = user; }

    public byte[] getContent() { return content; }

    public void setContent(byte[] content) { this.content = content; }
}
