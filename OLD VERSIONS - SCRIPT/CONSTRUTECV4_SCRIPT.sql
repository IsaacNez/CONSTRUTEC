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
    P_Budget INT NOT NULL,
    U_Code INT NOT NULL,
    U_ID INT NOT NULL
);

CREATE TABLE PROJECTXSTAGE(
    PXS_ID SERIAL PRIMARY KEY,
    P_ID INT NOT NULL,
    S_Name varchar(255) NOT NULL
);

CREATE TABLE STAGE(
    S_Name varchar(255) PRIMARY KEY,
    S_Status varchar(255) NOT NULL,
    S_DateStart Date not null,
    S_DateEnd Date not null,
    S_Description varchar(255) not null,
    S_Budget int not null
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
    P_ID int not null
);

CREATE TABLE PRODUCT(
    PR_ID int primary key,
    PR_Description varchar(255) not null,
    PR_Name varchar(255) not null,
    PR_Price int not null,
    PR_Quantity int not null
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

select use.u_id,use.u_name,uxd.u_code,uxr.r_id, case when uxd.u_code is null then uxd.u_code = 0 end as uxd.u_code = 0  from dbuser as use left outer join userxrole as uxr on (use.u_id = uxr.u_id) left outer join userxplusdata as uxd on (use.u_id = uxd.u_id) 
select * from getuser(1,'casa');