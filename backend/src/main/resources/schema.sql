CREATE SEQUENCE IF NOT EXISTS PlantHireRequestIDSequence START WITH 100 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS InvoiceIDSequence START WITH 100 INCREMENT BY 1;

create table if not exists users (username varchar(50) not null primary key, password varchar(50) not null, enabled boolean not null);
create table if not exists authorities (username varchar(50) not null, authority varchar(50) not null, constraint FK_AUTHORITIES_USERS foreign key(username) references users(username));

insert into users (username, password, enabled) SELECT 'admin', 'admin', true where not exists (select username, password, enabled from users where username = 'admin' and password = 'admin');
insert into authorities (username, authority) SELECT 'admin', 'ROLE_ADMIN' where not exists (select username, authority from authorities where username = 'admin' and authority = 'ROLE_ADMIN');
insert into users (username, password, enabled) SELECT 'user1', 'user1', true where not exists (select username, password, enabled from users where username = 'user1' and password = 'user1');
insert into authorities (username, authority) SELECT 'user1', 'SITE_ENGINEER' where not exists (select username, authority from authorities where username = 'user1' and authority = 'SITE_ENGINEER');
insert into users (username, password, enabled) SELECT 'user2', 'user2', true where not exists (select username, password, enabled from users where username = 'user2' and password = 'user2');
insert into authorities (username, authority) SELECT 'user2', 'WORK_ENGINEER' where not exists (select username, authority from authorities where username = 'user2' and authority = 'WORK_ENGINEER');
