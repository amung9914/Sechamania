insert into member (city,full_address,lat,lon,created_date,email,last_modified_date,nickname,password,profile_img,status,member_id) values ('city','fulladdr','lat','lon',NOW(),'admin@admin.com',NOW(),'admin','$2a$10$WvmocswXUNtFTenr8jZh4uNPwmRrTGufupnElllKk61.OaG1GqXQe','/img/defaultProfile.jpg','ACTIVE',1);
insert into authorities (created_date,last_modified_date,member_id,role,authorities_id) values (NOW(),NOW(),1,'USER',1);
insert into authorities (created_date,last_modified_date,member_id,role,authorities_id) values (NOW(),NOW(),1,'COMPANY',2);
insert into authorities (created_date,last_modified_date,member_id,role,authorities_id) values (NOW(),NOW(),1,'ADMIN',3);
