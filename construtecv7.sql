PGDMP                     	    t         
   construtec    9.6.0    9.6.0 J    �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                       false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                       false            �           1262    16480 
   construtec    DATABASE     �   CREATE DATABASE construtec WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'English_United States.1252' LC_CTYPE = 'English_United States.1252';
    DROP DATABASE construtec;
             Isaac    false                        2615    2200    public    SCHEMA        CREATE SCHEMA public;
    DROP SCHEMA public;
             postgres    false            �           0    0    SCHEMA public    COMMENT     6   COMMENT ON SCHEMA public IS 'standard public schema';
                  postgres    false    3                        3079    12387    plpgsql 	   EXTENSION     ?   CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;
    DROP EXTENSION plpgsql;
                  false            �           0    0    EXTENSION plpgsql    COMMENT     @   COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';
                       false    1            �            1255    17339 #   getuser(integer, character varying)    FUNCTION     �  CREATE FUNCTION getuser(request_id integer, request_password character varying) RETURNS TABLE(q_id integer, q_name character varying, q_code integer, q_role integer)
    LANGUAGE sql
    AS $$select use.u_id,use.u_name,(case when uxd.u_code is null then 0 else uxd.u_code::int end),uxr.r_id
from dbuser as use left outer join userxrole as uxr on (use.u_id = uxr.u_id) left outer join userxplusdata as uxd on (use.u_id = uxd.u_id)
where use.u_id = request_id and use.u_password =request_password;$$;
 V   DROP FUNCTION public.getuser(request_id integer, request_password character varying);
       public       postgres    false    3            �            1255    17338 g   insertuser(integer, character varying, character varying, integer, character varying, integer, integer)    FUNCTION     O  CREATE FUNCTION insertuser(m_id integer DEFAULT 0, m_name character varying DEFAULT ''::character varying, m_lname character varying DEFAULT ''::character varying, m_phone integer DEFAULT 0, m_password character varying DEFAULT ''::character varying, m_code integer DEFAULT 0, rm_id integer DEFAULT 0) RETURNS void
    LANGUAGE plpgsql
    AS $$
begin
	if m_id <> 0 then
    	if (exists(select * from dbuser where dbuser.u_id = m_id)) and (not exists(select * from userxrole where userxrole.r_id = rm_id and userxrole.u_id=m_id)) then
        	if m_code <> 0 and (not exists(select * from userxplusdata where userxplusdata.u_code = m_code and userxplusdata.u_id = m_id)) and rm_id = 2 then
        		insert into userxrole(r_id,u_id) values(rm_id,m_id);
                insert into userxplusdata(u_code,u_id) values(m_code,m_id);
            elseif m_code = 0 and rm_id = 3 and (not exists(select * from userxrole where userxrole.r_id = rm_id)) then
            	insert into userxrole(r_id,u_id) values(rm_id,m_id);
            elseif m_code = 0 and rm_id = 4 and (not exists(select * from userxrole where userxrole.r_id = rm_id)) then
            	insert into userxrole(r_id,u_id) values(rm_id,m_id);
            end if;
        elseif (not exists(select * from dbuser where dbuser.u_id = m_id))then
        	if rm_id = 2 and m_code <>0 then
            	insert into dbuser values(m_id,m_name, m_lname, m_phone, m_password);
                insert into userxplusdata values(m_code, m_id);
                insert into userxrole(r_id,u_id) values(rm_id,m_id);
            end if;
        end if;
    end if;
