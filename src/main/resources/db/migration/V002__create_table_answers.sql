create table answers (
    id serial primary key,
    answer_text varchar(100) not null,
    question_id int not null,
    constraint fk_question_id
        foreign key (question_id)
            references questions (id)
);