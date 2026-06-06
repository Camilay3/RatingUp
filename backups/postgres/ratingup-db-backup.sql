--
-- PostgreSQL database dump
--

\restrict zvGztfw3FzZlBDA4jDvruc4DnohnjQgDrqt8mmJYxlRiKsooBgViEU1wJapoooH

-- Dumped from database version 18.3
-- Dumped by pg_dump version 18.3

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: chapters; Type: TABLE; Schema: public; Owner: admin
--

CREATE TABLE public.chapters (
    id bigint NOT NULL,
    title character varying(255) NOT NULL,
    display_order integer NOT NULL
);


ALTER TABLE public.chapters OWNER TO admin;

--
-- Name: chapters_id_seq; Type: SEQUENCE; Schema: public; Owner: admin
--

CREATE SEQUENCE public.chapters_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.chapters_id_seq OWNER TO admin;

--
-- Name: chapters_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: admin
--

ALTER SEQUENCE public.chapters_id_seq OWNED BY public.chapters.id;


--
-- Name: flyway_schema_history; Type: TABLE; Schema: public; Owner: admin
--

CREATE TABLE public.flyway_schema_history (
    installed_rank integer NOT NULL,
    version character varying(50),
    description character varying(200) NOT NULL,
    type character varying(20) NOT NULL,
    script character varying(1000) NOT NULL,
    checksum integer,
    installed_by character varying(100) NOT NULL,
    installed_on timestamp without time zone DEFAULT now() NOT NULL,
    execution_time integer NOT NULL,
    success boolean NOT NULL
);


ALTER TABLE public.flyway_schema_history OWNER TO admin;

--
-- Name: images; Type: TABLE; Schema: public; Owner: admin
--

CREATE TABLE public.images (
    id integer NOT NULL,
    object_id character varying(255) NOT NULL,
    image_name character varying(255) NOT NULL,
    bucket_name character varying(255)
);


ALTER TABLE public.images OWNER TO admin;

--
-- Name: images_id_seq; Type: SEQUENCE; Schema: public; Owner: admin
--

CREATE SEQUENCE public.images_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.images_id_seq OWNER TO admin;

--
-- Name: images_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: admin
--

ALTER SEQUENCE public.images_id_seq OWNED BY public.images.id;


--
-- Name: progress; Type: TABLE; Schema: public; Owner: admin
--

CREATE TABLE public.progress (
    id bigint NOT NULL,
    chapters integer DEFAULT 1 NOT NULL,
    subtopics integer DEFAULT 1 NOT NULL,
    user_id bigint NOT NULL
);


ALTER TABLE public.progress OWNER TO admin;

--
-- Name: progress_id_seq; Type: SEQUENCE; Schema: public; Owner: admin
--

CREATE SEQUENCE public.progress_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.progress_id_seq OWNER TO admin;

--
-- Name: progress_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: admin
--

ALTER SEQUENCE public.progress_id_seq OWNED BY public.progress.id;


--
-- Name: subtopics; Type: TABLE; Schema: public; Owner: admin
--

CREATE TABLE public.subtopics (
    id bigint NOT NULL,
    title character varying(255) NOT NULL,
    display_order integer NOT NULL,
    chapter_id bigint NOT NULL
);


ALTER TABLE public.subtopics OWNER TO admin;

--
-- Name: subtopics_id_seq; Type: SEQUENCE; Schema: public; Owner: admin
--

CREATE SEQUENCE public.subtopics_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.subtopics_id_seq OWNER TO admin;

--
-- Name: subtopics_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: admin
--

ALTER SEQUENCE public.subtopics_id_seq OWNED BY public.subtopics.id;


--
-- Name: users; Type: TABLE; Schema: public; Owner: admin
--

CREATE TABLE public.users (
    id bigint NOT NULL,
    name character varying(255) NOT NULL,
    nickname character varying(255) NOT NULL,
    email character varying(255) NOT NULL,
    telefone character varying(11) NOT NULL,
    password character varying(255) NOT NULL,
    role character varying(255),
    reset_token character varying(255),
    reset_token_expiry timestamp without time zone,
    avatarurl character varying(255),
    creation_date timestamp without time zone DEFAULT now() NOT NULL
);


