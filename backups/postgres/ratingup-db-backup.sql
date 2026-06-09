--
-- PostgreSQL database dump
--

\restrict QRf2qmgDHsLUuOCiaYlAbvLHcGgY4X19em09oyaDhfhpUNLz8tVmJKnmhNdrP2s

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
1	08aecb5b-4ca6-4701-b8a7-42be3979e426	Ataque-ao-rei.png	book
2	52645203-f2a1-47b1-9d47-2204f8c9b968	Ataque-descoberto.png	book
3	f8caab54-78f1-4a81-9c2d-4f545ebac44e	Ataque-duplo.png	book
4	37fed185-8075-4b7d-b3af-195eb0fa2f32	CCT-Checks-Captures-Threats.png	book
5	1c39e819-f4b4-47e9-8b53-04aad1d28ef5	Captura-de-pecas.png	book
6	d8e155cf-4046-4ca6-b2c6-b567d2d208a8	Casas-claras-e-escuras.png	book
7	462301aa-8c8f-4539-80ec-975e44380893	Casas-fracas.png	book
8	fdb5459c-dca1-4eb1-9df9-fc70dcf819a6	Colunas-abertas-e-semi-abertas.png	book
9	af6504fd-632c-4dba-9e77-5452e9d82ad2	Colunas-fileiras-e-diagonais.png	book
10	4767bb45-06d1-4a28-8ecc-37196b5da438	Como-o-xadrez-se-tornou-esporte.png	book
11	07b89ca7-1242-44d8-871a-99eb000bf42c	Conectar-as-torres.png	book
12	d3d29c8f-a512-487b-98ae-e841a8892fed	Conhecendo-as-pecas.png	book
13	d215da66-43a8-433c-aef4-79bc912a1a97	Controle-do-centro.png	book
14	2e004ccf-ed23-484c-ba27-3f48d59e437a	Coordenacao-de-pecas.png	book
15	54ad3fb1-1f22-4055-9528-885a42c4f129	Cravada.png	book
16	cd9c0cf9-1d86-4d83-9ddf-c7bf834c2adb	Descoberta.png	book
17	c4babb7d-92b7-4663-9987-df5152214c04	Desenvolvimento-das-pecas.png	book
18	e0b6c695-fd31-482e-8b58-12c88ef36b96	Empate-por-afogamento.png	book
19	3d1055d1-5df0-432e-b572-5b2dd43f4498	Empate-por-repeticao.png	book
20	ecb7b5ff-d6e3-4a81-8805-a308cf3a272c	En-passant.png	book
21	424780f6-1990-41d7-ba7d-051fb2f81073	Estrutura-de-peoes.png	book
22	8ed59b08-31cf-4cdf-94cb-409893858a50	Estrutura-do-tabuleiro.png	book
23	92241a3c-0e91-42c6-a22a-a3de0474da63	Garfo.png	book
24	3c62291c-1213-447e-9889-31e9b2c73418	Historia-do-xadrez.png	book
25	6ee79af8-2b03-45af-a9d4-cbb795c19378	Mate-em-1.png	book
26	43ef8f34-8beb-4b0c-aa0a-bf9617c48da5	Material-insuficiente.png	book
27	b6b42a93-23df-4059-883c-fbb262b981de	Movimento-da-dama.png	book
28	8bf74bd4-0c42-49cf-8af6-f843330a7712	Movimento-da-torre.png	book
29	722fd3c1-f5a5-4c2c-9d61-8fa9c2f832f5	Movimento-do-bispo.png	book
30	e088b8ee-0c7c-470e-9bae-e416e47241dc	Movimento-do-cavalo.png	book
31	9f690c60-1aac-45f4-a0ee-ec3b1ff9e308	Movimento-do-peao.png	book
32	5b8bf1ad-ab05-4755-8d4f-b16ff91f6f45	Movimento-do-rei.png	book
33	f90966ff-e359-43c5-ad83-4985e5f47a3f	Nao-mover-a-mesma-peca-varias-vezes.png	book
34	f4c0d71e-d3d5-4dc7-99d2-b57a8afc18d2	Nomeacao-das-casas.png	book
35	52805c1b-c242-423e-a594-e487d1198682	O-que-e-Rating.png	book
36	688ede35-b0f4-47ce-818c-d3f0b89c9e30	O-que-e-o-final.png	book
37	a42f3e54-08c9-47dd-8264-7328db619f47	O-que-e-o-meio-jogo.png	book
38	0d74d9db-450f-403f-82b3-c82239ec3987	O-que-e-o-xadrez.png	book
39	7778a9c0-03e5-4a10-838e-fe4a8444383c	Objetivo-do-jogo.png	book
40	da48d19b-eb19-4250-9306-c8fac4dfa1af	Oposicao-de-reis.png	book
41	25bde385-55f7-4911-9754-341bc42a0cb4	Padronizacao-de-aberturas.png	book
42	1d5b8801-703a-41a5-870e-111001e16e79	Pecas-boas-vs-pecas-ruins.png	book
43	44111fe1-6d82-4d0e-ac70-7b0e5cf3158a	Planos-no-meio-jogo.png	book
44	7eaff8a0-7f74-43e1-8091-e6ea7a68cecf	Promocao-do-peao.png	book
45	5712eb28-e1b0-4e8d-a379-f7005970d870	Regra-do-quadrado-do-peao.png	book
46	b0fead2e-d110-4589-bea6-bdeb71913817	Regra-dos-50-lances.png	book
47	638b5e87-087f-42a1-9ba2-cdeffe96c24a	Rei-e-peao-vs-rei.png	book
48	4ab33ebd-5b29-4890-a0b1-ce4d2728884a	Roque-grande.png	book
49	9b94f0fb-502e-4f5d-b7b1-f3dce03e5b31	Roque-pequeno.png	book
50	4a9cd019-faf1-4e08-aab1-aab8cdde31f1	Seguranca-do-rei.png	book
51	43abd616-ac59-48f9-bcf8-de2a507f17ef	Tempo.png	book
52	01627aa1-934a-47f8-bfe9-10be6dd7b733	Tipos-de-final.png	book
53	8b2990ad-a44b-46c6-9bf1-85269c54ac0e	Valor-relativo-das-pecas.png	book
54	8a93690d-631a-48d6-93cc-e67be90248bf	Vocabulario-basico-do-xadrez.png	book
55	e654a871-07ef-46f0-85fe-2703e55bae4c	Vocabulario-popular-do-xadrez.png	book
56	a1a1fb08-8f1e-47a7-ab1a-468020d61841	Xeque-mate.png	book
57	a89d8efa-2000-49ad-bb40-55a97040a939	Xeque.png	book
58	a7cd0469-8f1a-4e58-be85-d10d5804cf2b	default.png	book
\.


--
-- Name: images_id_seq; Type: SEQUENCE SET; Schema: public; Owner: admin
--

SELECT pg_catalog.setval('public.images_id_seq', 58, true);


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

\unrestrict QRf2qmgDHsLUuOCiaYlAbvLHcGgY4X19em09oyaDhfhpUNLz8tVmJKnmhNdrP2s

