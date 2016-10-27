insert into dbrole values(1,'Admin');
insert into dbrole values(2,'Engineer');
insert into dbrole values(3,'Client');
insert into dbrole values(4,'General');

insert into dbuser values(930549742,'Admin','admin',88888888,'admin1234');
insert into dbuser values(304890149,'Isaac','Nunez',83048205,'casa');
insert into dbuser values(115610679,'Gabriel','Sanchez',86309586,'gjsanchez26');
insert into dbuser values(115820679,'Juan','Sanchez',84597895,'gjsanchez26');
insert into dbuser values(304750553,'Bryan','Masis',84719652,'coso2016');

insert into userxrole(r_id,u_id) values(1,930549742);
insert into userxrole(r_id,u_id) values(3,304890149); 
insert into userxrole(r_id,u_id) values(3,115820679); 
	
insert into userxplusdata values(202020,115610679);

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

insert into project(p_location,p_name,u_code,u_id) values('Cartago','Torre Medica',202020,304750553);
insert into project(p_location,p_name,u_code,u_id) values('San Jose','Restaurante',202020,115820679);



