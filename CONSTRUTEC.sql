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
    P_ID INT PRIMARY KEY,
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
    S_Name varchar(255) not null
);

CREATE TABLE STAGEXPRODUCT(
    SXPR_ID SERIAL PRIMARY KEY,
    S_Name varchar(255) not null,
    PR_ID int not null
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

ALTER TABLE dbCOMMENT
ADD CONSTRAINT C_C
FOREIGN KEY (S_Name) REFERENCES STAGE(S_Name)
ON DELETE CASCADE;