package com.thoughtworks.i1.emailSender.domain;

import javax.persistence.*;

@Entity(name = "I1_EMAIL_RECIPIENTS")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "EMAIL_RECIPIENTS_TYPE", discriminatorType = DiscriminatorType.STRING)
public abstract class EmailRecipients {
    @Id
    @Column(name = "EMAIL_RECIPIENTS_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "i1EmailRecipientsSeq")
    @SequenceGenerator(initialValue = 1, name = "i1EmailRecipientsSeq", sequenceName = "I1_EMAIL_RECIPIENTS_SEQUENCE")
    protected long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="EMAIL_RECIPIENTS_ADDRESS_ID", nullable=false, updatable=false)
    protected Address address;

    @ManyToOne
    @JoinColumn(name = "EMAIL_RECIPIENTS_EMAIL_ID", nullable = false, insertable = true)
    protected Email email;

    public EmailRecipients(Address address) {
        this.address = address;
    }

    public EmailRecipients(){
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }

    public void setEmail(Email email) {
        this.email = email;
    }
}