PGDMP     :    
            	    t         
   construtec    9.6.0    9.6.0 S    �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                       false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                       false            �           1262    25595 
   construtec    DATABASE     �   CREATE DATABASE construtec WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'English_United States.1252' LC_CTYPE = 'English_United States.1252';
    DROP DATABASE construtec;
             postgres    false                        2615    2200    public    SCHEMA        CREATE SCHEMA public;
    DROP SCHEMA public;
             postgres    false            �           0    0    SCHEMA public    COMMENT     6   COMMENT ON SCHEMA public IS 'standard public schema';
                  postgres    false    3                        3079    12387    plpgsql 	   EXTENSION     ?   CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;
    DROP EXTENSION plpgsql;
                  false            �           0    0    EXTENSION plpgsql    COMMENT     @   COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';
                       false    1            �            1255    25922    getcustomerservicebydate(date)    FUNCTION     �  CREATE FUNCTION getcustomerservicebydate(request_stageby date) RETURNS TABLE(gcs_pid integer, gcs_uid integer, gcs_uname character varying, gcs_uphone integer, gcs_plocation character varying, gcs_datestart date, gcs_status character varying, gcs_sname character varying)
    LANGUAGE sql
    AS $$select distinct prj.p_id,prj.u_id,dbu.u_name,dbu.u_phone,prj.p_location,pxs.pxs_datestart,pxs.pxs_status,pxs.s_name
from projectxstage as pxs left outer join project as prj on (pxs.p_id = prj.p_id) left outer join dbuser as dbu on (prj.u_id = dbu.u_id)
where (pxs_datestart - request_stageby <= 15::int) and (pxs_datestart > request_stageby) $$;
 E   DROP FUNCTION public.getcustomerservicebydate(request_stageby date);
       public       postgres    false    3            �            1255    25923 ;   getcustomerservicebyproductanddate(date, character varying)    FUNCTION     B  CREATE FUNCTION getcustomerservicebyproductanddate(request_date date, request_product character varying) RETURNS TABLE(csp_pid integer, csp_uid integer, csp_uname character varying, csp_uphone integer, csp_plocation character varying, csp_datestart date, csp_status character varying, csp_sname character varying)
    LANGUAGE sql
    AS $$select distinct prj.p_id, dbu.u_id, dbu.u_name, dbu.u_phone, prj.p_location,pxs.pxs_datestart,pxs.pxs_status,pxs.s_name
from stagexproduct as sxp left outer join product as pr on (sxp.pr_id = pr.pr_id) left outer join projectxstage as pxs on (sxp.p_id = pxs.p_id)
left outer join project as prj on (pxs.p_id = prj.p_id) left outer join dbuser as dbu on (prj.u_id = dbu.u_id)
where (pxs.pxs_datestart -request_date <15) and  pr.pr_name ~* request_product  and (pxs_datestart > request_date);$$;
 o   DROP FUNCTION public.getcustomerservicebyproductanddate(request_date date, request_product character varying);
       public       postgres    false    3            �            1255    25930    getprojectdetails(integer)    FUNCTION     D  CREATE FUNCTION getprojectdetails(searchp_idby integer) RETURNS TABLE(gpd_id integer, gpd_pbudget integer, gpd_name character varying, gpd_location character varying, gpd_engineer integer, gpd_owner integer, gpd_sname character varying, gpd_datestart date, gpd_dateend date, gpd_status character varying, gpd_budget integer, gpd_pid integer, gpd_price integer, gpd_quantity integer)
    LANGUAGE sql
    AS $$select distinct 
             		 prj.p_id,
                     prj.p_budget,
             		 prj.p_name,
                     prj.p_location,
                     prj.u_code,
                     prj.u_id,
                     pxs.s_name,
                     pxs.pxs_datestart,
                     pxs.pxs_dateend,
                     pxs.pxs_status,
                     pxs.pxs_budget,
                     (case when sxp.pr_id is null then 0 else sxp.pr_id::int end),
                     (case when sxp.pr_price is null then 0 else sxp.pr_price::int end),
                     (case when sxp.pr_quantity is null then 0 else sxp.pr_quantity::int end)
             from project as prj left outer join projectxstage as pxs on (prj.p_id = pxs.p_id)
             							 left outer join stagexproduct as sxp on (pxs.s_name = sxp.s_name and prj.p_id = sxp.p_id)
                                         where prj.p_id = searchp_idby;$$;
 >   DROP FUNCTION public.getprojectdetails(searchp_idby integer);
       public       postgres    false    3            �            1255    25920 #   getuser(integer, character varying)    FUNCTION     �  CREATE FUNCTION getuser(request_id integer, request_password character varying) RETURNS TABLE(q_id integer, q_name character varying, q_code integer, q_role integer)
    LANGUAGE sql
    AS $$select distinct use.u_id,use.u_name,(case when uxd.u_code is null then 0 else uxd.u_code::int end),uxr.r_id
