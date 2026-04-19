CREATE TABLE users (
	id BIGSERIAL PRIMARY KEY,
	email VARCHAR(255) NOT NULL UNIQUE,
	password TEXT NOT NULL,
	created_at TIMESTAMP DEFAULT now(),
	updated_at TIMESTAMP DEFAULT null
);

create table sessions (
	id BIGSERIAL PRIMARY KEY,
	session_id uuid NOT NULL,
	user_id bigint NOT NULL,
	token_hash TEXT NOT NULL,
	absolute_expiration DATE NOT NULL,
	is_revoked BOOL DEFAULT false,
	CONSTRAINT fk_user_id 
		FOREIGN KEY(user_id) 
		REFERENCES users(id)
);