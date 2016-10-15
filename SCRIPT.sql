CREATE TABLE dbUSER(
    U_ID INT NOT NULL,
    U_Name varchar(255) NOT NULL,
    U_LName varchar(255) NOT NULL,
    U_Phone INT NOT NULL,
    U_Password varchar(255) NOT NULL,
    PRIMARY KEY(U_ID)
);

CREATE TABLE USERXPLUSDATA(
    U_Code INT PRIMARY KEY,
    U_ID INT NOT NULL
);

CREATE TABLE USERXROLE(
    UXR_ID SERIAL PRIMARY KEY,
    R_ID INT NOT NULL,
    U_ID INT NOT NULL
);

CREATE TABLE dbROLE(
    R_ID SERIAL PRIMARY KEY,
    R_Name varchar(255) NOT NULL
);

CREATE TABLE PROJECT(
    P_ID serial PRIMARY KEY,
    P_Location varchar(255) NOT NULL,
    P_Name varchar(255) NOT NULL,
    P_Budget INT NOT NULL default 0,
    U_Code INT NOT NULL,
    U_ID INT NOT NULL
);

CREATE TABLE PROJECTXSTAGE(
    PXS_ID SERIAL PRIMARY KEY,
    P_ID INT NOT NULL,
    S_Name varchar(255) NOT NULL,
    PXS_DateStart Date not null,
    PXS_DateEnd Date not null,
    PXS_Status varchar(255) not null,
    PXS_Budget int not null default 0

);

CREATE TABLE STAGE(
    S_Name varchar(255) PRIMARY KEY,
    S_Description varchar(255) not null
);

CREATE TABLE dbCOMMENT(
    C_ID  serial primary key,
    C_Description varchar(255) not null,
    S_Name varchar(255) not null,
    P_ID int not null,
    U_ID int not null
);

CREATE TABLE STAGEXPRODUCT(
    SXPR_ID SERIAL PRIMARY KEY,
    S_Name varchar(255) not null,
    PR_ID int not null,
    P_ID int not null,
    PR_Price int not null default 0,
    PR_Quantity int not null default 0
);

CREATE TABLE PRODUCT(
    PR_ID int primary key,
    PR_Description varchar(255) not null,
    PR_Name varchar(255) not null,
    PR_Price int not null default 0,
    PR_Quantity int not null default 0
);

ALTER TABLE USERXPLUSDATA
ADD CONSTRAINT USE_ID
FOREIGN KEY (U_ID) REFERENCES dbUSER(U_ID)
ON DELETE CASCADE;

ALTER TABLE USERXROLE
ADD CONSTRAINT ROLE_C
FOREIGN KEY (R_ID) REFERENCES dbROLE(R_ID)
ON DELETE CASCADE;

ALTER TABLE USERXROLE
ADD CONSTRAINT USER_C
FOREIGN KEY (U_ID) REFERENCES dbUSER(U_ID)
ON DELETE CASCADE;

ALTER TABLE PROJECT
ADD CONSTRAINT P_C
FOREIGN KEY (U_Code) REFERENCES USERXPLUSDATA(U_Code)
ON DELETE CASCADE;

ALTER TABLE PROJECT
ADD CONSTRAINT P_CO
FOREIGN KEY (U_ID) REFERENCES dbUSER(U_ID)
ON DELETE CASCADE;

ALTER TABLE PROJECTXSTAGE
ADD CONSTRAINT PXS_C
FOREIGN KEY (P_ID) REFERENCES PROJECT(P_ID)
ON DELETE CASCADE;

ALTER TABLE PROJECTXSTAGE
ADD CONSTRAINT PXS_CO
FOREIGN KEY (S_Name) REFERENCES STAGE(S_Name)
ON DELETE CASCADE;

ALTER TABLE STAGEXPRODUCT
ADD CONSTRAINT SXPR_C
FOREIGN KEY (S_Name) REFERENCES STAGE(S_Name)
ON DELETE CASCADE;

ALTER TABLE STAGEXPRODUCT
ADD CONSTRAINT SXPR_CO
FOREIGN KEY (PR_ID) REFERENCES PRODUCT(PR_ID)
ON DELETE CASCADE;

ALTER TABLE STAGEXPRODUCT
ADD CONSTRAINT SXPR_COPID
FOREIGN KEY (P_ID) REFERENCES PROJECT(P_ID)
ON DELETE CASCADE;

ALTER TABLE dbCOMMENT
ADD CONSTRAINT C_C
FOREIGN KEY (S_Name) REFERENCES STAGE(S_Name)
ON DELETE CASCADE;

ALTER TABLE dbCOMMENT
ADD CONSTRAINT C_PID
FOREIGN KEY (P_ID) REFERENCES PROJECT(P_ID)
ON DELETE CASCADE;