end;
$$;
 �   DROP FUNCTION public.insertuser(m_id integer, m_name character varying, m_lname character varying, m_phone integer, m_password character varying, m_code integer, rm_id integer);
       public       postgres    false    1    3            �            1259    16544 	   dbcomment    TABLE     �   CREATE TABLE dbcomment (
    c_id integer NOT NULL,
    c_description character varying(255) NOT NULL,
    s_name character varying(255) NOT NULL,
    p_id integer NOT NULL,
    u_id integer NOT NULL
);
    DROP TABLE public.dbcomment;
       public         postgres    false    3            �            1259    16542    dbcomment_c_id_seq    SEQUENCE     t   CREATE SEQUENCE dbcomment_c_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 )   DROP SEQUENCE public.dbcomment_c_id_seq;
       public       postgres    false    3    196            �           0    0    dbcomment_c_id_seq    SEQUENCE OWNED BY     ;   ALTER SEQUENCE dbcomment_c_id_seq OWNED BY dbcomment.c_id;
            public       postgres    false    195            �            1259    16512    dbrole    TABLE     _   CREATE TABLE dbrole (
    r_id integer NOT NULL,
    r_name character varying(255) NOT NULL
);
    DROP TABLE public.dbrole;
       public         postgres    false    3            �            1259    16510    dbrole_r_id_seq    SEQUENCE     q   CREATE SEQUENCE dbrole_r_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 &   DROP SEQUENCE public.dbrole_r_id_seq;
       public       postgres    false    190    3            �           0    0    dbrole_r_id_seq    SEQUENCE OWNED BY     5   ALTER SEQUENCE dbrole_r_id_seq OWNED BY dbrole.r_id;
            public       postgres    false    189            �            1259    16489    dbuser    TABLE     �   CREATE TABLE dbuser (
    u_id integer NOT NULL,
    u_name character varying(255) NOT NULL,
    u_lname character varying(255) NOT NULL,
    u_phone integer NOT NULL,
    u_password character varying(255) NOT NULL
);
    DROP TABLE public.dbuser;
       public         postgres    false    3            �            1259    16561    product    TABLE     �   CREATE TABLE product (
    pr_id integer NOT NULL,
    pr_description character varying(255) NOT NULL,
    pr_name character varying(255) NOT NULL,
    pr_price integer NOT NULL,
    pr_quantity integer NOT NULL
);
    DROP TABLE public.product;
       public         postgres    false    3            �            1259    16720    project_p_id_seq    SEQUENCE     r   CREATE SEQUENCE project_p_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 '   DROP SEQUENCE public.project_p_id_seq;
       public       postgres    false    3            �            1259    16518    project    TABLE       CREATE TABLE project (
    p_id integer DEFAULT nextval('project_p_id_seq'::regclass) NOT NULL,
    p_location character varying(255) NOT NULL,
    p_name character varying(255) NOT NULL,
    p_budget integer DEFAULT 0 NOT NULL,
    u_code integer NOT NULL,
    u_id integer NOT NULL
);
    DROP TABLE public.project;
       public         postgres    false    200    3            �            1259    16528    projectxstage    TABLE     �   CREATE TABLE projectxstage (
    pxs_id integer NOT NULL,
    p_id integer NOT NULL,
    s_name character varying(255) NOT NULL
);
 !   DROP TABLE public.projectxstage;
       public         postgres    false    3            �            1259    16526    projectxstage_pxs_id_seq    SEQUENCE     z   CREATE SEQUENCE projectxstage_pxs_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 /   DROP SEQUENCE public.projectxstage_pxs_id_seq;
       public       postgres    false    193    3            �           0    0    projectxstage_pxs_id_seq    SEQUENCE OWNED BY     G   ALTER SEQUENCE projectxstage_pxs_id_seq OWNED BY projectxstage.pxs_id;
            public       postgres    false    192            �            1259    16534    stage    TABLE       CREATE TABLE stage (
    s_name character varying(255) NOT NULL,
    s_status character varying DEFAULT 'created'::character varying NOT NULL,
    s_datestart date,
    s_dateend date,
    s_description character varying(255) NOT NULL,
    s_budget integer DEFAULT 0 NOT NULL
);
    DROP TABLE public.stage;
       public         postgres    false    3            �            1259    16555    stagexproduct    TABLE     �   CREATE TABLE stagexproduct (
    sxpr_id integer NOT NULL,
    s_name character varying(255) NOT NULL,
    pr_id integer NOT NULL,
    p_id integer NOT NULL
);
 !   DROP TABLE public.stagexproduct;
       public         postgres    false    3            �            1259    16553    stagexproduct_sxpr_id_seq    SEQUENCE     {   CREATE SEQUENCE stagexproduct_sxpr_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 0   DROP SEQUENCE public.stagexproduct_sxpr_id_seq;
       public       postgres    false    198    3            �           0    0    stagexproduct_sxpr_id_seq    SEQUENCE OWNED BY     I   ALTER SEQUENCE stagexproduct_sxpr_id_seq OWNED BY stagexproduct.sxpr_id;
            public       postgres    false    197            �            1259    16497    userxplusdata    TABLE     W   CREATE TABLE userxplusdata (
    u_code integer NOT NULL,
    u_id integer NOT NULL
);
 !   DROP TABLE public.userxplusdata;
       public         postgres    false    3            �            1259    16504 	   userxrole    TABLE     n   CREATE TABLE userxrole (
    uxr_id integer NOT NULL,
    r_id integer NOT NULL,
    u_id integer NOT NULL
);
    DROP TABLE public.userxrole;
       public         postgres    false    3            �            1259    16502    userxrole_uxr_id_seq    SEQUENCE     v   CREATE SEQUENCE userxrole_uxr_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 +   DROP SEQUENCE public.userxrole_uxr_id_seq;
       public       postgres    false    3    188            �           0    0    userxrole_uxr_id_seq    SEQUENCE OWNED BY     ?   ALTER SEQUENCE userxrole_uxr_id_seq OWNED BY userxrole.uxr_id;
            public       postgres    false    187                       2604    16547    dbcomment c_id    DEFAULT     b   ALTER TABLE ONLY dbcomment ALTER COLUMN c_id SET DEFAULT nextval('dbcomment_c_id_seq'::regclass);
 =   ALTER TABLE public.dbcomment ALTER COLUMN c_id DROP DEFAULT;
       public       postgres    false    196    195    196                       2604    16515    dbrole r_id    DEFAULT     \   ALTER TABLE ONLY dbrole ALTER COLUMN r_id SET DEFAULT nextval('dbrole_r_id_seq'::regclass);
 :   ALTER TABLE public.dbrole ALTER COLUMN r_id DROP DEFAULT;
       public       postgres    false    189    190    190            
           2604    16531    projectxstage pxs_id    DEFAULT     n   ALTER TABLE ONLY projectxstage ALTER COLUMN pxs_id SET DEFAULT nextval('projectxstage_pxs_id_seq'::regclass);
 C   ALTER TABLE public.projectxstage ALTER COLUMN pxs_id DROP DEFAULT;
       public       postgres    false    193    192    193                       2604    16558    stagexproduct sxpr_id    DEFAULT     p   ALTER TABLE ONLY stagexproduct ALTER COLUMN sxpr_id SET DEFAULT nextval('stagexproduct_sxpr_id_seq'::regclass);
 D   ALTER TABLE public.stagexproduct ALTER COLUMN sxpr_id DROP DEFAULT;
       public       postgres    false    198    197    198                       2604    16507    userxrole uxr_id    DEFAULT     f   ALTER TABLE ONLY userxrole ALTER COLUMN uxr_id SET DEFAULT nextval('userxrole_uxr_id_seq'::regclass);
 ?   ALTER TABLE public.userxrole ALTER COLUMN uxr_id DROP DEFAULT;
       public       postgres    false    188    187    188            �          0    16544 	   dbcomment 
   TABLE DATA               E   COPY dbcomment (c_id, c_description, s_name, p_id, u_id) FROM stdin;
    public       postgres    false    196   �Y       �           0    0    dbcomment_c_id_seq    SEQUENCE SET     :   SELECT pg_catalog.setval('dbcomment_c_id_seq', 1, false);
            public       postgres    false    195            �          0    16512    dbrole 
   TABLE DATA               '   COPY dbrole (r_id, r_name) FROM stdin;
    public       postgres    false    190   �Y       �           0    0    dbrole_r_id_seq    SEQUENCE SET     7   SELECT pg_catalog.setval('dbrole_r_id_seq', 1, false);
            public       postgres    false    189            �          0    16489    dbuser 
   TABLE DATA               E   COPY dbuser (u_id, u_name, u_lname, u_phone, u_password) FROM stdin;
    public       postgres    false    185   Z       �          0    16561    product 
   TABLE DATA               Q   COPY product (pr_id, pr_description, pr_name, pr_price, pr_quantity) FROM stdin;
    public       postgres    false    199   jZ       �          0    16518    project 
   TABLE DATA               L   COPY project (p_id, p_location, p_name, p_budget, u_code, u_id) FROM stdin;
    public       postgres    false    191   �Z       �           0    0    project_p_id_seq    SEQUENCE SET     8   SELECT pg_catalog.setval('project_p_id_seq', 1, false);
            public       postgres    false    200            �          0    16528    projectxstage 
   TABLE DATA               6   COPY projectxstage (pxs_id, p_id, s_name) FROM stdin;
    public       postgres    false    193   �Z       �           0    0    projectxstage_pxs_id_seq    SEQUENCE SET     @   SELECT pg_catalog.setval('projectxstage_pxs_id_seq', 1, false);
            public       postgres    false    192            �          0    16534    stage 
   TABLE DATA               [   COPY stage (s_name, s_status, s_datestart, s_dateend, s_description, s_budget) FROM stdin;
    public       postgres    false    194   �Z       �          0    16555    stagexproduct 
   TABLE DATA               >   COPY stagexproduct (sxpr_id, s_name, pr_id, p_id) FROM stdin;
    public       postgres    false    198   �Z       �           0    0    stagexproduct_sxpr_id_seq    SEQUENCE SET     A   SELECT pg_catalog.setval('stagexproduct_sxpr_id_seq', 1, false);
            public       postgres    false    197            �          0    16497    userxplusdata 
   TABLE DATA               .   COPY userxplusdata (u_code, u_id) FROM stdin;
    public       postgres    false    186   �Z       �          0    16504 	   userxrole 
   TABLE DATA               0   COPY userxrole (uxr_id, r_id, u_id) FROM stdin;
    public       postgres    false    188   &[       �           0    0    userxrole_uxr_id_seq    SEQUENCE SET     ;   SELECT pg_catalog.setval('userxrole_uxr_id_seq', 5, true);
            public       postgres    false    187                       2606    16552    dbcomment dbcomment_pkey 
   CONSTRAINT     Q   ALTER TABLE ONLY dbcomment
    ADD CONSTRAINT dbcomment_pkey PRIMARY KEY (c_id);
 B   ALTER TABLE ONLY public.dbcomment DROP CONSTRAINT dbcomment_pkey;
       public         postgres    false    196    196                       2606    16517    dbrole dbrole_pkey 
   CONSTRAINT     K   ALTER TABLE ONLY dbrole
    ADD CONSTRAINT dbrole_pkey PRIMARY KEY (r_id);
 <   ALTER TABLE ONLY public.dbrole DROP CONSTRAINT dbrole_pkey;
       public         postgres    false    190    190                       2606    16496    dbuser dbuser_pkey 
   CONSTRAINT     K   ALTER TABLE ONLY dbuser
    ADD CONSTRAINT dbuser_pkey PRIMARY KEY (u_id);
 <   ALTER TABLE ONLY public.dbuser DROP CONSTRAINT dbuser_pkey;
       public         postgres    false    185    185            "           2606    16568    product product_pkey 
   CONSTRAINT     N   ALTER TABLE ONLY product
    ADD CONSTRAINT product_pkey PRIMARY KEY (pr_id);
 >   ALTER TABLE ONLY public.product DROP CONSTRAINT product_pkey;
       public         postgres    false    199    199                       2606    16714    project project_pkey 
   CONSTRAINT     M   ALTER TABLE ONLY project
    ADD CONSTRAINT project_pkey PRIMARY KEY (p_id);
 >   ALTER TABLE ONLY public.project DROP CONSTRAINT project_pkey;
       public         postgres    false    191    191                       2606    16533     projectxstage projectxstage_pkey 
   CONSTRAINT     [   ALTER TABLE ONLY projectxstage
    ADD CONSTRAINT projectxstage_pkey PRIMARY KEY (pxs_id);
 J   ALTER TABLE ONLY public.projectxstage DROP CONSTRAINT projectxstage_pkey;
       public         postgres    false    193    193                       2606    16541    stage stage_pkey 
   CONSTRAINT     K   ALTER TABLE ONLY stage
    ADD CONSTRAINT stage_pkey PRIMARY KEY (s_name);
 :   ALTER TABLE ONLY public.stage DROP CONSTRAINT stage_pkey;
       public         postgres    false    194    194                        2606    16560     stagexproduct stagexproduct_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY stagexproduct
    ADD CONSTRAINT stagexproduct_pkey PRIMARY KEY (sxpr_id);
 J   ALTER TABLE ONLY public.stagexproduct DROP CONSTRAINT stagexproduct_pkey;
       public         postgres    false    198    198                       2606    16501     userxplusdata userxplusdata_pkey 
   CONSTRAINT     [   ALTER TABLE ONLY userxplusdata
    ADD CONSTRAINT userxplusdata_pkey PRIMARY KEY (u_code);
 J   ALTER TABLE ONLY public.userxplusdata DROP CONSTRAINT userxplusdata_pkey;
       public         postgres    false    186    186                       2606    16509    userxrole userxrole_pkey 
   CONSTRAINT     S   ALTER TABLE ONLY userxrole
    ADD CONSTRAINT userxrole_pkey PRIMARY KEY (uxr_id);
 B   ALTER TABLE ONLY public.userxrole DROP CONSTRAINT userxrole_pkey;
       public         postgres    false    188    188            *           2606    16614    dbcomment c_c    FK CONSTRAINT     s   ALTER TABLE ONLY dbcomment
    ADD CONSTRAINT c_c FOREIGN KEY (s_name) REFERENCES stage(s_name) ON DELETE CASCADE;
 7   ALTER TABLE ONLY public.dbcomment DROP CONSTRAINT c_c;
       public       postgres    false    2076    194    196            &           2606    16584    project p_c    FK CONSTRAINT     y   ALTER TABLE ONLY project
    ADD CONSTRAINT p_c FOREIGN KEY (u_code) REFERENCES userxplusdata(u_code) ON DELETE CASCADE;
 5   ALTER TABLE ONLY public.project DROP CONSTRAINT p_c;
       public       postgres    false    191    186    2066            '           2606    16589    project p_co    FK CONSTRAINT     o   ALTER TABLE ONLY project
    ADD CONSTRAINT p_co FOREIGN KEY (u_id) REFERENCES dbuser(u_id) ON DELETE CASCADE;
 6   ALTER TABLE ONLY public.project DROP CONSTRAINT p_co;
       public       postgres    false    191    185    2064            +           2606    16728    dbcomment p_idfk2com    FK CONSTRAINT     x   ALTER TABLE ONLY dbcomment
    ADD CONSTRAINT p_idfk2com FOREIGN KEY (p_id) REFERENCES project(p_id) ON DELETE CASCADE;
 >   ALTER TABLE ONLY public.dbcomment DROP CONSTRAINT p_idfk2com;
       public       postgres    false    196    2072    191            /           2606    16723    stagexproduct p_idfkcom    FK CONSTRAINT     {   ALTER TABLE ONLY stagexproduct
    ADD CONSTRAINT p_idfkcom FOREIGN KEY (p_id) REFERENCES project(p_id) ON DELETE CASCADE;
 A   ALTER TABLE ONLY public.stagexproduct DROP CONSTRAINT p_idfkcom;
       public       postgres    false    191    2072    198            )           2606    16715    projectxstage pxs_c    FK CONSTRAINT     w   ALTER TABLE ONLY projectxstage
    ADD CONSTRAINT pxs_c FOREIGN KEY (p_id) REFERENCES project(p_id) ON DELETE CASCADE;
 =   ALTER TABLE ONLY public.projectxstage DROP CONSTRAINT pxs_c;
       public       postgres    false    2072    193    191            (           2606    16599    projectxstage pxs_co    FK CONSTRAINT     z   ALTER TABLE ONLY projectxstage
    ADD CONSTRAINT pxs_co FOREIGN KEY (s_name) REFERENCES stage(s_name) ON DELETE CASCADE;
 >   ALTER TABLE ONLY public.projectxstage DROP CONSTRAINT pxs_co;
       public       postgres    false    2076    193    194            $           2606    16574    userxrole role_c    FK CONSTRAINT     s   ALTER TABLE ONLY userxrole
    ADD CONSTRAINT role_c FOREIGN KEY (r_id) REFERENCES dbrole(r_id) ON DELETE CASCADE;
 :   ALTER TABLE ONLY public.userxrole DROP CONSTRAINT role_c;
       public       postgres    false    188    190    2070            -           2606    16604    stagexproduct sxpr_c    FK CONSTRAINT     z   ALTER TABLE ONLY stagexproduct
    ADD CONSTRAINT sxpr_c FOREIGN KEY (s_name) REFERENCES stage(s_name) ON DELETE CASCADE;
 >   ALTER TABLE ONLY public.stagexproduct DROP CONSTRAINT sxpr_c;
       public       postgres    false    198    2076    194            .           2606    16609    stagexproduct sxpr_co    FK CONSTRAINT     {   ALTER TABLE ONLY stagexproduct
    ADD CONSTRAINT sxpr_co FOREIGN KEY (pr_id) REFERENCES product(pr_id) ON DELETE CASCADE;
 ?   ALTER TABLE ONLY public.stagexproduct DROP CONSTRAINT sxpr_co;
       public       postgres    false    199    2082    198            ,           2606    16733    dbcomment u_idfk2com    FK CONSTRAINT     w   ALTER TABLE ONLY dbcomment
    ADD CONSTRAINT u_idfk2com FOREIGN KEY (u_id) REFERENCES dbuser(u_id) ON DELETE CASCADE;
 >   ALTER TABLE ONLY public.dbcomment DROP CONSTRAINT u_idfk2com;
       public       postgres    false    2064    185    196            #           2606    16569    userxplusdata use_id    FK CONSTRAINT     w   ALTER TABLE ONLY userxplusdata
    ADD CONSTRAINT use_id FOREIGN KEY (u_id) REFERENCES dbuser(u_id) ON DELETE CASCADE;
 >   ALTER TABLE ONLY public.userxplusdata DROP CONSTRAINT use_id;
       public       postgres    false    2064    185    186            %           2606    16579    userxrole user_c    FK CONSTRAINT     s   ALTER TABLE ONLY userxrole
    ADD CONSTRAINT user_c FOREIGN KEY (u_id) REFERENCES dbuser(u_id) ON DELETE CASCADE;
 :   ALTER TABLE ONLY public.userxrole DROP CONSTRAINT user_c;
       public       postgres    false    188    185    2064            �      x������ � �      �   3   x�3�tL����2�t�K��KM-�2�t.-.��2M8�S�R�s�b���� 2��      �   H   x�3��,NLL��+�K��060�020�LN,N�2�#g��TT����X�Y�iabnhifję�_������ @n      �      x������ � �      �      x������ � �      �      x������ � �      �      x������ � �      �      x������ � �      �      x�3206�4�2204�4����� 	�      �   !   x�3�4�4�2�4�&@��)�m����� 4�v     