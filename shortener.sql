CREATE TABLE public."user" (
	username varchar(64) NOT null primary key,
	email varchar(128) unique NOT NULL,
	"password" varchar(64) NOT NULL,
	last_login timestamp NULL
);

CREATE TABLE public.url_shortener (
	id SERIAL primary key,
	url varchar(1024) NOT NULL,
	hash varchar unique null,
	username varchar NOT null references "user"(username),
	last_accessed timestamp NULL,
	count int NULL,
	unique(hash, username)
);

create table public.allowed_domains(
	username varchar references "user"(username),
	allowed_domain varchar,
	unique (username, allowed_domain)
)