ALTER TABLE dbCOMMENT
ADD CONSTRAINT C_UID
FOREIGN KEY (U_ID) REFERENCES dbUSER(U_ID)
ON DELETE CASCADE;



create or replace function insertuser(
    m_id int = 0,
    m_name varchar(255) = '',
    m_lname varchar(255) = '',
    m_phone int =0,
    m_password varchar(255) = '',
    m_code int = 0 ,
    rm_id int =0
)
returns void as
$function$
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
$function$
language 'plpgsql';

create or replace function getuser(
    request_id int,
    request_password varchar(255)
)
returns table(q_id int, q_name varchar(255), q_code int, q_role int) as
'select use.u_id,use.u_name,(case when uxd.u_code is null then 0 else uxd.u_code::int end),uxr.r_id
from dbuser as use left outer join userxrole as uxr on (use.u_id = uxr.u_id) left outer join userxplusdata as uxd on (use.u_id = uxd.u_id)
where use.u_id = request_id and use.u_password =request_password;'
language 'sql';

create or replace function getprojectdetails(
    searchp_idby int
)
returns table(gpd_id int, 
              gpd_name varchar(255),
              gpd_location varchar(255),
              gpd_engineer int,
              gpd_owner int,
              gpd_sname varchar(255),
              gpd_datestart date,
              gpd_dateend date,
              gpd_status varchar(255),
              gpd_pid int,
              gpd_price int,
              gpd_quantity int
             ) as
             'select prj.p_id,
             		 prj.p_name,
                     prj.p_location,
                     prj.u_code,
                     prj.u_id,
                     pxs.s_name,
                     pxs.pxs_datestart,
                     pxs.pxs_dateend,
                     pxs.pxs_status,
                     sxp.pr_id,
                     sxp.pr_price,
                     sxp.pr_quantity
             from project as prj left outer join projectxstage as pxs on (prj.p_id = pxs.p_id)
             							 left outer join stagexproduct as sxp on (pxs.s_name = sxp.s_name and prj.p_id = sxp.p_id)
                                         where prj.p_id = searchp_idby;'
language 'sql';

create or replace function getcustomerservice(
    request_stageby date
)
returns table(
    gcs_stage varchar(255),
    gcs_datestart date,
    gcs_comment varchar(255)
) as
'select pxs.s_name, pxs.pxs_datestart, dbc.c_description 
from projectxstage as pxs left outer join dbcomment as dbc on (pxs.s_name = dbc.s_name)
where (pxs.pxs_datestart - request_stageby)< 15::int'
language 'sql';

select * from getcustomerservice('2016-10-20');
insert into dbrole values(1,'Admin');
insert into dbrole values(2,'Engineer');
insert into dbrole values(3,'Client');
insert into dbrole values(4,'General');
insert into dbuser values(930549742,'Admin','admin',88888888,'admin1234');
insert into dbuser values(304890149,'Isaac','Nunez',83048205,'casa');
insert into dbuser values(115610679,'Gabriel','Sanchez',86309586,'fockyou');
insert into dbuser values(304750553,'Bryan','Masis',84719652,'coso2016');
insert into userxrole(r_id,u_id) values(1,930549742);
insert into userxrole(r_id,u_id) values(3,304890149); 	
insert into userxplusdata values(420,115610679);
insert into userxrole(r_id,u_id) values(2,115610679);
insert into userxrole(r_id,u_id) values(3,304750553);



insert into stage values('Trabajo preliminar','Preparacion de terreno');
insert into stage values('Cimientos','Creacion de vigas y piso');
insert into stage values('Paredes','Construccion de paredes');
insert into stage values('Concreto reforzado','Construccion de estructuras');
insert into stage values('Techos','Construccion de vigas del techo');
insert into stage values('Cielos','Construccion de cielorazo');
insert into stage values('Repello','Repello de paredes');
insert into stage values('Entrepisos','Division entre pisos');
insert into stage values('Pisos','Chorreo de pisos');
insert into stage values('Enchapes','Enchapado de pisos y paredes');
insert into stage values('Instalacion pluvial','Instalacion canoas y tuberia');
insert into stage values('Instalacion sanitaria','Instalacion de servicios sanitarios');
insert into stage values('Instalacion electrica','Instalacion electrica y tomas electricos');
insert into stage values('Puertas','Instalacion de puertas');
insert into stage values('Cerrajeria','Instalacion de cerrajeria');
insert into stage values('Ventanas','Instalacion de las ventanas');
insert into stage values('Closets','Instalacion de los closets');
insert into stage values('Mueble de cocina','Instalacion de muebles de cocina');
insert into stage values('Escaleras','Instalacion de escaleras');

select * from dbuser;
select * from stage;

insert into project values(3,'cartago','casa',100000,420,115610679);



