create table answer_alternatives (
    id serial,
    answer varchar(100) not null,
    question_id int not null,
    constraint answer_alternatives_fk
        foreign key (question_id)
             references questions (id)

)