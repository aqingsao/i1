create table EMAIL (EMAIL_ID number(19,0) not null, attachments raw(255), MESSAGE varchar2(255 char), SUBJECT varchar2(255 char), EMAIL_FROM_ADDRESS number(19,0) not null, EMAIL_REPLY_TO_ADDRESS number(19,0), primary key (EMAIL_ID));
alter table EMAIL add constraint FK_EMAIL_FROM_ADDRESS foreign key (EMAIL_FROM_ADDRESS) references EMAIL_ADDRESS(EMAIL_ADDRESS_ID);
alter table EMAIL add constraint FK_EMAIL_REPLY_TO_ADDRESS foreign key (EMAIL_REPLY_TO_ADDRESS) references EMAIL_ADDRESS(EMAIL_ADDRESS_ID);
create sequence EMAIL_SEQUENCE;