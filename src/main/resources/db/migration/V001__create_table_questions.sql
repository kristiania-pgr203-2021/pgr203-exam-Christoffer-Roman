create table questions
(
    id serial primary key,
    question_text varchar(100) not null,
    questionnaire_id int not null
)