from dbuser as use left outer join userxrole as uxr on (use.u_id = uxr.u_id) left outer join userxplusdata as uxd on (use.u_id = uxd.u_id)
where use.u_id = request_id and use.u_password =request_password;$$;
 V   DROP FUNCTION public.getuser(request_id integer, request_password character varying);
       public       postgres    false    3            �            1255    25919 g   insertuser(integer, character varying, character varying, integer, character varying, integer, integer)    FUNCTION     O  CREATE FUNCTION insertuser(m_id integer DEFAULT 0, m_name character varying DEFAULT ''::character varying, m_lname character varying DEFAULT ''::character varying, m_phone integer DEFAULT 0, m_password character varying DEFAULT ''::character varying, m_code integer DEFAULT 0, rm_id integer DEFAULT 0) RETURNS void
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
       public       postgres    false    3    1            �            1255    25924    updatebudget()    FUNCTION     �  CREATE FUNCTION updatebudget() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
begin
update projectxstage
set pxs_budget = temp.sum
from (select pxs.pxs_id, pxs.p_id ,pxs.s_name, sum(sxp.pr_price*sxp.pr_quantity)
from projectxstage as pxs left outer join stagexproduct as sxp on (pxs.s_name=sxp.s_name)
    where pxs.p_id = sxp.p_id
group by pxs.s_name,pxs.pxs_id,pxs.p_id) temp
where  (temp.pxs_id = projectxstage.pxs_id) and (temp.p_id = projectxstage.p_id);
return NEW;
end;
$$;
 %   DROP FUNCTION public.updatebudget();
       public       postgres    false    3    1            �            1255    25926    updateprojectbudget()    FUNCTION     u  CREATE FUNCTION updateprojectbudget() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
