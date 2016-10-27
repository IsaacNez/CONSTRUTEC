INSERT INTO CATEGORY(CA_ID,CDescription) VALUES('Ferreteria','Uso general'),('Construccion','Uso general'),('Jardineria','Uso general'),('Obra gris','Uso general');
INSERT INTO CLIENT(C_ID,FName,LName,CAddress,Phone,Day,Month,Year,Penalization,CPassword) VALUES(1,'Juan','Perez','Cartago',8888888,1,1,1,0,'admin1234'),(2,'Juana','Perez','San Jose',8888888,1,1,1,0,'admin1234'),(3,'Juan','Perez','Cartago',8888888,1,1,1,0,'admin1234');
INSERT INTO SUCURSAL(S_ID,SName,SAddress) VALUES(100,'EPATEC Cartago','Cartago Centro'),(101,'EPATEC Guana','Guana Centro'),(102,'EPATEC Limon','Limon Centro');
INSERT INTO EPROVIDER(P_ID,PName,LName,PAddress,Phone,Day,Month,Year) VALUES(5,'Juan','Proveedor','Cartago',8888,2,2,2),(6,'Juan','Proveedor1','Cartago',8888,2,2,2),(7,'Juan','Proveedor2','Cartago',8888,2,2,2);
INSERT INTO PRODUCT(PR_ID,Price,Extent,PDescription,Quantity,PName,S_ID,P_ID) VALUES(1,1500,1,'USO',100,'Martillo',100,5),(2,7500,1,'USO',100,'Pintura Golden',101,6),(3,1000,1,'USO',100,'Clavos',102,7),(4,500,1,'USO',100,'Esponja',102,7),
(5,2500,1,'USO',100,'Silicon',102,7),(6,10000,1,'USO',100,'Ceramica',102,7);
INSERT INTO PC(CA_ID,PR_ID) VALUES('Ferreteria',1),('Construccion',1),('Construccion',2),('Construccion',3),('Construccion',1),('Jardineria',4),('Construccion',5),('Construccion',6);