ALTER TABLE public.users OWNER TO admin;

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: admin
--

CREATE SEQUENCE public.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.users_id_seq OWNER TO admin;

--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: admin
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- Name: chapters id; Type: DEFAULT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.chapters ALTER COLUMN id SET DEFAULT nextval('public.chapters_id_seq'::regclass);


--
-- Name: images id; Type: DEFAULT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.images ALTER COLUMN id SET DEFAULT nextval('public.images_id_seq'::regclass);


--
-- Name: progress id; Type: DEFAULT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.progress ALTER COLUMN id SET DEFAULT nextval('public.progress_id_seq'::regclass);


--
-- Name: subtopics id; Type: DEFAULT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.subtopics ALTER COLUMN id SET DEFAULT nextval('public.subtopics_id_seq'::regclass);


--
-- Name: users id; Type: DEFAULT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- Data for Name: chapters; Type: TABLE DATA; Schema: public; Owner: admin
--

COPY public.chapters (id, title, display_order) FROM stdin;
1	Introdução ao Xadrez	1
2	O Tabuleiro e as Peças	2
3	Movimento das peças	3
4	Regras Fundamentais	4
5	Regras Especiais	5
6	Princípios Básicos de Abertura	6
7	Noções Básicas de Tática	7
8	Como desenvolver o meio-jogo	8
9	Formas de ganhar o final de um jogo	9
\.


--
-- Data for Name: flyway_schema_history; Type: TABLE DATA; Schema: public; Owner: admin
--

COPY public.flyway_schema_history (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) FROM stdin;
1	1	Create tables	SQL	V1__Create_tables.sql	563508879	admin	2026-06-06 23:22:46.382649	33	t
2	2	Insert caps	SQL	V2__Insert_caps.sql	328253502	admin	2026-06-06 23:22:46.472234	3	t
3	3	Insert subs	SQL	V3__Insert_subs.sql	-556153942	admin	2026-06-06 23:22:46.497279	8	t
4	4	Create images table	SQL	V4__Create_images_table.sql	370351547	admin	2026-06-06 23:22:46.525232	11	t
5	5	Add avatarurl user	SQL	V5__Add_avatarurl_user.sql	-656058000	admin	2026-06-06 23:22:46.545452	2	t
6	6	Add creationdate user	SQL	V6__Add_creationdate_user.sql	-610006652	admin	2026-06-06 23:22:46.555734	3	t
7	7	Add column bucketName	SQL	V7__Add_column_bucketName.sql	1120654227	admin	2026-06-06 23:22:46.566788	2	t
8	\N	Insert Admin	SQL	R__Insert_Admin.sql	1077741121	admin	2026-06-06 23:22:46.575217	6	t
\.


--
-- Data for Name: images; Type: TABLE DATA; Schema: public; Owner: admin
--

