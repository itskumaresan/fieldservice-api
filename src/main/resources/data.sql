insert into schedules(ID, EVENT_ID, USER_ID, SCHEDULE_DATE, CREATED, UPDATED) 
values (10000,20001,30001,PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'), PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'), PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'));
insert into schedules(ID, EVENT_ID, USER_ID, SCHEDULE_DATE, CREATED, UPDATED) 
values (10001,20001,30002,PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'), PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'), PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'));
insert into schedules(ID, EVENT_ID, USER_ID, SCHEDULE_DATE, CREATED, UPDATED) 
values (10002,20002,30001,PARSEDATETIME('25 Apr 2019','dd MMM yyyy','en'), PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'), PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'));
insert into schedules(ID, EVENT_ID, USER_ID, SCHEDULE_DATE, CREATED, UPDATED) 
values (10003,20002,30000,PARSEDATETIME('25 Apr 2019','dd MMM yyyy','en'), PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'), PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'));
insert into schedules(ID, EVENT_ID, USER_ID, SCHEDULE_DATE, CREATED, UPDATED) 
values (10004,20003,30000,PARSEDATETIME('26 Apr 2019','dd MMM yyyy','en'), PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'), PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'));
insert into schedules(ID, EVENT_ID, USER_ID, SCHEDULE_DATE, CREATED, UPDATED) 
values (10005,20003,30000,PARSEDATETIME('26 Apr 2019','dd MMM yyyy','en'), PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'), PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'));

insert into events(ID, ACTIVE, CREATED, END_DATE, ZONE_ID, NAME, SEVERITY, START_DATE, NUMBER_OF_WORKERS_REQUIRED, STATUS, UPDATED) 
values (20001, 'Y', PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'), PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'), 1, 'Event1', 'CRITICAL', PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'), 5, 'ASSIGNED', PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'));
insert into events(ID, ACTIVE, CREATED, END_DATE, ZONE_ID, NAME, SEVERITY, START_DATE, NUMBER_OF_WORKERS_REQUIRED, STATUS, UPDATED) 
values (20002, 'Y', PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'), PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'), 2, 'Event2', 'MODERATE', PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'), 2, 'ASSIGNED', PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'));
insert into events(ID, ACTIVE, CREATED, END_DATE, ZONE_ID, NAME, SEVERITY, START_DATE, NUMBER_OF_WORKERS_REQUIRED, STATUS, UPDATED) 
values (20003, 'Y', PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'), PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'), 3, 'Event3', 'LOW', PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'), 1, 'ASSIGNED', PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'));
insert into events(ID, ACTIVE, CREATED, END_DATE, ZONE_ID, NAME, SEVERITY, START_DATE, NUMBER_OF_WORKERS_REQUIRED, STATUS, UPDATED) 
values (20004, 'Y', PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'), PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'), 4, 'Event4', 'CRITICAL', PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'), 10, 'UNASSIGNED', PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'));
insert into events(ID, ACTIVE, CREATED, END_DATE, ZONE_ID, NAME, SEVERITY, START_DATE, NUMBER_OF_WORKERS_REQUIRED, STATUS, UPDATED) 
values (20005, 'Y', PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'), PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'), 5, 'Event5', 'MODERATE', PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'), 8, 'HOLD', PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'));
insert into events(ID, ACTIVE, CREATED, END_DATE, ZONE_ID, NAME, SEVERITY, START_DATE, NUMBER_OF_WORKERS_REQUIRED, STATUS, UPDATED) 
values (20006, 'Y', PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'), PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'), 1, 'Event6', 'LOW', PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'), 100, 'COMPLETED', PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'));

insert into users(ID, ACTIVE, CREATED, EMAIL, ZONE_ID, NAME, PASSWORD, ROLE, STATUS, UPDATED, START_DATE, END_DATE) 
values (30001, 'Y', PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'), 'aaa@gmail.com', 1, 'User1', 'Test@123', 'WORKER', 'ASSIGNED', PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'), PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'), PARSEDATETIME('30 Apr 2019','dd MMM yyyy','en'));
insert into users(ID, ACTIVE, CREATED, EMAIL, ZONE_ID, NAME, PASSWORD, ROLE, STATUS, UPDATED, START_DATE, END_DATE) 
values (30002, 'Y', PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'), 'bbb@gmail.com', 2, 'User2', 'Test@123', 'WORKER', 'ASSIGNED', PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'), PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'), PARSEDATETIME('30 Apr 2019','dd MMM yyyy','en'));
insert into users(ID, ACTIVE, CREATED, EMAIL, ZONE_ID, NAME, PASSWORD, ROLE, STATUS, UPDATED, START_DATE, END_DATE) 
values (30003, 'Y', PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'), 'ccc@gmail.com', 3, 'User3', 'Test@123', 'WORKER', 'INPROGRESS', PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'), PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'), PARSEDATETIME('30 Apr 2019','dd MMM yyyy','en'));
insert into users(ID, ACTIVE, CREATED, EMAIL, ZONE_ID, NAME, PASSWORD, ROLE, STATUS, UPDATED, START_DATE, END_DATE) 
values (30004, 'Y', PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'), 'ddd@gmail.com', 4, 'User4', 'Test@123', 'WORKER', 'INPROGRESS', PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'), PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'), PARSEDATETIME('30 Apr 2019','dd MMM yyyy','en'));
insert into users(ID, ACTIVE, CREATED, EMAIL, ZONE_ID, NAME, PASSWORD, ROLE, STATUS, UPDATED, START_DATE, END_DATE) 
values (30005, 'Y', PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'), 'eee@gmail.com', 5, 'User5', 'Test@123', 'WORKER', 'ASSIGNED', PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'), PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'), PARSEDATETIME('30 Apr 2019','dd MMM yyyy','en'));
insert into users(ID, ACTIVE, CREATED, EMAIL, ZONE_ID, NAME, PASSWORD, ROLE, STATUS, UPDATED, START_DATE, END_DATE) 
values (30006, 'Y', PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'), 'fff@gmail.com', 5, 'User6', 'Test@123', 'WORKER', 'ASSIGNED', PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'), PARSEDATETIME('24 Apr 2019','dd MMM yyyy','en'), PARSEDATETIME('30 Apr 2019','dd MMM yyyy','en'));
