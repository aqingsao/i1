create table I1_EMAIL (EMAIL_ID number(19,0) not null, ATTACHMENTS raw(255), MESSAGE varchar2(255 char), SUBJECT varchar2(255 char), EMAIL_FROM_ADDRESS number(19,0) not null, EMAIL_REPLY_TO_ADDRESS number(19,0), primary key (EMAIL_ID));
alter table I1_EMAIL add constraint FK_I1_EMAIL_FROM_ADDRESS foreign key (EMAIL_FROM_ADDRESS) references I1_EMAIL_ADDRESS(EMAIL_ADDRESS_ID);
alter table I1_EMAIL add constraint FK_I1_EMAIL_REPLY_TO_ADDRESS foreign key (EMAIL_REPLY_TO_ADDRESS) references I1_EMAIL_ADDRESS(EMAIL_ADDRESS_ID);
create sequence I1_EMAIL_SEQUENCE;