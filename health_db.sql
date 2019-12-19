/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2019/11/13 11:29:11                          */
/*==============================================================*/


drop table if exists body_index;

drop table if exists collection;

drop table if exists comment;

drop table if exists cookbook;

drop table if exists disease;

drop table if exists eye_protection;

drop table if exists health;

drop table if exists health_care;

drop table if exists heart_record;

drop table if exists keep_fit;

drop table if exists med_kit;

drop table if exists medicine_chest;

drop table if exists menu;

drop table if exists message;

drop table if exists psychological;

drop table if exists scan_history;

drop table if exists user;

/*==============================================================*/
/* Table: body_index                                            */
/*==============================================================*/
create table body_index
(
   id                   int auto_increment,
   userTel              varchar(200),
   userHeight           double,
   userWeight           double,
   userHeart            int,
   huserBloodPressure   int,
   duserBloodPressure   int,
   isNormal            int,
   userBIM              double,
   primary key(id)
);

/*==============================================================*/
/* Table: collection                                            */
/*==============================================================*/
create table collection
(
   id                   int auto_increment,
   userId               int,
   tableName            varchar(20),
   tableId              int,
   primary key(id)
);

/*==============================================================*/
/* Table: comment                                               */
/*==============================================================*/
create table comment
(
   id                   int auto_increment,
   messageId            int,
   content              varchar(3000),
   sendPersonId         int,
   sendPersonName       varchar(200),
   likeNumber       int,
   primary key(id)
);

/*==============================================================*/
/* Table: cookbook                                              */
/*==============================================================*/
create table cookbook
(
   id                   int auto_increment,
   keyWord              varchar(512),
   description          varchar(1024),
   images               varchar(1024),
   name                 varchar(128),
   material             varchar(1024),
   steps                varchar(3000),
   likeNumber           int,
   primary key(id)
);

/*==============================================================*/
/* Table: disease                                               */
/*==============================================================*/
create table disease
(
   id                   int auto_increment,
   keyWord              varchar(512),
   titles             varchar(256),
   content              varchar(3000),
   images               varchar(1024),
   likeNumber           int,
   primary key(id)
);

/*==============================================================*/
/* Table: eye_protection                                        */
/*==============================================================*/
create table eye_protection
(
   id                   int auto_increment,
   keyWord              varchar(512),
   titles             varchar(256),
   content              varchar(3000),
   images               varchar(1024),
   likeNumber           int,
   primary key(id)
);

/*==============================================================*/
/* Table: health                                                */
/*==============================================================*/
create table health
(
   id                   int auto_increment,
   keyWord              varchar(512),
   titles             varchar(256),
   content              varchar(3000),
   images               varchar(1024),
   likeNumber           int,
   primary key(id)
);

/*==============================================================*/
/* Table: health_care                                           */
/*==============================================================*/
create table health_care
(
   id                   int auto_increment,
   keyWord              varchar(512),
   titles             varchar(256),
   content              varchar(3000),
   images               varchar(1024),
   likeNumber           int,
   primary key(id)
);

/*==============================================================*/
/* Table: heart_record                                          */
/*==============================================================*/
create table heart_record
(
   id                   int auto_increment,
   userTel              varchar(200),
   time                 timestamp,
   userHeart            int,
   felling              int,
   huserBloodPressure   int,
   duserBloodPressure   int,
   primary key(id)
);

/*==============================================================*/
/* Table: keep_fit                                              */
/*==============================================================*/
create table keep_fit
(
   id                   int auto_increment,
   keyWord              varchar(512),
   titles             varchar(256),
   content              varchar(3000),
   images               varchar(1024),
   likeNumber           int,
   primary key(id)
);

/*==============================================================*/
/* Table: med_kit                                               */
/*==============================================================*/
create table med_kit
(
   id                   int auto_increment,
   userId               int,
   medicineChestId      int,
   primary key(id)
);

/*==============================================================*/
/* Table: medicine_chest                                        */
/*==============================================================*/
create table medicine_chest
(
   id                   int auto_increment,
   keyWord              varchar(512),
   name                 varchar(128),
   englishName          varchar(128),
   pinYin               varchar(128),
   dosage              varchar(200),
   characters            varchar(1024),
   indications           varchar(1024),
   note            varchar(1024),
   primary key(id)
);

/*==============================================================*/
/* Table: menu                                                  */
/*==============================================================*/
create table menu
(
   id                   int auto_increment,
   userId               int,
   cookBookId           int,
   primary key(id)
);

/*==============================================================*/
/* Table: message                                               */
/*==============================================================*/
create table message
(
   id                   int auto_increment,
   userId               int,
   content              varchar(3000),
   images               varchar(1024),
   primary key(id)
);

/*==============================================================*/
/* Table: psychological                                         */
/*==============================================================*/
create table psychological
(
   id                   int auto_increment,
   keyWord              varchar(512),
   titles             varchar(256),
   content              varchar(3000),
   images               varchar(1024),
   likeNumber           int,
   primary key(id)
);

/*==============================================================*/
/* Table: scan_history                                          */
/*==============================================================*/
create table scan_history
(
   id                   int auto_increment,
   userId               int,
   tableName            varchar(20),
   tableId              int,
   primary key(id)
);

/*==============================================================*/
/* Table: user                                                  */
/*==============================================================*/
create table user
(
   id                   int auto_increment,
   userName             varchar(60),
   userPassword         varchar(20),
   bodyId               int,
   userImage            varchar(200),
   sex                  varchar(10),
   phoneNumber          varchar(200),
   primary key(id)
);

