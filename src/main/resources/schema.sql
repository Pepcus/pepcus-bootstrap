 
 CREATE TABLE IF NOT EXISTS core.tenant
(
   id serial  NOT NULL,
   key character varying(64),
   name character varying(64),
   description character varying(64),
   is_active boolean,
   created_on int8,
   modified_on int8,
   created_by character varying(64),
   modified_by character varying(64),
   CONSTRAINT pk_tenant_id PRIMARY KEY (id)
);

 CREATE TABLE IF NOT EXISTS core.oauth_tenant_details
(
   id serial  NOT NULL,
   tenant_id INTEGER,
   client_id character varying(64),
   client_secret character varying(64),
   scope VARCHAR(256),
   authorized_grant_types VARCHAR(256),
   redirect_uri VARCHAR(256),
   authorities VARCHAR(256),
   access_token_validity INTEGER,
   refresh_token_validity INTEGER,
   additional_information VARCHAR(4096),
   autoapprove VARCHAR(256),
   created_on int8,
   modified_on int8,
   is_active boolean,
   created_by character varying(64),
   modified_by character varying(64),
   CONSTRAINT pk_auth_tenant_details_id PRIMARY KEY (id),
   CONSTRAINT fk_auth_tenant_details_tenant FOREIGN KEY (tenant_id)
      REFERENCES core.tenant(id) MATCH SIMPLE
	  ON UPDATE NO ACTION ON DELETE NO ACTION
);

 CREATE TABLE IF NOT EXISTS core.role
(
   id serial  NOT NULL,
   tenant_key character varying(64),
   key character varying(64),
   name character varying(64),
   description character varying(64),
   type character varying(64),
   created_on int8,
   modified_on int8,
   is_active boolean,
   created_by character varying(64),
   modified_by character varying(64),
   CONSTRAINT pk_role_id PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS core.permission
(
   id serial  NOT NULL,
   key character varying(64),
   name character varying(64),
   description character varying(64),
   created_on int8,
   modified_on int8,
   is_active boolean,
   created_by character varying(64),
   modified_by character varying(64),
   CONSTRAINT pk_permission_id PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS core.role_permission
(
   id serial  NOT NULL,
   role_id INTEGER,
   permission_id INTEGER,
   CONSTRAINT pk_role_permission_id PRIMARY KEY (id),
   CONSTRAINT fk_role_permission_role FOREIGN KEY (role_id)
      REFERENCES core.role(id) MATCH SIMPLE
	  ON UPDATE NO ACTION ON DELETE NO ACTION,
   CONSTRAINT fk_role_permission_permission FOREIGN KEY (permission_id)
      REFERENCES core.permission(id) MATCH SIMPLE
	  ON UPDATE NO ACTION ON DELETE NO ACTION
);

 CREATE TABLE IF NOT EXISTS core.users
(
   id serial  NOT NULL,
   first_name character varying(64),
   last_name character varying(64),
   email character varying(64),
   dob date,
   username character varying(64),
   encrypted_password character varying(64),
   role_id integer NOT NULL,
   tenant_id integer NOT NULL,
   created_on int8,
   modified_on int8,
   is_active boolean,
   created_by character varying(64),
   modified_by character varying(64),
   CONSTRAINT pk_users_id PRIMARY KEY (id),
   CONSTRAINT fk_role_role_id FOREIGN KEY (role_id)
      REFERENCES core.role (id),
   CONSTRAINT fk_tenant_tenant_id FOREIGN KEY (tenant_id)
      REFERENCES core.tenant (id) 
);

-- =======================================

-- Insert statements for Tenant master entry
-- INSERT INTO core.tenant ("key","name",description,is_active,created_on,modified_on,created_by,modified_by) VALUES ('PEP_BS','Pepcus Bootstrap','Pepcus Bootstrap',true,1587308245,1587308245,'admin','admin');

-- Insert statements for oauth_tenant_details master entry
-- INSERT INTO core.oauth_tenant_details (tenant_id,client_id,client_secret,"scope",authorized_grant_types,redirect_uri,authorities,access_token_validity,refresh_token_validity,additional_information,autoapprove,created_on,modified_on,created_by,modified_by,is_active) VALUES (1,'c75aa799','6252ff83437e3bcd6828aa34287fc503','all','implicit, authorization_code, , password, refresh_token',NULL,NULL,1800,1800,NULL,'true',1587308416,1587308416,'admin','admin',true);

-- Insert statements for role master entry
-- INSERT INTO core."role" (tenant_key,"key","name",description,"type",created_on,modified_on,created_by,modified_by,is_active) VALUES (NULL,'sysadm','SYSTEM_ADMINISTRATOR','System Administrator','admin',1587308514,1587308514,'admin','admin',true);

-- Insert statements for user master entry
-- INSERT INTO core.users (first_name,last_name,email,dob,username,encrypted_password,is_active,role_id,tenant_id,created_on,modified_on,created_by,modified_by) VALUES ('Sandeep','Vishwakarma','sandeep.vishwakarma@pepcus.com','1988-08-08','svishwakarma','$2a$10$gl.GsExKSWslWIKDAo.wBeWar4fR1R.BzydtTa3FLDiAt2RuG0edW',true,1,1,1587308666,1587308666,'admin','admin');