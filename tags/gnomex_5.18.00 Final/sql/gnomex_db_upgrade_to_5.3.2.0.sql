use gnomex;

alter table gnomex.BillingItem add column completeDate datetime null;


alter table SlideSource add column sortOrder int null;

update SlideSource set sortOrder = 1 where codeSlideSource = 'CORE';
update SlideSource set sortOrder = 2 where codeSlideSource = 'CLIENT';
update SlideSource set sortOrder = 3 where codeSlideSource = 'REUSE';
update SlideSource set slideSource = 'New microarray purchased by lab' where codeSlideSource = 'CLIENT';
update SlideSource set slideSource = 'New microarray purchased from core facility' where codeSlideSource = 'CORE';
update SlideSource set slideSource = 'Strip and reuse existing microarray' where codeSlideSource = 'REUSE';    
