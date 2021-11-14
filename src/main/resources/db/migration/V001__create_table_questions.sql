create table questions
(
    id serial primary key,
    question_title varchar (100) not null,
    question_text varchar(100),
    question_type int not null
)