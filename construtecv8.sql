PGDMP                     	    t         
   construtec    9.6.0    9.6.0 L    �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                       false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                       false            �           1262    17672 
   construtec    DATABASE     �   CREATE DATABASE construtec WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'English_United States.1252' LC_CTYPE = 'English_United States.1252';
    DROP DATABASE construtec;
             postgres    false                        2615    2200    public    SCHEMA        CREATE SCHEMA public;
    DROP SCHEMA public;
             postgres    false            �           0    0    SCHEMA public    COMMENT     6   COMMENT ON SCHEMA public IS 'standard public schema';
                  postgres    false    3                        3079    12387    plpgsql 	   EXTENSION     ?   CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;
    DROP EXTENSION plpgsql;
                  false            �           0    0    EXTENSION plpgsql    COMMENT     @   COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';
                       false    1            �            1255    18314 #   getuser(integer, character varying)    FUNCTION     �  CREATE FUNCTION getuser(request_id integer, request_password character varying) RETURNS TABLE(q_id integer, q_name character varying, q_code integer, q_role integer)
    LANGUAGE sql
    AS $$select use.u_id,use.u_name,(case when uxd.u_code is null then 0 else uxd.u_code::int end),uxr.r_id
from dbuser as use left outer join userxrole as uxr on (use.u_id = uxr.u_id) left outer join userxplusdata as uxd on (use.u_id = uxd.u_id)
where use.u_id = request_id and use.u_password =request_password;$$;
 V   DROP FUNCTION public.getuser(request_id integer, request_password character varying);
       public       postgres    false    3            �            1255    18313 g   insertuser(integer, character varying, character varying, integer, character varying, integer, integer)    FUNCTION     O  CREATE FUNCTION insertuser(m_id integer DEFAULT 0, m_name character varying DEFAULT ''::character varying, m_lname character varying DEFAULT ''::character varying, m_phone integer DEFAULT 0, m_password character varying DEFAULT ''::character varying, m_code integer DEFAULT 0, rm_id integer DEFAULT 0) RETURNS void
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
       public       postgres    false    1    3            �            1259    18223 	   dbcomment    TABLE     �   CREATE TABLE dbcomment (
    c_id integer NOT NULL,
    c_description character varying(255) NOT NULL,
    s_name character varying(255) NOT NULL,
    p_id integer NOT NULL,
    u_id integer NOT NULL
);
    DROP TABLE public.dbcomment;
       public         postgres    false    3            �            1259    18221    dbcomment_c_id_seq    SEQUENCE     t   CREATE SEQUENCE dbcomment_c_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 )   DROP SEQUENCE public.dbcomment_c_id_seq;
       public       postgres    false    197    3            �           0    0    dbcomment_c_id_seq    SEQUENCE OWNED BY     ;   ALTER SEQUENCE dbcomment_c_id_seq OWNED BY dbcomment.c_id;
            public       postgres    false    196            �            1259    18188    dbrole    TABLE     _   CREATE TABLE dbrole (
    r_id integer NOT NULL,
    r_name character varying(255) NOT NULL
);
    DROP TABLE public.dbrole;
       public         postgres    false    3            �            1259    18186    dbrole_r_id_seq    SEQUENCE     q   CREATE SEQUENCE dbrole_r_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 &   DROP SEQUENCE public.dbrole_r_id_seq;
       public       postgres    false    190    3            �           0    0    dbrole_r_id_seq    SEQUENCE OWNED BY     5   ALTER SEQUENCE dbrole_r_id_seq OWNED BY dbrole.r_id;
            public       postgres    false    189            �            1259    18165    dbuser    TABLE     �   CREATE TABLE dbuser (
    u_id integer NOT NULL,
    u_name character varying(255) NOT NULL,
    u_lname character varying(255) NOT NULL,
    u_phone integer NOT NULL,
    u_password character varying(255) NOT NULL
);
    DROP TABLE public.dbuser;
       public         postgres    false    3            �            1259    18240    product    TABLE     �   CREATE TABLE product (
    pr_id integer NOT NULL,
    pr_description character varying(255) NOT NULL,
    pr_name character varying(255) NOT NULL,
    pr_price integer NOT NULL,
    pr_quantity integer NOT NULL
);
    DROP TABLE public.product;
       public         postgres    false    3            �            1259    18196    project    TABLE     �   CREATE TABLE project (
    p_id integer NOT NULL,
    p_location character varying(255) NOT NULL,
    p_name character varying(255) NOT NULL,
    p_budget integer NOT NULL,
    u_code integer NOT NULL,
    u_id integer NOT NULL
);
    DROP TABLE public.project;
       public         postgres    false    3            �            1259    18194    project_p_id_seq    SEQUENCE     r   CREATE SEQUENCE project_p_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 '   DROP SEQUENCE public.project_p_id_seq;
       public       postgres    false    192    3            �           0    0    project_p_id_seq    SEQUENCE OWNED BY     7   ALTER SEQUENCE project_p_id_seq OWNED BY project.p_id;
            public       postgres    false    191            �            1259    18207    projectxstage    TABLE     �   CREATE TABLE projectxstage (
    pxs_id integer NOT NULL,
    p_id integer NOT NULL,
    s_name character varying(255) NOT NULL,
    pxs_datestart date NOT NULL,
    pxs_dateend date NOT NULL,
    pxs_budget integer NOT NULL
);
 !   DROP TABLE public.projectxstage;
       public         postgres    false    3            �            1259    18205    projectxstage_pxs_id_seq    SEQUENCE     z   CREATE SEQUENCE projectxstage_pxs_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 /   DROP SEQUENCE public.projectxstage_pxs_id_seq;
       public       postgres    false    194    3            �           0    0    projectxstage_pxs_id_seq    SEQUENCE OWNED BY     G   ALTER SEQUENCE projectxstage_pxs_id_seq OWNED BY projectxstage.pxs_id;
            public       postgres    false    193            �            1259    18213    stage    TABLE     v   CREATE TABLE stage (
    s_name character varying(255) NOT NULL,
    s_description character varying(255) NOT NULL
);
    DROP TABLE public.stage;
       public         postgres    false    3            �            1259    18234    stagexproduct    TABLE     �   CREATE TABLE stagexproduct (
    sxpr_id integer NOT NULL,
    s_name character varying(255) NOT NULL,
    pr_id integer NOT NULL,
    p_id integer NOT NULL
);
 !   DROP TABLE public.stagexproduct;
       public         postgres    false    3            �            1259    18232    stagexproduct_sxpr_id_seq    SEQUENCE     {   CREATE SEQUENCE stagexproduct_sxpr_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 0   DROP SEQUENCE public.stagexproduct_sxpr_id_seq;
       public       postgres    false    199    3            �           0    0    stagexproduct_sxpr_id_seq    SEQUENCE OWNED BY     I   ALTER SEQUENCE stagexproduct_sxpr_id_seq OWNED BY stagexproduct.sxpr_id;
            public       postgres    false    198            �            1259    18173    userxplusdata    TABLE     W   CREATE TABLE userxplusdata (
    u_code integer NOT NULL,
    u_id integer NOT NULL
);
 !   DROP TABLE public.userxplusdata;
       public         postgres    false    3            �            1259    18180 	   userxrole    TABLE     n   CREATE TABLE userxrole (
    uxr_id integer NOT NULL,
    r_id integer NOT NULL,
    u_id integer NOT NULL
);
    DROP TABLE public.userxrole;
       public         postgres    false    3            �            1259    18178    userxrole_uxr_id_seq    SEQUENCE     v   CREATE SEQUENCE userxrole_uxr_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 +   DROP SEQUENCE public.userxrole_uxr_id_seq;
       public       postgres    false    188    3            �           0    0    userxrole_uxr_id_seq    SEQUENCE OWNED BY     ?   ALTER SEQUENCE userxrole_uxr_id_seq OWNED BY userxrole.uxr_id;
            public       postgres    false    187            
           2604    18226    dbcomment c_id    DEFAULT     b   ALTER TABLE ONLY dbcomment ALTER COLUMN c_id SET DEFAULT nextval('dbcomment_c_id_seq'::regclass);
 =   ALTER TABLE public.dbcomment ALTER COLUMN c_id DROP DEFAULT;
       public       postgres    false    197    196    197                       2604    18191    dbrole r_id    DEFAULT     \   ALTER TABLE ONLY dbrole ALTER COLUMN r_id SET DEFAULT nextval('dbrole_r_id_seq'::regclass);
 :   ALTER TABLE public.dbrole ALTER COLUMN r_id DROP DEFAULT;
       public       postgres    false    189    190    190                       2604    18199    project p_id    DEFAULT     ^   ALTER TABLE ONLY project ALTER COLUMN p_id SET DEFAULT nextval('project_p_id_seq'::regclass);
 ;   ALTER TABLE public.project ALTER COLUMN p_id DROP DEFAULT;
       public       postgres    false    191    192    192            	           2604    18210    projectxstage pxs_id    DEFAULT     n   ALTER TABLE ONLY projectxstage ALTER COLUMN pxs_id SET DEFAULT nextval('projectxstage_pxs_id_seq'::regclass);
 C   ALTER TABLE public.projectxstage ALTER COLUMN pxs_id DROP DEFAULT;
       public       postgres    false    194    193    194                       2604    18237    stagexproduct sxpr_id    DEFAULT     p   ALTER TABLE ONLY stagexproduct ALTER COLUMN sxpr_id SET DEFAULT nextval('stagexproduct_sxpr_id_seq'::regclass);
 D   ALTER TABLE public.stagexproduct ALTER COLUMN sxpr_id DROP DEFAULT;
       public       postgres    false    199    198    199                       2604    18183    userxrole uxr_id    DEFAULT     f   ALTER TABLE ONLY userxrole ALTER COLUMN uxr_id SET DEFAULT nextval('userxrole_uxr_id_seq'::regclass);
 ?   ALTER TABLE public.userxrole ALTER COLUMN uxr_id DROP DEFAULT;
       public       postgres    false    187    188    188            �          0    18223 	   dbcomment 
   TABLE DATA               E   COPY dbcomment (c_id, c_description, s_name, p_id, u_id) FROM stdin;
    public       postgres    false    197   [       �           0    0    dbcomment_c_id_seq    SEQUENCE SET     :   SELECT pg_catalog.setval('dbcomment_c_id_seq', 1, false);
            public       postgres    false    196            �          0    18188    dbrole 
   TABLE DATA               '   COPY dbrole (r_id, r_name) FROM stdin;
    public       postgres    false    190   8[       �           0    0    dbrole_r_id_seq    SEQUENCE SET     7   SELECT pg_catalog.setval('dbrole_r_id_seq', 1, false);
            public       postgres    false    189            �          0    18165    dbuser 
   TABLE DATA               E   COPY dbuser (u_id, u_name, u_lname, u_phone, u_password) FROM stdin;
    public       postgres    false    185   {[       �          0    18240    product 
   TABLE DATA               Q   COPY product (pr_id, pr_description, pr_name, pr_price, pr_quantity) FROM stdin;
    public       postgres    false    200   \       �          0    18196    project 
   TABLE DATA               L   COPY project (p_id, p_location, p_name, p_budget, u_code, u_id) FROM stdin;
    public       postgres    false    192   3\       �           0    0    project_p_id_seq    SEQUENCE SET     8   SELECT pg_catalog.setval('project_p_id_seq', 1, false);
            public       postgres    false    191            �          0    18207    projectxstage 
   TABLE DATA               ^   COPY projectxstage (pxs_id, p_id, s_name, pxs_datestart, pxs_dateend, pxs_budget) FROM stdin;
    public       postgres    false    194   P\       �           0    0    projectxstage_pxs_id_seq    SEQUENCE SET     @   SELECT pg_catalog.setval('projectxstage_pxs_id_seq', 1, false);
            public       postgres    false    193            �          0    18213    stage 
   TABLE DATA               /   COPY stage (s_name, s_description) FROM stdin;
    public       postgres    false    195   m\       �          0    18234    stagexproduct 
   TABLE DATA               >   COPY stagexproduct (sxpr_id, s_name, pr_id, p_id) FROM stdin;
    public       postgres    false    199   �]       �           0    0    stagexproduct_sxpr_id_seq    SEQUENCE SET     A   SELECT pg_catalog.setval('stagexproduct_sxpr_id_seq', 1, false);
            public       postgres    false    198            �          0    18173    userxplusdata 
   TABLE DATA               .   COPY userxplusdata (u_code, u_id) FROM stdin;
    public       postgres    false    186   �]       �          0    18180 	   userxrole 
   TABLE DATA               0   COPY userxrole (uxr_id, r_id, u_id) FROM stdin;
    public       postgres    false    188   ^       �           0    0    userxrole_uxr_id_seq    SEQUENCE SET     ;   SELECT pg_catalog.setval('userxrole_uxr_id_seq', 4, true);
            public       postgres    false    187                       2606    18231    dbcomment dbcomment_pkey 
   CONSTRAINT     Q   ALTER TABLE ONLY dbcomment
    ADD CONSTRAINT dbcomment_pkey PRIMARY KEY (c_id);
 B   ALTER TABLE ONLY public.dbcomment DROP CONSTRAINT dbcomment_pkey;
       public         postgres    false    197    197                       2606    18193    dbrole dbrole_pkey 
   CONSTRAINT     K   ALTER TABLE ONLY dbrole
    ADD CONSTRAINT dbrole_pkey PRIMARY KEY (r_id);
 <   ALTER TABLE ONLY public.dbrole DROP CONSTRAINT dbrole_pkey;
       public         postgres    false    190    190                       2606    18172    dbuser dbuser_pkey 
   CONSTRAINT     K   ALTER TABLE ONLY dbuser
    ADD CONSTRAINT dbuser_pkey PRIMARY KEY (u_id);
 <   ALTER TABLE ONLY public.dbuser DROP CONSTRAINT dbuser_pkey;
       public         postgres    false    185    185                       2606    18247    product product_pkey 
   CONSTRAINT     N   ALTER TABLE ONLY product
    ADD CONSTRAINT product_pkey PRIMARY KEY (pr_id);
 >   ALTER TABLE ONLY public.product DROP CONSTRAINT product_pkey;
       public         postgres    false    200    200                       2606    18204    project project_pkey 
   CONSTRAINT     M   ALTER TABLE ONLY project
    ADD CONSTRAINT project_pkey PRIMARY KEY (p_id);
 >   ALTER TABLE ONLY public.project DROP CONSTRAINT project_pkey;
       public         postgres    false    192    192                       2606    18212     projectxstage projectxstage_pkey 
   CONSTRAINT     [   ALTER TABLE ONLY projectxstage
    ADD CONSTRAINT projectxstage_pkey PRIMARY KEY (pxs_id);
 J   ALTER TABLE ONLY public.projectxstage DROP CONSTRAINT projectxstage_pkey;
       public         postgres    false    194    194                       2606    18220    stage stage_pkey 
   CONSTRAINT     K   ALTER TABLE ONLY stage
    ADD CONSTRAINT stage_pkey PRIMARY KEY (s_name);
 :   ALTER TABLE ONLY public.stage DROP CONSTRAINT stage_pkey;
       public         postgres    false    195    195                       2606    18239     stagexproduct stagexproduct_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY stagexproduct
    ADD CONSTRAINT stagexproduct_pkey PRIMARY KEY (sxpr_id);
 J   ALTER TABLE ONLY public.stagexproduct DROP CONSTRAINT stagexproduct_pkey;
       public         postgres    false    199    199                       2606    18177     userxplusdata userxplusdata_pkey 
   CONSTRAINT     [   ALTER TABLE ONLY userxplusdata
    ADD CONSTRAINT userxplusdata_pkey PRIMARY KEY (u_code);
 J   ALTER TABLE ONLY public.userxplusdata DROP CONSTRAINT userxplusdata_pkey;
       public         postgres    false    186    186                       2606    18185    userxrole userxrole_pkey 
   CONSTRAINT     S   ALTER TABLE ONLY userxrole
    ADD CONSTRAINT userxrole_pkey PRIMARY KEY (uxr_id);
 B   ALTER TABLE ONLY public.userxrole DROP CONSTRAINT userxrole_pkey;
       public         postgres    false    188    188            '           2606    18298    dbcomment c_c    FK CONSTRAINT     s   ALTER TABLE ONLY dbcomment
    ADD CONSTRAINT c_c FOREIGN KEY (s_name) REFERENCES stage(s_name) ON DELETE CASCADE;
 7   ALTER TABLE ONLY public.dbcomment DROP CONSTRAINT c_c;
       public       postgres    false    2073    195    197            (           2606    18303    dbcomment c_pid    FK CONSTRAINT     s   ALTER TABLE ONLY dbcomment
    ADD CONSTRAINT c_pid FOREIGN KEY (p_id) REFERENCES project(p_id) ON DELETE CASCADE;
 9   ALTER TABLE ONLY public.dbcomment DROP CONSTRAINT c_pid;
       public       postgres    false    197    192    2069            )           2606    18308    dbcomment c_uid    FK CONSTRAINT     r   ALTER TABLE ONLY dbcomment
    ADD CONSTRAINT c_uid FOREIGN KEY (u_id) REFERENCES dbuser(u_id) ON DELETE CASCADE;
 9   ALTER TABLE ONLY public.dbcomment DROP CONSTRAINT c_uid;
       public       postgres    false    185    197    2061            #           2606    18263    project p_c    FK CONSTRAINT     y   ALTER TABLE ONLY project
    ADD CONSTRAINT p_c FOREIGN KEY (u_code) REFERENCES userxplusdata(u_code) ON DELETE CASCADE;
 5   ALTER TABLE ONLY public.project DROP CONSTRAINT p_c;
       public       postgres    false    192    186    2063            $           2606    18268    project p_co    FK CONSTRAINT     o   ALTER TABLE ONLY project
    ADD CONSTRAINT p_co FOREIGN KEY (u_id) REFERENCES dbuser(u_id) ON DELETE CASCADE;
 6   ALTER TABLE ONLY public.project DROP CONSTRAINT p_co;
       public       postgres    false    2061    192    185            %           2606    18273    projectxstage pxs_c    FK CONSTRAINT     w   ALTER TABLE ONLY projectxstage
    ADD CONSTRAINT pxs_c FOREIGN KEY (p_id) REFERENCES project(p_id) ON DELETE CASCADE;
 =   ALTER TABLE ONLY public.projectxstage DROP CONSTRAINT pxs_c;
       public       postgres    false    2069    194    192            &           2606    18278    projectxstage pxs_co    FK CONSTRAINT     z   ALTER TABLE ONLY projectxstage
    ADD CONSTRAINT pxs_co FOREIGN KEY (s_name) REFERENCES stage(s_name) ON DELETE CASCADE;
 >   ALTER TABLE ONLY public.projectxstage DROP CONSTRAINT pxs_co;
       public       postgres    false    2073    195    194            !           2606    18253    userxrole role_c    FK CONSTRAINT     s   ALTER TABLE ONLY userxrole
    ADD CONSTRAINT role_c FOREIGN KEY (r_id) REFERENCES dbrole(r_id) ON DELETE CASCADE;
 :   ALTER TABLE ONLY public.userxrole DROP CONSTRAINT role_c;
       public       postgres    false    2067    190    188            *           2606    18283    stagexproduct sxpr_c    FK CONSTRAINT     z   ALTER TABLE ONLY stagexproduct
    ADD CONSTRAINT sxpr_c FOREIGN KEY (s_name) REFERENCES stage(s_name) ON DELETE CASCADE;
 >   ALTER TABLE ONLY public.stagexproduct DROP CONSTRAINT sxpr_c;
       public       postgres    false    2073    195    199            +           2606    18288    stagexproduct sxpr_co    FK CONSTRAINT     {   ALTER TABLE ONLY stagexproduct
    ADD CONSTRAINT sxpr_co FOREIGN KEY (pr_id) REFERENCES product(pr_id) ON DELETE CASCADE;
 ?   ALTER TABLE ONLY public.stagexproduct DROP CONSTRAINT sxpr_co;
       public       postgres    false    2079    200    199            ,           2606    18293    stagexproduct sxpr_copid    FK CONSTRAINT     |   ALTER TABLE ONLY stagexproduct
    ADD CONSTRAINT sxpr_copid FOREIGN KEY (p_id) REFERENCES project(p_id) ON DELETE CASCADE;
 B   ALTER TABLE ONLY public.stagexproduct DROP CONSTRAINT sxpr_copid;
       public       postgres    false    199    192    2069                        2606    18248    userxplusdata use_id    FK CONSTRAINT     w   ALTER TABLE ONLY userxplusdata
    ADD CONSTRAINT use_id FOREIGN KEY (u_id) REFERENCES dbuser(u_id) ON DELETE CASCADE;
 >   ALTER TABLE ONLY public.userxplusdata DROP CONSTRAINT use_id;
       public       postgres    false    186    2061    185            "           2606    18258    userxrole user_c    FK CONSTRAINT     s   ALTER TABLE ONLY userxrole
    ADD CONSTRAINT user_c FOREIGN KEY (u_id) REFERENCES dbuser(u_id) ON DELETE CASCADE;
 :   ALTER TABLE ONLY public.userxrole DROP CONSTRAINT user_c;
       public       postgres    false    185    188    2061            �      x������ � �      �   3   x�3�tL����2�t�K��KM-�2�t��L�+�2�tO�K-J������ !�      �   �   x�-���0Dg�c���I<`ae1��
�H�:��Gn8非N���-lo�X@����!Y�C���j��R�7�>[d���q Q`��y^p֒��S�{�ϵ.�.22;�ͫ8j$I[ȵU��ec�� �*�      �      x������ � �      �      x������ � �      �      x������ � �      �   U  x�mR1n�0��W�����C�:ua�k�@J6���C�>!+m9�]w�tG���B:qޝ] �ZA$!�8��6���S����L��>(՟ut�MK��!e���F,�Q�
2ׂw�u��bz�^(�=쑷�f��i���O�a��Dx��|.[څ��j�zt�Kc.Fl�(�v��#�%q�w�)�墓ܹь���6D�X}?8���xr/���UB��2)�JQ��>�=��R��/���u*<�~[���rv��dJ�b�M�k@'�ڙ��)��Q�V�5̜i����1:�-�y�q�(��q�V�����%�*�F��c�/Y�!      �      x������ � �      �      x�312�44453403������ "&      �   9   x�%ɹ�0��&G걢]��)������
�zW�e"`���,�޴���C�:?
     