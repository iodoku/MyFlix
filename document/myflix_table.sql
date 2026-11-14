create table mv_user (
    userid varchar2(50) not null,
    userpw varchar2(255) not null,
    nick varchar2(20 char) not null,
    email varchar2(255) not null,
    joindt date default sysdate not null,
    
    CONSTRAINT pk_user_id primary key (userid)
);

comment on table mv_user is '유저정보';
comment on column mv_user.userid is '유저id';
comment on column mv_user.userpw is '유저pw';
comment on column mv_user.nick is '닉네임';
comment on column mv_user.email is '이메일';
comment on column mv_user.joindt is '가입일자';

create table mv_board (
  r_no   number(10,0)  generated always as identity   not null,
  r_title  varchar2(200 char) not null,
  m_title  varchar2(200 char) not null,
  m_dt     varchar2(10) not null,
  m_genre1 varchar2(50) not null,
  m_genre2 varchar2(50),
  m_genre3 varchar2(50),
  m_filename varchar2(255),
  m_filepath varchar2(1000),
  r_content varchar2(4000) not null,
  r_dt date default sysdate not null,
  r_rating number(2, 1) not null,
  userid varchar2(50) not null,
  
  CONSTRAINT pk_rno_id primary key (r_no),
  CONSTRAINT fk_mvboard_userid FOREIGN KEY (userid) REFERENCES mv_user(userid)
);

comment on table mv_board is '리뷰정보';
comment on column mv_board.r_no is '리뷰번호';
comment on column mv_board.r_title is '리뷰제목';
comment on column mv_board.m_title is '영화제목';
comment on column mv_board.m_dt is '영화개봉일자';
comment on column mv_board.m_genre1 is '장르1';
comment on column mv_board.m_genre2 is '장르2';
comment on column mv_board.m_genre3 is '장르3';
comment on column mv_board.m_filename is '파일이름';
comment on column mv_board.m_filepath is '파일경로';
comment on column mv_board.r_content is '리뷰내용';
comment on column mv_board.r_dt is '리뷰작성일자';
comment on column mv_board.r_rating is '리뷰 게시글 평점';
comment on column mv_board.userid is '리뷰 작성자 아이디';

create table mv_reply (
    rp_no number(5, 0) generated always as identity not null,
    rp_content varchar2(4000) not null,
    rp_rating number(2, 1) not null,
    rp_dt date default sysdate not null,
    userid varchar2(50) not null,
    r_no number(10, 0) not null,
    
    CONSTRAINT pk_rpno_id primary key (rp_no),
    CONSTRAINT fk_mvreply_userid foreign key (userid) references mv_user(userid),
    CONSTRAINT fk_mvreply_rno foreign key (r_no) references mv_board(r_no)
);

comment on table mv_reply is '댓글정보';
comment on column mv_reply.rp_no is '댓글번호';
comment on column mv_reply.rp_content is '댓글내용';
comment on column mv_reply.rp_rating is '리뷰 게시글 별점';
comment on column mv_reply.rp_dt is '댓글작성일자';
comment on column mv_reply.userid is '댓글 작성자 아이디';
comment on column mv_reply.r_no is '리뷰번호';

ALTER TABLE MV_USER ADD CONSTRAINT UK_MV_USER_EMAIL UNIQUE (EMAIL);