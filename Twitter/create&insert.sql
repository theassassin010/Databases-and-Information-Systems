drop table if exists password;
drop table if exists comment;
drop table if exists post;
drop table if exists follows;
drop table if exists public.user;

create table public.user(
	uid	varchar(20),
	name	varchar(30),
	email	varchar(50),
	primary key (uid)
);

create table public.password(
	uid 	 varchar(20),
	password varchar(20),
	foreign key (uid) references public.user (uid)
		on delete cascade
);

create table follows(
	uid1	varchar(20),
	uid2	varchar(20),
	foreign key (uid1) references public.user (uid)
		on delete cascade,
	foreign key (uid2) references public.user (uid)
		on delete cascade
);

create table post(
	postid		varchar(50),
	uid		varchar(20),
	text		varchar(200),
	timestamp	timestamp(0),
	primary key(postid),
	foreign key (uid) references public.user (uid)
		on delete cascade
);

create table comment(
	commentid	varchar(50),
	postid		varchar(50),
	uid		varchar(20),
	timestamp	timestamp(0),
	text		varchar(200),
	primary key(commentid),
	foreign key (postid) references public.post (postid)
		on delete cascade,
	foreign key (uid) references public.user (uid)
		on delete cascade
);

﻿delete from password;
delete from comment;
delete from post;
delete from follows;
delete from public.user;

insert into public.user values ('00128', 'Zhang', 'zg@gmail.com');
insert into public.user values ('12345', 'Shankar', 'sh@gmail.com');
insert into public.user values ('19991', 'Brandt', 'bz@gmail.com');
insert into public.user values ('23121', 'Chavez', 'ca@gmail.com');
insert into public.user values ('44553', 'Peltier', 'pa@gmail.com');
insert into public.user values ('45678', 'Levy', 'pl@gmail.com');
insert into public.user values ('54321', 'Williams', 'we@gmail.com');
insert into public.user values ('55739', 'Sanchez', 'ss@gmail.com');

insert into password values ('00128', 'Zhang');
insert into password values ('12345', 'Shankar');
insert into password values ('19991', 'Brandt');
insert into password values ('23121', 'Chavez');
insert into password values ('44553', 'Peltier');
insert into password values ('45678', 'Levy');
insert into password values ('54321', 'Williams');
insert into password values ('55739', 'Sanchez');

insert into follows values ('00128', '12345');
insert into follows values ('00128', '45678');
insert into follows values ('00128', '55739');
insert into follows values ('45678', '00128');
insert into follows values ('45678', '45678');
insert into follows values ('45678', '55739');
insert into follows values ('55739', '12345');
insert into follows values ('55739', '45678');
insert into follows values ('55739', '00128');
);
