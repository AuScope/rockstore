--
-- PostgreSQL database dump
--

-- Dumped from database version 9.4.3
-- Dumped by pg_dump version 9.4.0
-- Started on 2015-09-08 14:37:35

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

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
    staffid_field_manager character varying(100)
);


--
-- TOC entry 196 (class 1259 OID 235951)
-- Name: rs_sample; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE rs_sample (
    id integer DEFAULT nextval('rs_sample_id_seq'::regclass) NOT NULL,
    igsn character varying(200),
    csiro_sample_id character varying(200),
    sample_type character varying(50),
    bhid character varying(255),
    depth double precision,
    datum character varying(50),
    zone character varying(50),
    container_id character varying(100),
    subcollection_id character varying(100),
    external_ref character varying(200),
    sample_collector character varying(200),
    date_sampled timestamp without time zone,
    sample_dispose boolean,
    date_disposed timestamp without time zone,
    staffid_disposed character varying(100),
    collection_id character varying(100),
    location geometry(Point,4326)
);


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
    collection_id character varying(100),
    source character varying(200),
    total_pallet integer,
    rscollection bytea,
    samplerangebysubcollection bytea,
    igsn character varying(15) DEFAULT ('CSRWASC'::text || to_char(nextval('rs_subcollection_igsn_seq'::regclass), 'FM00000'::text)) NOT NULL
);


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
    metadata_id integer
);


--
-- TOC entry 3389 (class 0 OID 0)
-- Dependencies: 192
-- Name: COLUMN users.source_link; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN users.source_link IS '

';


--
-- TOC entry 3252 (class 2606 OID 235942)
-- Name: pk_rs_collection_id; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY rs_collection
    ADD CONSTRAINT pk_rs_collection_id PRIMARY KEY (id);


--
-- TOC entry 3260 (class 2606 OID 235958)
-- Name: pk_rs_sample_id; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY rs_sample
    ADD CONSTRAINT pk_rs_sample_id PRIMARY KEY (id);


--
-- TOC entry 3256 (class 2606 OID 235950)
-- Name: pk_rs_subcollection_id; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY rs_subcollection
    ADD CONSTRAINT pk_rs_subcollection_id PRIMARY KEY (id);


--
-- TOC entry 3262 (class 2606 OID 236027)
-- Name: pk_staffs_staff_id; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY staffs
    ADD CONSTRAINT pk_staffs_staff_id PRIMARY KEY (staff_id);


--
-- TOC entry 3248 (class 2606 OID 229427)
-- Name: pk_users_source_id; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY users
    ADD CONSTRAINT pk_users_source_id PRIMARY KEY (source_id);


--
-- TOC entry 3254 (class 2606 OID 235969)
-- Name: unique_collection_id; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY rs_collection
    ADD CONSTRAINT unique_collection_id UNIQUE (collection_id);


--
-- TOC entry 3264 (class 2606 OID 236029)
-- Name: unique_staffs_contact_name; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY staffs
    ADD CONSTRAINT unique_staffs_contact_name UNIQUE (contact_name);


--
-- TOC entry 3258 (class 2606 OID 235971)
-- Name: unique_subcollection_id; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY rs_subcollection
    ADD CONSTRAINT unique_subcollection_id UNIQUE (subcollection_id);


--
-- TOC entry 3250 (class 2606 OID 236031)
-- Name: unique_users_contact_name; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY users
    ADD CONSTRAINT unique_users_contact_name UNIQUE (contact_name);


--
-- TOC entry 3266 (class 2606 OID 235972)
-- Name: fk_rs_sample_collection_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY rs_sample
    ADD CONSTRAINT fk_rs_sample_collection_id FOREIGN KEY (collection_id) REFERENCES rs_collection(collection_id);


--
-- TOC entry 3267 (class 2606 OID 235977)
-- Name: fk_rs_sample_subcollection_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY rs_sample
    ADD CONSTRAINT fk_rs_sample_subcollection_id FOREIGN KEY (subcollection_id) REFERENCES rs_subcollection(subcollection_id);


--
-- TOC entry 3265 (class 2606 OID 235982)
-- Name: fk_rs_subcollection_collection_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY rs_subcollection
    ADD CONSTRAINT fk_rs_subcollection_collection_id FOREIGN KEY (collection_id) REFERENCES rs_collection(collection_id);


-- Completed on 2015-09-08 14:37:35

--
-- PostgreSQL database dump complete
--