COPY public.images (id, object_id, image_name, bucket_name) FROM stdin;
1	a77cd5a8-7592-4b3c-ba58-591fc48ed46f	Captura-de-pecas.png	book
2	00605832-f2f8-4ef5-9283-10b6246e35fa	Casas-claras-e-escuras.png	book
3	9fad9e69-d2be-4d18-afe0-5e04d6ca2024	Colunas-fileiras-e-diagonais.png	book
4	9aae66e3-d477-40a7-98f7-70a4f40016a8	Como-o-xadrez-se-tornou-esporte.png	book
5	aecdc422-b2e6-4bf1-aeb7-560ec809557a	Conhecendo-as-pecas.png	book
6	6540ead4-374d-4c9a-921e-125b1b433962	Empate-por-afogamento.png	book
7	f7bf644b-c625-4e09-ab7e-b5013911ca47	Estrutura-do-tabuleiro.png	book
8	89fe9b8b-35c2-4b07-aa83-10e403adc08d	Historia-do-xadrez.png	book
9	e4c469e7-ec90-48e9-a1a1-36aedafa43d9	Movimento-da-dama.png	book
10	e9680305-8781-4fe2-bece-4d96f351301c	Movimento-da-torre.png	book
11	ac44e82a-6fc4-44cc-b3c1-c8c1f1b8c59d	Movimento-do-bispo.png	book
12	046244a2-386b-4345-9e27-bb2830655411	Movimento-do-cavalo.png	book
13	2fe3b053-deca-4970-b45f-f62a9bcfb088	Movimento-do-peao.png	book
14	797ce217-baa4-4bc5-a5d8-acdf47c10cfb	Movimento-do-rei.png	book
15	a7d3baee-c25d-4225-828a-94fe78410d83	Nomeacao-das-casas.png	book
16	b46c4ee5-11cd-437f-8255-58e7e6c6f38b	O-que-e-Rating.png	book
17	85d5f706-1c74-4752-83f8-d18214fed12a	O-que-e-o-xadrez.png	book
18	52cdca87-3471-4ffe-b830-a25bd3250f1c	Objetivo-do-jogo.png	book
19	e71a5b06-fa12-4c36-b55f-0b66015591b1	Roque-pequeno.png	book
20	c2a18409-3549-43b5-9fc2-51d2a02697e4	Valor-relativo-das-pecas.png	book
21	1904792b-20d1-4cdf-83bd-40dd9e930984	Vocabulario-basico-do-xadrez.png	book
22	9b9f26b2-512e-4595-aade-cc374b1fb486	Vocabulario-popular-do-xadrez.png	book
23	3312bce8-1e7a-48f5-b513-af0ca606900e	Xeque-mate.png	book
24	fc938b5d-5322-4ce9-adba-697aeccb6848	Xeque.png	book
25	448c9d32-4806-4130-8efe-4b37d1862875	default.png	book
\.


--
-- Data for Name: progress; Type: TABLE DATA; Schema: public; Owner: admin
--

COPY public.progress (id, chapters, subtopics, user_id) FROM stdin;
1	9	6	1
\.


--
-- Data for Name: subtopics; Type: TABLE DATA; Schema: public; Owner: admin
--

COPY public.subtopics (id, title, display_order, chapter_id) FROM stdin;
1	O que é o xadrez	1	1
2	Objetivo do jogo	2	1
3	História do xadrez	3	1
4	Como o xadrez se tornou esporte	4	1
5	O que é Rating	5	1
6	Vocabulário básico do xadrez	6	1
7	Vocabulário popular do xadrez	7	1
8	Estrutura do tabuleiro	1	2
9	Casas claras e escuras	2	2
10	Colunas, fileiras e diagonais	3	2
11	Nomeação das casas	4	2
12	Conhecendo as peças	5	2
13	Valor relativo das peças	6	2
14	Movimento do peão	1	3
15	Movimento da torre	2	3
16	Movimento do cavalo	3	3
17	Movimento do bispo	4	3
18	Movimento da dama	5	3
19	Movimento do rei	6	3
20	Captura de peças	7	3
21	Xeque	1	4
22	Xeque-mate	2	4
23	Empate por afogamento	3	4
24	Empate por repetição	4	4
25	Regra dos 50 lances	5	4
26	Material insuficiente	6	4
27	Tempo	7	4
28	Roque pequeno	1	5
29	Roque grande	2	5
30	En passant	3	5
31	Promoção do peão	4	5
32	Controle do centro	1	6
33	Desenvolvimento das peças	2	6
34	Segurança do rei	3	6
35	Não mover a mesma peça várias vezes	4	6
36	Conectar as torres	5	6
37	Padronização de aberturas	6	6
38	Ataque duplo	1	7
39	Cravada	2	7
40	Garfo	3	7
41	Descoberta	4	7
42	Ataque descoberto	5	7
43	Mate em 1	6	7
44	Estrutura de peões	7	7
45	CCT (Checks, Captures, Threats)	8	7
46	O que é o meio-jogo	1	8
47	Planos no meio-jogo	2	8
48	Ataque ao rei	3	8
49	Casas fracas	4	8
50	Colunas abertas e semi-abertas	5	8
51	Peças boas vs peças ruins	6	8
52	Coordenação de peças	7	8
53	O que é o final	1	9
54	Tipos de final	2	9
55	Oposição de reis	3	9
56	Rei e peão vs rei	4	9
57	Regra do quadrado do peão	5	9
58	Padrões básicos de mate	6	9
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: admin
--

