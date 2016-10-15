create function insertuser(
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
            else if m_code = 0 and rm_id = 3 and (not exists(select * from userxrole.r_id = rm_id)) then
            	insert into userxrole(r_id,u_id) values(rm_id,m_id);
            else if m_code = 0 and rm_id = 4 and (not exists(select * from userxrole.r_id = rm_id)) then
            	insert into userxrole(r_id,u_id) values(rm_id,m_id);
            end if;
        else if (not exists(select * from dbuser where dbuser.u_id = m_id))
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

select *  from insertuser(1,'Isaac','Nunez',83048205,'casa',2030,2);