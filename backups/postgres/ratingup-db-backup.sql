--
-- PostgreSQL database dump
--

\restrict Xj4gnI6r0KvmBGxlmgnfSc36u5HV6RCP6wv2Nw38ghc2yuTLDVZezVvRYRtdrTI

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
-- Name: images id; Type: DEFAULT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.images ALTER COLUMN id SET DEFAULT nextval('public.images_id_seq'::regclass);


--
-- Data for Name: images; Type: TABLE DATA; Schema: public; Owner: admin
--

COPY public.images (id, object_id, image_name, bucket_name) FROM stdin;
1	54715b0f-07c4-4bb0-bda5-6962c98f6562	Captura-de-peças.png	book
2	cdea01de-a1d1-4e2a-b34d-3781cc7478d0	Casas-claras-e-escuras.png	book
3	23c8fdff-5f0c-40f1-9b47-7ba0459bd8d9	Colunas-fileiras-e-diagonais.png	book
4	b4880fb4-a702-448d-af9b-8776a884f313	Como-o-xadrez-se-tornou-esporte.png	book
5	21fdcf56-5ecb-44c9-96ed-b6aa4ac4e4df	Conhecendo-as-peças.png	book
6	dee72673-fc53-4644-94fb-358703d3e4db	Empate-por-afogamento.png	book
7	535076c9-bbb8-4ca8-ab07-83f19c302e26	Estrutura-do-tabuleiro.png	book
8	5601dbb7-85fe-4225-9686-4f8e3a117cdc	História-do-xadrez.png	book
9	35004a8c-58a3-4752-80b2-0455f59e4475	Movimento-da-dama.png	book
10	516da701-ec72-4e04-b5cc-5d1693f53158	Movimento-da-torre.png	book
11	37553615-19fc-450c-967f-fe26cccc11e7	Movimento-do-bispo.png	book
12	4aa45b67-2042-42f0-bd69-dd8986c0ca3f	Movimento-do-cavalo.png	book
13	378879f2-b0c0-477f-99e4-94de320dccec	Movimento-do-peão.png	book
14	765ef74a-af73-45d3-b9ba-a7f9d6ed6d55	Movimento-do-rei.png	book
15	973239f9-7341-454b-82ac-b79129577857	Nomeação-das-casas.png	book
16	397e6f0d-376c-4d8a-a763-94d4b59f290e	O-que-é-Rating.png	book
17	520c17c1-d0f4-4995-af20-b61f7d55b756	O-que-é-o-xadrez.png	book
18	0d30cb40-0193-41a5-90ff-f0cf6200bcd7	Objetivo-do-jogo.png	book
19	eb65c52a-963b-4f03-b2c5-98ec71ef9c75	Roque-pequeno.png	book
20	369c01e9-28ba-43d1-8f6a-4c7ddfffd0d9	Valor-relativo-das-peças.png	book
21	8fa66898-8136-4cd7-bd6e-8f7063568489	Vocabulário-básico-do-xadrez.png	book
22	4e151365-344c-4b04-a21c-bdd12b85030c	Vocabulário-popular-do-xadrez.png	book
23	bd474320-1982-4cba-8869-85e9cf4cdc2d	Xeque-mate.png	book
24	e2e9c864-d1fb-45e0-8b77-3e8feabb9af5	Xeque.png	book
25	8132d20b-47eb-4a04-9471-5f89db87cb73	default.png	book
\.


--
-- Name: images_id_seq; Type: SEQUENCE SET; Schema: public; Owner: admin
--

SELECT pg_catalog.setval('public.images_id_seq', 25, true);


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
-- PostgreSQL database dump complete
--

\unrestrict Xj4gnI6r0KvmBGxlmgnfSc36u5HV6RCP6wv2Nw38ghc2yuTLDVZezVvRYRtdrTI