begin
update project
set p_budget = tempstage.sum
from(
select pxs.p_id, sum(pxs.pxs_budget)
from project as prj left outer join projectxstage as pxs on (prj.p_id = pxs.p_id)
where prj.p_id = pxs.p_id
group by pxs.p_id) tempstage
where project.p_id = tempstage.p_id;
return NEW;
end;
$$;
 ,   DROP FUNCTION public.updateprojectbudget();
       public       postgres    false    3    1            �            1259    25825 	   dbcomment    TABLE     �   CREATE TABLE dbcomment (
    c_id integer NOT NULL,
    c_description character varying(255) NOT NULL,
    s_name character varying(255) NOT NULL,
    p_id integer NOT NULL,
    u_id integer NOT NULL
);
    DROP TABLE public.dbcomment;
       public         postgres    false    3            �            1259    25823    dbcomment_c_id_seq    SEQUENCE     t   CREATE SEQUENCE dbcomment_c_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 )   DROP SEQUENCE public.dbcomment_c_id_seq;
       public       postgres    false    197    3            �           0    0    dbcomment_c_id_seq    SEQUENCE OWNED BY     ;   ALTER SEQUENCE dbcomment_c_id_seq OWNED BY dbcomment.c_id;
            public       postgres    false    196            �            1259    25785    dbrole    TABLE     _   CREATE TABLE dbrole (
    r_id integer NOT NULL,
    r_name character varying(255) NOT NULL
);
    DROP TABLE public.dbrole;
       public         postgres    false    3            �            1259    25783    dbrole_r_id_seq    SEQUENCE     q   CREATE SEQUENCE dbrole_r_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 &   DROP SEQUENCE public.dbrole_r_id_seq;
       public       postgres    false    3    190            �           0    0    dbrole_r_id_seq    SEQUENCE OWNED BY     5   ALTER SEQUENCE dbrole_r_id_seq OWNED BY dbrole.r_id;
            public       postgres    false    189            �            1259    25762    dbuser    TABLE     �   CREATE TABLE dbuser (
    u_id integer NOT NULL,
    u_name character varying(255) NOT NULL,
    u_lname character varying(255) NOT NULL,
    u_phone integer NOT NULL,
    u_password character varying(255) NOT NULL
);
    DROP TABLE public.dbuser;
       public         postgres    false    3            �            1259    25844    product    TABLE     �   CREATE TABLE product (
    pr_id integer NOT NULL,
    pr_description character varying(255) NOT NULL,
    pr_name character varying(255) NOT NULL,
    pr_price integer DEFAULT 0 NOT NULL,
    pr_quantity integer DEFAULT 0 NOT NULL
);
    DROP TABLE public.product;
       public         postgres    false    3            �            1259    25793    project    TABLE     �   CREATE TABLE project (
    p_id integer NOT NULL,
    p_location character varying(255) NOT NULL,
    p_name character varying(255) NOT NULL,
    p_budget integer DEFAULT 0 NOT NULL,
    u_code integer NOT NULL,
    u_id integer NOT NULL
);
    DROP TABLE public.project;
       public         postgres    false    3            �            1259    25791    project_p_id_seq    SEQUENCE     r   CREATE SEQUENCE project_p_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 '   DROP SEQUENCE public.project_p_id_seq;
       public       postgres    false    192    3            �           0    0    project_p_id_seq    SEQUENCE OWNED BY     7   ALTER SEQUENCE project_p_id_seq OWNED BY project.p_id;
            public       postgres    false    191            �            1259    25805    projectxstage    TABLE       CREATE TABLE projectxstage (
    pxs_id integer NOT NULL,
    p_id integer NOT NULL,
    s_name character varying(255) NOT NULL,
    pxs_datestart date NOT NULL,
    pxs_dateend date NOT NULL,
    pxs_status character varying(255) NOT NULL,
    pxs_budget integer DEFAULT 0 NOT NULL
);
 !   DROP TABLE public.projectxstage;
       public         postgres    false    3            �            1259    25803    projectxstage_pxs_id_seq    SEQUENCE     z   CREATE SEQUENCE projectxstage_pxs_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 /   DROP SEQUENCE public.projectxstage_pxs_id_seq;
       public       postgres    false    3    194            �           0    0    projectxstage_pxs_id_seq    SEQUENCE OWNED BY     G   ALTER SEQUENCE projectxstage_pxs_id_seq OWNED BY projectxstage.pxs_id;
            public       postgres    false    193            �            1259    25815    stage    TABLE     v   CREATE TABLE stage (
    s_name character varying(255) NOT NULL,
    s_description character varying(255) NOT NULL
);
    DROP TABLE public.stage;
       public         postgres    false    3            �            1259    25836    stagexproduct    TABLE     �   CREATE TABLE stagexproduct (
    sxpr_id integer NOT NULL,
    s_name character varying(255) NOT NULL,
    pr_id integer NOT NULL,
    p_id integer NOT NULL,
    pr_price integer DEFAULT 0 NOT NULL,
    pr_quantity integer DEFAULT 0 NOT NULL
);
 !   DROP TABLE public.stagexproduct;
       public         postgres    false    3            �            1259    25834    stagexproduct_sxpr_id_seq    SEQUENCE     {   CREATE SEQUENCE stagexproduct_sxpr_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 0   DROP SEQUENCE public.stagexproduct_sxpr_id_seq;
       public       postgres    false    3    199            �           0    0    stagexproduct_sxpr_id_seq    SEQUENCE OWNED BY     I   ALTER SEQUENCE stagexproduct_sxpr_id_seq OWNED BY stagexproduct.sxpr_id;
            public       postgres    false    198            �            1259    25770    userxplusdata    TABLE     W   CREATE TABLE userxplusdata (
    u_code integer NOT NULL,
    u_id integer NOT NULL
);
 !   DROP TABLE public.userxplusdata;
       public         postgres    false    3            �            1259    25777 	   userxrole    TABLE     n   CREATE TABLE userxrole (
    uxr_id integer NOT NULL,
    r_id integer NOT NULL,
    u_id integer NOT NULL
);
    DROP TABLE public.userxrole;
       public         postgres    false    3            �            1259    25775    userxrole_uxr_id_seq    SEQUENCE     v   CREATE SEQUENCE userxrole_uxr_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 +   DROP SEQUENCE public.userxrole_uxr_id_seq;
       public       postgres    false    3    188            �           0    0    userxrole_uxr_id_seq    SEQUENCE OWNED BY     ?   ALTER SEQUENCE userxrole_uxr_id_seq OWNED BY userxrole.uxr_id;
            public       postgres    false    187                       2604    25828    dbcomment c_id    DEFAULT     b   ALTER TABLE ONLY dbcomment ALTER COLUMN c_id SET DEFAULT nextval('dbcomment_c_id_seq'::regclass);
 =   ALTER TABLE public.dbcomment ALTER COLUMN c_id DROP DEFAULT;
       public       postgres    false    196    197    197                       2604    25788    dbrole r_id    DEFAULT     \   ALTER TABLE ONLY dbrole ALTER COLUMN r_id SET DEFAULT nextval('dbrole_r_id_seq'::regclass);
 :   ALTER TABLE public.dbrole ALTER COLUMN r_id DROP DEFAULT;
       public       postgres    false    190    189    190                       2604    25796    project p_id    DEFAULT     ^   ALTER TABLE ONLY project ALTER COLUMN p_id SET DEFAULT nextval('project_p_id_seq'::regclass);
 ;   ALTER TABLE public.project ALTER COLUMN p_id DROP DEFAULT;
       public       postgres    false    191    192    192                       2604    25808    projectxstage pxs_id    DEFAULT     n   ALTER TABLE ONLY projectxstage ALTER COLUMN pxs_id SET DEFAULT nextval('projectxstage_pxs_id_seq'::regclass);
 C   ALTER TABLE public.projectxstage ALTER COLUMN pxs_id DROP DEFAULT;
       public       postgres    false    194    193    194                       2604    25839    stagexproduct sxpr_id    DEFAULT     p   ALTER TABLE ONLY stagexproduct ALTER COLUMN sxpr_id SET DEFAULT nextval('stagexproduct_sxpr_id_seq'::regclass);
 D   ALTER TABLE public.stagexproduct ALTER COLUMN sxpr_id DROP DEFAULT;
       public       postgres    false    198    199    199                       2604    25780    userxrole uxr_id    DEFAULT     f   ALTER TABLE ONLY userxrole ALTER COLUMN uxr_id SET DEFAULT nextval('userxrole_uxr_id_seq'::regclass);
 ?   ALTER TABLE public.userxrole ALTER COLUMN uxr_id DROP DEFAULT;
       public       postgres    false    187    188    188            �          0    25825 	   dbcomment 
   TABLE DATA               E   COPY dbcomment (c_id, c_description, s_name, p_id, u_id) FROM stdin;
    public       postgres    false    197   ?q       �           0    0    dbcomment_c_id_seq    SEQUENCE SET     9   SELECT pg_catalog.setval('dbcomment_c_id_seq', 4, true);
            public       postgres    false    196            �          0    25785    dbrole 
   TABLE DATA               '   COPY dbrole (r_id, r_name) FROM stdin;
    public       postgres    false    190   �q       �           0    0    dbrole_r_id_seq    SEQUENCE SET     7   SELECT pg_catalog.setval('dbrole_r_id_seq', 1, false);
            public       postgres    false    189            �          0    25762    dbuser 
   TABLE DATA               E   COPY dbuser (u_id, u_name, u_lname, u_phone, u_password) FROM stdin;
    public       postgres    false    185   �q       �          0    25844    product 
   TABLE DATA               Q   COPY product (pr_id, pr_description, pr_name, pr_price, pr_quantity) FROM stdin;
    public       postgres    false    200   �r       �          0    25793    project 
   TABLE DATA               L   COPY project (p_id, p_location, p_name, p_budget, u_code, u_id) FROM stdin;
    public       postgres    false    192   �r       �           0    0    project_p_id_seq    SEQUENCE SET     7   SELECT pg_catalog.setval('project_p_id_seq', 2, true);
            public       postgres    false    191            �          0    25805    projectxstage 
   TABLE DATA               j   COPY projectxstage (pxs_id, p_id, s_name, pxs_datestart, pxs_dateend, pxs_status, pxs_budget) FROM stdin;
    public       postgres    false    194   ks       �           0    0    projectxstage_pxs_id_seq    SEQUENCE SET     ?   SELECT pg_catalog.setval('projectxstage_pxs_id_seq', 5, true);
            public       postgres    false    193            �          0    25815    stage 
   TABLE DATA               /   COPY stage (s_name, s_description) FROM stdin;
    public       postgres    false    195   �s       �          0    25836    stagexproduct 
   TABLE DATA               U   COPY stagexproduct (sxpr_id, s_name, pr_id, p_id, pr_price, pr_quantity) FROM stdin;
    public       postgres    false    199   Iu       �           0    0    stagexproduct_sxpr_id_seq    SEQUENCE SET     A   SELECT pg_catalog.setval('stagexproduct_sxpr_id_seq', 10, true);
            public       postgres    false    198            �          0    25770    userxplusdata 
   TABLE DATA               .   COPY userxplusdata (u_code, u_id) FROM stdin;
    public       postgres    false    186   �u       �          0    25777 	   userxrole 
   TABLE DATA               0   COPY userxrole (uxr_id, r_id, u_id) FROM stdin;
    public       postgres    false    188   �u       �           0    0    userxrole_uxr_id_seq    SEQUENCE SET     ;   SELECT pg_catalog.setval('userxrole_uxr_id_seq', 4, true);
            public       postgres    false    187            '           2606    25833    dbcomment dbcomment_pkey 
   CONSTRAINT     Q   ALTER TABLE ONLY dbcomment
    ADD CONSTRAINT dbcomment_pkey PRIMARY KEY (c_id);
 B   ALTER TABLE ONLY public.dbcomment DROP CONSTRAINT dbcomment_pkey;
       public         postgres    false    197    197                       2606    25790    dbrole dbrole_pkey 
   CONSTRAINT     K   ALTER TABLE ONLY dbrole
    ADD CONSTRAINT dbrole_pkey PRIMARY KEY (r_id);
 <   ALTER TABLE ONLY public.dbrole DROP CONSTRAINT dbrole_pkey;
       public         postgres    false    190    190                       2606    25769    dbuser dbuser_pkey 
   CONSTRAINT     K   ALTER TABLE ONLY dbuser
    ADD CONSTRAINT dbuser_pkey PRIMARY KEY (u_id);
 <   ALTER TABLE ONLY public.dbuser DROP CONSTRAINT dbuser_pkey;
       public         postgres    false    185    185            +           2606    25853    product product_pkey 
   CONSTRAINT     N   ALTER TABLE ONLY product
    ADD CONSTRAINT product_pkey PRIMARY KEY (pr_id);
 >   ALTER TABLE ONLY public.product DROP CONSTRAINT product_pkey;
       public         postgres    false    200    200            !           2606    25802    project project_pkey 
   CONSTRAINT     M   ALTER TABLE ONLY project
    ADD CONSTRAINT project_pkey PRIMARY KEY (p_id);
 >   ALTER TABLE ONLY public.project DROP CONSTRAINT project_pkey;
       public         postgres    false    192    192            #           2606    25814     projectxstage projectxstage_pkey 
   CONSTRAINT     [   ALTER TABLE ONLY projectxstage
    ADD CONSTRAINT projectxstage_pkey PRIMARY KEY (pxs_id);
 J   ALTER TABLE ONLY public.projectxstage DROP CONSTRAINT projectxstage_pkey;
       public         postgres    false    194    194            %           2606    25822    stage stage_pkey 
   CONSTRAINT     K   ALTER TABLE ONLY stage
    ADD CONSTRAINT stage_pkey PRIMARY KEY (s_name);
 :   ALTER TABLE ONLY public.stage DROP CONSTRAINT stage_pkey;
       public         postgres    false    195    195            )           2606    25843     stagexproduct stagexproduct_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY stagexproduct
    ADD CONSTRAINT stagexproduct_pkey PRIMARY KEY (sxpr_id);
 J   ALTER TABLE ONLY public.stagexproduct DROP CONSTRAINT stagexproduct_pkey;
       public         postgres    false    199    199                       2606    25774     userxplusdata userxplusdata_pkey 
   CONSTRAINT     [   ALTER TABLE ONLY userxplusdata
    ADD CONSTRAINT userxplusdata_pkey PRIMARY KEY (u_code);
 J   ALTER TABLE ONLY public.userxplusdata DROP CONSTRAINT userxplusdata_pkey;
       public         postgres    false    186    186                       2606    25782    userxrole userxrole_pkey 
   CONSTRAINT     S   ALTER TABLE ONLY userxrole
    ADD CONSTRAINT userxrole_pkey PRIMARY KEY (uxr_id);
 B   ALTER TABLE ONLY public.userxrole DROP CONSTRAINT userxrole_pkey;
       public         postgres    false    188    188            :           2620    25925    stagexproduct auto_update    TRIGGER     h   CREATE TRIGGER auto_update AFTER INSERT ON stagexproduct FOR EACH ROW EXECUTE PROCEDURE updatebudget();
 2   DROP TRIGGER auto_update ON public.stagexproduct;
       public       postgres    false    216    199            9           2620    25927 !   projectxstage auto_update_project    TRIGGER     w   CREATE TRIGGER auto_update_project AFTER UPDATE ON projectxstage FOR EACH ROW EXECUTE PROCEDURE updateprojectbudget();
 :   DROP TRIGGER auto_update_project ON public.projectxstage;
       public       postgres    false    201    194            3           2606    25904    dbcomment c_c    FK CONSTRAINT     s   ALTER TABLE ONLY dbcomment
    ADD CONSTRAINT c_c FOREIGN KEY (s_name) REFERENCES stage(s_name) ON DELETE CASCADE;
 7   ALTER TABLE ONLY public.dbcomment DROP CONSTRAINT c_c;
       public       postgres    false    2085    197    195            4           2606    25909    dbcomment c_pid    FK CONSTRAINT     s   ALTER TABLE ONLY dbcomment
    ADD CONSTRAINT c_pid FOREIGN KEY (p_id) REFERENCES project(p_id) ON DELETE CASCADE;
 9   ALTER TABLE ONLY public.dbcomment DROP CONSTRAINT c_pid;
       public       postgres    false    192    2081    197            5           2606    25914    dbcomment c_uid    FK CONSTRAINT     r   ALTER TABLE ONLY dbcomment
    ADD CONSTRAINT c_uid FOREIGN KEY (u_id) REFERENCES dbuser(u_id) ON DELETE CASCADE;
 9   ALTER TABLE ONLY public.dbcomment DROP CONSTRAINT c_uid;
       public       postgres    false    185    2073    197            /           2606    25869    project p_c    FK CONSTRAINT     y   ALTER TABLE ONLY project
    ADD CONSTRAINT p_c FOREIGN KEY (u_code) REFERENCES userxplusdata(u_code) ON DELETE CASCADE;
 5   ALTER TABLE ONLY public.project DROP CONSTRAINT p_c;
       public       postgres    false    2075    186    192            0           2606    25874    project p_co    FK CONSTRAINT     o   ALTER TABLE ONLY project
    ADD CONSTRAINT p_co FOREIGN KEY (u_id) REFERENCES dbuser(u_id) ON DELETE CASCADE;
 6   ALTER TABLE ONLY public.project DROP CONSTRAINT p_co;
       public       postgres    false    2073    192    185            1           2606    25879    projectxstage pxs_c    FK CONSTRAINT     w   ALTER TABLE ONLY projectxstage
    ADD CONSTRAINT pxs_c FOREIGN KEY (p_id) REFERENCES project(p_id) ON DELETE CASCADE;
 =   ALTER TABLE ONLY public.projectxstage DROP CONSTRAINT pxs_c;
       public       postgres    false    2081    194    192            2           2606    25884    projectxstage pxs_co    FK CONSTRAINT     z   ALTER TABLE ONLY projectxstage
    ADD CONSTRAINT pxs_co FOREIGN KEY (s_name) REFERENCES stage(s_name) ON DELETE CASCADE;
 >   ALTER TABLE ONLY public.projectxstage DROP CONSTRAINT pxs_co;
       public       postgres    false    2085    194    195            -           2606    25859    userxrole role_c    FK CONSTRAINT     s   ALTER TABLE ONLY userxrole
    ADD CONSTRAINT role_c FOREIGN KEY (r_id) REFERENCES dbrole(r_id) ON DELETE CASCADE;
 :   ALTER TABLE ONLY public.userxrole DROP CONSTRAINT role_c;
       public       postgres    false    190    188    2079            6           2606    25889    stagexproduct sxpr_c    FK CONSTRAINT     z   ALTER TABLE ONLY stagexproduct
    ADD CONSTRAINT sxpr_c FOREIGN KEY (s_name) REFERENCES stage(s_name) ON DELETE CASCADE;
 >   ALTER TABLE ONLY public.stagexproduct DROP CONSTRAINT sxpr_c;
       public       postgres    false    2085    195    199            7           2606    25894    stagexproduct sxpr_co    FK CONSTRAINT     {   ALTER TABLE ONLY stagexproduct
    ADD CONSTRAINT sxpr_co FOREIGN KEY (pr_id) REFERENCES product(pr_id) ON DELETE CASCADE;
 ?   ALTER TABLE ONLY public.stagexproduct DROP CONSTRAINT sxpr_co;
       public       postgres    false    199    2091    200            8           2606    25899    stagexproduct sxpr_copid    FK CONSTRAINT     |   ALTER TABLE ONLY stagexproduct
    ADD CONSTRAINT sxpr_copid FOREIGN KEY (p_id) REFERENCES project(p_id) ON DELETE CASCADE;
 B   ALTER TABLE ONLY public.stagexproduct DROP CONSTRAINT sxpr_copid;
       public       postgres    false    2081    192    199            ,           2606    25854    userxplusdata use_id    FK CONSTRAINT     w   ALTER TABLE ONLY userxplusdata
    ADD CONSTRAINT use_id FOREIGN KEY (u_id) REFERENCES dbuser(u_id) ON DELETE CASCADE;
 >   ALTER TABLE ONLY public.userxplusdata DROP CONSTRAINT use_id;
       public       postgres    false    185    186    2073            .           2606    25864    userxrole user_c    FK CONSTRAINT     s   ALTER TABLE ONLY userxrole
    ADD CONSTRAINT user_c FOREIGN KEY (u_id) REFERENCES dbuser(u_id) ON DELETE CASCADE;
 :   ALTER TABLE ONLY public.userxrole DROP CONSTRAINT user_c;
       public       postgres    false    185    188    2073            �   ^   x�3�,)JLJ��W(I-���KL������d�Ċ89�L,,M,��9�R�S�3K+KS	�7�LN,ƪʘ��������ܒ+F��� �\+      �   3   x�3�tL����2�t�K��KM-�2�t��L�+�2�tO�K-J������ !�      �   �   x�-���0Dg�c���I<`ae1��
�H�:��Gn8非N���-lo�X@����!Y�C���j��R�7�>[d���q Q`��y^p֒��S�{�ϵ.�.22;�ͫ8j$I[ȵU��ec�� �*�      �   `   x�3�t��)HM,��M,*�����4450�420�2��I,ʸ��e%r�č92�J� ��(QhDJj�9L�	grX�3��/�44�J��qqq ]��      �   `   x�3�LN,*IL��ŉ�� �ibd�ihhjfh`fn�e�阓�U�����R�.o���VHIU�HL�,IL���)�hl`bai`hb����� d��      �   }   x�3�4�)JLJ��W((J�����K,�4204�54�52�2u9���K�J+S�R�9�L��5D�5��kқ���_�Po��.CcS.#���ĢԔT�4Y�4�r�v$X�	�+�#c���� ��M!      �   A  x�mR1n�0��W�����C��:u��k�@J6м����q���ݑw�uPn�$uR����++;/��P�"
5F#�U�X���p���ОL 1��M�f�w�"��[�l��n�W�t�;ʶ�l�!X4X0�'27���$� ��{�h�Mk�s����Z��4P��D5G�̅�����&�6���6�@�U���Cu�9�2-��-�� s�]��K��.y���
��w����l�7d��ΏVi���'�'<	��>mo�M�YW��S([�M�f��{�Ssq� �����e���ƍ��"�o��      �   `   x�3�IM��/�4ASN#.#��P"d��X���Z�i3���M@����pA�n3��H��abp�,�z�\c�a�1����f\1z\\\ �P,?      �      x�312�44453403������ "&      �   9   x�Ǳ�@��&�`��K��#��N����d!!�Q�Y�
OGw1�wC^~�9�
     