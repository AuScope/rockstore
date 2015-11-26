--
-- PostgreSQL database dump
--

-- Dumped from database version 9.4.3
-- Dumped by pg_dump version 9.4.0
-- Started on 2015-09-21 11:33:10

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 7 (class 2615 OID 228924)
-- Name: topology; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA topology;


--
-- TOC entry 215 (class 3079 OID 11861)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 3505 (class 0 OID 0)
-- Dependencies: 215
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


--
-- TOC entry 216 (class 3079 OID 227637)
-- Name: postgis; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS postgis WITH SCHEMA public;


--
-- TOC entry 3506 (class 0 OID 0)
-- Dependencies: 216
-- Name: EXTENSION postgis; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON EXTENSION postgis IS 'PostGIS geometry, geography, and raster spatial types and functions';


--
-- TOC entry 217 (class 3079 OID 228925)
-- Name: postgis_topology; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS postgis_topology WITH SCHEMA topology;


--
-- TOC entry 3507 (class 0 OID 0)
-- Dependencies: 217
-- Name: EXTENSION postgis_topology; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON EXTENSION postgis_topology IS 'PostGIS topology spatial types and functions';


SET search_path = public, pg_catalog;

--
-- TOC entry 1366 (class 1255 OID 236321)
-- Name: increment_version(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION increment_version() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
begin
  new.version := new.version + 1;
  return new;
end;
$$;


--
-- TOC entry 1368 (class 1255 OID 236347)
-- Name: rs_collection_audit(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION rs_collection_audit() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
    BEGIN
        --
        -- Create a row in emp_audit to reflect the operation performed on emp,
        -- make use of the special variable TG_OP to work out the operation.
        --
        IF (TG_OP = 'DELETE') THEN
            INSERT INTO rs_collection_audit SELECT OLD.*, 'DELETE' ,now();
            RETURN OLD;
        ELSIF (TG_OP = 'UPDATE') THEN
            INSERT INTO rs_collection_audit SELECT NEW.*, 'UPDATE', now();
            RETURN NEW;
        ELSIF (TG_OP = 'INSERT') THEN
            INSERT INTO rs_collection_audit SELECT NEW.*, 'INSERT', now();
            RETURN NEW;
        END IF;
        RETURN NULL; -- result is ignored since this is an AFTER trigger
    END;
$$;


--
-- TOC entry 1369 (class 1255 OID 236353)
-- Name: rs_sample_audit(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION rs_sample_audit() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
    BEGIN
        --
        -- Create a row in emp_audit to reflect the operation performed on emp,
        -- make use of the special variable TG_OP to work out the operation.
        --
        IF (TG_OP = 'DELETE') THEN
            INSERT INTO rs_sample_audit SELECT OLD.*, 'DELETE' ,now();
            RETURN OLD;
        ELSIF (TG_OP = 'UPDATE') THEN
            INSERT INTO rs_sample_audit SELECT NEW.*, 'UPDATE', now();
            RETURN NEW;
        ELSIF (TG_OP = 'INSERT') THEN
            INSERT INTO rs_sample_audit SELECT NEW.*, 'INSERT', now();
            RETURN NEW;
        END IF;
        RETURN NULL; -- result is ignored since this is an AFTER trigger
    END;
$$;


--
-- TOC entry 1367 (class 1255 OID 236350)
-- Name: rs_subcollection_audit(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION rs_subcollection_audit() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
    BEGIN
        --
        -- Create a row in emp_audit to reflect the operation performed on emp,
        -- make use of the special variable TG_OP to work out the operation.
        --
        IF (TG_OP = 'DELETE') THEN
            INSERT INTO rs_subcollection_audit SELECT OLD.*, 'DELETE' ,now();
            RETURN OLD;
        ELSIF (TG_OP = 'UPDATE') THEN
            INSERT INTO rs_subcollection_audit SELECT NEW.*, 'UPDATE', now();
            RETURN NEW;
        ELSIF (TG_OP = 'INSERT') THEN
            INSERT INTO rs_subcollection_audit SELECT NEW.*, 'INSERT', now();
            RETURN NEW;
        END IF;
        RETURN NULL; -- result is ignored since this is an AFTER trigger
    END;
$$;


SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 213 (class 1259 OID 236357)
-- Name: checkout_registry; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE checkout_registry (
    id integer NOT NULL,
    staff_ident character varying(30) NOT NULL,
    date_checkout timestamp without time zone NOT NULL,
    date_dueback timestamp without time zone,
    email character varying(150),
    name character varying(150),
    subcollection_id character varying(50) NOT NULL,
    checkout_status boolean,
    date_checkin timestamp without time zone,
    checkoutby character varying(50),
    checkinby character varying(50)
);


--
-- TOC entry 212 (class 1259 OID 236355)
-- Name: checkout_registry_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE checkout_registry_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3508 (class 0 OID 0)
-- Dependencies: 212
-- Name: checkout_registry_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE checkout_registry_id_seq OWNED BY checkout_registry.id;



--
-- TOC entry 200 (class 1259 OID 235996)
-- Name: rs_collection_collection_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE rs_collection_collection_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 99999
    CACHE 1;


--
-- TOC entry 197 (class 1259 OID 235959)
-- Name: rs_collection_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE rs_collection_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 99999
    CACHE 1;


--
-- TOC entry 194 (class 1259 OID 235935)
-- Name: rs_collection; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE rs_collection (
    id integer DEFAULT nextval('rs_collection_id_seq'::regclass) NOT NULL,
    collection_id character varying(50) DEFAULT ('C'::text || to_char(nextval('rs_collection_collection_id_seq'::regclass), 'FM00000'::text)),
    project character varying(100),
    staffid_responsible character varying(100),
    project_result character varying(200),
    project_publication character varying(200),
    project_close_date timestamp without time zone,
    available_to_public boolean,
    archive_due timestamp without time zone,
    staffid_field_manager character varying(100),
    version integer DEFAULT 0,
    last_update_user character varying(100)
);


--
-- TOC entry 209 (class 1259 OID 236328)
-- Name: rs_collection_audit; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE rs_collection_audit (
    id integer NOT NULL,
    collection_id character varying(50),
    project character varying(100),
    staffid_responsible character varying(100),
    project_result character varying(200),
    project_publication character varying(200),
    project_close_date timestamp without time zone,
    available_to_public boolean,
    archive_due timestamp without time zone,
    staffid_field_manager character varying(100),
    version integer,
    last_update_user character varying(100),
    operation character varying(100),
    audit_timestamp timestamp without time zone
);


--
-- TOC entry 199 (class 1259 OID 235965)
-- Name: rs_sample_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE rs_sample_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 206 (class 1259 OID 236226)
-- Name: rs_sample_igsn_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE rs_sample_igsn_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 196 (class 1259 OID 235951)
-- Name: rs_sample; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE rs_sample (
    id integer DEFAULT nextval('rs_sample_id_seq'::regclass) NOT NULL,
    igsn character varying(50) DEFAULT ('CSRWA'::text || nextval('rs_sample_igsn_seq'::regclass)) NOT NULL,
    csiro_sample_id character varying(200),
    sample_type character varying(50),
    bhid character varying(255),
    depth double precision,
    datum character varying(50),
    container_id character varying(100),
    subcollection_id character varying(100),
    external_ref character varying(300),
    sample_collector character varying(200),
    date_sampled timestamp without time zone,
    sample_dispose boolean,
    date_disposed timestamp without time zone,
    staffid_disposed character varying(100),
    location geometry(Point,4326),
    orig_lat character varying(50),
    orig_lon character varying(50),
    version integer DEFAULT 0,
    last_update_user character varying(100)
);


--
-- TOC entry 211 (class 1259 OID 236341)
-- Name: rs_sample_audit; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE rs_sample_audit (
    id integer NOT NULL,
    igsn character varying(50) NOT NULL,
    csiro_sample_id character varying(200),
    sample_type character varying(50),
    bhid character varying(255),
    depth double precision,
    datum character varying(50),
    container_id character varying(100),
    subcollection_id character varying(100),
    external_ref character varying(200),
    sample_collector character varying(200),
    date_sampled timestamp without time zone,
    sample_dispose boolean,
    date_disposed timestamp without time zone,
    staffid_disposed character varying(100),
    location geometry(Point,4326),
    orig_lat character varying(50),
    orig_lon character varying(50),
    version integer,
    last_update_user character varying(100),
    operation character varying(100),
    audit_timestamp timestamp without time zone
);


--
-- TOC entry 198 (class 1259 OID 235962)
-- Name: rs_subcollection_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE rs_subcollection_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 99999
    CACHE 1;


--
-- TOC entry 205 (class 1259 OID 236224)
-- Name: rs_subcollection_igsn_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE rs_subcollection_igsn_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 201 (class 1259 OID 235999)
-- Name: rs_subcollection_subcollection_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE rs_subcollection_subcollection_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 99999
    CACHE 1;


--
-- TOC entry 195 (class 1259 OID 235943)
-- Name: rs_subcollection; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE rs_subcollection (
    id integer DEFAULT nextval('rs_subcollection_id_seq'::regclass) NOT NULL,
    old_id character varying(100),
    subcollection_id character varying(100) DEFAULT ('SC'::text || to_char(nextval('rs_subcollection_subcollection_id_seq'::regclass), 'FM00000'::text)),
    location_in_storage character varying(100),
    storage_type character varying(100),
    hazardous boolean,
    source character varying(200),
    total_pallet integer,
    igsn character varying(50) DEFAULT ('CSRWASC'::text || to_char(nextval('rs_subcollection_igsn_seq'::regclass), 'FM00000'::text)) NOT NULL,
    collection_id character varying(100),
    version integer DEFAULT 0,
    last_update_user character varying(100),
    previous_pallet_id character varying(50),
    disposed_insufficient_info boolean DEFAULT false
);


--
-- TOC entry 210 (class 1259 OID 236334)
-- Name: rs_subcollection_audit; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE rs_subcollection_audit (
    id integer NOT NULL,
    old_id character varying(100),
    subcollection_id character varying(100),
    location_in_storage character varying(100),
    storage_type character varying(100),
    hazardous boolean,
    source character varying(200),
    total_pallet integer,
    igsn character varying(50) NOT NULL,
    collection_id character varying(100),
    version integer DEFAULT 0,
    last_update_user character varying(100),
    previous_pallet_id character varying(50),
    disposed_insufficient_info boolean,
    operation character varying(100),
    audit_timestamp timestamp without time zone
);


--
-- TOC entry 202 (class 1259 OID 236002)
-- Name: sample_range_by_subcollection; Type: VIEW; Schema: public; Owner: -
--

CREATE VIEW sample_range_by_subcollection AS
 SELECT rss.subcollection_id,
    min(rs.container_id::text) AS min_container,
    max(rs.container_id::text) AS max_container,
    min(rs.csiro_sample_id::text) AS min_sample,
    max(rs.csiro_sample_id::text) AS max_sample
   FROM rs_subcollection rss left join rs_sample rs on rss.subcollection_id=rs.subcollection_id
  GROUP BY rss.subcollection_id;


--
-- TOC entry 203 (class 1259 OID 236010)
-- Name: staffs_staff_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE staffs_staff_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 204 (class 1259 OID 236019)
-- Name: staffs; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE staffs (
    staff_id integer DEFAULT nextval('staffs_staff_id_seq'::regclass) NOT NULL,
    organization character varying(255) NOT NULL,
    contact_name character varying(255) NOT NULL,
    phone character varying(255),
    email character varying(255),
    address character varying(255),
    city character varying(255),
    state character varying(255),
    zip_code character varying(255)
);


--
-- TOC entry 214 (class 1259 OID 236363)
-- Name: user_permission; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE user_permission (
    staff character varying(50) NOT NULL,
    geologist boolean DEFAULT false,
    can_edit boolean DEFAULT false,
    admin boolean DEFAULT false
);


--
-- TOC entry 193 (class 1259 OID 229428)
-- Name: users_source_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE users_source_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 192 (class 1259 OID 229420)
-- Name: users; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE users (
    source_id integer DEFAULT nextval('users_source_id_seq'::regclass) NOT NULL,
    organization character varying(255) NOT NULL,
    source_description text,
    source_link text,
    contact_name character varying(255) NOT NULL,
    phone character varying(255),
    email character varying(255),
    address character varying(255),
    city character varying(255),
    state character varying(255),
    zip_code character varying(255),
    metadata_id integer,
    admin boolean,
    can_edit boolean,
    geologist boolean
);


CREATE SEQUENCE public.igsn_log_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
  
CREATE TABLE public.igsn_log
(
  id integer NOT NULL DEFAULT nextval('igsn_log_id_seq'::regclass),
  igsn character varying(100),
  handle character varying(150),
  time_stamp timestamp without time zone DEFAULT now(),
  CONSTRAINT pk_igsn_id PRIMARY KEY (id)
)

--
-- TOC entry 3509 (class 0 OID 0)
-- Dependencies: 192
-- Name: COLUMN users.source_link; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN users.source_link IS '

';


--
-- TOC entry 3342 (class 2604 OID 236360)
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY checkout_registry ALTER COLUMN id SET DEFAULT nextval('checkout_registry_id_seq'::regclass);


--
-- TOC entry 3371 (class 2606 OID 236362)
-- Name: pk_checkout_registry; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY checkout_registry
    ADD CONSTRAINT pk_checkout_registry PRIMARY KEY (id);


--
-- TOC entry 3369 (class 2606 OID 236320)
-- Name: pk_rs_checkout; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY rs_checkout
    ADD CONSTRAINT pk_rs_checkout PRIMARY KEY (id);


--
-- TOC entry 3351 (class 2606 OID 235942)
-- Name: pk_rs_collection_id; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY rs_collection
    ADD CONSTRAINT pk_rs_collection_id PRIMARY KEY (id);


--
-- TOC entry 3363 (class 2606 OID 235958)
-- Name: pk_rs_sample_id; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY rs_sample
    ADD CONSTRAINT pk_rs_sample_id PRIMARY KEY (id);


--
-- TOC entry 3357 (class 2606 OID 235950)
-- Name: pk_rs_subcollection_id; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY rs_subcollection
    ADD CONSTRAINT pk_rs_subcollection_id PRIMARY KEY (id);


--
-- TOC entry 3365 (class 2606 OID 236027)
-- Name: pk_staffs_staff_id; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY staffs
    ADD CONSTRAINT pk_staffs_staff_id PRIMARY KEY (staff_id);


--
-- TOC entry 3347 (class 2606 OID 229427)
-- Name: pk_users_source_id; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY users
    ADD CONSTRAINT pk_users_source_id PRIMARY KEY (source_id);


--
-- TOC entry 3353 (class 2606 OID 236299)
-- Name: uk_leog553l723h2jafxcuuh4h4r; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY rs_collection
    ADD CONSTRAINT uk_leog553l723h2jafxcuuh4h4r UNIQUE (collection_id);


--
-- TOC entry 3359 (class 2606 OID 236301)
-- Name: uk_tkjvp67xjk45u17344q63saf; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY rs_subcollection
    ADD CONSTRAINT uk_tkjvp67xjk45u17344q63saf UNIQUE (subcollection_id);


--
-- TOC entry 3355 (class 2606 OID 235969)
-- Name: unique_collection_id; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY rs_collection
    ADD CONSTRAINT unique_collection_id UNIQUE (collection_id);


--
-- TOC entry 3367 (class 2606 OID 236029)
-- Name: unique_staffs_contact_name; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY staffs
    ADD CONSTRAINT unique_staffs_contact_name UNIQUE (contact_name);


--
-- TOC entry 3361 (class 2606 OID 235971)
-- Name: unique_subcollection_id; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY rs_subcollection
    ADD CONSTRAINT unique_subcollection_id UNIQUE (subcollection_id);


--
-- TOC entry 3349 (class 2606 OID 236031)
-- Name: unique_users_contact_name; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY users
    ADD CONSTRAINT unique_users_contact_name UNIQUE (contact_name);


--
-- TOC entry 3373 (class 2606 OID 236367)
-- Name: user_permission_pk; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY user_permission
    ADD CONSTRAINT user_permission_pk PRIMARY KEY (staff);


--
-- TOC entry 3377 (class 2620 OID 236349)
-- Name: rs_collection_audit_trigger; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER rs_collection_audit_trigger AFTER INSERT OR DELETE OR UPDATE ON rs_collection FOR EACH ROW EXECUTE PROCEDURE rs_collection_audit();


--
-- TOC entry 3376 (class 2620 OID 236325)
-- Name: rs_collection_version_trigger; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER rs_collection_version_trigger BEFORE UPDATE ON rs_collection FOR EACH ROW EXECUTE PROCEDURE increment_version();


--
-- TOC entry 3381 (class 2620 OID 236354)
-- Name: rs_sample_audit_trigger; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER rs_sample_audit_trigger AFTER INSERT OR DELETE OR UPDATE ON rs_sample FOR EACH ROW EXECUTE PROCEDURE rs_sample_audit();


--
-- TOC entry 3380 (class 2620 OID 236327)
-- Name: rs_sample_version_trigger; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER rs_sample_version_trigger BEFORE UPDATE ON rs_sample FOR EACH ROW EXECUTE PROCEDURE increment_version();


--
-- TOC entry 3379 (class 2620 OID 236351)
-- Name: rs_subcollection_audit_trigger; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER rs_subcollection_audit_trigger AFTER INSERT OR DELETE OR UPDATE ON rs_subcollection FOR EACH ROW EXECUTE PROCEDURE rs_subcollection_audit();


--
-- TOC entry 3378 (class 2620 OID 236326)
-- Name: rs_subcollection_version_trigger; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER rs_subcollection_version_trigger BEFORE UPDATE ON rs_subcollection FOR EACH ROW EXECUTE PROCEDURE increment_version();


--
-- TOC entry 3375 (class 2606 OID 235977)
-- Name: fk_rs_sample_subcollection_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY rs_sample
    ADD CONSTRAINT fk_rs_sample_subcollection_id FOREIGN KEY (subcollection_id) REFERENCES rs_subcollection(subcollection_id);


--
-- TOC entry 3374 (class 2606 OID 236293)
-- Name: fk_rs_subcollection_id_collection_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY rs_subcollection
    ADD CONSTRAINT fk_rs_subcollection_id_collection_id FOREIGN KEY (collection_id) REFERENCES rs_collection(collection_id);


-- Completed on 2015-09-21 11:33:11

--
-- PostgreSQL database dump complete
--

