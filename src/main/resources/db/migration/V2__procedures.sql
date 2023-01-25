CREATE OR REPLACE PROCEDURE insert_participant(_height integer, _weight integer, _birth_date date, _f_name VARCHAR, _s_name VARCHAR, _m_name VARCHAR, _rank VARCHAR)
LANGUAGE SQL
AS $$
    INSERT INTO participants(height, weight, birth_date, f_name, m_name, s_name, rank)
    VALUES (_height, _weight, _birth_date, _f_name, _s_name, _m_name, _rank)
$$;

CREATE OR REPLACE PROCEDURE insert_trainer(_birth_date date, _f_name VARCHAR, _s_name VARCHAR)
LANGUAGE SQL
AS $$
    INSERT INTO trainers (birth_date, f_name, s_name)
    VALUES ( _birth_date, _f_name, _s_name)
$$;

CREATE OR REPLACE PROCEDURE insert_training(_trainer_id integer, _participant_id integer)
LANGUAGE SQL
AS $$
    INSERT INTO training (trainer_id, participant_id)
    VALUES (_trainer_id, _participant_id)
$$;

CREATE OR REPLACE PROCEDURE insert_result(_date date, _participant_id integer, _score integer)
LANGUAGE SQL
AS $$
    INSERT INTO results (date, participant_id, score)
    VALUES (_date, _participant_id, _score)
$$;