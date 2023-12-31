CREATE SCHEMA IF NOT EXISTS hare;

alter table if exists hare.option drop constraint if exists FKj4v863kav72h60f62v0rsmuq7;
alter table if exists hare.session_theme_option drop constraint if exists FK9eekyf48kyh7bigph9mehn6ie;
alter table if exists hare.session_theme_option drop constraint if exists FK1e0khi4u5lphlk7s2r4i8nowm;
alter table if exists hare.session_theme_option drop constraint if exists FK5cddypsthy7a4nwsau591r904;
alter table if exists hare.users drop constraint if exists FK9wlj6sdd6ivmeakcj3as4w72o;
drop table if exists hare.option cascade;
drop table if exists hare.session cascade;
drop table if exists hare.session_theme_option cascade;
drop table if exists hare.theme cascade;
drop table if exists hare.users cascade;
drop sequence if exists hare.hibernate_sequence;
create sequence hare.hibernate_sequence start 1 increment 1;
create table hare.option (id int8 not null, option_name varchar(255), theme_id int8, primary key (id));
create table hare.session (id uuid not null, creation_date_time timestamp, primary key (id));
create table hare.session_theme_option (id int8 not null, creation_date_time timestamp, option_id int8, session_id uuid, theme_id int8, primary key (id));
create table hare.theme (id int8 not null, theme_name varchar(255), primary key (id));
create table hare.users (id int8 not null, is_active boolean, role varchar(255), user_name varchar(255), session_id uuid, primary key (id));
alter table if exists hare.option add constraint FKj4v863kav72h60f62v0rsmuq7 foreign key (theme_id) references hare.theme;
alter table if exists hare.session_theme_option add constraint FK9eekyf48kyh7bigph9mehn6ie foreign key (option_id) references hare.option;
alter table if exists hare.session_theme_option add constraint FK1e0khi4u5lphlk7s2r4i8nowm foreign key (session_id) references hare.session;
alter table if exists hare.session_theme_option add constraint FK5cddypsthy7a4nwsau591r904 foreign key (theme_id) references hare.theme;
alter table if exists hare.users add constraint FK9wlj6sdd6ivmeakcj3as4w72o foreign key (session_id) references hare.session;