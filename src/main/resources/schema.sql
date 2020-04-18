
  
 CREATE TABLE IF NOT EXISTS core.tenant
(
   id serial  NOT NULL,
   key character varying(64),
   name character varying(64),
   description character varying(64),
   is_active boolean,
   created_on timestamp,
   modified_on timestamp,
   created_by character varying(64),
   modified_by character varying(64),
   CONSTRAINT pk_tenant_id PRIMARY KEY (id)
);

 CREATE TABLE IF NOT EXISTS core.login
(
   id serial  NOT NULL,
   username character varying(64),
   password character varying(64),
   created_on timestamp,
   modified_on timestamp,
   created_by character varying(64),
   modified_by character varying(64),
   CONSTRAINT pk_login_id PRIMARY KEY (id)
);

 CREATE TABLE IF NOT EXISTS core.role
(
   id serial  NOT NULL,
   key character varying(64),
   name character varying(64),
   type character varying(64),
   created_on timestamp,
   modified_on timestamp,
   created_by character varying(64),
   modified_by character varying(64),
   CONSTRAINT pk_role_id PRIMARY KEY (id)
);


 CREATE TABLE IF NOT EXISTS core.user
(
   id serial  NOT NULL,
   first_name character varying(64),
   last_name character varying(64),
   email character varying(64),
   dob date,
   is_active boolean,
   login_id integer NOT NULL,
   role_id integer NOT NULL,
   tenant_id integer NOT NULL,
   created_on timestamp,
   modified_on timestamp,
   created_by character varying(64),
   modified_by character varying(64),
   CONSTRAINT pk_user_id PRIMARY KEY (id),
   CONSTRAINT fk_login_login_id FOREIGN KEY (login_id)
      REFERENCES core.login (id),
   CONSTRAINT fk_role_role_id FOREIGN KEY (role_id)
      REFERENCES core.role (id),
   CONSTRAINT fk_tenant_tenant_id FOREIGN KEY (tenant_id)
      REFERENCES core.tenant (id) 
);

 CREATE TABLE IF NOT EXISTS core.oauth_client_details
(
   id serial  NOT NULL,
   client_id character varying(64),
   client_secret character varying(64),
   redirect_url character varying(64),
   is_active boolean,
   created_on timestamp,
   modified_on timestamp,
   created_by character varying(64),
   modified_by character varying(64),
   CONSTRAINT pk_oauth_client_details_id PRIMARY KEY (id)
);