COPY public.users (id, name, nickname, email, telefone, password, role, reset_token, reset_token_expiry, avatarurl, creation_date) FROM stdin;
1	Administrador	Master	ratingupadmin@gmail.com	8599998888	$2a$10$xyTt8KT9Qzza60DqXaRg..faEUSULs1DAgunQLmYkbgg..wPIHRm.	ADMIN	\N	\N	\N	2026-06-06 23:22:46.578259
\.


--
-- Name: chapters_id_seq; Type: SEQUENCE SET; Schema: public; Owner: admin
--

SELECT pg_catalog.setval('public.chapters_id_seq', 9, true);


--
-- Name: images_id_seq; Type: SEQUENCE SET; Schema: public; Owner: admin
--

SELECT pg_catalog.setval('public.images_id_seq', 25, true);


--
-- Name: progress_id_seq; Type: SEQUENCE SET; Schema: public; Owner: admin
--

SELECT pg_catalog.setval('public.progress_id_seq', 1, true);


--
-- Name: subtopics_id_seq; Type: SEQUENCE SET; Schema: public; Owner: admin
--

SELECT pg_catalog.setval('public.subtopics_id_seq', 58, true);


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: admin
--

SELECT pg_catalog.setval('public.users_id_seq', 1, true);


--
-- Name: chapters chapters_pkey; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.chapters
    ADD CONSTRAINT chapters_pkey PRIMARY KEY (id);


--
-- Name: flyway_schema_history flyway_schema_history_pk; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.flyway_schema_history
    ADD CONSTRAINT flyway_schema_history_pk PRIMARY KEY (installed_rank);


--
-- Name: images images_image_name_key; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.images
    ADD CONSTRAINT images_image_name_key UNIQUE (image_name);


--
-- Name: images images_object_id_key; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.images
    ADD CONSTRAINT images_object_id_key UNIQUE (object_id);


--
-- Name: images images_pkey; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.images
    ADD CONSTRAINT images_pkey PRIMARY KEY (id);


--
-- Name: progress progress_pkey; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.progress
    ADD CONSTRAINT progress_pkey PRIMARY KEY (id);


--
-- Name: progress progress_user_id_key; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.progress
    ADD CONSTRAINT progress_user_id_key UNIQUE (user_id);


--
-- Name: subtopics subtopics_pkey; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.subtopics
    ADD CONSTRAINT subtopics_pkey PRIMARY KEY (id);


--
-- Name: users uk_user_email; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk_user_email UNIQUE (email);


--
-- Name: users uk_user_nickname; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk_user_nickname UNIQUE (nickname);


--
-- Name: users uk_user_tel; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk_user_tel UNIQUE (telefone);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: flyway_schema_history_s_idx; Type: INDEX; Schema: public; Owner: admin
--

CREATE INDEX flyway_schema_history_s_idx ON public.flyway_schema_history USING btree (success);


--
-- Name: progress progress_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.progress
    ADD CONSTRAINT progress_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: subtopics subtopics_chapter_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.subtopics
    ADD CONSTRAINT subtopics_chapter_id_fkey FOREIGN KEY (chapter_id) REFERENCES public.chapters(id);


--
-- PostgreSQL database dump complete
--

\unrestrict zvGztfw3FzZlBDA4jDvruc4DnohnjQgDrqt8mmJYxlRiKsooBgViEU1wJapoooH

